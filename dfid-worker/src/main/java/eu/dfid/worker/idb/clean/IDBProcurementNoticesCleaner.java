package eu.dfid.worker.idb.clean;

import eu.dfid.worker.clean.BaseDfidTenderCleaner;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderProjectPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderPublicationPlugin;
import eu.dl.dataaccess.dto.clean.CleanTender;
import eu.dl.dataaccess.dto.codetables.PublicationFormType;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.worker.clean.plugin.BodyPlugin;
import eu.dl.worker.clean.plugin.DateTimePlugin;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Procurement notices cleaner for Inter-American Development Bank (IDB).
 *
 * @author Marek Mikes
 */
public class IDBProcurementNoticesCleaner extends BaseDfidTenderCleaner {
    private static final String VERSION = "2";

    private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("MMMM dd, yyyy")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .toFormatter(Locale.US);

    private static final DateTimeFormatter DATETIME_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("dd/MMM/yyyy")
            //default values for time
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter(Locale.US);

    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
                .registerPlugin("dfid-project", new DFIDTenderProjectPlugin(null, null))
                .registerPlugin("dfid-publications",
                        new DFIDTenderPublicationPlugin(null, DATE_FORMATTER, getFormTypeMapping()))
                .registerPlugin("bodies", new BodyPlugin(null, null, null))
                .registerPlugin("datetime", new DateTimePlugin(DATE_FORMATTER));
        
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

        mapping.put(PublicationFormType.CONTRACT_NOTICE, Arrays.asList("SPECIFIC", "Specific", "GENERAL", "General"));
        mapping.put(PublicationFormType.CONTRACT_AWARD, Arrays.asList("AWARD", "Award"));

        return mapping;
    }

    @Override
    protected final ParsedTender preProcessParsedItem(final ParsedTender parsedItem) {
        return parsedItem;
    }
}
