package eu.dfid.worker.wb.clean;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

import eu.dfid.worker.clean.BaseDfidTenderCleaner;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderDatePlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderIntegerPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderProjectPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderPublicationPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderShortTextPlugin;
import eu.dfid.worker.wb.clean.utils.WBCleanUtils;
import eu.dfid.worker.wb.clean.utils.WBMappingUtils;
import eu.dl.dataaccess.dto.clean.CleanTender;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.worker.clean.plugin.DatePlugin;
import eu.dl.worker.clean.plugin.LotPlugin;
import eu.dl.worker.clean.plugin.PricePlugin;
import eu.dl.worker.clean.plugin.SelectionMethodPlugin;
import eu.dl.worker.clean.plugin.TenderSupplyTypePlugin;

/**
 * Contract cleaner for World Bank.
 *
 * @author Tomas Mrazek
 */
public class WBMajorContractAwardsSODACleaner extends BaseDfidTenderCleaner {

    private static final String VERSION = "1.0";

    private static final Locale LOCALE = new Locale("en");
    
    private static final NumberFormat NUMBER_FORMAT;
    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(LOCALE);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');
        NUMBER_FORMAT = new DecimalFormat("#,##0.###", formatSymbols);
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
            .registerPlugin("date", new DatePlugin(DATE_FORMATTER))            
            .registerPlugin("selection-method", new SelectionMethodPlugin(WBMappingUtils.getSelectionMethodMapping()))
            .registerPlugin("lots", new LotPlugin(NUMBER_FORMAT, DATE_FORMATTER, new HashMap<>()))
            .registerPlugin("prices", new PricePlugin(NUMBER_FORMAT))            
            .registerPlugin("supply-type", new TenderSupplyTypePlugin(WBMappingUtils.getSupplyTypeMapping()))

            .registerPlugin("dfid-shortstring", new DFIDTenderShortTextPlugin())
            .registerPlugin("dfid-date", new DFIDTenderDatePlugin(DATE_FORMATTER))
            .registerPlugin("dfid-project",
                new DFIDTenderProjectPlugin(WBMappingUtils.getRegionMapping(), WBMappingUtils.getCountryMapping()))
            .registerPlugin("dfid-integer", new DFIDTenderIntegerPlugin(NUMBER_FORMAT))
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
