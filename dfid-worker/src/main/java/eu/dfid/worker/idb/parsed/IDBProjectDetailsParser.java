package eu.dfid.worker.idb.parsed;

import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedFinancingSummaryItem;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedProjectOperation;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedWeightedAttribute;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.BaseDFIDProjectParser;
import eu.dfid.worker.parser.ParserUtils;
import eu.dl.core.UnrecoverableException;
import eu.dl.dataaccess.dto.parsed.ParsedFunding;
import eu.dl.dataaccess.dto.parsed.ParsedPrice;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.raw.RawData;
import eu.dl.worker.utils.jsoup.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.jsoup.select.Elements;

/**
 * Project details parser for Inter-American Development Bank (IDB).
 *
 * @author Tomas Mrazek
 */
public class IDBProjectDetailsParser extends BaseDFIDProjectParser {

    private static final String VERSION = "2";

    /**
     * IDB financing price is shown in USD millions.
     */
    private static final double IDB_FINANCING_MULTIPLIER = 1000000;

    private static final Locale LOCALE = Locale.US;

    /**
     * IDB financing number format.
     */
    private static final NumberFormat NUMBER_FORMAT;

    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(LOCALE);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');
        NUMBER_FORMAT = new DecimalFormat("#,##0.###", formatSymbols);
    }

    @Override
    public final List<ParsedProject> parse(final RawData rawProject) {
        ParsedProject parsedProject = new ParsedProject();

        final Document document = Jsoup.parse(rawProject.getSourceData());

        final Elements projectElements = document.getElementsByClass("group-left");
        final Element originNode = projectElements.first();

        if (originNode == null) {
            logger.error("HTML node with class 'group-left' which includes project data is missing");
            throw new UnrecoverableException("HTML node with project data is missing");
        }
        
        ProjectDetails projectDetails = new ProjectDetails();
        
        projectDetails.extractProjectDetails(originNode.getElementsByClass("project-detail").first());
        projectDetails.extractProjectDetails(originNode.getElementsByClass("project-information").first());
        projectDetails.extractProjectDetails(originNode.getElementsByClass("operation-number").first());

        //this currency is used for every price from project detail except IDB financing (this is shown in
        //US$ millions) and final cost (this is shown in US$)
        final String currency = projectDetails.getSimpleValue("reporting currency");

        parsedProject
                .setName(JsoupUtils.getStringValueByClass(originNode, "project-title"))
                .setStatus(JsoupUtils.getStringValueByClass(originNode, "project-status-data"))
                .setDescription( JsoupUtils.getStringValueByClass(originNode, "project-description"))
                .setProjectId( projectDetails.getSimpleValue("project number"))
                .setOperations( convertOperations(projectDetails.getListValue("operation number")))
                .setCountry( projectDetails.getSimpleValue("project country"))
                .addMajorSector( new DFIDParsedWeightedAttribute().setName( projectDetails.getSimpleValue("project sector")))
                .addSector( new DFIDParsedWeightedAttribute().setName( projectDetails.getSimpleValue("project subsector")))
                .setLendingInstrument( projectDetails.getSimpleValue("project type"))
                .setEnvironmentalAndSocialCategory( projectDetails.getSimpleValue("environmental and social impact category"))
                .setSignatureDates(projectDetails.getListValue("contract signature date"))
                .setApprovalDate( projectDetails.getSimpleValue("approval date"))
                .setDonorFinancings(parseDonorFinancings(projectDetails.getListValue("amount"), currency))
                .addDonorFinancing(praseIDBFinancing(projectDetails.getSimpleValue("idb financing")))
                .setEstimatedCost( parsePrice( projectDetails.getSimpleValue("estimated total cost"), currency))
                .setEstimatedBorrowerFinancing( parsePrice(projectDetails.getSimpleValue("estimated country counterpart financing"), currency))
                .addFinancingSummaryItem(parseFinancingSummary(projectDetails, currency)) //??
                .setLendingInstrumentTypes(projectDetails.getListValue("financing type"))
                .setFundings(parseFundings(projectDetails.getListValue("fund")))
                .setFinalCost(parsePrice( projectDetails.getSimpleValue("total cost"), "USD"))
                .setBorrowerFinancing( parsePrice(projectDetails.getSimpleValue("country counterpart financing"), currency))
                .addPublication(new ParsedPublication()
                        .setLastUpdate((String) projectDetails.getSimpleValue("reporting date"))
                        .setIsIncluded(true)
                        .setSource(PublicationSources.IDB)
                        .setHumanReadableUrl(rawProject.getSourceUrl().toString()));

        /*
        project.financingSummary.incomeCollected.netAmount
        getFirstTableValueByLabel(projectInformation, "income collected")
         */
        return Arrays.asList(parsedProject);
    }


    /**
     * @param projectInformationNode node with project informations
     * @param currency currency of prices
     *
     * @return list of donor financings
     */
    private List<ParsedPrice> parseDonorFinancings(final List<String> values, final String currency) {

        return values.stream()
                .map((amount) -> new ParsedPrice().setNetAmount(removeCurrencyFromAmount(amount)).setCurrency(currency))
                .collect(Collectors.toList());
    }

    /**
     * @param projectInformationNode node with project informations
     *
     * @return list of fundings
     */
    private List<ParsedFunding> parseFundings(final List<String> fundValues) {
        return fundValues.stream()
                .map((fund) -> new ParsedFunding().setSource(fund))
                .collect(Collectors.toList());
    }

    /**
     * Parses financing summary.
     *
     * @param projectDetails which includes project details data
     * @param currency currency of parsed prices
     *
     * @return financing summary or null in case that no values were parsed
     */
    private DFIDParsedFinancingSummaryItem parseFinancingSummary(final ProjectDetails projectDetails, final String currency) {

        final DFIDParsedFinancingSummaryItem summary = new DFIDParsedFinancingSummaryItem();
        boolean isSummaryEmpty = true;

        final ParsedPrice cancelledPrice = parsePrice(projectDetails.getSimpleValue("cancelled amount"), currency);
        if (cancelledPrice != null) {
            summary.setCancelledAmount(cancelledPrice);
            isSummaryEmpty = false;
        }

        final ParsedPrice disbursedPrice = parsePrice(projectDetails.getSimpleValue("isbursed to date"), currency);
        if (disbursedPrice != null) {
            summary.setDisbursed(disbursedPrice);
            isSummaryEmpty = false;
        }

        final ParsedPrice repaymentsPrice = parsePrice(projectDetails.getSimpleValue("repayments"), currency);
        if (repaymentsPrice != null) {
            summary.setRepayments(repaymentsPrice);
            isSummaryEmpty = false;
        }

        return (isSummaryEmpty ? null : summary);
    }


    /**
     * converts project operations.
     *
     * @param list of the operations
     *
     * @return list of converted operations
     */
    private List<DFIDParsedProjectOperation> convertOperations(final List<String> operationNumbers) {

        if (operationNumbers == null) {
            return null;
        }

        return operationNumbers.stream()
                .map((number) -> new DFIDParsedProjectOperation().setOperationNumber(number))
                .collect(Collectors.toList());
    }

    /**
     * Removes currency from the string where currency and amount value is and
     * returns just the amount value.
     *
     * @param currencyAndAmount string where currency and amount value is
     *
     * @return String or null
     */
    private String removeCurrencyFromAmount(final String currencyAndAmount) {
        // the amount is value without spaces or it is something like "USD 3,417,240"
        if (currencyAndAmount.contains(" ")) {
            assert currencyAndAmount.indexOf(" ") == 3 : "The value of amount should start with three letters of currency and space";
            return currencyAndAmount.substring(currencyAndAmount.indexOf(" ") + 1);
        } else {
            return currencyAndAmount;
        }
    }

    /**
     * Parses IDB financing price. IDB financing is shown in US$ millions.
     *
     * @param value element that includes IDB financing data
     *
     * @return parsed price
     */
    private ParsedPrice praseIDBFinancing(final String value) {
        ParsedPrice price = parsePrice(value, "USD");
        if (price == null) {
            return null;
        }

        String multipliedAmount = ParserUtils.multiply(price.getNetAmount(), IDB_FINANCING_MULTIPLIER, NUMBER_FORMAT);

        if (!multipliedAmount.equals(price.getNetAmount())) {
            price.setNetAmount(multipliedAmount);
        } else {
            return price;
        }

        return price;
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }

    private ParsedPrice parsePrice(final String priceWithCurrency, final String currency) {
        if (priceWithCurrency!=null) {
            return new ParsedPrice().setNetAmount(removeCurrencyFromAmount(priceWithCurrency)).setCurrency(currency);
        } else {
            return null;
        }
    }
}

