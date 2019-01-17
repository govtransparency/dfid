package eu.dfid.worker.idb.parsed;

import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedProjectOperation;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.AbstractDFIDContractsParser;
import eu.dl.dataaccess.dto.parsed.ParsedAddress;
import eu.dl.dataaccess.dto.parsed.ParsedBid;
import eu.dl.dataaccess.dto.parsed.ParsedBody;
import eu.dl.dataaccess.dto.parsed.ParsedPrice;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.dataaccess.dto.parsed.ParsedTenderLot;
import eu.dl.dataaccess.dto.raw.RawData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Awarded contracts (goods & works) parser for Inter-American Development Bank (IDB).
 *
 * @author Marek Mikes
 */
public class IDBAwardedContractsGoodsAndWorksParser extends AbstractDFIDContractsParser {
    private static final String VERSION = "4";


    @Override
    public final List<ParsedTender> parse(final RawData rawTender) {
        List<ParsedTender> parsedTenders = new ArrayList<>();

        final Document resultPage = Jsoup.parse(rawTender.getSourceData());
        
        final Element resultTable = resultPage.getElementsByClass("table-result-container").first();
        
        final Elements rows = resultTable.getElementsByClass("each-result");
        for (int i = 0; i < rows.size(); i +=1) {
            Map<String, String> details = convertResultRowToData(rows.get(i));
            
            DFIDParsedTender parsedTender = new DFIDParsedTender();
             // parse DFID attributes
             parsedTender
                    .setDonorFinancing(new ParsedPrice()
                            .setCurrency("USD")
                            .setNetAmount(details.get("total amount")))
                    .setProcurementType(details.get("type"))
                    .setProject(new ParsedProject()
                            .addOperation(new DFIDParsedProjectOperation()
                                    .setOperationNumber(details.get("operation number"))))
                    .addMajorSector(details.get("executing agency sector"));
             
             
             // parse common attributes
             
            parsedTender
                    .setSupplyType(GOODS_AND_WORKS)
                    .addBuyer(new ParsedBody()
                            .setName(details.get("executing agency"))
                            .setAddress(new ParsedAddress()
                                    .setCountry(details.get("country"))))
                    .addPublication(new ParsedPublication()
                            .setSourceId(details.get("source id"))
                            .setIsIncluded(true)
                            .setSource(PublicationSources.IDB)
                            .setHumanReadableUrl(rawTender.getSourceUrl().toString()))
                    .setContractSignatureDate(details.get("contract signature date"))
                    .addLot(new ParsedTenderLot()
                            .addBid(new ParsedBid()
                                    .setIsWinning(Boolean.TRUE.toString())
                                    .addBidder(new ParsedBody()
                                            .setName(details.get("awarded firm"))
                                            .setAddress(parseBidderAddress(details.get("awarded country"))))))
                    .setFinalPrice(new ParsedPrice()
                            .setCurrency("USD")
                            .setNetAmount(details.get("contract amount")))
                    .setProcedureType(details.get("procurement method"))
                    .setEstimatedCompletionDate(details.get("contract end date"));
            
             parsedTenders.add(parsedTender);

        }
        System.out.println("parsedTenders list: " + parsedTenders.size());
        return parsedTenders;
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
