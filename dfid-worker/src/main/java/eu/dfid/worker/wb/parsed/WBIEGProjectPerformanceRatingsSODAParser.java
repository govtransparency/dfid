package eu.dfid.worker.wb.parsed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedBody;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedBodyEvaluation;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedProjectEvaluation;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.BaseDFIDProjectParser;
import eu.dl.core.UnrecoverableException;
import eu.dl.dataaccess.dto.parsed.ParsedPrice;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.raw.RawData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * IEG (Independent Evaluation Group) World Bank Project Performance Ratings parser.
 *
 * @author Marek Mikes
 */
public class WBIEGProjectPerformanceRatingsSODAParser extends BaseDFIDProjectParser {
    private static final String VERSION = "1";

    @Override
    public final List<ParsedProject> parse(final RawData rawProject) {
        ObjectMapper mapper = new ObjectMapper();
        final JsonNode rootNode;
        try {
            rootNode = mapper.readTree(rawProject.getSourceData());
        } catch (IOException e) {
            throw new UnrecoverableException("Unable to load JSON", e);
        }

        ParsedProject parsedProject = new ParsedProject();
        parsedProject
                .setProjectId(getFieldTextValue(rootNode, "project_id"))
                .setName(getFieldTextValue(rootNode, "project_name"))
                .setRegion(getFieldTextValue(rootNode, "region"))
                .setCountryCode(getFieldTextValue(rootNode, "country_code"))
                .setCountry(getFieldTextValue(rootNode, "country_name"))
                .setApprovalDate(getFieldTextValue(rootNode, "approval_date_2"))
                .setApprovalFiscalYear(getFieldTextValue(rootNode, "approval_fy"))
                .setSectorBoard(getFieldTextValue(rootNode, "sector_board"))
                .setAgreementType(getFieldTextValue(rootNode, "agreement_type"))
                .setFinalCost(new ParsedPrice()
                        .setCurrency("USD")
                        .setNetAmount(getFieldTextValue(rootNode, "lending_project_cost")))
                .addDonorFinancing(new ParsedPrice()
                        .setNetAmount(getFieldTextValue(rootNode, "net_commitment")))
                .setDeactivationDate(getFieldTextValue(rootNode, "deactivation_date_2"))
                .setExitFiscalYear(getFieldTextValue(rootNode, "exit_fy"))
                .addLendingInstrumentType(getFieldTextValue(rootNode, "lending_instrument_type"))
                .setProductLine(getFieldTextValue(rootNode, "product_line"))
                .setEvaluation(new DFIDParsedProjectEvaluation()
                        .setEvaluationDate(getFieldTextValue(rootNode, "ieg_evaldate_2"))
                        .setEvaluationFiscalYear(getFieldTextValue(rootNode, "ieg_evalfy"))
                        .setEvaluationType(getFieldTextValue(rootNode, "ieg_evaltype"))
                        .setProjectErrExAnte(getFieldTextValue(rootNode, "err_at_appraisal"))
                        .setProjectErrExPost(getFieldTextValue(rootNode, "err_at_completion"))
                        .setProjectOutcome(getFieldTextValue(rootNode, "ieg_outcome"))
                        .setRiskToDevelopment(getFieldTextValue(rootNode, "ieg_rdo"))
                        .setProjectImpact(getFieldTextValue(rootNode, "disc_ieg_idimpact"))
                        .setIcrQuality(getFieldTextValue(rootNode, "ieg_icrquality"))
                        .setSustainabilityRating(getFieldTextValue(rootNode, "disc_ieg_sustainability"))
                        .setMeQuality(getFieldTextValue(rootNode, "ieg_mequality"))
                        .setReportUrl(getFieldTextValue(rootNode, "ieg_sourcedocumenturl")))
                .setDonorEvaluation(new DFIDParsedBodyEvaluation()
                        .setQualityExAnte(getFieldTextValue(rootNode, "ieg_bankqualityatentry"))
                        .setSupervisionQuality(getFieldTextValue(rootNode, "ieg_bankqualityofsupervision"))
                        .setOverallPerformance(getFieldTextValue(rootNode, "ieg_overallbankperf")))
                .setBorrower(new DFIDParsedBody()
                        .setBodyEvaluation(new DFIDParsedBodyEvaluation()
                                .setQualityExAnte(getFieldTextValue(rootNode, "disc_ieg_borrprep"))
                                .setImplementingAgencyPerformance(getFieldTextValue(rootNode, "ieg_borrimplementation"))
                                .setGovernmentPerformance(getFieldTextValue(rootNode, "ieg_borrcompliance"))
                                .setOverallPerformance(getFieldTextValue(rootNode, "ieg_overallborrperf"))))
                .addPublication(new ParsedPublication()
                        .setIsIncluded(true)
                        .setSource(PublicationSources.WB)
                        .setHumanReadableUrl(rawProject.getSourceUrl().toString()));

        return new ArrayList<>(Collections.singletonList(parsedProject));
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }

    /**
     * Gets text of field.
     *
     * @param rootNode
     *      root node
     * @param fieldName
     *      name of field
     * @return text of the field or null if the field is not found
     */
    private String getFieldTextValue(final JsonNode rootNode, final String fieldName) {
        JsonNode node = rootNode.get(fieldName);
        return node == null ? null : node.textValue();
    }
}
