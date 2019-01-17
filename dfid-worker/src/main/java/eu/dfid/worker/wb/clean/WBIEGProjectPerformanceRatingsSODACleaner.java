package eu.dfid.worker.wb.clean;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.codetables.DFIDRegion;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.clean.BaseDfidProjectCleaner;
import eu.dfid.worker.clean.plugin.project.DFIDProjectBodyEvaluationPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectBodyPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectDatePlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectEvaluationPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectIntegerPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectLendingInstrumentPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectPricePlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectPublicationPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectRegionPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectShortTextPlugin;
import eu.dfid.worker.wb.clean.utils.WBCleanUtils;
import eu.dfid.worker.wb.clean.utils.WBMappingUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Contract cleaner for World Bank.
 *
 * @author Tomas Mrazek
 */
public class WBIEGProjectPerformanceRatingsSODACleaner extends BaseDfidProjectCleaner {

    private static final String VERSION = "1.0";

    private static final Locale LOCALE = new Locale("en");

    private static final NumberFormat NUMBER_FORMAT;

    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(LOCALE);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');
        NUMBER_FORMAT = new DecimalFormat("#,##0.###", formatSymbols);
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/uu");

    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
                .registerPlugin("dfid-shortstring", new DFIDProjectShortTextPlugin())
                .registerPlugin("dfid-region", new DFIDProjectRegionPlugin(getRegionMapping()))
                .registerPlugin("dfid-date", new DFIDProjectDatePlugin(DATE_FORMATTER))
                .registerPlugin("dfid-integer", new DFIDProjectIntegerPlugin(NUMBER_FORMAT))
                .registerPlugin("dfid-price", new DFIDProjectPricePlugin(NUMBER_FORMAT))
                .registerPlugin("dfid-lendingInstrument", new DFIDProjectLendingInstrumentPlugin(null,
                        WBMappingUtils.getLendingInstrumentTypeMapping()))
                .registerPlugin("dfid-body", new DFIDProjectBodyPlugin(null, null))
                .registerPlugin("dfid-bodyEvaluation", new DFIDProjectBodyEvaluationPlugin())
                .registerPlugin("dfid-evaluation", new DFIDProjectEvaluationPlugin(NUMBER_FORMAT, DATE_FORMATTER))
                .registerPlugin("publications", new DFIDProjectPublicationPlugin(NUMBER_FORMAT, DATE_FORMATTER, null));
    }

    /**
     * @return region mapping
     */
    private Map<Enum, List<String>> getRegionMapping() {
        HashMap<Enum, List<String>> mapping = new HashMap<>();

        mapping.put(DFIDRegion.EAST_ASIA_AND_PACIFIC, Collections.singletonList("EAP"));
        mapping.put(DFIDRegion.EUROPE_AND_CENTRAL_ASIA, Collections.singletonList("ECA"));
        mapping.put(DFIDRegion.AFRICA, Collections.singletonList("AFR"));
        mapping.put(DFIDRegion.MIDDLE_EAST_AND_NORTH_AFRICA, Collections.singletonList("MNA"));
        mapping.put(DFIDRegion.OTHER, Collections.singletonList("OTH"));
        mapping.put(DFIDRegion.SOUTH_ASIA, Collections.singletonList("SAR"));
        mapping.put(DFIDRegion.LATIN_AMERICA_AND_CARIBBEAN, Collections.singletonList("LCR"));

        return mapping;
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
