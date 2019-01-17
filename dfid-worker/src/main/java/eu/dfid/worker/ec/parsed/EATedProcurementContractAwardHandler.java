package eu.dfid.worker.ec.parsed;

import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dl.dataaccess.dto.parsed.ParsedAddress;
import eu.dl.dataaccess.dto.parsed.ParsedBid;
import eu.dl.dataaccess.dto.parsed.ParsedBody;
import eu.dl.dataaccess.dto.parsed.ParsedPrice;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.dataaccess.dto.parsed.ParsedTenderLot;
import eu.dl.worker.utils.jsoup.JsoupUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Procurement parser for EuropeAid of European Commission that are published in TED.
 *
 * @author Tomas Mrazek
 */
public final class EATedProcurementContractAwardHandler {

    private static final Pattern PRICE_PATTERN = Pattern.compile("(^| )(([0-9]+[ .,]?)+) ([A-Z]{3})");
    
    /**
     * Suppress default constructor for noninstantiability.
     */
    private EATedProcurementContractAwardHandler() {
        throw new AssertionError();
    }

    /**
     * Parses contract notice specific data and update the given {@code parsedTender}.
     * 
     * @param parsedTender
     *      parsed tender
     * @param document
     *      document including data
     * @return updated tender
     */
    public static List<ParsedTender> parse(final DFIDParsedTender parsedTender, final Document document) {
        final Element formSectionNode = EATedProcurementParserUtils.parseFormSectionNode(document);
        final Element originNode = EATedProcurementParserUtils.parseOriginNode(formSectionNode);

        ParsedTender updatedTender = parsedTender
            .setPurpose(JsoupUtils.getFirstValueByLabel(originNode, "(?i)DAC code"))
            .addPublication(parseContractNotice(originNode))
            .addPublications(parsePreviousPublications(originNode))
            .setAwardDecisionDate(JsoupUtils.getFirstValueByLabel(originNode, "(?i)Date of award of the contract"))
            .setEstimatedDurationInMonths(
                JsoupUtils.getFirstValueByLabel(originNode, "(?i)Duration of contract"))
            .setLots(parseLots(originNode));
        
        updatedTender = parseBuyerAssignedIdAndFinalPrice(updatedTender, originNode);
        
        return Arrays.asList(updatedTender);
    }
    
    /**
     * @param context
     *      context that contains previous publications data
     * @return list of previous publications
     */
    private static List<ParsedPublication> parsePreviousPublications(final Element context) {
        final Element publicationsWrapper = JsoupUtils.getFirstLabeledValueNode(context,
            "Previous publication(s) in Official Journal S");
        
        if (publicationsWrapper == null) {
            return null;
        }
        
        final List<ParsedPublication> publications = new ArrayList<>();
        final Elements publicationNodes = publicationsWrapper.getElementsContainingOwnText("Notice number in OJ");
        final Pattern p = Pattern.compile("Notice number in OJ: (.+) of (.+)");
        for (Element node : publicationNodes) {            
            Matcher m = p.matcher(node.text());
            if (m.find()) {
                publications.add(new ParsedPublication()
                    .setIsIncluded(false)
                    .setSourceId(m.group(1))
                    .setPublicationDate(m.group(2))
                    .setSourceFormType(node.previousElementSibling().text()));
            }
        }

        return (publications.isEmpty() ? null : publications);
    }
    
    /**
     * Parses lots for the given {@code context}. In case of multiple lots a document is poorly structured. Parser isn't
     * able to parse lots in this case.
     *
     * @param context
     *      context that includes lots data
     * @return list of lots
     */
    private static List<ParsedTenderLot> parseLots(final Element context) {  
        String title = JsoupUtils.getFirstValueByLabel(context,
            "(?i)(Lot number and lot title|Number and title of the lot)");

        String count = JsoupUtils.getFirstValueByLabel(context, "(?i)Number of tenders received");

        ParsedBid bid = null;

        final Element biddersNode = JsoupUtils.getFirstLabeledValueNode(context,
            "(?i)Name, address and nationality of (the )?(successful|selected) tenderer");

        if (biddersNode != null) {
            bid = new ParsedBid()
                .setIsWinning(Boolean.TRUE.toString())
                .setIsConsortium(Boolean.TRUE.toString());

            if (biddersNode.childNodeSize() == 1) {
                bid.addBidder(new ParsedBody()
                    .setIsLeader(Boolean.TRUE.toString())
                    .setName(biddersNode.text()));
            } else {
                //leader
                final Elements leaderNode = biddersNode.getElementsContainingOwnText("Official name of the leader");
                //other bidders
                final Elements otherBiddersNode = biddersNode.getElementsContainingOwnText("Official name of others");

                if (leaderNode.isEmpty() && otherBiddersNode.isEmpty()) {
                    bid.addBidder(new ParsedBody()
                        .setIsLeader(Boolean.TRUE.toString())
                        .setAddress(new ParsedAddress().setRawAddress(biddersNode.text())));
                } else {
                    if (!leaderNode.isEmpty()) {
                        final Element countryNode = leaderNode.get(0).nextElementSibling();
                        ParsedAddress leaderAddress = null;
                        if (countryNode != null) {
                            leaderAddress = new ParsedAddress()
                                .setCountry(countryNode.text().split(":")[1]);

                            final Element rawAddressNode = countryNode.nextElementSibling();
                            if (rawAddressNode != null) {
                                leaderAddress.setRawAddress(rawAddressNode.text());
                            }
                        }

                        bid.addBidder(new ParsedBody()
                            .setIsLeader(Boolean.TRUE.toString())
                            .setName(leaderNode.get(0).ownText().split(":")[1])
                            .setAddress(leaderAddress));
                    }

                    if (!otherBiddersNode.isEmpty()) {
                        Element bidderNode = otherBiddersNode.get(0).nextElementSibling();
                        while (bidderNode != null) {
                            String[] otherBidderData = bidderNode.text().split(";");
                            bid.addBidder(new ParsedBody()
                                .setIsLeader(Boolean.FALSE.toString())
                                .setName(otherBidderData[0])
                                .setAddress(new ParsedAddress()
                                    .setCountry(otherBidderData[1])));

                            bidderNode = bidderNode.nextElementSibling();
                        }
                    }
                }
            }
        }

        if (Arrays.asList(title, count, bid).stream().anyMatch(n -> n != null)) {
            return Collections.singletonList(new ParsedTenderLot()
                .setTitle(title)
                .setBidsCount(count)
                .addBid(bid));
        }

        return null;
    }
    
