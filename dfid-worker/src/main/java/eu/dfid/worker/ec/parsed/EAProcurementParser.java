package eu.dfid.worker.ec.parsed;

import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.BaseDFIDTenderParser;
import eu.dl.dataaccess.dto.parsed.ParsedAddress;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Procurement parser for EuropeAid of European Commission.
 *
 * @author Tomas Mrazek
 */
public class EAProcurementParser extends BaseDFIDTenderParser {
    private static final String VERSION = "2";

    @Override
    public final List<ParsedTender> parse(final RawData rawTender) {
        final Document resultPage = Jsoup.parse(rawTender.getSourceData());

        final Elements dataRows = JsoupUtils.select("#content_ > table > tbody > tr",
                resultPage);
        if (dataRows == null || dataRows.isEmpty() || dataRows.size() < 6) {
            logger.error("Unable to parse tender because of unexpected page structure");
            return Collections.emptyList();
        }

        //In case that tender detail contains budget information, these are placed in the 6-th row
        final boolean isBudgetSet = (dataRows.get(5).child(0).ownText().contains("Budget"));

        //In case that bidDeadline is defined so status and bidDeadline strings are separated by '»' or '«', these
        //characters are removed for easier cleaning process
        final String[] lotStatusAndBidDeadline =
                dataRows.get(4).child(1).text().replace("»", "").replace("«", "").split("\\s+");
        // Mostly we have lot status and bid deadline, but sometimes there is only lot status. See
        // https://webgate.ec.europa.eu/europeaid/online-services/index.cfm?ADSSChck=1490787164923&do=publi.detPUB&
        // searchtype=AS&debpub=23%2F09%2F1997&finpub=23%2F09%2F1997&orderby=upd&orderbyad=Desc&nbPubliList=50&page=1&
        // aoref=111481
        assert lotStatusAndBidDeadline.length == 2 || lotStatusAndBidDeadline.length == 1;

        final ParsedTender parsedTender = new DFIDParsedTender()
                .setProject(new ParsedProject()
                        .setName(dataRows.get(2).child(1).text()))
                .setTitle(dataRows.get(0).text())
                .addPublication(new ParsedPublication()
                        .setSourceTenderId(dataRows.get(5).child(isBudgetSet ? 3 : 2).text())
                        .setSource(PublicationSources.EA)
                        .setHumanReadableUrl(rawTender.getSourceUrl().toString())
                        .setPublicationDate(dataRows.get(3).child(3).text())
                        .setLastUpdate(dataRows.get(4).child(3).text())
                        .setIsIncluded(true))
                .addLot(new ParsedTenderLot()
                        .setStatus(lotStatusAndBidDeadline[0])
                        .setAddressOfImplementation(new ParsedAddress()
                                .setCountry(dataRows.get(2).child(3).text())))
                .setBidDeadline(lotStatusAndBidDeadline.length == 2 ? lotStatusAndBidDeadline[1] : null)
                .setSupplyType(dataRows.get(3).child(1).text());

        if (isBudgetSet) {
            //Currency string is surrounded by parentheses, these ones are removed for easier cleaning process
            final String[] estimatedPriceAndCurrency =
                    dataRows.get(5).child(1).text().replace("(", "").replace(")", "").split(" ");
            // Mostly we have the amount and currency, but sometimes there is only currency. See
            // https://webgate.ec.europa.eu/europeaid/online-services/index.cfm?ADSSChck=1490382967699&do=publi.detPUB&
            // searchtype=AS&debpub=06%2F01%2F2005&finpub=06%2F01%2F2005&orderby=upd&orderbyad=Desc&nbPubliList=50&
            // page=1&aoref=120806
            assert estimatedPriceAndCurrency.length == 2 || estimatedPriceAndCurrency.length == 1;

            parsedTender
                    .setEstimatedPrice(new ParsedPrice()
                            .setNetAmount(estimatedPriceAndCurrency[0])
                            .setCurrency(estimatedPriceAndCurrency.length == 2 ? estimatedPriceAndCurrency[1] : null));
        }

        final int documentsRowIndex = 7;
        if (dataRows.size() > documentsRowIndex) {
            final Elements documentElements =
                    JsoupUtils.select("table table > tbody > tr", dataRows.get(documentsRowIndex));

            for (Element documentElement : documentElements) {
                final Element anchor = JsoupUtils.selectFirst("a", documentElement);
                if (anchor != null) {
                    parsedTender
                            .addPublication(new ParsedPublication()
                                    .setSourceFormType(anchor.text())
                                    .setSource(PublicationSources.EA)
                                    .setPublicationDate(documentElement.child(2).text())
                                    .setIsIncluded(false));
                }
            }
        }

        return Arrays.asList(parsedTender);
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }

    @Override
    protected final String countryOfOrigin(final ParsedTender parsed, final RawData raw){
        return null;
    }
}
