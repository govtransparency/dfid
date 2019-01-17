package eu.dfid.worker.wb.parsed;

import eu.dfid.dataaccess.dto.parsed.DFIDParsedBody;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedFinancing;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedFinancingSummaryItem;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedPrice;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedPublication;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedWeightedAttribute;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.BaseDFIDProjectParser;
import eu.dfid.worker.parser.ParserUtils;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.raw.RawData;
import eu.dl.worker.utils.jsoup.JsoupUtils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Tender parser for WB Project & Operations DFID data.
 *
 * @author Michal Riha
 */
public class WBPOWebProjectParser extends BaseDFIDProjectParser {
    private static final String VERSION = "1";

    /**
     * Prased prices are in USD millions.
     */
    private static final double PRICE_MULTIPLIER = 1000000;

    /**
     * Prices number format.
     */
    private static final NumberFormat NUMBER_FORMAT;
    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.US);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');
        NUMBER_FORMAT = new DecimalFormat("#,##0.###", formatSymbols);
    }

    /**
     * Base URI for snippets.
     */
    private static final String BASE_URI = "http://projects.worldbank.org/";

    @Override
    public final List<ParsedProject> parse(final RawData rawTender) {
        final Document overview = Jsoup.parse(rawTender.getSourceData(), BASE_URI);

        @SuppressWarnings("unchecked") final LinkedHashMap<String, String> snippets =
                (LinkedHashMap<String, String>) rawTender.getMetaData().get("additionalUrls");

        @SuppressWarnings("unchecked") final String projectName = (String) rawTender.getMetaData().get("projectName");

        Document details = null;
        Document financial = null;
        Document procurementNotices = null;
        Document procurementContract = null;
        Document documents = null;

        for (String key : snippets.keySet()) {
            if (key.contains("projectdetails.htm")) {
                details = Jsoup.parse(snippets.get(key), BASE_URI);
            } else if (key.contains("financial.htm")) {
                financial = Jsoup.parse(snippets.get(key), BASE_URI);
            } else if (key.contains("procurement.htm") && !key.contains("contractdata")) {
                procurementNotices = Jsoup.parse(snippets.get(key), BASE_URI);
            } else if (key.contains("procurement.htm") && key.contains("contractdata")) {
                procurementContract = Jsoup.parse(snippets.get(key), BASE_URI);
            } else if (key.contains("documents.htm")) {
                documents = Jsoup.parse(snippets.get(key), BASE_URI);
            }
        }

        String projectStatus = JsoupUtils.getFirstValueByLabel(overview, "(?i)Status");
        if (projectStatus == null) {
            projectStatus = JsoupUtils.getFirstValueByLabel(details, "(?i)Status");
        }

        ParsedProject parsedProject = new ParsedProject()
            .setName(projectName)
            .setStatus(projectStatus)
            // Overview tab
            .setDescription(JsoupUtils.selectText("h2.abstract > p", overview))
            .setProjectId(JsoupUtils.selectText("thead th.second", overview))
            .setCountry(JsoupUtils.getFirstValueByLabel(overview, "(?i)Country"))
            .setRegion(JsoupUtils.getFirstValueByLabel(overview, "(?i)Region"))
            .setApprovalDate(JsoupUtils.getFirstValueByLabel(overview, "(?i)Approval Date"))
            .setClosingDate(JsoupUtils.getFirstValueByLabel(overview, "(?i)Closing Date"))
            .setFinalCost(parsePrice(JsoupUtils.getFirstValueByLabel(overview, "(?i)Total Project Cost")))
            .addDonorFinancing(parsePrice(JsoupUtils.getFirstValueByLabel(overview, "(?i)Commitment Amount")))
            .setTeamLeader(JsoupUtils.getFirstValueByLabel(overview, "(?i)Team Leader"))
            // Details tab
            .setEnvironmentalAndSocialCategory(JsoupUtils.getFirstValueByLabel(details, "(?i)Environmental Category"))
            .setSectors(parseDetailSectors(details))
            .setBorrower(new DFIDParsedBody().setName(JsoupUtils.getFirstValueByLabel(details, "(?i)Borrower")))
            .setImplementingAgency(new DFIDParsedBody()
                .setName(JsoupUtils.getFirstValueByLabel(details, "(?i)Implementing Agency")))
            .setThemes(parseDetailThemes(details))
            // Financials
            .setFinancingPlan(parseFinancingPlans(financial))
            .setProductLine(JsoupUtils.getFirstValueByLabel(financial, "(?i)Product Line"))
            .addDonorFinancing(parsePrice(
                JsoupUtils.selectText("tr:containsOwn(IBRD + IDA Commitment) > td", financial)))
            .setGrantAmount(parsePrice(JsoupUtils.getFirstValueByLabel(financial, "(?i)Grant Amount")))
            .setFinancingSummary(parseFinancingSummary(financial))
            .setLendingInstrument(JsoupUtils.getFirstValueByLabel(financial, "(?i)Lending Instrument"));

        //Publications
        List<ParsedPublication> publications = new ArrayList<>();
        // Procurement Notices
        publications.addAll(parseNoticesPublications(procurementNotices));
        // Procurement Contract Data
        publications.addAll(parseContractPublications(procurementContract));
        // Documents
        publications.addAll(parseDocumentsPublications(documents));
        
        parsedProject.setPublications(publications);

        return Arrays.asList(parsedProject);
    }

    @Override
    protected final String getVersion() {
        return VERSION;
    }

    /**
     * Parse publications from documents page.
     *
     * @param documents    documents page to be parsed from
     *
     * @return List<ParsedPublication> or empty list
     */
    private List<ParsedPublication> parseDocumentsPublications(final Document documents) {
        final List<ParsedPublication> result = new ArrayList<>();

        final Elements tableLines = documents.select("table#projDocsTable > tbody > tr");

        if (tableLines == null || tableLines.isEmpty()) {
            return Collections.emptyList();
        } else {
            for (Element tableLine : tableLines) {
                result.add(new DFIDParsedPublication()
                        .setHumanReadableUrl(tableLine.select("td:eq(1) > a").first().attr("abs:href"))
                        .setPublicationDate(tableLine.select("td:eq(2)").first().ownText())
                        .setSourceFormType(tableLine.select("td:eq(4)").first().ownText())
                        .setIsIncluded(false));
            }
        }

        return result.isEmpty() ? null : result;
    }

    /**
     * Parse publications from procurement contract page.
     *
     * @param procurementContract procurement contract page to be parsed from
     *
     * @return List<ParsedPublication> or empty list
     */
    private List<ParsedPublication> parseContractPublications(final Document procurementContract) {
        final List<ParsedPublication> result = new ArrayList<>();

        final Elements tableLines = procurementContract.select("table#contractdata > tbody > tr");

        if (tableLines == null || tableLines.isEmpty()) {
            return Collections.emptyList();
        } else {
            for (Element tableLine : tableLines) {
                result.add(new DFIDParsedPublication()
                    .setHumanReadableUrl(tableLine.select("td:eq(0) > a").first().attr("abs:href"))
                    .setIsIncluded(false)
                    .setSourceFormType("Contract Award"));
            }
        }

        return result;
    }

    /**
     * Parse publications from procurement notices page.
     *
     * @param procurementNotices procurement notices page to be parsed from
     *
     * @return List<ParsedPublication> or empty list
     */
    private List<ParsedPublication> parseNoticesPublications(final Document procurementNotices) {
        final Elements tableLines = procurementNotices.select("table#procurementnotices > tbody > tr");
        final List<ParsedPublication> result = new ArrayList<>();

        if (tableLines == null || tableLines.isEmpty()) {
            return Collections.emptyList();
        } else {
            for (Element tableLine : tableLines) {
                result.add(new DFIDParsedPublication()
                        .setHumanReadableUrl(tableLine.select("td:eq(0) > a").first().attr("abs:href"))
                        .setSourceFormType(JsoupUtils.selectText("td.notice", tableLine))
                        .setIsIncluded(false)
                        .setPublicationDate(tableLine.select("td.published").first().ownText()));
            }
        }

        return result;
    }

    /**
     * Parse financing summary from financials page.
     *
     * @param financials financials page to be parsed from
     *
     * @return List<DFIDParsedFinancingSummaryItem> or null
     */
    private List<DFIDParsedFinancingSummaryItem> parseFinancingSummary(final Document financials) {
        final Elements tableLines = financials.select(".summaryTable > table.second > tbody > tr");
        final List<DFIDParsedFinancingSummaryItem> result = new ArrayList<>();

        if (tableLines == null || tableLines.isEmpty()) {
            return null;
        } else {
            for (Element tableLine : tableLines) {
                result.add(new DFIDParsedFinancingSummaryItem()
                        .setLoanNumber(JsoupUtils.selectText("td.first", tableLine))
                        .setApprovalDate(JsoupUtils.selectText("td.second", tableLine))
                        .setClosingDate(JsoupUtils.selectText("td.third", tableLine))
                        .setPrincipal(parsePrice(JsoupUtils.selectText("td.fourth", tableLine)))
                        .setDisbursed(parsePrice(JsoupUtils.selectText("td.fifth", tableLine)))
                        .setRepayments(parsePrice(JsoupUtils.selectText("td.seventh", tableLine)))
                        .setInterestChargesFees(parsePrice(JsoupUtils.selectText("td.eigth", tableLine))));
            }
        }

        return result.isEmpty() ? null : result;
    }

    /**
     * Parse financing planos from financials page.
     *
     * @param financials financials page to be parsed from
     *
     * @return List<DFIDParsedFinancing> or null
     */
    private List<DFIDParsedFinancing> parseFinancingPlans(final Document financials) {
        final Elements tableLines = financials.select("table.tableFinPlan > tbody > tr");
        final List<DFIDParsedFinancing> result = new ArrayList<>();

        if (tableLines == null || tableLines.isEmpty()) {
            return null;
        } else {
            for (Element tableLine : tableLines) {
                result.add(new DFIDParsedFinancing()
                        .setFinancier(JsoupUtils.selectText("td", tableLine))
                        .setCommitment(new DFIDParsedPrice()
                            .setAmountWithVat(JsoupUtils.selectText("td + td", tableLine))));
            }
        }

        return result.isEmpty() ? null : result;
    }

    /**
     * Parse detail sectors.
     *
     * @param details details page to be parsed from
     *
     * @return List<DFIDParsedWeightedAttribute> or null
     */
    private List<DFIDParsedWeightedAttribute> parseDetailSectors(final Document details) {
        List<DFIDParsedWeightedAttribute> results = new ArrayList<>();
        Elements elements = details.select("table[summary=Sectors] > tbody > tr");

        results.addAll(elements.stream().map(element -> new DFIDParsedWeightedAttribute()
                .setName(JsoupUtils.selectText("td.sector1new", element))
                .setWeight(JsoupUtils.selectText("td.sector2new", element))).collect(Collectors.toList()));

        return results.isEmpty() ? null : results;
    }

    /**
     * Parse detail themes.
     *
     * @param details details page to be parsed from
     * @return List<DFIDParsedWeightedAttribute> or null
     */
    private List<DFIDParsedWeightedAttribute> parseDetailThemes(final Document details) {
        List<DFIDParsedWeightedAttribute> results = new ArrayList<>();
        Elements elements = details.select("table[summary=Themes] > tbody > tr");

        results.addAll(elements.stream().map(element -> new DFIDParsedWeightedAttribute()
                .setName(JsoupUtils.selectText("td.theme1new", element))
                .setWeight(JsoupUtils.selectText("td.theme2new", element))).collect(Collectors.toList()));

        return results.isEmpty() ? null : results;
    }

    /**
     * Method creates {@link eu.dfid.dataaccess.dto.parsed.DFIDParsedPrice} instance for the given
     * {@code amountString} in USD millions. In case, that passed amount string includes number but the parsing to
     * double of this one fails the method returns parsed price with the setAmountWithVat parameter set to
     * {@code amountString}.
     *
     * @param amount
     *      string that includes amount in USD millions
     * @return parsed price
     */
    private DFIDParsedPrice parsePrice(final String amount) {
        if (amount == null || amount.isEmpty()) {
            return null;
        }

        DFIDParsedPrice price = new DFIDParsedPrice();

        String multipliedAmount =  ParserUtils.multiply(amount, PRICE_MULTIPLIER, NUMBER_FORMAT);
        if (!multipliedAmount.equals(amount)) {
            price
                .setAmountWithVat(multipliedAmount)
                .setCurrency("USD");
        } else {
            price.setAmountWithVat(amount);
        }

        return price;
    }
}
