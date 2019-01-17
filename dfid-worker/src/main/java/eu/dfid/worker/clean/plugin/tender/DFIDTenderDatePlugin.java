package eu.dfid.worker.clean.plugin.tender;

import eu.dfid.dataaccess.dto.clean.DFIDCleanTender;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dl.worker.clean.plugin.BaseDateTimePlugin;
import eu.dl.worker.clean.utils.DateUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Plugin used to clean dfid date fields.
 *
 * @author Tomas Mrazek
 */
public final class DFIDTenderDatePlugin
    extends BaseDateTimePlugin<DFIDTenderDatePlugin, DFIDParsedTender, DFIDCleanTender> {
    
    /**
     * DatePlugin should be initialised with the pattern of the date.
     *
     * @param formatter
     *            formatter used to parse the date/datetime fields
     */
    public DFIDTenderDatePlugin(final DateTimeFormatter formatter) {
        super(formatter);
    }

    /**
     * Plugin constructor with configuration.
     *
     * @param formatters
     *       list of datetime formatters
     */
    public DFIDTenderDatePlugin(final List<DateTimeFormatter> formatters) {
        super(formatters);
    }

    /**
     * Cleans date and date fields.
     *
     * @param parsedTender
     *            tender with source data
     * @param cleanTender
     *            tender with clean data
     *
     * @return tender with cleaned data
     */
    @Override
    public DFIDCleanTender clean(final DFIDParsedTender parsedTender, final DFIDCleanTender cleanTender) {
        final LocalDate noObjectionDate = DateUtils.cleanDate(parsedTender.getNoObjectionDate(), formatters);
        cleanTender.setNoObjectionDate(noObjectionDate);
        logger.debug("Cleaned noObjectionDate for parsed tender {}, clean value \"{}\"", parsedTender.getId(),
            noObjectionDate);

        final LocalDate estimatedInvitationDate = DateUtils.cleanDate(parsedTender.getEstimatedInvitationDate(),
            formatters);
        cleanTender.setEstimatedInvitationDate(estimatedInvitationDate);
        logger.debug("Cleaned estimatedInvitationDate for parsed tender {}, clean value \"{}\"", parsedTender.getId(),
            estimatedInvitationDate);

        return cleanTender;
    }

}