/**
 * utility class for making access to project details simpler
 * since this class is used only here, to save some memory, it is defined as a private class
 * @author fepetoo
 */
final class ProjectDetails {
    private final Map<String, List> map = new HashMap<>();

    /**
     * adds a simple value to project details map
     * @param key the id of the value
     * @param value the string value
     */
    void addValue(String key, String value) {
        List<String> valueList = map.get(key);
        if (valueList == null) {
            valueList = new ArrayList<>();
        }
        valueList.add(value);
        map.put(key, valueList);
    }

    /**
     * gets the first occurrence of the given key's value
     * @param key the key which value we are interesting in 
     * @return the value or null if not found
     */
    String getSimpleValue(String key) {
        String result = null;
        if (map.containsKey(key)) {
            List<String> valueList = map.get(key);
            if (valueList!=null && !valueList.isEmpty()) {
               result  = valueList.get(0);
            }
        }
        return result;
    }

    /**
     * return all the values for the given key
     * @param key the key which values we are interesting in 
     * @return list of values (or empty list, if key element fount)
     */
    List<String> getListValue(String key) {
        List<String> result = new ArrayList<>();
        if (map.containsKey(key)) {
            result = map.get(key);
        }
        return result;
    }

    /**
     * extracts project data from html element into an internal map
     * @param sectionOnly the html section the code has to be extract from
     */
    void extractProjectDetails(Element sectionOnly) {
        if (sectionOnly != null) {
            Elements projectFields = sectionOnly.getElementsByClass("project-field");
            Iterator<Element> iterator = projectFields.iterator();
            while (iterator.hasNext()) {
                Element next = iterator.next();
                String projectFieldTitle = JsoupUtils.getStringValueByClass(next, "project-field-title").toLowerCase();
                String projectFieldValue = JsoupUtils.getStringValueByClass(next, "project-field-data");
                List<String> valueList = map.get(projectFieldTitle);
                if (valueList != null) {
                    if (!valueList.contains(projectFieldValue)) {
                        valueList.add(projectFieldValue); 
                        map.put(projectFieldTitle, valueList);
                    }
                } else {
                    valueList = new ArrayList<>();
                    valueList.add(JsoupUtils.getStringValueByClass(next, "project-field-data"));
                    map.put(projectFieldTitle, valueList);
                }
            }
        }
    }
}
