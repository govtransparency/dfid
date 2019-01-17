package eu.dfid.worker.clean.plugin.project;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.clean.utils.CleanUtils;
import eu.dl.worker.clean.plugin.BaseDateTimePlugin;

/**
 * Plugin used to clean DFID project publications.
 *
 * @author Tomas Mrazek
 */
public class DFIDProjectPublicationPlugin
    extends BaseDateTimePlugin<DFIDProjectPublicationPlugin, ParsedProject, DFIDCleanProject> {

    private final List<NumberFormat> numberFormat;
    private final Map<Enum, List<String>> formTypeMapping;

    /**
     * Plugin constructor with configuration.
     *
     * @param numberFormat
     *      number format
     * @param formatter
     *      datetime formatter
     * @param formTypeMapping
     *      mapping for form type
     */
    public DFIDProjectPublicationPlugin(final NumberFormat numberFormat, final DateTimeFormatter formatter,
        final Map<Enum, List<String>> formTypeMapping) {
        super(formatter);
        this.numberFormat = Arrays.asList(numberFormat);
        this.formTypeMapping = formTypeMapping;
    }

    /**
     * Plugin constructor with configuration.
     *
     * @param numberFormat
     *      number format
     * @param formatters
     *      list of datetime formatters
     * @param formTypeMapping
     *      mapping for form type
     */
    public DFIDProjectPublicationPlugin(final NumberFormat numberFormat, final List<DateTimeFormatter> formatters,
        final Map<Enum, List<String>> formTypeMapping) {
        super(formatters);
        this.numberFormat = Arrays.asList(numberFormat);
        this.formTypeMapping = formTypeMapping;
    }

    /**
     * Plugin constructor with configuration.
     *
     * @param numberFormat
     *      list of number formats
     * @param formatters
     *      list of datetime formatters
     * @param formTypeMapping
     *      mapping for form type
     */
    public DFIDProjectPublicationPlugin(final List<NumberFormat> numberFormat, final List<DateTimeFormatter> formatters,
        final Map<Enum, List<String>> formTypeMapping) {
        super(formatters);
        this.numberFormat = numberFormat;
        this.formTypeMapping = formTypeMapping;
    }

    /**
     * Cleans DFID publications.
     *
     * @param parsed
     *            tender with source data
     * @param clean
     *            tender with clean data
     *
     * @return tender with cleaned data
     */
    @Override
    public final DFIDCleanProject clean(final ParsedProject parsed, final DFIDCleanProject clean) {
        if (parsed.getPublications() != null) {
            logger.debug("Cleaning publications for parsed project {} starts", parsed.getId());

            clean.setPublications(CleanUtils.cleanPublications(
                parsed.getPublications(), numberFormat, formatters, formTypeMapping));

            logger.debug("Cleaning publications for parsed project {} finished", parsed.getId());
        }

        return clean;
    }
}