    /**
     * Update {@code parsedTender} with buyerAssignedId and finalPrice.
     *
     * @param parsedTender
     *      updated parsed tender
     * @param originNode
     *      node that includes form data
     * @return updated parsed tender
     */
    private static ParsedTender parseBuyerAssignedIdAndFinalPrice(final ParsedTender parsedTender,
        final Element originNode) {

        final Element node = JsoupUtils.getFirstLabeledValueNode(originNode, "(?i)Contract number and value");
      
        if (node == null || node.childNodeSize() == 0) {
            return parsedTender;
        }

        String buyerAssignedId = null;
        ParsedPrice finalPrice = null;
      
        if (node.childNodeSize() == 2) {            
            finalPrice = parsePrice(matchPrice(clearBuyerAssignedIdAndFinalPriceString(node.child(0).text())));
            buyerAssignedId = clearBuyerAssignedIdAndFinalPriceString(node.child(1).text());
        } else {
            final String dataString = clearBuyerAssignedIdAndFinalPriceString(node.child(0).text());
            final Matcher finalPriceMatcher = matchPrice(dataString);            
            if (finalPriceMatcher != null) {
                finalPrice = parsePrice(finalPriceMatcher);

                buyerAssignedId = dataString
                    //remove money string
                    .replace(finalPriceMatcher.group(0), "")
                    //remove separator of buyerAssignedId and finalPrice 
                    .replaceAll("(.*)( ?[-;.,]|and)", "");
            }            
        }

        return parsedTender
            .setBuyerAssignedId(buyerAssignedId)
            .setFinalPrice(finalPrice);
    }
    
    /**
     * Attempts to find price in the given string.
     * 
     * @param priceString
     *      string that includes price
     * @return matcher that matches price in {@code priceString} or null
     */
    private static Matcher matchPrice(final String priceString) {
        final Matcher moneyMatcher = PRICE_PATTERN.matcher(priceString);
        if (moneyMatcher.find()) {
            return moneyMatcher;
        }
        
        return null;
    }
    
    /**
     * Parses price from matcher.
     *
     * @param priceMatcher
     *      matcher that matches price pattern
     * @return parsed price
     */
    private static ParsedPrice parsePrice(final Matcher priceMatcher) {
        if (priceMatcher != null) {
            return new ParsedPrice()
                .setNetAmount(priceMatcher.group(2))
                .setCurrency(priceMatcher.group(4));
        }
        
        return null;
    }
    
    /**
     * Removes usual non-data strings from {@code input} string that includes buyerAssignedId and/or finalPrice data. 
     * @param input
     *      string that includes buyerAssignedId and/or finalPrice data
     * @return cleaned string
     */
    private static String clearBuyerAssignedIdAndFinalPriceString(final String input) {
        return input.replaceAll("(Contract number|Value of the contract|Contract value|value):?", "");
    }

    /**
     * Parses contract notice publication.
     *
     * @param context
     *      cotext that includes publication data
     * @return parsed contract notice publication
     */
    private static ParsedPublication parseContractNotice(final Element context) {
        final Element node =
            JsoupUtils.getFirstLabeledValueNode(context, "(?i)Publication date of the contract notice");
        if (node == null) {
            return null;
        }

        return new ParsedPublication()
            .setPublicationDate(JsoupUtils.selectText("p:eq(0)", node))
            .setSourceFormType("Contract notice")
            .setIsIncluded(false);
    }
}
