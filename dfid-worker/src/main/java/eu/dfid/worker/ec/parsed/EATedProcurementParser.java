package eu.dfid.worker.ec.parsed;

import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.BaseDFIDTenderParser;
import eu.dl.core.UnrecoverableException;
import eu.dl.dataaccess.dto.parsed.ParsedAddress;
import eu.dl.dataaccess.dto.parsed.ParsedBody;
import eu.dl.dataaccess.dto.parsed.ParsedCPV;
import eu.dl.dataaccess.dto.parsed.ParsedFunding;
import eu.dl.dataaccess.dto.parsed.ParsedPrice;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.dataaccess.dto.raw.RawData;
import eu.dl.worker.utils.jsoup.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Procurement parser for EuropeAid of European Commission that are published in TED.
 *
 * @author Tomas Mrazek
 */
public final class EATedProcurementParser extends BaseDFIDTenderParser {
    private static final String VERSION = "1";
    
    @Override
    public List<ParsedTender> parse(final RawData rawTender) {
        final Document resultPage = Jsoup.parse(rawTender.getSourceData(), "", Parser.xmlParser());
        
        if (!isEuropeAid(resultPage)) {
            logger.error("Raw tender {} doesn't seems like EuropAid TED form", rawTender.getId());
            throw new UnrecoverableException("Raw tender doesn't seems like EuropAid TED form");
        }

        final Element codedDataSection = EATedProcurementParserUtils.parseCodeDataSectionNode(resultPage);
        final Element translationSection = EATedProcurementParserUtils.parseTranslationSectionNode(resultPage);
        final Element formSection = EATedProcurementParserUtils.parseFormSectionNode(resultPage);

        final Element noticeData = JsoupUtils.selectFirst("NOTICE_DATA", codedDataSection);
        final Element codifData = JsoupUtils.selectFirst("CODIF_DATA", codedDataSection);        
        
        final Element mlTiDoc = JsoupUtils.selectFirst("ML_TI_DOC[LG=EN]", translationSection);

        final Element originNode = EATedProcurementParserUtils.parseOriginNode(formSection);

        final String sourceFormType = JsoupUtils.selectText("TD_DOCUMENT_TYPE", codifData);

        //tender.siteVisit -  JsoupUtils.getFirstValueByLabel(originNode, "Information meeting and/or site visit")
        
        //tender.estimatedIvitationDate -
        //  JsoupUtils.getFirstValueByLabel(originNode, "Provisional date of invitation to tender")
        
        //tender.bidsRecipient - JsoupUtils.getFirstValueByLabel(originNode, "How applications may be submitted")
        
        //tender.furtherInformationProfivder -
        //  JsoupUtils.getFirstValueByLabel(originNode,"How to obtain the tender dossier")
        
        final DFIDParsedTender parsedTender = (DFIDParsedTender) new DFIDParsedTender()
            .setProject(parseProject(originNode))
            .setLegalBasis(JsoupUtils.getFirstValueByLabel(originNode, "(?i)Legal basis"))
            .setEstimatedInvitationDate(
                JsoupUtils.getFirstValueByLabel(originNode, "(?i)Provisional date of invitation to tender"))
            .setTitle(JsoupUtils.selectText("TI_TEXT", mlTiDoc))
            .addBuyer(parseBuyer(JsoupUtils.getFirstLabeledValueNode(originNode, "(?i)Contracting authority"),
                noticeData, mlTiDoc))
            .addPublication(new ParsedPublication()
                .setPublicationDate(JsoupUtils.selectText("REF_OJS > DATE_PUB", codedDataSection))
                .setSourceId(JsoupUtils.selectText("NOTICE_DATA > NO_DOC_OJS", noticeData))
                .setHumanReadableUrl(
                    JsoupUtils.selectText("URI_LIST > URI_DOC[LG=EN]", noticeData))
                .setDispatchDate(JsoupUtils.selectText("DS_DATE_DISPATCH", codifData))
                .setSourceFormType(sourceFormType)
                .setSourceTenderId(parseSourceTenderId(originNode))
                .setSource(PublicationSources.EU_TED)
                .setIsIncluded(true))
            .addPublications(parsePreviousPublications(originNode))
            .setCpvs(parseCPV(JsoupUtils.select("ORIGINAL_CPV", noticeData)))
            .setBidDeadline(JsoupUtils.selectText("DT_DATE_FOR_SUBMISSION", codifData))            
            .setSupplyType(JsoupUtils.selectText("NC_CONTRACT_NATURE", codifData))
            .setProcedureType(parseProcedureType(codifData, originNode))
            .setAddressOfImplementation(new ParsedAddress()
                .setCountry(JsoupUtils.selectText("FD_OTH_NOT > STI_DOC:eq(2)", originNode)))
            .addFunding(new ParsedFunding()
                .setProgramme(JsoupUtils.getFirstValueByLabel(originNode, "(?i)Financing"))
                .setSource(JsoupUtils.selectText("RP_REGULATION", codifData)))
            .setDescription(JsoupUtils.getFirstValueByLabel(originNode, "(?i)Contract description"))            
            .setEstimatedStartDate(
                JsoupUtils.getFirstValueByLabel(originNode, "(?i)Provisional commencement date of the contract"))
            .setSelectionMethod(JsoupUtils.getFirstValueByLabel(originNode, "(?i)Award criteria"))
            .setAreVariantsAccepted(JsoupUtils.getFirstValueByLabel(originNode, "(?i)Number of tenders"));

        String estPrice = EATedProcurementParserUtils.selectTextByLabel("(?i)(Maximum )?budget", originNode);
        if (estPrice != null) {
            parsedTender.setEstimatedPrice(new ParsedPrice().setNetAmount(estPrice).setCurrency("EUR"));
        }

        if (sourceFormType != null) {
            switch (sourceFormType.toLowerCase()) {
                case "contract notice":
                case "prior information notice":
                case "prior information notice without call for competition":
                    return EATedProcurementContractNoticeHandler.parse(parsedTender, resultPage);
                case "contract award notice":
                case "contract award":
                case "voluntary ex ante transparency notice":
                    return EATedProcurementContractAwardHandler.parse(parsedTender, resultPage);
                default:
                    logger.error("No parser implemented for {} form type.", sourceFormType);
                    throw new UnrecoverableException("Could not create appropriate parsed.");
            }
        }

        logger.error("Unable to choose parser because of the sourceFormType was not found.", sourceFormType);
        throw new UnrecoverableException("Could not create appropriate parsed.");
    }

