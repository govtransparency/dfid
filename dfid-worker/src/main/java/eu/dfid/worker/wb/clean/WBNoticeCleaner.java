package eu.dfid.worker.wb.clean;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import eu.dfid.worker.clean.BaseDfidTenderCleaner;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderProjectPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderPublicationPlugin;
import eu.dfid.worker.wb.clean.utils.WBCleanUtils;
import eu.dfid.worker.wb.clean.utils.WBMappingUtils;
import eu.dl.dataaccess.dto.clean.CleanTender;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.worker.clean.plugin.BodyPlugin;
import eu.dl.worker.clean.plugin.DatePlugin;
import eu.dl.worker.clean.plugin.DateTimePlugin;
import eu.dl.worker.clean.plugin.SelectionMethodPlugin;

/**
 * Notice cleaner for World Bank.
 *
 * @author Tomas Mrazek
 */
public class WBNoticeCleaner extends BaseDfidTenderCleaner {

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

    /**
     * This invisible character appears after year in datetime.
     */
    private static final Character CURIOUS_WHITE_SPACE = (char) 160;

    private static final List<DateTimeFormatter> DATETIME_FORMATTERS = Arrays.asList(
        new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("MMMM d, uuuu" + CURIOUS_WHITE_SPACE + " HH:mm")
            .toFormatter(LOCALE),

        new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("MMMM d, uuuu  HH:mm")
            .toFormatter(LOCALE)
    );

    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
            .registerPlugin("date", new DatePlugin(DATE_FORMATTER))
            .registerPlugin("datetime", new DateTimePlugin(DATETIME_FORMATTERS))
            .registerPlugin("selection-method", new SelectionMethodPlugin(WBMappingUtils.getSelectionMethodMapping()))
            .registerPlugin("body", new BodyPlugin(null, null, WBMappingUtils.getCountryFullNameMapping()))

            .registerPlugin("dfid-project", new DFIDTenderProjectPlugin(null,
                WBMappingUtils.getCountryFullNameMapping()))
            .registerPlugin("dfid-publications",
                new DFIDTenderPublicationPlugin(NUMBER_FORMAT, DATE_FORMATTER, WBMappingUtils.getFormTypeMapping()));
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
