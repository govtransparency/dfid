package eu.dfid.worker.idb.clean;

import eu.dfid.worker.clean.BaseDfidTenderCleaner;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderProjectOperationPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderProjectPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderPublicationPlugin;
import eu.dl.dataaccess.dto.clean.CleanTender;
import eu.dl.dataaccess.dto.codetables.PublicationFormType;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Procurement plans cleaner for Inter-American Development Bank (IDB).
 *
 * @author Marek Mikes
 */
public class IDBProcurementPlansCleaner extends BaseDfidTenderCleaner {

    private static final String VERSION = "1";

    private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("MMM d, yyyy")
        .toFormatter(Locale.US);

    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
                .registerPlugin("dfid-project", new DFIDTenderProjectPlugin(null, null))
                .registerPlugin("dfid-operations", new DFIDTenderProjectOperationPlugin())
                .registerPlugin("dfid-publications",
                        new DFIDTenderPublicationPlugin(null, DATE_FORMATTER, getFormTypeMapping()));
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }

    @Override
    protected final CleanTender postProcessSourceSpecificRules(final ParsedTender parsedItem,
                                                               final CleanTender cleanItem) {
        return cleanItem;
    }

    /**
     * @return mapping for form types
     */
    private static HashMap<Enum, List<String>> getFormTypeMapping() {
        HashMap<Enum, List<String>> mapping = new HashMap<>();

        mapping.put(PublicationFormType.PRIOR_INFORMATION_NOTICE, Arrays.asList("Procurement Plan"));

        return mapping;
    }

    @Override
    protected final ParsedTender preProcessParsedItem(final ParsedTender parsedItem) {
        return parsedItem;
    }
}
