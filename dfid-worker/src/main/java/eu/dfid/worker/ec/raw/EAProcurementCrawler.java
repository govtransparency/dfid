package eu.dfid.worker.ec.raw;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import eu.dfid.worker.raw.BaseDfidIncrementalPagedSourceHttpCrawler;
import eu.dl.core.UnrecoverableException;
import eu.dl.worker.raw.utils.CrawlerUtils;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tender crawler for calls for proposals & Procurement notices of EuropeAid (European Commission).
 *
 * @author Marek Mikes
 */
public final class EAProcurementCrawler extends BaseDfidIncrementalPagedSourceHttpCrawler {
    private static final String VERSION = "2";

    private static final String SOURCE_URL = "https://webgate.ec.europa.eu/europeaid/online-services/";

    private static final String RESULT_PAGE_URL_PATTERN = SOURCE_URL
        + "index.cfm?do=publi.welcome&nbPubliList=50&orderby=upd&orderbyad=Desc&searchtype=AS&debpub=%1$s&finpub=%1$s";

    private static final String RESULT_IN_ENGLISH = "&userlanguage=en";

    // test if text value ('text()') is a number is done by 'number(text())=number(text())'. XPath function number()
    // returns NaN if parameter is not number and comparison NaN and NaN returns false.
    private static final String NEXT_BUTTON_XPATH =
            "//td[@class='aboveline']/table/tbody/tr/td[@class='center']/span[number(text())=number(text())" +
                    "]/following-sibling::a[1]";

    private static final LocalDate OLDEST_NOTICE_DATE = LocalDate.of(1996, Month.APRIL, 17);
    private static final DateTimeFormatter URL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd'%2F'MM'%2F'yyyy");

    /**
     * Default constructor.
     */
    public EAProcurementCrawler() {
        super();
        getWebClient().getOptions().setJavaScriptEnabled(false);
    }

    @Override
    protected HtmlPage getSearchResultsStartPageForDate(final LocalDate incrementDate) {
        String dateString = incrementDate.format(URL_DATE_FORMATTER);
        String resultPageUrl = String.format(RESULT_PAGE_URL_PATTERN, dateString);
        final HtmlPage resultPage;
        try {
            resultPage = getWebClient().getPage(resultPageUrl);
        } catch (IOException e) {
            logger.error("Getting result for date {} failed", incrementDate, e);
            throw new UnrecoverableException("Unable to get result page", e);
        }

        // check whether all results are returned
        if (resultPage.getFirstByXPath("//span[@class='error']/strong") != null) {
            logger.error("Page {} displays message that my selection returns more than 500 records!", resultPageUrl);
        }
        return resultPage;
    }

    @Override
    public HtmlPage extractDetailsFromPage(final HtmlPage page) {
        @SuppressWarnings("unchecked") final List<HtmlAnchor> detailPageLinks = (List<HtmlAnchor>) page.getByXPath(
                "//table/tbody/tr[@class]/th/a");

        for (HtmlAnchor detailPageLink : detailPageLinks) {
            String hrefAttr = detailPageLink.getHrefAttribute();

            if (hrefAttr.equals("javascript:void(0)")) {
                Matcher m = Pattern.compile("index\\.cfm?[^']+").matcher(detailPageLink.getAttribute("onclick"));
                if (m.find()) {
                    createAndPublishMessage(SOURCE_URL + m.group() + RESULT_IN_ENGLISH);
                } else {
                    logger.error("Unable to get detail url for '{}'", detailPageLink.asText());
                }
            } else {
                createAndPublishMessage(hrefAttr);
            }
        }
        return page;
    }

    @Override
    protected String getVersion() {
        return VERSION;
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
    public boolean isPageValid(final HtmlPage page) {
        return true;
    }

    @Override
    protected ChronoUnit getIncrementUnit() {
        return ChronoUnit.DAYS;
    }
}
