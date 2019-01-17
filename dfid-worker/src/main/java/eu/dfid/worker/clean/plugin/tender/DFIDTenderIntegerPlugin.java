package eu.dfid.worker.clean.plugin.tender;


import eu.dfid.dataaccess.dto.clean.DFIDCleanTender;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dl.worker.clean.plugin.BaseNumberPlugin;
import eu.dl.worker.clean.utils.NumberUtils;
import java.text.NumberFormat;
import java.util.List;

/**
 * Plugin used for cleaning dfid integer fields.
 *
 * @author Tomas Mrazek
 */
public final class DFIDTenderIntegerPlugin extends BaseNumberPlugin<DFIDParsedTender, DFIDCleanTender> {
    /**
     * DFIDIntegerPlugin initialization by the number format.
     *
     * @param format
     *            number format
     */
    public DFIDTenderIntegerPlugin(final NumberFormat format) {
        super(format);
    }

    /**
     * DFIDIntegerPlugin initialization by the number formats.
     *
     * @param formats
     *            list of number formats
     */
    public DFIDTenderIntegerPlugin(final List<NumberFormat> formats) {
        super(formats);
    }

    /**
     * Cleans integer fields.
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
        final Integer fiscalYear = NumberUtils.cleanInteger(parsedTender.getFiscalYear(), formats);
        cleanTender.setFiscalYear(fiscalYear);
        logger.debug("Cleaned fiscalYear for parsed tender {}, clean value \"{}\"", parsedTender.getId(), fiscalYear);

        return cleanTender;
    }
}
