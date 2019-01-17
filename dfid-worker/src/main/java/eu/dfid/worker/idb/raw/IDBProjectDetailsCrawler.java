package eu.dfid.worker.idb.raw;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import eu.dfid.worker.raw.BaseDfidIncrementalPagedSourceHttpCrawler;
import eu.dl.core.UnrecoverableException;
import eu.dl.worker.raw.utils.CrawlerUtils;

/**
 * Project details crawler for Inter-American Development Bank (IDB).
 *
 * @author Marek Mikes
 */
public final class IDBProjectDetailsCrawler extends BaseDfidIncrementalPagedSourceHttpCrawler {
    private static final String VERSION = "3";

    private static final String SOURCE_URL = "https://www.iadb.org";
    private static final String RESULT_PAGE_URL_PATTERN = SOURCE_URL + "/en/projects-search?query[yearFrom]=%1$s&query[yearTo]=%1$s";
    private static final String NEXT_BUTTON_XPATH = "//a[contains(@title, 'Go to next page')]";
    private static final int OLDEST_PROJECT_DETAIL_YEAR = 1960;

    /**
     * Initialization of the crawler.
     */
    public IDBProjectDetailsCrawler() {
        super();
        getWebClient().getOptions().setJavaScriptEnabled(false);
    }

    @Override
    protected HtmlPage getSearchResultsStartPageForDate(final LocalDate incrementDate) {
        final Year actualYear = Year.of(incrementDate.getYear());
        String resultPageUrl = String.format(RESULT_PAGE_URL_PATTERN, actualYear);
        try {
            return getWebClient().getPage(resultPageUrl);
        } catch (IOException e) {
            logger.error("Getting result for year {} failed", actualYear, e);
            throw new UnrecoverableException("Unable to get result page", e);
        }
    }

    @Override
    public HtmlPage extractDetailsFromPage(final HtmlPage page) {
        @SuppressWarnings("unchecked") final List<HtmlAnchor> projectDetailLinks = (List<HtmlAnchor>) page.getByXPath(
                "//table/tbody/tr/td/a");
        for (HtmlAnchor projectDetailLink : projectDetailLinks) {
            createAndPublishMessage(SOURCE_URL + projectDetailLink.getHrefAttribute());
        }
        return page;
    }

    @Override
    protected LocalDate getDefaultStartDate() {
        return LocalDate.of(OLDEST_PROJECT_DETAIL_YEAR, Month.JANUARY, 1);
    }

    @Override
    protected ChronoUnit getIncrementUnit() {
        return ChronoUnit.YEARS;
    }

    @Override
    protected String getVersion() {
        return VERSION;
    }

    @Override
    public HtmlPage getNextPage(final HtmlPage actualPage) {
        return CrawlerUtils.clickElement(actualPage, NEXT_BUTTON_XPATH);
    }

    @Override
    public boolean isPageValid(final HtmlPage page) {
        return true;
    }
}
