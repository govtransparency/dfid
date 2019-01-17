package eu.dfid.worker.wb.parsed;

import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.BaseDFIDTenderParser;
import eu.dl.dataaccess.dto.parsed.ParsedAddress;
import eu.dl.dataaccess.dto.parsed.ParsedBid;
import eu.dl.dataaccess.dto.parsed.ParsedBody;
import eu.dl.dataaccess.dto.parsed.ParsedPrice;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.dataaccess.dto.parsed.ParsedTenderLot;
import eu.dl.dataaccess.dto.raw.RawData;
import eu.dl.worker.utils.jsoup.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contract parser for World Bank.
 *
 * @author Marek Mikes
 */
public class WBContractParser extends BaseDFIDTenderParser {
    private static final String VERSION = "1";

    @Override
    public final List<ParsedTender> parse(final RawData rawTender) {
        final Document form = Jsoup.parse(rawTender.getSourceData());

        DFIDParsedTender parsedTender = new DFIDParsedTender();

        // parse DFID attributes
        parsedTender
                .setProject(new ParsedProject()
                        .setProjectId(parseProjectId(form))
                        .setName(parseProjectName(form))
                        .setCountry(parseProjectCountry(form))
                        .setTeamLeader(parseProjectTeamLeader(form)))
                .setNoObjectionDate(parseTenderNoObjectionDate(form))
                .setProcurementType(parseTenderProcurementType(form));

        // parse common attributes
        parsedTender
                .setTitle(parseTenderTitle(form))
                .setBuyerAssignedId(parseTenderBuyerAssignedId(form))
                .setContractSignatureDate(parseTenderContractSignatureDate(form))
                .setLots(Collections.singletonList(new ParsedTenderLot()
                        .setContractNumber(parseTenderLotContractNumber(form))
                        .setBids(parseTenderLotBids(form))))
                .setFinalPrice(parseTenderFinalPrice(form))
                .setSelectionMethod(parseTenderSelectionMethod(form))
                .addPublication(new ParsedPublication()
                        .setIsIncluded(true)
                        .setSource(PublicationSources.WB)
                        .setHumanReadableUrl(rawTender.getSourceUrl().toString()));

        return new ArrayList<>(Collections.singletonList(parsedTender));
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }

    /**
     * Parse tender title value from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderTitle(final Document form) {
        return JsoupUtils.selectText("h2", form);
    }

    /**
     * Parse project Id value from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static String parseProjectId(final Document form) {
        assert JsoupUtils.selectText("div.column-left tr:nth-child(1) > td:nth-child(1)", form)
                .equals("Project ID");
        return JsoupUtils.selectText("div.column-left tr:nth-child(1) > td:nth-child(2)", form);
    }

    /**
     * Parse project name value from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static String parseProjectName(final Document form) {
        assert JsoupUtils.selectText("div.column-left tr:nth-child(2) > td:nth-child(1)", form)
                .equals("Project Title");
        return JsoupUtils.selectText("div.column-left tr:nth-child(2) > td:nth-child(2)", form);
    }

    /**
     * Parse project country value from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static String parseProjectCountry(final Document form) {
        assert JsoupUtils.selectText("div.column-left tr:nth-child(3) > td:nth-child(1)", form)
                .equals("Country");
        return JsoupUtils.selectText("div.column-left tr:nth-child(3) > td:nth-child(2)", form);
    }

    /**
     * Parse project team leader value from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static String parseProjectTeamLeader(final Document form) {
        assert JsoupUtils.selectText("div.column-left tr:nth-child(4) > td:nth-child(1)", form)
                .equals("Team Leader");
        return JsoupUtils.selectText("div.column-left tr:nth-child(4) > td:nth-child(2)", form);
    }

    /**
     * Parse tender buyer assigned Id value from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderBuyerAssignedId(final Document form) {
        assert JsoupUtils.selectText("div.column-left tr:nth-child(5) > td:nth-child(1)", form)
                .equals("Borrower Contract Reference");
        return JsoupUtils.selectText("div.column-left tr:nth-child(5) > td:nth-child(2)", form);
    }

    /**
     * Parse tender no objection date value from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderNoObjectionDate(final Document form) {
        assert JsoupUtils.selectText("div.column-left tr:nth-child(6) > td:nth-child(1)", form)
                .equals("No Objection Date");
        return JsoupUtils.selectText("div.column-left tr:nth-child(6) > td:nth-child(2)", form);
    }

    /**
     * Parse tender contract signature date value from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderContractSignatureDate(final Document form) {
        assert JsoupUtils.selectText("div.column-left tr:nth-child(7) > td:nth-child(1)", form)
                .equals("Signing Date");
        return JsoupUtils.selectText("div.column-left tr:nth-child(7) > td:nth-child(2)", form);
    }

    /**
     * Parse tender lot contract number value from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderLotContractNumber(final Document form) {
        assert JsoupUtils.selectText("div.column-right tr:nth-child(1) > td:nth-child(1)", form)
                .equals("Contract No");
        return JsoupUtils.selectText("div.column-right tr:nth-child(1) > td:nth-child(2)", form);
    }

    /**
     * Parse tender lot bids from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static List<ParsedBid> parseTenderLotBids(final Document form) {
        final List<ParsedBid> bids = new ArrayList<>();

        final Elements biddersRows = JsoupUtils.select("table:contains(Contractor Name) > tbody > tr", form);
        for (Element bidderRow : biddersRows) {
            bids.add(new ParsedBid()
                    .setIsWinning(Boolean.TRUE.toString())
                    .addBidder(new ParsedBody()
                            .setName(JsoupUtils.selectText("td:nth-child(1)", bidderRow))
                            .setAddress(new ParsedAddress()
                                    .setCountry(JsoupUtils.selectText("td:nth-child(2)", bidderRow)))));
        }

        return (bids.isEmpty() ? null : bids);
    }

    /**
     * Parses tender final price from document.
     *
     * @param form document to be parsed
     *
     * @return parsed tender final price
     */
    private static ParsedPrice parseTenderFinalPrice(final Document form) {
        assert JsoupUtils.selectText("div.column-right tr:nth-child(2) > td:nth-child(1)", form)
                .equals("Total Contract Amount");
        String totalContractAmount = JsoupUtils.selectText(
                "div.column-right tr:nth-child(2) > td:nth-child(2)", form);

        if (totalContractAmount == null) {
            return null;
        }

        String[] currencyAndAmount = totalContractAmount
                .trim().replaceAll("\\s{2,}", " ").split(" ");
        assert currencyAndAmount.length == 2;

        return (new ParsedPrice()
                .setCurrency(currencyAndAmount[0])
                .setNetAmount(currencyAndAmount.length < 2 ? null : currencyAndAmount[1]));
    }

    /**
     * Parse tender selection method value from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderSelectionMethod(final Document form) {
        assert JsoupUtils.selectText("div.column-right tr:nth-child(4) > td:nth-child(1)", form)
                .equals("Procurement Method");
        return JsoupUtils.selectText("div.column-right tr:nth-child(4) > td:nth-child(2)", form);
    }

    /**
     * Parse tender procurement type value from document.
     *
     * @param form document to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderProcurementType(final Document form) {
        assert JsoupUtils.selectText("div.column-right tr:nth-child(5) > td:nth-child(1)", form)
                .equals("Procurement Type");
        return JsoupUtils.selectText("div.column-right tr:nth-child(5) > td:nth-child(2)", form);
    }

    @Override
    protected final String countryOfOrigin(final ParsedTender parsed, final RawData raw) {
        return null;
    }
}