    /**
     * Parses previous publications.
     *
     * @param context
     *      context that includes previous publications
     * @return the list of publications or an empty list if no publication was found
     */
    private List<ParsedPublication> parsePreviousPublications(final Element context) {
        Element publicationsNode =
            JsoupUtils.getFirstLabeledValueNode(context, "(?i)Previous publication\\(s\\) in Official Journal S");

        if (publicationsNode == null) {
            return Collections.emptyList();
        }

        Elements nodes = publicationsNode.select("p:matchesOwn([0-9]{4}/S [0-9]+\\-[0-9]{6})");

        List<ParsedPublication> publications = new ArrayList<>();
        for (Element n : nodes) {
            Matcher m = Pattern.compile("(?<id>\\d{4}/S \\d+\\-\\d{6}).+(?<date>\\d{1,2}\\.\\d{1,2}\\.\\d{4})")
                .matcher(n.text());

            if (m.find()) {
                publications.add(new ParsedPublication()
                    .setIsIncluded(false)
                    .setSource(PublicationSources.EU_TED)
                    .setSourceId(m.group("id"))
                    .setPublicationDate(m.group("date")));
            }
        }

        return publications;
    }

    /**
     * @param codifData
     *      CODIF_DATA node
     * @param originNode
     *      origin node
     * @return procedure type or null
     */
    private String parseProcedureType(final Element codifData, final Element originNode) {
        String procedureType = JsoupUtils.selectText("PR_PROC", codifData);
        if (procedureType.equals("Not applicable")) {
            procedureType = JsoupUtils.getFirstValueByLabel(originNode, "(?i)Procedure");
        }
        
        return procedureType != null ? StringUtils.strip(procedureType, ".") : null;
    }

    /**
     * @param context
     *      context that includes project data
     * @return parsed project
     */
    private ParsedProject parseProject(final Element context) {
        Element node = JsoupUtils.getFirstLabeledValueNode(context, "(?i)Programme title");
        if (node == null) {
            node = JsoupUtils.getFirstLabeledValueNode(context, "(?i)Programme");
        }
        
        return node != null ? new ParsedProject().setName(node.text()) : null;
    }

    /**
     * @param context
     *      context that includes source tender id
     * @return source tender id
     */
    private String parseSourceTenderId(final Element context) {
        final String rawSourceTenderId = JsoupUtils.getFirstValueByLabel(context, "(?i)Publication reference");
        
        //sometimes publication reference and date appears in one chapter separated by "and". Source tender id is first.
        return (rawSourceTenderId != null ? rawSourceTenderId.split(" and ")[0] : null);
    }

    /**
     * @param context
     *      context that includes buyer data
     * @param noticeDataNode
     *      NOTICE_DATA element
     * @param mlTiDocNode
     *      ML_TI_DOC element
     * @return buyer
     */
    private ParsedBody parseBuyer(final Element context, final Element noticeDataNode, final Element mlTiDocNode) {
        final Element node = JsoupUtils.getFirstLabeledValueNode(context, "(?i)Contracting authority");

        return new ParsedBody()
            .setName(JsoupUtils.selectText("ORGANISATION", node))
            .setAddress(new ParsedAddress()
                .setRawAddress(JsoupUtils.selectText("ADDRESS", node))
                .setPostcode(JsoupUtils.selectText("POSTAL_CODE", node))
                //alternative for country is mlTiDoc > TI_CY (fullname not ISO code)
                .setCountry(JsoupUtils.selectAttribute("ISO_COUNTRY", "VALUE", noticeDataNode))
                .setCity(JsoupUtils.selectText("TI_TOWN", mlTiDocNode)))
            .setPhone(JsoupUtils.selectText("PHONE", node))
            .setEmail(JsoupUtils.selectText("E_MAIL", node));
    }
    
    /**
     * @param cpvNodes
     *      nodes that include CPVs data
     * @return list of CPVs
     */
    private List<ParsedCPV> parseCPV(final Elements cpvNodes) {
        if (cpvNodes == null) {
            return null;
        }
        
        final List<ParsedCPV> cpvs = new ArrayList<>();
        for (Element node : cpvNodes) {
            cpvs.add(new ParsedCPV().setCode(node.attr("CODE")));
        }

        return cpvs;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }
    
    /**
     * Checks whether {@code document} contains EuropeAid indetifier.
     *
     * @param document
     *      parsed document 
     * @return true only and only if file contains EuropeAid data
     */
    private boolean isEuropeAid(final Document document) {        
        final String regulationCode =
            JsoupUtils.selectText("TED_EXPORT > CODED_DATA_SECTION > CODIF_DATA > RP_REGULATION", document);

        return regulationCode != null && regulationCode.equalsIgnoreCase("External aid and European Development Fund");
    }

    @Override
    public String countryOfOrigin(final ParsedTender parsed, final RawData raw){
        return null;
    }
}
