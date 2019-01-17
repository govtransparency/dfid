package eu.dfid.worker.wb.clean;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.clean.BaseDfidProjectCleaner;
import eu.dfid.worker.clean.plugin.project.DFIDProjectBodyPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectDatePlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectFinancingPlanPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectFinancingSummaryPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectLendingInstrumentPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectLongTextPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectOperationPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectPricePlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectPublicationPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectRegionPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectShortTextPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectStatusPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectWeightedAttributesPlugin;
import eu.dfid.worker.wb.clean.utils.WBCleanUtils;
import eu.dfid.worker.wb.clean.utils.WBMappingUtils;

/**
 * Projects cleaner for World Bank web.
 *
 * @author Tomas Mrazek
 */
public class WBPOWebProjectCleaner extends BaseDfidProjectCleaner {

    private static final String VERSION = "1.0";

    private static final Locale LOCALE = new Locale("en");

    private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("MMMM d, uuuu")
        .toFormatter(LOCALE);

    private static final NumberFormat NUMBER_FORMAT;
    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(LOCALE);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');
        NUMBER_FORMAT = new DecimalFormat("#,##0.###", formatSymbols);
    }

    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
            .registerPlugin("dfid-publications",
                new DFIDProjectPublicationPlugin(NUMBER_FORMAT, DATE_FORMATTER, WBMappingUtils.getFormTypeMapping()))
            .registerPlugin("dfid-operations", new DFIDProjectOperationPlugin())
            .registerPlugin("dfid-shortstrings", new DFIDProjectShortTextPlugin())
            .registerPlugin("dfid-longstrings", new DFIDProjectLongTextPlugin())
            .registerPlugin("dfid-region", new DFIDProjectRegionPlugin(WBMappingUtils.getRegionMapping()))
            .registerPlugin("dfid-status", new DFIDProjectStatusPlugin(WBMappingUtils.getProjectStatusMapping()))
            .registerPlugin("dfid-price", new DFIDProjectPricePlugin(NUMBER_FORMAT))
            .registerPlugin("dfid-body", new DFIDProjectBodyPlugin(WBMappingUtils.getRegionMapping(),
                WBMappingUtils.getCountryFullNameMapping()))
            .registerPlugin("dfid-sectors", new DFIDProjectWeightedAttributesPlugin(NUMBER_FORMAT))
            .registerPlugin("dfid-financingPlan", new DFIDProjectFinancingPlanPlugin(NUMBER_FORMAT))
            .registerPlugin("dfid-financingSummary", new DFIDProjectFinancingSummaryPlugin(DATE_FORMATTER,
                NUMBER_FORMAT))
            .registerPlugin("dfid-date", new DFIDProjectDatePlugin(DATE_FORMATTER))
            .registerPlugin("dfid-lendingInstrument",
                new DFIDProjectLendingInstrumentPlugin(WBMappingUtils.getLendingInstrumentMapping(), null));
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }

    @Override
    protected final DFIDCleanProject postProcessSourceSpecificRules(final ParsedProject parsedItem,
        final DFIDCleanProject cleanItem) {

        return cleanItem;
    }
    
    @Override
    protected final ParsedProject preProcessParsedItem(final ParsedProject parsedItem) {
        return (ParsedProject) WBCleanUtils.replaceNAWithNull(parsedItem);
    }
}
