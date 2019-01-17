package eu.dfid.worker.wb.clean;

import eu.dfid.worker.clean.BaseDfidTenderCleaner;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderDatePlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderProjectPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderShortTextPlugin;
import eu.dfid.worker.wb.clean.utils.WBCleanUtils;
import eu.dfid.worker.wb.clean.utils.WBMappingUtils;
import eu.dl.dataaccess.dto.clean.CleanTender;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.worker.clean.plugin.DatePlugin;
import eu.dl.worker.clean.plugin.LotPlugin;
import eu.dl.worker.clean.plugin.PricePlugin;
import eu.dl.worker.clean.plugin.PublicationPlugin;
import eu.dl.worker.clean.plugin.SelectionMethodPlugin;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Locale;

/**
 * Contract cleaner for World Bank.
 *
 * @author Tomas Mrazek
 */
public class WBContractCleaner extends BaseDfidTenderCleaner {

    private static final String VERSION = "1.0";

    private static final Locale LOCALE = new Locale("en");

    private static final NumberFormat NUMBER_FORMAT;

    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(LOCALE);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');
        NUMBER_FORMAT = new DecimalFormat("#,##0.###", formatSymbols);
    }

    private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("MMMM d, uuuu")
            .toFormatter(LOCALE);

    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
                .registerPlugin("date", new DatePlugin(DATE_FORMATTER))
                .registerPlugin("selection-method",
                        new SelectionMethodPlugin(WBMappingUtils.getSelectionMethodMapping()))
                .registerPlugin("lots", new LotPlugin(NUMBER_FORMAT, DATE_FORMATTER, new HashMap<>()))
                .registerPlugin("prices", new PricePlugin(NUMBER_FORMAT))
                .registerPlugin("publications", new PublicationPlugin(NUMBER_FORMAT, DATE_FORMATTER, null))
                .registerPlugin("dfid-shortstring", new DFIDTenderShortTextPlugin())
                .registerPlugin("dfid-date", new DFIDTenderDatePlugin(DATE_FORMATTER))
                .registerPlugin("dfid-project", new DFIDTenderProjectPlugin(null, null));
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
        return (ParsedTender) WBCleanUtils.replaceNAWithNull(parsedItem);
    }
}
