package eu.dfid.worker.clean.plugin.tender;

import eu.dfid.dataaccess.dto.clean.DFIDCleanTender;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dl.worker.clean.plugin.BaseCleaningPlugin;
import eu.dl.worker.utils.ArrayUtils;
import eu.dl.worker.clean.utils.StringUtils;
import java.util.List;

/**
 * Plugin used to clean dfid short string fields.
 *
 * @author Tomas Mrazek
 *
 */
public final class DFIDTenderShortTextPlugin extends BaseCleaningPlugin<DFIDParsedTender, DFIDCleanTender> {
    
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
        String procurementType = StringUtils.cleanShortString(parsedTender.getProcurementType());
        cleanTender.setProcurementType(procurementType);
        logger.debug("Cleaned procurementType for parsed tender {}, clean value \"{}\"", parsedTender.getId(),
            procurementType);

        List<String> majorSectors = ArrayUtils.walk(parsedTender.getMajorSectors(), (parsedSector) -> {
                String cleanSector = StringUtils.cleanShortString(parsedSector);
                logger.debug("Cleaned majorSector for parsed tender {}, clean value \"{}\"", parsedTender.getId(),
                    cleanSector);
                return cleanSector;
            });
        cleanTender.setMajorSectors(majorSectors);

        return cleanTender;
    }
}
