package eu.dfid.worker.wb.raw;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import eu.dfid.worker.raw.BaseDfidIncrementalPagedSourceHttpCrawler;
import eu.dl.core.UnrecoverableException;
import eu.dl.worker.utils.jsoup.JsoupUtils;

/**
 * Searches http://www.worldbank.org/projects/search?lang=en for projects html details.
 *
 * @author Tomas Mrazek
 */
public final class WBPOWebProjectCrawler extends BaseDfidIncrementalPagedSourceHttpCrawler {
    private static final String VERSION = "3.0";
    private static final String SOURCE_URL = "http://projects.worldbank.org/p2e/";

    private static final String RESULT_SET_URL_TEMPLATE = SOURCE_URL + "projectsearchpagination.html" +
            //pagination
            "?noOfRows=%1$d&startIndex=%2$d" +
            //approval date (strdate and enddate are the same for purposes of date increment)
            "&strdate=%3$s&enddate=%3$s" +
            //control parameters
            "&lang=en&isProcurementContract=searchresult&tf=resultstab" +
            //filter parameters
            "&projectfinancialtype_exact=&status_exact=&countryshortname_exact=&regionname_exact=" +
            "&sector_exact=&sectorcode_exact=&mjsectorcode_exact=&theme_exact=&themecode_exact=&mjtheme_exact=" +
            "&mjthemecode_exact=&prodline_exact=&lendinginstr_exact=&envassesmentcategorycode=&countrycode_exact=" +
            "&searchString=";

    private static final DateTimeFormatter RESULT_SET_URL_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    /**
     * Count of projects on results page.
     */
    private static final int RESULT_ROWS_COUNT = 500;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(1947, Month.MAY, 9);

    private int pageNumber = 0;
    private LocalDate parsingDate;

    /**
     * Detail snippets.
     */
    private enum DetailSnippet {
        OVERVIEW("overview"), DETAILS("projectdetails"), FINANCIAL("financial"), PROCUREMENT(
                "procurement"), PERFORMANCE("performance"), RESULTS("results"), DOCUMENT("documents");
        /**
         * Name of the HTML document.
         */
        private final String documentName;

        /**
         * Constructor with document name initialization.
         *
         * @param documentName
         *         name of the HTML document
         */
        DetailSnippet(final String documentName) {
            this.documentName = documentName;
        }

        /**
         * @param id
         *         project id
         *
         * @return url of the ajax snippet for the given project id
         */
        public String getUrl(final String id) {
            return String.format("%s%s.html?projId=%s&lang=en", SOURCE_URL, documentName, id);
        }
    }

    /**
     * Default constructor.
     */
    public WBPOWebProjectCrawler() {
        super();
        getWebClient().getOptions().setJavaScriptEnabled(false);
    }

    @Override
    protected HtmlPage getSearchResultsStartPageForDate(final LocalDate incrementDate) {
        pageNumber = 1;
        parsingDate = incrementDate;
        logger.info("Crawling date {}, page {}", parsingDate, pageNumber);
        return getPage(parsingDate);
    }

    @Override
    public HtmlPage getNextPage(final HtmlPage actualPage) {
        if (pageNumber == 0 || parsingDate == null) {
            return null;
        }
        pageNumber++;
        logger.info("Crawling date {}, page {}", parsingDate, pageNumber);
        return getPage(parsingDate);
    }

    @Override
    public HtmlPage extractDetailsFromPage(final HtmlPage page) {
        final Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
        final Elements rows = JsoupUtils.select("#n07v1-generic-list-table > tbody > tr", doc);
        
        for (Element row : rows) {
            final String projectId = JsoupUtils.selectText("td:eq(2)", row).trim();
            final String projectName = JsoupUtils.selectText("td:eq(0)", row).trim();

            final HashMap<String, Object> metaData = new HashMap<>();
            metaData.put("projectName", projectName);
            metaData.put("additionalUrls", Arrays.asList(
                DetailSnippet.DETAILS.getUrl(projectId),
                DetailSnippet.DOCUMENT.getUrl(projectId),
                DetailSnippet.FINANCIAL.getUrl(projectId),
                DetailSnippet.PERFORMANCE.getUrl(projectId),
                DetailSnippet.PROCUREMENT.getUrl(projectId),
                // Get Procurement with subsection contractdata
                DetailSnippet.PROCUREMENT.getUrl(projectId) + "&subTab=contractdata",
                DetailSnippet.RESULTS.getUrl(projectId)));
            
            createAndPublishMessage(DetailSnippet.OVERVIEW.getUrl(projectId), metaData);
        }
        return page;
    }

    @Override
    public boolean isPageValid(final HtmlPage page) {
        return true;
    }

    @Override
    protected LocalDate getDefaultStartDate() {
        return DEFAULT_START_DATE;
    }

    @Override
    protected ChronoUnit getIncrementUnit() {
        return ChronoUnit.DAYS;
    }

    @Override
    protected String getVersion() {
        return VERSION;
    }

    /**
     * Returns response for given url.
     *
     * @param url
     *         url
     *
     * @return response or null if request fails.
     */
    private HtmlPage getUrlResponse(final String url) {
        try {
            return getWebClient().getPage(url);
        } catch (Exception e) {
            logger.error("Request to the page {} failed.", url, e);
            throw new UnrecoverableException("Unable to crawl page", e);
        }
    }

    /**
     * For the given {@code pageNumber} and {@code date} returns result page.
     *
     * @param date
     *         date
     *
     * @return valid result page or null
     */
    private HtmlPage getPage(final LocalDate date) {
        final String url = String.format(RESULT_SET_URL_TEMPLATE, RESULT_ROWS_COUNT,
                (pageNumber - 1) * RESULT_ROWS_COUNT, date.format(RESULT_SET_URL_DATE_FORMATTER));

        final HtmlPage response = getUrlResponse(url);
        return (isResultPageValid(response) ? response : null);
    }

    /**
     * Checks whether the given page is valid result page. That page contains at least one project element.
     *
     * @param page
     *         validated page
     *
     * @return decision if page is valid or not
     */
    private boolean isResultPageValid(final HtmlPage page) {
        return JsoupUtils.exists("tbody > tr",
                Jsoup.parse(page.getWebResponse().getContentAsString()).getElementById("n07v1-generic-list-table"));
    }
}
