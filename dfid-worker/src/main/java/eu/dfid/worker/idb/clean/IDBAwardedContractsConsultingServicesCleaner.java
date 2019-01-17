package eu.dfid.worker.idb.clean;

import eu.dfid.worker.clean.BaseDfidTenderCleaner;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderProjectOperationPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderProjectPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderPublicationPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderShortTextPlugin;
import eu.dl.dataaccess.dto.clean.CleanTender;
import eu.dl.dataaccess.dto.codetables.TenderSupplyType;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.worker.clean.plugin.BodyPlugin;
import eu.dl.worker.clean.plugin.DatePlugin;
import eu.dl.worker.clean.plugin.LotPlugin;
import eu.dl.worker.clean.plugin.PricePlugin;
import eu.dl.worker.clean.plugin.TenderSupplyTypePlugin;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Tender cleaner for IDB AwardedContractsConsultingServicesCleaner.
 */
public class IDBAwardedContractsConsultingServicesCleaner extends BaseDfidTenderCleaner {
    private static final String VERSION = "2";

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
    private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("MMM d, yyyy")
                    .toFormatter(Locale.US),
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("MM/dd/yyyy")
                    .toFormatter(Locale.US));
    
    @SuppressWarnings("unchecked")
    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
                .registerPlugin("date", new DatePlugin(DATE_FORMATTERS))
                .registerPlugin("price", new PricePlugin(NUMBER_FORMAT))
                .registerPlugin("lot", new LotPlugin(NUMBER_FORMAT, DATE_FORMATTERS, new HashMap<>()))
                .registerPlugin("dfid-publication",
                        new DFIDTenderPublicationPlugin(NUMBER_FORMAT, DATE_FORMATTERS, null))
                .registerPlugin("body", new BodyPlugin(null, null))
                .registerPlugin("dfid-project", new DFIDTenderProjectPlugin(null, null))
                .registerPlugin("dfid-operations", new DFIDTenderProjectOperationPlugin())
                .registerPlugin("dfid-shortString", new DFIDTenderShortTextPlugin())
                .registerPlugin("supply-type", new TenderSupplyTypePlugin(getSupplyTypeMapping()));
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

    @Override
    protected final ParsedTender preProcessParsedItem(final ParsedTender parsedItem) {
        return parsedItem;
    }

    /**
     * @return supply type mapping for cleaning process
     */
    private static Map<Enum, List<String>> getSupplyTypeMapping() {
        final Map<Enum, List<String>> mapping = new HashMap<>();
        mapping.put(TenderSupplyType.SERVICES, Arrays.asList("CONSULTING SERVICES"));
        return mapping;
    }
}
