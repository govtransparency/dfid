package eu.dfid.worker.wb.parsed;

import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedWeightedAttribute;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.BaseDFIDProjectParser;
import eu.dl.dataaccess.dto.parsed.ParsedAddress;
import eu.dl.dataaccess.dto.parsed.ParsedBody;
import eu.dl.dataaccess.dto.parsed.ParsedPrice;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
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
 * Project parser for WB Project & Operations DFID data.
 *
 * @author Tomas Mrazek
 */
public class WBPOProjectParser extends BaseDFIDProjectParser {
    private static final String VERSION = "1";

    @Override
    public final List<ParsedProject> parse(final RawData rawTender) {
        final Document doc = Jsoup.parse(rawTender.getSourceData());

        ParsedProject parsedProject = new ParsedProject();

        parsedProject
            .setProjectId(JsoupUtils.selectText("id", doc))
            .setRegion(JsoupUtils.selectText("regionname", doc))
            .setProductLine(JsoupUtils.selectText("prodlinetext", doc))
            .setLendingInstrument(JsoupUtils.selectText("lendinginstr", doc))
            .addLendingInstrumentType(JsoupUtils.selectText("lendinginstrtype", doc))
            .setStatus(JsoupUtils.selectText("status", doc))
            .setName(JsoupUtils.selectText("project_name", doc))
            .setApprovalDate(JsoupUtils.selectText("boardapprovaldate", doc))
            .setGrantAmount(new ParsedPrice()
                .setAmountWithVat(JsoupUtils.selectText("grantamt", doc)))
            .setCountry(JsoupUtils.selectText("countryname", doc))
            .setTeamLeader(JsoupUtils.selectText("teamleaderupi", doc))
            .setFinalCost(new ParsedPrice()
                .setAmountWithVat(JsoupUtils.selectText("totalcommamt", doc)))
            .setCountryCode(JsoupUtils.selectText("countrycode", doc))
            .setSectors(parseWeightedAttributes(
                JsoupUtils.selectNumberedElements((num) -> "sector" + num.toString(), doc)))
            .setThemes(parseWeightedAttributes(
                JsoupUtils.selectNumberedElements((num) -> "theme" + num.toString(), doc)))
            .setLocations(parseLocations(JsoupUtils.select("locations > location", doc)))
            .setMajorSectors(parseWeightedAttributes(JsoupUtils.select("majorsector_percent", doc)))
            .setDescription(JsoupUtils.selectText("project_abstract", doc))
            .setClosingDate(JsoupUtils.selectText("closingdate", doc))
                .addPublication(new ParsedPublication()
                        .setIsIncluded(true)
                        .setSource(PublicationSources.WB)
                        .setHumanReadableUrl(rawTender.getSourceUrl().toString()));
        
        Element implementingAgencyNode = JsoupUtils.selectFirst("impagency", doc);
        if (implementingAgencyNode != null) {
            parsedProject.setImplementingAgency(new ParsedBody()
                .setName(implementingAgencyNode.text()));
        }

        Element borrowerNode = JsoupUtils.selectFirst("borrower", doc);
        if (borrowerNode != null) {
            parsedProject.setBorrower(new ParsedBody()
                .setName(borrowerNode.text()));
        }

        return Collections.singletonList(parsedProject);
    }

    @Override
    protected final String getVersion() {
        return VERSION;
    }

    /**
     * Parses weighted attributes from the given list of attribute nodes.
     *
     * @param attributeNodes
     *      attribute nodes
     * @return the list of parsed weighted attributes or null
     */
    private List<DFIDParsedWeightedAttribute> parseWeightedAttributes(final Elements attributeNodes) {
        if (attributeNodes == null  || attributeNodes.isEmpty()) {
            return null;
        }

        List<DFIDParsedWeightedAttribute> attributes = new ArrayList<>();
        for (Element node : attributeNodes) {
            attributes.add(new DFIDParsedWeightedAttribute()
                .setName(JsoupUtils.selectText(node.tagName() + ".name", node))
                .setWeight(JsoupUtils.selectText(node.tagName() + ".percent", node)));
        }

        return attributes;
    }

    /**
     * Parses locations from the given list of location nodes.
     *
     * @param locationNodes
     *      locations nodes
     * @return the list of prased locations or null
     */
    private List<ParsedAddress> parseLocations(final Elements locationNodes) {
        if (locationNodes == null || locationNodes.isEmpty()) {
            return null;
        }

        List<ParsedAddress> locations = new ArrayList<>();
        for (Element node : locationNodes) {
            locations.add(new ParsedAddress()
                .setCountry(JsoupUtils.selectText("location.country", node))
                .setCity(JsoupUtils.selectText("location.geoLocName", node)));
        }

        return locations;
    }
}
