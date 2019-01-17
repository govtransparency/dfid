package eu.dfid.worker.ec.clean;

import eu.dfid.worker.clean.BaseDfidTenderCleaner;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderProjectPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderPublicationPlugin;
import eu.dl.dataaccess.dto.clean.CleanTender;
import eu.dl.dataaccess.dto.codetables.TenderLotStatus;
import eu.dl.dataaccess.dto.codetables.TenderSupplyType;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.worker.clean.plugin.DateTimePlugin;
import eu.dl.worker.clean.plugin.LotPlugin;
import eu.dl.worker.clean.plugin.PricePlugin;
import eu.dl.worker.clean.plugin.TenderSupplyTypePlugin;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by michalriha on 07/03/2017.
 */
public class EAProcurementCleaner extends BaseDfidTenderCleaner {
    private static final String VERSION = "1";

    @Override
    public final String getVersion() {
        return VERSION;
    }

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    private static final List<DateTimeFormatter> DATE_FORMATTER = Arrays.asList(
        new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("MMM d, yyyy")
            .toFormatter(Locale.US),
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    );

    private static final DateTimeFormatter DATETIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("dd/MM/yyyy")
            //default values for time
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    @SuppressWarnings("unchecked")
    @Override
    protected final void registerSpecificPlugins() {
        Map<String, Map<Enum, List<String>>> lotMappings = new HashMap<>();
        lotMappings.put("statusMapping", getLotStatusMapping());

        pluginRegistry
            .registerPlugin("dfid-project", new DFIDTenderProjectPlugin(null, null))
            .registerPlugin("dfid-publications", new DFIDTenderPublicationPlugin(NUMBER_FORMAT, DATE_FORMATTER, null))
            .registerPlugin("lot", new LotPlugin(NUMBER_FORMAT, DATE_FORMATTER, lotMappings))
            .registerPlugin("supplyType", new TenderSupplyTypePlugin(supplyTypeMapping()))
            .registerPlugin("datetime", new DateTimePlugin(DATETIME_FORMATTER))
            .registerPlugin("price", new PricePlugin(NUMBER_FORMAT));
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
     * Supply type mapping.
     *
     * @return mapping
     */
    private Map<Enum, List<String>> supplyTypeMapping() {
        final Map<Enum, List<String>> mapping = new HashMap<>();

        mapping.put(TenderSupplyType.WORKS, Arrays.asList("Works"));
        mapping.put(TenderSupplyType.SERVICES, Arrays.asList("Services"));
        mapping.put(TenderSupplyType.SUPPLIES, Arrays.asList("Supplies"));
        mapping.put(TenderSupplyType.OTHER, Arrays.asList("Action Grants", "Grant", "Not applicable"));

        return mapping;
    }

    /**
     * @return lot status mapping for cleaning process
     */
    private static Map<Enum, List<String>> getLotStatusMapping() {
        final Map<Enum, List<String>> mapping = new HashMap<>();

        mapping.put(TenderLotStatus.FINISHED, Arrays.asList("Closed"));
        mapping.put(TenderLotStatus.CANCELLED, Arrays.asList("Cancelled"));

        return mapping;
    }
}
