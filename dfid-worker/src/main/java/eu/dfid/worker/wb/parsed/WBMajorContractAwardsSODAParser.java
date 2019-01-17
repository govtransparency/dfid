package eu.dfid.worker.wb.parsed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.BaseDFIDTenderParser;
import eu.dl.core.UnrecoverableException;
import eu.dl.dataaccess.dto.parsed.ParsedAddress;
import eu.dl.dataaccess.dto.parsed.ParsedBid;
import eu.dl.dataaccess.dto.parsed.ParsedBody;
import eu.dl.dataaccess.dto.parsed.ParsedPrice;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.dataaccess.dto.parsed.ParsedTenderLot;
import eu.dl.dataaccess.dto.raw.RawData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Major Contract Awards data (downloaded through Socrata Open Data API) parser for World Bank.
 *
 * @author Marek Mikes
 */
public class WBMajorContractAwardsSODAParser extends BaseDFIDTenderParser {
    private static final String VERSION = "1";

    @Override
    public final List<ParsedTender> parse(final RawData rawTender) {
        ObjectMapper mapper = new ObjectMapper();
        final JsonNode rootNode;
        try {
            rootNode = mapper.readTree(rawTender.getSourceData());
        } catch (IOException e) {
            throw new UnrecoverableException("Unable to load JSON", e);
        }

        DFIDParsedTender parsedTender = new DFIDParsedTender();

        // parse DFID attributes
        parsedTender
                .setProject(new ParsedProject()
                        .setRegion(rootNode.get("region").textValue())
                        .setBorrower(new ParsedBody()
                                .setAddress(new ParsedAddress()
                                        .setCountry(rootNode.get("borrower_country_code").textValue())))
                        .setProjectId(rootNode.get("project_id").textValue())
                        .setName(rootNode.get("project_name").textValue())
                        .setProductLine(rootNode.get("product_line").textValue()))
                .setFiscalYear(rootNode.get("fiscal_year").textValue())
                .setProcurementType(rootNode.get("procurement_type").textValue())
                .setMajorSectors(Collections.singletonList(rootNode.get("major_sector").textValue()));

        // parse common attributes
        parsedTender
                .addPublication(new ParsedPublication()
                        .setSourceFormType("contract award")
                        .setLastUpdate(rootNode.get("as_of_date").textValue())
                        .setIsIncluded(true)
                        .setSource(PublicationSources.WB)
                        .setHumanReadableUrl(rawTender.getSourceUrl().toString()))
                .setSupplyType(rootNode.get("procurement_category").textValue())
                .setSelectionMethod(rootNode.get("procurement_method").textValue())
                .addLot(new ParsedTenderLot()
                        .setContractNumber(rootNode.get("wb_contract_number").textValue())
                        .addBid(new ParsedBid()
                                .setIsWinning(Boolean.TRUE.toString())
                                .addBidder(new ParsedBody()
                                        .setName(rootNode.get("supplier").textValue())
                                        .setAddress(new ParsedAddress()
                                                .setCountry(rootNode.get("supplier_country_code").textValue())))))
                .setDescription(rootNode.get("contract_description").textValue())
                .setContractSignatureDate(rootNode.get("contract_signing_date").textValue())
                .setFinalPrice(new ParsedPrice()
                        .setCurrency("USD")
                        .setNetAmount(rootNode.get("supplier_contract_amount_usd").textValue()))
                .setBuyerAssignedId(rootNode.get("borrower_contract_reference_number").textValue());

        return new ArrayList<>(Collections.singletonList(parsedTender));
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
