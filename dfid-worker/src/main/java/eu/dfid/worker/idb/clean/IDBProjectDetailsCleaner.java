package eu.dfid.worker.idb.clean;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.codetables.ProjectStatus;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.clean.BaseDfidProjectCleaner;
import eu.dfid.worker.clean.plugin.project.DFIDProjectDatePlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectFinancingSummaryPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectFundingPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectLongTextPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectOperationPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectPricePlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectPublicationPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectShortTextPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectStatusPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectWeightedAttributesPlugin;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by michal on 20.12.16.
 */
public class IDBProjectDetailsCleaner extends BaseDfidProjectCleaner {
    private static final String VERSION = "2";

    private static final Locale LOCALE = new Locale("en");
    private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("MMM d, yy")
        .toFormatter(LOCALE);
    
     private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
        new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("yyyy/MM/dd")
                .toFormatter(Locale.US),
        new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("LLL/dd/yyyy")
                .toFormatter(Locale.US));

    private static final NumberFormat NUMBER_FORMAT;
    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(LOCALE);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');
        NUMBER_FORMAT = new DecimalFormat("#,##0.###", formatSymbols);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
                .registerPlugin("date", new DFIDProjectDatePlugin(DATE_FORMATTERS))
                .registerPlugin("prices", new DFIDProjectPricePlugin(NUMBER_FORMAT))
                .registerPlugin("shortString", new DFIDProjectShortTextPlugin())
                .registerPlugin("longString", new DFIDProjectLongTextPlugin())
                .registerPlugin("publications", new DFIDProjectPublicationPlugin(null, DATE_FORMATTER, null))
                .registerPlugin("operations", new DFIDProjectOperationPlugin())
                .registerPlugin("status", new DFIDProjectStatusPlugin(statusMapping()))
                .registerPlugin("weightedAttributes", new DFIDProjectWeightedAttributesPlugin(NUMBER_FORMAT))
                // TODO: uncomment when mappings are known
//                .registerPlugin("lendingInstrument + type", new DFIDLendingInstrumentPlugin(
//                        lendingInstrumentMapping(), lendingInstrumentTypeMapping()))
                .registerPlugin("financingSummary", new DFIDProjectFinancingSummaryPlugin(DATE_FORMATTER,
                        NUMBER_FORMAT))
                .registerPlugin("fundings", new DFIDProjectFundingPlugin(NUMBER_FORMAT));
    }

    /**
     * Lending instrument mapping.
     *
     * @return mapping
     */
    private Map<Enum, List<String>> lendingInstrumentMapping() {
        final Map<Enum, List<String>> instrumentMapping = new HashMap<>();
        // TODO: create this mapping

        return instrumentMapping;
    }

    /**
     * Lending instrument mapping types.
     *
     * @return mapping
     */
    private Map<Enum, List<String>> lendingInstrumentTypeMapping() {
        final Map<Enum, List<String>> instrumentTypeMapping = new HashMap<>();
        // TODO: create this mapping

        return instrumentTypeMapping;
    }

    /**
     * Project status enum mapping.
     *
     * @return mapping
     */
    private Map<Enum, List<String>> statusMapping() {
        final Map<Enum, List<String>> statusMapping = new HashMap<>();

        statusMapping.put(ProjectStatus.COMPLETED, Arrays.asList("Completed"));
        statusMapping.put(ProjectStatus.CLOSED, Arrays.asList("Closed (all the Oper", "Cancelled"));
        statusMapping.put(ProjectStatus.APPROVED, Arrays.asList("Approved", "Approved/Pending Eli"));
        statusMapping.put(ProjectStatus.IMPLEMENTATION, Arrays.asList("Implementation"));

        return statusMapping;
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }

    @Override
    protected final DFIDCleanProject postProcessSourceSpecificRules(final ParsedProject parsedItem, final
    DFIDCleanProject
            cleanItem) {
        return cleanItem;
    }

    @Override
    protected final ParsedProject preProcessParsedItem(final ParsedProject parsedItem) {
        return parsedItem;
    }
}
