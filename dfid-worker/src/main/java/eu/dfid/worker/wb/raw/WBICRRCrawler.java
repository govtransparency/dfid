package eu.dfid.worker.wb.raw;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import eu.dfid.worker.raw.BaseDfidIncrementalPagedSourceHttpCrawler;
import eu.dl.core.UnrecoverableException;
import eu.dl.worker.raw.utils.CrawlerUtils;
import eu.dl.worker.utils.ThreadUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * Implementation Completion and Results Report documents crawler for World Bank.
 *
 * @author Marek Mikes
 */
public final class WBICRRCrawler extends BaseDfidIncrementalPagedSourceHttpCrawler {
    private static final String VERSION = "3";
    private static final String SOURCE_DOMAIN = "http://documents.worldbank.org";
    private static final String RESULT_PAGE_URL_PATTERN = SOURCE_DOMAIN +
            "/curated/en/docadvancesearch/docs?query=&sType=2&author=&aType=2&docTitle=&tType=2&colti=&tType" +
            "=undefined&qDate=3&fromDate=&toDate=&disclDate=0&fromDisclDate=%1$s&toDisclDate=%1$s&region=&cntry=&lang"
            + "" + "=&lndinstr=&prdln=&envcat=&majorDocTY=&docTY=540632&teraTopic=&topic=&discType=&discStatus"
            + "=&report=&loan" + "=&credit=&projectId=&trustFunds=&UNRegnNbr=&sortDesc=docdt&sortType=desc";

    private static final String NEXT_BUTTON_XPATH = "//a[text()='NEXT >>']";

    private static final LocalDate OLDEST_NOTICE_DATE = LocalDate.of(2004, Month.JUNE, 18);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private static final int MAX_SLEEP_COUNT = 20;
    private static final int SLEEP_TIME = 1000;

    private final WebClient webClientForDetails;

    /**
     * Default constructor, initializes everything important i.e. urls, xpath of elements etc.
     */
    public WBICRRCrawler() {
        super();
        // Turning off HtmlUnit warnings. Warnings somehow cause process to run out of memory, when crawling goes
        // too fast.
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        // JavaScript of base web client has to be enabled, otherwise paging (click on next button) does not work
        // we use another web client to see detail, otherwise it messes with going to next page in paged view
        webClientForDetails = new WebClient();
        webClientForDetails.getOptions().setUseInsecureSSL(true);
        webClientForDetails.getOptions().setThrowExceptionOnScriptError(false);
        webClientForDetails.getOptions().setJavaScriptEnabled(false);
    }

    @Override
    protected HtmlPage getSearchResultsStartPageForDate(final LocalDate incrementDate) {
        String dateString = incrementDate.format(DATE_FORMATTER);
        String resultPageUrl = String.format(RESULT_PAGE_URL_PATTERN, dateString);
        try {
            return getWebClient().getPage(resultPageUrl);
        } catch (IOException e) {
            logger.error("Getting result for date {} failed", incrementDate, e);
            throw new UnrecoverableException("Unable to get result page", e);
        }
    }

    @Override
    public HtmlPage extractDetailsFromPage(final HtmlPage page) {
        waitForPageLoad(page);

        @SuppressWarnings("unchecked") final List<HtmlAnchor> documentDetailPageLinks = (List<HtmlAnchor>) page
                .getByXPath("//div[@id='listView']/div/a");
        for (HtmlAnchor documentDetailPageLink : documentDetailPageLinks) {
            try {
                final HtmlPage documentDetailPage = webClientForDetails.getPage(
                        documentDetailPageLink.getHrefAttribute());

                HtmlAnchor txtFileLink = documentDetailPage
                        .getFirstByXPath("//ul[@class='documentLnks']/li[@class='textdoc']/a");
                if (txtFileLink == null) {
                    createAndPublishMessage(documentDetailPage.getUrl().toString(),
                            documentDetailPage.getWebResponse().getContentAsString());
                } else {
                    final HashMap<String, Object> metaData = new HashMap<>();
                    metaData.put("additionalUrls",
                            Collections.singletonList(SOURCE_DOMAIN + txtFileLink.getHrefAttribute()));

                    createAndPublishMessage(documentDetailPage.getUrl().toString(),
                            documentDetailPage.getWebResponse().getContentAsString(), metaData);
                }
            } catch (IOException e) {
                logger.error("Crawling failed for page #{} on url {} with exception {}", getCurrentPageNumber(),
                        getCurrentPageUrl(), e);
                throw new UnrecoverableException("Crawling failed.", e);
            }
        }
        return page;
    }

    @Override
    protected LocalDate getDefaultStartDate() {
        return OLDEST_NOTICE_DATE;
    }

    @Override
    public HtmlPage getNextPage(final HtmlPage actualPage) {
        return CrawlerUtils.clickElement(actualPage, NEXT_BUTTON_XPATH);
    }

    @Override
    protected String getVersion() {
        return VERSION;
    }

    @Override
    public boolean isPageValid(final HtmlPage page) {
        return true;
    }

    @Override
    protected ChronoUnit getIncrementUnit() {
        return ChronoUnit.DAYS;
    }

    /**
     * Waits some time until the page is not fully loaded.
     *
     * @param page
     *         current list page
     */
    private void waitForPageLoad(final HtmlPage page) {
        // Wait for ajax is finished. AJAX creates the loader element with id  before sending request. After AJAX is finished,
        // removes this loader element.
        int waitTriedTimes = 0;
        while (page.getElementById("loadImg") != null && waitTriedTimes < MAX_SLEEP_COUNT) {
            logger.info("Page is not loaded -> sleep");
            ThreadUtils.sleep(SLEEP_TIME);
            waitTriedTimes++;
        }

        if (waitTriedTimes < MAX_SLEEP_COUNT) {
            logger.info("Page is loaded, crawler continues");
        } else {
            logger.error("Crawler has slept to load the page {} ms with no success", MAX_SLEEP_COUNT * SLEEP_TIME);
            throw new UnrecoverableException("Unable to crawl page");
        }
    }
}
