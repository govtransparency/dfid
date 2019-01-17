package eu.dfid.worker.ec.clean;

import eu.dfid.worker.clean.BaseDfidTenderCleaner;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderLongTextPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderPublicationPlugin;
import eu.dl.dataaccess.dto.clean.CleanTender;
import eu.dl.dataaccess.dto.codetables.TenderSupplyType;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.worker.clean.plugin.AddressPlugin;
import eu.dl.worker.clean.plugin.IntegerPlugin;
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
 * Created by michalriha on 07/03/2017.
 */
public class EAContractCleaner extends BaseDfidTenderCleaner {
    private static final String VERSION = "1";
    @Override
    public final String getVersion() {
        return VERSION;
    }

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("MMM d, yyyy")
            .toFormatter(Locale.US);

    @SuppressWarnings("unchecked")
    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
            .registerPlugin("dfid-publication", new DFIDTenderPublicationPlugin(NUMBER_FORMAT, DATE_FORMATTER, null))
            .registerPlugin("dfid-longtext", new DFIDTenderLongTextPlugin())
            .registerPlugin("lot", new LotPlugin(NUMBER_FORMAT, DATE_FORMATTER, new HashMap<>()))
            .registerPlugin("price", new PricePlugin(NUMBER_FORMAT))
            .registerPlugin("supplyType",  new TenderSupplyTypePlugin(supplyTypeMapping()))
            .registerPlugin("address", new AddressPlugin())
            .registerPlugin("integer", new IntegerPlugin(NUMBER_FORMAT));
    }

    /**
     * Supply type mapping.
     *
     * @return mapping
     */
    private Map<Enum, List<String>> supplyTypeMapping() {
        final Map<Enum, List<String>> mapping = new HashMap<>();
        mapping.put(TenderSupplyType.WORKS, Arrays.asList("Works"));
        mapping.put(TenderSupplyType.SERVICES, Arrays.asList("Services", "Furnitures", "Pro forma registration" +
                " (Program Estimates, Budget Support)", "Specific contract (ex-letter of contract, order form, etc.)",
                "Specific contract (ex-request for services and ord"));
        mapping.put(TenderSupplyType.SUPPLIES, Arrays.asList("Supplies"));

        return mapping;
    }

    @Override
    protected final ParsedTender preProcessParsedItem(final ParsedTender parsedItem) {
        return parsedItem;
    }

    @Override
    protected final CleanTender postProcessSourceSpecificRules(final ParsedTender parsedItem,
                                                               final CleanTender cleanItem) {
        return cleanItem;
    }
}
