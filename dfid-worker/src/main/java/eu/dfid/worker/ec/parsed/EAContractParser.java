package eu.dfid.worker.ec.parsed;

import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
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
import java.util.List;

/**
 * Contract (this keyword is used on web) parser for EuropeAid of European Commission.
 *
 * @author Marek Mikes
 */
public class EAContractParser extends BaseDFIDTenderParser {
    private static final String VERSION = "1";

    @Override
    public final List<ParsedTender> parse(final RawData rawTender) {
        List<ParsedTender> parsedTenders = new ArrayList<>();

        final Document resultPage = Jsoup.parse(rawTender.getSourceData());
        final Integer publicationYear = (Integer) rawTender.getMetaData().get("publicationYear");

        final Elements contractRows = JsoupUtils.select("table table > tbody > tr[id]", resultPage);
        for (Element contractRow : contractRows) {
            DFIDParsedTender parsedTender = new DFIDParsedTender();

            // parse DFID attributes
            parsedTender
                    .setPurpose(parseTenderPurpose(contractRow));

            // parse common attributes
            parsedTender
                    .addPublication(new ParsedPublication()
                            .setSourceId(parsePublicationSourceId(contractRow))
                            .setSource(PublicationSources.EA)
                            .setPublicationDate("Jan 1, " + publicationYear)
                            .setHumanReadableUrl(rawTender.getSourceUrl().toString())
                            .setIsIncluded(true))
                    .setTitle(parseTenderTitle(contractRow))
                    .addLot(new ParsedTenderLot()
                            .addBid(new ParsedBid()
                                    .setIsWinning(Boolean.TRUE.toString())
                                    .addBidder(new ParsedBody()
                                            .setName(parseBidderName(contractRow))
                                            .setAddress(new ParsedAddress()
                                                    .setCountry(parseBidderCountry(contractRow))))))
                    .setAddressOfImplementation(new ParsedAddress()
                            .setCountry(parseCountryOfImplementation(contractRow))
                            .setRawAddress(parseRawAddressOfImplementation(contractRow)))
                    .setFinalPrice(new ParsedPrice()
                            .setNetAmount(parseTenderFinalNetAmount(contractRow)))
                    .setSupplyType(parseTenderSupplyType(contractRow))
                    .setEstimatedDurationInDays(parseTenderEstimatedDuration(contractRow, "days"))
                    .setEstimatedDurationInMonths(parseTenderEstimatedDuration(contractRow, "months"));

            assert parsedTender.getEstimatedDurationInDays() != null
                    || parsedTender.getEstimatedDurationInMonths() != null
                    : "Duration should be always set. It was not parsed for row" + contractRow.toString() + ".";

            parsedTenders.add(parsedTender);
        }

        return parsedTenders;
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }

    /**
     * Parse publication source ID value from row representing contract.
     *
     * @param contractRow
     *         row representing contract to be parsed
     *
     * @return String or Null
     */
    private static String parsePublicationSourceId(final Element contractRow) {
        return JsoupUtils.selectText("td:nth-child(1)", contractRow);
    }

    /**
     * Parse tender title value from row representing contract.
     *
     * @param contractRow
     *         row representing contract to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderTitle(final Element contractRow) {
        return JsoupUtils.selectText("td:nth-child(2)", contractRow);
    }

    /**
     * Parse tender purpose value from row representing contract.
     *
     * @param contractRow
     *         row representing contract to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderPurpose(final Element contractRow) {
        return JsoupUtils.selectText("td:nth-child(3)", contractRow);
    }

    /**
     * Parse bidder name value from row representing contract.
     *
     * @param contractRow
     *         row representing contract to be parsed
     *
     * @return String or Null
     */
    private static String parseBidderName(final Element contractRow) {
        return JsoupUtils.selectText("td:nth-child(5)", contractRow);
    }

    /**
     * Parse bidder country value from row representing contract.
     *
     * @param contractRow
     *         row representing contract to be parsed
     *
     * @return String or Null
     */
    private static String parseBidderCountry(final Element contractRow) {
        return JsoupUtils.selectText("td:nth-child(6)", contractRow);
    }

    /**
     * Parse country of implementation value from row representing contract.
     *
     * @param contractRow
     *         row representing contract to be parsed
     *
     * @return String or Null
     */
    private static String parseCountryOfImplementation(final Element contractRow) {
        return JsoupUtils.selectText("td:nth-child(7)", contractRow);
    }

    /**
     * Parse raw address of implementation value from row representing contract.
     *
     * @param contractRow
     *         row representing contract to be parsed
     *
     * @return String or Null
     */
    private static String parseRawAddressOfImplementation(final Element contractRow) {
        return JsoupUtils.selectText("td:nth-child(8)", contractRow);
    }

    /**
     * Parse tender final net amount from row representing contract.
     *
     * @param contractRow
     *         row representing contract to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderFinalNetAmount(final Element contractRow) {
        return JsoupUtils.selectText("td:nth-child(9)", contractRow);
    }

    /**
     * Parse tender supply type value from row representing contract.
     *
     * @param contractRow
     *         row representing contract to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderSupplyType(final Element contractRow) {
        return JsoupUtils.selectText("td:nth-child(10)", contractRow);
    }

    /**
     * Parse tender estimated duration value from row representing contract.
     *
     * @param contractRow
     *         row representing contract to be parsed
     * @param unit
     *         unit in string representing days or months
     *
     * @return String or Null
     */
    private static String parseTenderEstimatedDuration(final Element contractRow, final String unit) {
        String duration = JsoupUtils.selectText("td:nth-child(11)", contractRow);
        String[] numberAndUnit = duration.split(" ");
        assert numberAndUnit.length == 2;
        return (numberAndUnit[1].equals(unit)) ? numberAndUnit[0] : null;
    }

    @Override
    protected final String countryOfOrigin(final ParsedTender parsed, final RawData raw){
        return null;
    }
}
