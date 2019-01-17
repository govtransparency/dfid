package eu.dfid.worker.wb.clean;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.codetables.LendingInstrument;
import eu.dfid.dataaccess.dto.codetables.LendingInstrumentType;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.clean.BaseDfidProjectCleaner;
import eu.dfid.worker.clean.plugin.project.DFIDProjectBodyPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectDatePlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectLendingInstrumentPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectLocationPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectLongTextPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectPricePlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectPublicationPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectRegionPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectShortTextPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectStatusPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectWeightedAttributesPlugin;
import eu.dfid.worker.wb.clean.utils.WBCleanUtils;
import eu.dfid.worker.wb.clean.utils.WBMappingUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Project cleaner for World Bank Projects and Operations.
 *
 * @author Tomas Mrazek
 */
public class WBPOProjectCleaner extends BaseDfidProjectCleaner {

    private static final String VERSION = "1.0";

    private static final Locale LOCALE = new Locale("en");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'",
            LOCALE);

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
                .registerPlugin("dfid-shortstrings", new DFIDProjectShortTextPlugin())
                .registerPlugin("dfid-region", new DFIDProjectRegionPlugin(WBMappingUtils.getRegionMapping()))
                .registerPlugin("dfid-lendingInstrument", new DFIDProjectLendingInstrumentPlugin(
                        getLendingInstrumentMapping(), getLendingInstrumentTypeMapping()))
                .registerPlugin("dfid-status", new DFIDProjectStatusPlugin(WBMappingUtils.getProjectStatusMapping()))
                .registerPlugin("dfid-date", new DFIDProjectDatePlugin(DATE_FORMATTER))
                .registerPlugin("dfid-price", new DFIDProjectPricePlugin(NUMBER_FORMAT))
                .registerPlugin("dfid-weightedAttributes", new DFIDProjectWeightedAttributesPlugin(NUMBER_FORMAT))
                .registerPlugin("dfid-locations", new DFIDProjectLocationPlugin())
                .registerPlugin("dfid-body", new DFIDProjectBodyPlugin(null, null))
                .registerPlugin("dfid-longtext", new DFIDProjectLongTextPlugin())
                .registerPlugin("publications", new DFIDProjectPublicationPlugin(NUMBER_FORMAT, DATE_FORMATTER, null));
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

    /**
     * @return lending instrument mapping
     */
    private Map<Enum, List<String>> getLendingInstrumentMapping() {
        Map<Enum, List<String>> mapping = WBMappingUtils.getLendingInstrumentMapping();

        /*
        LendingInstrument.PROGRAM_FOR_RESULTS mapping update:

        WBMappingUtils.getLendingInstrumentMapping produces singleton lists, therefore is necessary to create instance
        of ArrayList because in next step will be added another mapped string for LendingInstrument.PROGRAM_FOR_RESULTS
        key.
        */
        List<String> programForResultsStrings = new ArrayList<>(mapping.get(LendingInstrument.PROGRAM_FOR_RESULTS));
        programForResultsStrings.add("Program-for-results");
        mapping.put(LendingInstrument.PROGRAM_FOR_RESULTS, programForResultsStrings);

        return mapping;
    }

    /**
     * @return lending instrument type mapping
     */
    private Map<Enum, List<String>> getLendingInstrumentTypeMapping() {
        Map<Enum, List<String>> mapping = new HashMap<>();

        mapping.put(LendingInstrumentType.IN, Collections.singletonList("IN"));
        mapping.put(LendingInstrumentType.AD, Collections.singletonList("AD"));
        mapping.put(LendingInstrumentType.PR, Collections.singletonList("PR"));
        mapping.put(LendingInstrumentType.UNIDENTIFIED, Collections.singletonList("XX"));

        return mapping;
    }

    @Override
    protected final ParsedProject preProcessParsedItem(final ParsedProject parsedItem) {
        return (ParsedProject) WBCleanUtils.replaceNAWithNull(parsedItem);
    }
}
