package eu.dfid.worker.clean.plugin.tender;

import eu.dfid.dataaccess.dto.clean.DFIDCleanTender;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dl.worker.clean.plugin.BaseCleaningPlugin;
import eu.dl.worker.clean.utils.StringUtils;

/**
 * Plugin used to clean dfid short string fields.
 *
 * @author Tomas Mrazek
 *
 */
public final class DFIDTenderLongTextPlugin extends BaseCleaningPlugin<DFIDParsedTender, DFIDCleanTender> {
    
    /**
     * Cleans dfid tender short string fields.
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
        String legalBasis = StringUtils.cleanLongString(parsedTender.getLegalBasis());
        cleanTender.setLegalBasis(legalBasis);
        logger.debug("Cleaned legalBasis for parsed tender {}, clean value \"{}\"", parsedTender.getId(),
            legalBasis);

        String purpose = StringUtils.cleanLongString(parsedTender.getPurpose());
        cleanTender.setPurpose(purpose);
        logger.debug("Cleaned purpose for parsed tender {}, clean value \"{}\"", parsedTender.getId(),
            purpose);

        return cleanTender;
    }
}
