package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanFinancing;
import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedFinancing;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.BaseNumberPlugin;
import eu.dl.worker.clean.utils.CleanUtils;
import eu.dl.worker.utils.ArrayUtils;
import eu.dl.worker.clean.utils.PriceUtils;
import eu.dl.worker.clean.utils.StringUtils;
import java.text.NumberFormat;

/**
 * Created by michal on 21.12.16.
 */
public class DFIDProjectFinancingPlanPlugin extends BaseNumberPlugin<ParsedProject, DFIDCleanProject> {
    /**
     * Default constructor.
     *
     * @param format format
     */
    public DFIDProjectFinancingPlanPlugin(final NumberFormat format) {
        super(format);
    }

    @Override
    public final DFIDCleanProject clean(final ParsedProject parsedItem, final DFIDCleanProject cleanItem) {
        cleanItem.setFinancingPlan(ArrayUtils.walk(parsedItem.getFinancingPlan(),
                item -> cleanFinancingSummary(item, CleanUtils.getParsedItemCountry(parsedItem))));

        return cleanItem;
    }

    /**
     * Clean financing summary.
     *
     * @param item item to clean
     * @param country country
     * @return clean item
     */
    private DFIDCleanFinancing cleanFinancingSummary(final DFIDParsedFinancing item, final String country) {
        return new DFIDCleanFinancing()
            .setCommitment(PriceUtils.cleanPrice(item.getCommitment(), formats, country))
            .setFinancier(StringUtils.cleanShortString(item.getFinancier()));
    }
}
