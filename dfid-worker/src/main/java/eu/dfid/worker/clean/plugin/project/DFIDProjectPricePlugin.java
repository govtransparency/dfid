package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.BaseNumberPlugin;
import eu.dl.worker.clean.utils.CleanUtils;
import eu.dl.worker.utils.ArrayUtils;
import eu.dl.worker.clean.utils.PriceUtils;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by michal on 21.12.16.
 */
public class DFIDProjectPricePlugin extends BaseNumberPlugin<ParsedProject, DFIDCleanProject> {
    /**
     * Constructor with number format.
     *
     * @param format
     *            number format
     */
    public DFIDProjectPricePlugin(final NumberFormat format) {
        super(format);
    }

    /**
     * Constructor with list of number formats.
     *
     * @param formats
     *            number formats
     */
    public DFIDProjectPricePlugin(final List<NumberFormat> formats) {
        super(formats);
    }

    /**
     * Cleans prices.
     *
     * @param parsedProject
     *            tender with source data
     * @param cleanProject
     *            tender with clean data
     *
     * @return tender with cleaned data
     */
    @Override
    public final DFIDCleanProject clean(final ParsedProject parsedProject, final DFIDCleanProject cleanProject) {
        String country = CleanUtils.getParsedItemCountry(parsedProject);
        
        cleanProject.setDonorFinancings(ArrayUtils.walk(parsedProject.getDonorFinancings(),
                donorFinancing -> PriceUtils.cleanPrice(donorFinancing, formats, country)));

        cleanProject.setEstimatedBorrowerFinancing(PriceUtils.cleanPrice(parsedProject
                .getEstimatedBorrowerFinancing(), formats, country));

        cleanProject.setBorrowerFinancing(PriceUtils.cleanPrice(parsedProject
                .getBorrowerFinancing(), formats, country));

        cleanProject.setEstimatedCost(PriceUtils.cleanPrice(parsedProject
                .getEstimatedCost(), formats, country));

        cleanProject.setFinalCost(PriceUtils.cleanPrice(parsedProject.getFinalCost(), formats, country));

        cleanProject.setGrantAmount(PriceUtils.cleanPrice(parsedProject.getGrantAmount(), formats, country));

        return cleanProject;
    }
}
