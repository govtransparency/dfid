package eu.dfid.worker.clean.plugin.tender;

import eu.dfid.dataaccess.dto.clean.DFIDCleanTender;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dl.worker.clean.plugin.BaseNumberPlugin;
import eu.dl.worker.clean.utils.CleanUtils;
import eu.dl.worker.clean.utils.PriceUtils;

import java.text.NumberFormat;
import java.util.List;

/**
 * Plugin used to clean DFID price.
 *
 * @author Marek Mikes
 */
public class DFIDTenderPricePlugin extends BaseNumberPlugin<DFIDParsedTender, DFIDCleanTender> {
    /**
     * Constructor with number formats.
     *
     * @param format
     *            number formats
     */
    public DFIDTenderPricePlugin(final NumberFormat format) {
        super(format);
    }

    /**
     * Constructor with list of number formats.
     *
     * @param formats
     *            number formats
     */
    public DFIDTenderPricePlugin(final List<NumberFormat> formats) {
        super(formats);
    }

    /**
     * Cleans price.
     *
     * @param parsedTender
     *            tender with source data
     * @param cleanTender
     *            tender with clean data
     *
     * @return tender with cleaned data
     */
    @Override
    public final DFIDCleanTender clean(final DFIDParsedTender parsedTender, final DFIDCleanTender cleanTender) {
        logger.debug("Cleaning donor financing price for parsed tender {} starts", parsedTender.getId());
        cleanTender.setDonorFinancing(PriceUtils.cleanPrice(parsedTender.getDonorFinancing(), formats,
            CleanUtils.getParsedItemCountry(parsedTender)));
        logger.debug("Cleaning donor financing price for parsed tender {} finished", parsedTender.getId());

        return cleanTender;
    }
}
