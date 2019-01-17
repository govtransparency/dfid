package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.dataaccess.dto.generic.Funding;
import eu.dl.dataaccess.dto.parsed.ParsedFunding;
import eu.dl.worker.clean.plugin.BaseNumberPlugin;
import eu.dl.worker.clean.utils.CleanUtils;
import eu.dl.worker.utils.ArrayUtils;
import eu.dl.worker.clean.utils.NumberUtils;
import eu.dl.worker.clean.utils.PriceUtils;
import eu.dl.worker.clean.utils.StringUtils;

import java.text.NumberFormat;

/**
 * Created by michal on 21.12.16.
 */
public class DFIDProjectFundingPlugin extends BaseNumberPlugin<ParsedProject, DFIDCleanProject> {

    /**
     * Default constructor.
     *
     * @param format format
     */
    public DFIDProjectFundingPlugin(final NumberFormat format) {
        super(format);
    }

    @Override
    public final DFIDCleanProject clean(final ParsedProject parsedItem, final DFIDCleanProject cleanItem) {
        cleanItem.setFundings(ArrayUtils.walk(parsedItem.getFundings(), funding -> cleanFunding(funding,
            CleanUtils.getParsedItemCountry(parsedItem))));

        return cleanItem;
    }

    /**
     * Clean funding.
     *
     * @param funding funding to clean
     * @param country country
     * @return Funding
     */
    private Funding cleanFunding(final ParsedFunding funding, final String country) {
        return new Funding()
                .setSource(StringUtils.cleanShortString(funding.getSource()))
                .setIsEuFund(StringUtils.cleanBoolean(funding.getIsEuFund()))
                .setProgramme(StringUtils.cleanShortString(funding.getProgramme()))
                .setAmount(PriceUtils.cleanPrice(funding.getAmount(), formats, country))
                .setProportion(NumberUtils.cleanInteger(funding.getProportion(), formats));
    }
}
