package eu.dfid.worker.ec.clean;

import eu.dfid.worker.clean.BaseDfidTenderCleaner;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderDatePlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderLongTextPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderProjectPlugin;
import eu.dfid.worker.clean.plugin.tender.DFIDTenderPublicationPlugin;
import eu.dl.dataaccess.dto.clean.CleanTender;
import eu.dl.dataaccess.dto.codetables.PublicationFormType;
import eu.dl.dataaccess.dto.codetables.SelectionMethod;
import eu.dl.dataaccess.dto.codetables.TenderProcedureType;
import eu.dl.dataaccess.dto.codetables.TenderSupplyType;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.worker.clean.plugin.AddressPlugin;
import eu.dl.worker.clean.plugin.BodyPlugin;
import eu.dl.worker.clean.plugin.DatePlugin;
import eu.dl.worker.clean.plugin.DateTimePlugin;
import eu.dl.worker.clean.plugin.FundingsPlugin;
import eu.dl.worker.clean.plugin.LotPlugin;
import eu.dl.worker.clean.plugin.PricePlugin;
import eu.dl.worker.clean.plugin.TenderProcedureTypePlugin;
import eu.dl.worker.clean.plugin.TenderSupplyTypePlugin;
import eu.dl.worker.clean.utils.CodeTableUtils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Procurement cleaner for EuropeAid of European Commission that are published in TED.
 *
 * @author Tomas Mrazek
 */
public class EATedProcurementCleaner extends BaseDfidTenderCleaner {
    private static final String VERSION = "1";
    
    private static final Locale LOCALE = Locale.ENGLISH;

    private static final NumberFormat NUMBER_FORMAT;
    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(LOCALE);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(' ');
        NUMBER_FORMAT = new DecimalFormat("#,##0.###", formatSymbols);
    }

    private static final DateTimeFormatter OPTIONAL_TIME = DateTimeFormatter.ofPattern("[ H:m][.]");

    private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
        new DateTimeFormatterBuilder().appendPattern("d.M.uuuu").append(OPTIONAL_TIME)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter(),
        new DateTimeFormatterBuilder().appendPattern("uuuuMMdd").append(OPTIONAL_TIME)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter(),
        new DateTimeFormatterBuilder().appendPattern("[d ]MMMM uuuu").append(OPTIONAL_TIME)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter(Locale.US));
    
    @SuppressWarnings("unchecked")
    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
            .registerPlugin("dfid-publication", new DFIDTenderPublicationPlugin(NUMBER_FORMAT, DATE_FORMATTERS, formTypeMapping()))
            .registerPlugin("dfif-date", new DFIDTenderDatePlugin(DATE_FORMATTERS))
            .registerPlugin("dfid-project", new DFIDTenderProjectPlugin(null, null))
            .registerPlugin("dfid-longtext", new DFIDTenderLongTextPlugin())
            .registerPlugin("body", new BodyPlugin(null, null))
            .registerPlugin("price", new PricePlugin(NUMBER_FORMAT))
            .registerPlugin("supplyType", new TenderSupplyTypePlugin(supplyTypeMapping()))
            .registerPlugin("procedureType", new TenderProcedureTypePlugin(procedureTypeMapping()))
            .registerPlugin("date", new DatePlugin(DATE_FORMATTERS))
            .registerPlugin("datetime", new DateTimePlugin(DATE_FORMATTERS))
            .registerPlugin("address", new AddressPlugin())
            .registerPlugin("fundings", new FundingsPlugin(NUMBER_FORMAT))
            .registerPlugin("lots", new LotPlugin(Collections.singletonList(NUMBER_FORMAT), DATE_FORMATTERS, Collections.emptyMap()));
    }

    /**
     * @return procedure type mapping
     */
    private Map<Enum, List<String>> procedureTypeMapping() {
        // inicialize mapping
        final Map<Enum, List<String>> mapping =
            CodeTableUtils.enumToMapping(TenderProcedureType.class, e -> e.name().toLowerCase().replace("_", ""));

        mapping.get(TenderProcedureType.OPEN).add("Open procedure");
        mapping.get(TenderProcedureType.NEGOTIATED_WITHOUT_PUBLICATION).addAll(Arrays.asList(
            "Negotiated without a prior call for competition", "Negotiated without a call for competition"));
        mapping.get(TenderProcedureType.RESTRICTED).add("Restricted procedure");
        mapping.get(TenderProcedureType.COMPETITIVE_DIALOG).add("Competitive dialogue");
        mapping.get(TenderProcedureType.OTHER).add("Not applicable");

        mapping.put(null, Arrays.asList("Not specified"));
        
        return mapping;
    }

    /**
     * @return form type mapping
     */
    private Map<Enum, List<String>> formTypeMapping() {
        final Map<Enum, List<String>> mapping = new HashMap<>();
        mapping.put(PublicationFormType.CONTRACT_NOTICE, Arrays.asList("Contract notice"));
        mapping.put(PublicationFormType.CONTRACT_AWARD, Arrays.asList("Contract award notice", "Contract award",
            "Voluntary ex ante transparency notice"));
        mapping.put(PublicationFormType.PRIOR_INFORMATION_NOTICE, Arrays.asList("Prior information notice",
            "Prior information notice without call for competition"));

        return mapping;
    }
    
    /**
     * @return supply type mapping
     */
    private Map<Enum, List<String>> supplyTypeMapping() {
        final Map<Enum, List<String>> mapping = new HashMap<>();
        mapping.put(TenderSupplyType.WORKS, Arrays.asList("Works", "Work contract"));
        mapping.put(TenderSupplyType.SERVICES, Arrays.asList("Services", "Service contract"));
        mapping.put(TenderSupplyType.SUPPLIES, Arrays.asList("Supplies", "Supply contract"));

        return mapping;
    }

    @Override
    protected final ParsedTender preProcessParsedItem(final ParsedTender parsedItem) {
        return parsedItem;
    }

    @Override
    protected final CleanTender postProcessSourceSpecificRules(final ParsedTender parsedItem,
                                                               final CleanTender cleanItem) {

        // selection method cleaning
        String parsedSelectionMethod = parsedItem.getSelectionMethod();
        if (parsedSelectionMethod != null) {
            SelectionMethod selectionMethod = null;
            if (parsedSelectionMethod.contains("quality")) {
                selectionMethod = SelectionMethod.QUALITY_AND_COST_BASED_SELECTION;
            } else if (parsedSelectionMethod.contains("most")) {
                selectionMethod = SelectionMethod.MEAT;
            } else if (parsedSelectionMethod.matches("(?i).*(lowest|best value|price).*")) {
                selectionMethod = SelectionMethod.LOWEST_PRICE;
            }
            cleanItem.setSelectionMethod(selectionMethod);

            logger.debug("Cleaned selectionMethod for parsed tender {}, clean value \"{}\"", parsedItem.getId(),
                selectionMethod);
        }

        return cleanItem;
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }
}
