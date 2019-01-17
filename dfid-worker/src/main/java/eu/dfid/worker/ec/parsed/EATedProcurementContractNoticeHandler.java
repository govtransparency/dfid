package eu.dfid.worker.ec.parsed;

import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.dataaccess.dto.parsed.ParsedTenderLot;
import eu.dl.worker.utils.jsoup.JsoupUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Procurement parser for EuropeAid of European Commission that are published in TED.
 *
 * @author Tomas Mrazek
 */
public final class EATedProcurementContractNoticeHandler {
    /**
     * List of titles that identify begin of economic requirements data.
     */
    private static final List<String> ECONOMIC_REQUIREMENTS = Arrays.asList("economic and financial capacity");
    /**
     * List of titles that identify begin of technical requirements data.
     */
    private static final List<String> TECHNICAL_REQUIREMENTS = Arrays.asList("technical capacity of tenderer",
        "technical and professional capacity");
    /**
     * List of titles that identify begin of personal requirements data.
     */
    private static final List<String> PERSONAL_REQUIREMENTS = Arrays.asList("professional capacity of tenderer");
    /**
     * 
     */
    private static final List<String> REQUIREMENTS_BREAKS = Arrays.asList("this means that the contract",
        "an economic operator may");
    
    /**
     * Suppress default constructor for noninstantiability.
     */
    private EATedProcurementContractNoticeHandler() {
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
            .addPublication(parsePriorInformationNotice(originNode))            
            .setLots(parseLots(originNode))
            .setEconomicRequirements(
                EATedProcurementParserUtils.selectTextByLabel("(?i)Selection criteria", originNode));

        updatedTender = parseEstimatedDuration(updatedTender, originNode);
        
        return Arrays.asList(updatedTender);
    }
    
    /**
     * Parses estimated duration of the tender and update the given {@code parsedTender}.
     *
     * @param parsedTender
     *      parsed tender for update
     * @param context
     *      context taht icludes estimated duration data
     * @return updated parsed tender
     */
    private static ParsedTender parseEstimatedDuration(final ParsedTender parsedTender, final Element context) {
        final String rawDuration =
            JsoupUtils.getFirstValueByLabel(context, "(?i)period of implementation of tasks");
        
        if (rawDuration == null) {
            return parsedTender;
        }
        
        final Pattern p = Pattern.compile("[0-9]+");
        final Matcher m = p.matcher(rawDuration);
        if (m.find()) {
            final String durationValue = m.group();
            if (rawDuration.contains("days")) {
                parsedTender.setEstimatedDurationInDays(durationValue);
            } else if (rawDuration.contains("months")) {
                parsedTender.setEstimatedDurationInMonths(durationValue);
            }
        }

        return parsedTender;        
    }
            
    /**
     * Parses prior information notice publication.
     * 
     * @param context
     *      cotext that includes publication data
     * @return parsed prior information notice publication
     */
    private static ParsedPublication parsePriorInformationNotice(final Element context) {
        final Element node =
            JsoupUtils.getFirstLabeledValueNode(context, "(?i)Date of publication of prior information notice");
        if (node == null) {
            return null;
        }
        
        return new ParsedPublication()
            .setPublicationDate(JsoupUtils.selectText("p:eq(0)", node))
            .setSourceId(JsoupUtils.selectText("p:eq(1)", node))
            .setSourceFormType("Prior information notice")
            .setIsIncluded(false);
    }
    
    /**
     * Parses lots.
     *
     * @param context
     *      context that includes lots data
     * @return list of lots
     */
    private static List<ParsedTenderLot> parseLots(final Element context) {
        final Element node =
            JsoupUtils.getFirstLabeledValueNode(context, "(?i)Number and titles of lots");
        if (node == null) {
            return null;
        }
        
        final List<ParsedTenderLot> lots = new ArrayList<>();
        if (node.childNodeSize() == 1) {
            lots.add(new ParsedTenderLot()
                .setTitle(JsoupUtils.selectText("p", node)));
        } else {
            for (Element p : node.children()) {
                //lot node starts with number followed by ';' or contains substring lot/Lot followed by space and number
                if (!p.text().matches("[1-9]+; .+") && !p.text().matches(".*[Ll]ot [1-9]+.+")) {
                    continue;
                }
                
                lots.add(new ParsedTenderLot()
                    .setTitle(p.text()));
            }
        }

        return lots;
    }
}
