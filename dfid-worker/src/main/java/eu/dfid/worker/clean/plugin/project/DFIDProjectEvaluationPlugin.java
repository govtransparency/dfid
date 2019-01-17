package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.clean.DFIDCleanProjectEvaluation;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedProjectEvaluation;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.BaseDateTimePlugin;
import eu.dl.worker.clean.utils.DateUtils;
import eu.dl.worker.clean.utils.NumberUtils;
import eu.dl.worker.clean.utils.StringUtils;
import eu.dl.worker.clean.utils.URLSchemeType;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Plugin used to clean dfid project evaluation.
 *
 * @author Tomas Mrazek
 */
public final class DFIDProjectEvaluationPlugin
    extends BaseDateTimePlugin<DFIDProjectEvaluationPlugin, ParsedProject, DFIDCleanProject> {

    private final List<NumberFormat> numberFormat;

    /**
     * Plugin constructor with configuration.
     *
     * @param numberFormat
     *      number format
     * @param formatter
     *      datetime formatter
     */
    public DFIDProjectEvaluationPlugin(final NumberFormat numberFormat, final DateTimeFormatter formatter) {
        super(formatter);
        this.numberFormat = Arrays.asList(numberFormat);
    }

    /**
     * Plugin constructor with configuration.
     *
     * @param numberFormat
     *      number format
     * @param formatters
     *      list of datetime formatters
     */
    public DFIDProjectEvaluationPlugin(final NumberFormat numberFormat, final List<DateTimeFormatter> formatters) {
        super(formatters);
        this.numberFormat = Arrays.asList(numberFormat);
    }

    /**
     * Plugin constructor with configuration.
     *
     * @param numberFormat
     *      list of number formats
     * @param formatters
     *      list of datetime formatters
     */
    public DFIDProjectEvaluationPlugin(final List<NumberFormat> numberFormat,
        final List<DateTimeFormatter> formatters) {
        super(formatters);
        this.numberFormat = numberFormat;
    }

    /**
     * Cleans dfid project body fields.
     *
     * @param parsedProject
     *            project with source data
     * @param cleanProject
     *            project with clean data
     *
     * @return project with cleaned data
     */
    @Override
    public DFIDCleanProject clean(final ParsedProject parsedProject, final DFIDCleanProject cleanProject) {

        logger.debug("Cleaning of evaluation for parsed project {} starts", parsedProject.getId());

        DFIDParsedProjectEvaluation evaluation = parsedProject.getEvaluation();

        if (evaluation != null) {
            cleanProject.setEvaluation(new DFIDCleanProjectEvaluation()
                .setEvaluationDate(DateUtils.cleanDate(evaluation.getEvaluationDate(), formatters))
                .setEvaluationFiscalYear(NumberUtils.cleanInteger(evaluation.getEvaluationFiscalYear(), numberFormat))
                .setEvaluationType(StringUtils.cleanShortString(evaluation.getEvaluationType()))
                .setIcrQuality(StringUtils.cleanShortString(evaluation.getIcrQuality()))
                .setMeQuality(StringUtils.cleanShortString(evaluation.getMeQuality()))
                .setProjectErrExAnte(NumberUtils.cleanInteger(evaluation.getProjectErrExAnte(), numberFormat))
                .setProjectErrExPost(NumberUtils.cleanInteger(evaluation.getProjectErrExPost(), numberFormat))
                .setProjectImpact(StringUtils.cleanShortString(evaluation.getProjectImpact()))
                .setProjectOutcome(StringUtils.cleanShortString(evaluation.getProjectOutcome()))
                .setReportUrl(StringUtils.cleanURL(evaluation.getReportUrl(), URLSchemeType.HTTP))
                .setRiskToDevelopment(StringUtils.cleanShortString(evaluation.getRiskToDevelopment()))
                .setSustainabilityRating(StringUtils.cleanShortString(evaluation.getSustainabilityRating()))
            );
        }

        logger.debug("Cleaning of evaluation for parsed project {} finished", parsedProject.getId());

        return cleanProject;
    }
}
