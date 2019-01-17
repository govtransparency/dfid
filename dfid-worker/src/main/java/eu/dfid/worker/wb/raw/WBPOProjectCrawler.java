package eu.dfid.worker.wb.raw;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import eu.dfid.worker.raw.BaseDfidIncrementalCrawler;
import eu.dl.core.UnrecoverableException;
import eu.dl.worker.utils.jsoup.JsoupUtils;

/**
 * Downloads projects xml using api. Api provides filtering only by approval year. From year xml are extracted
 * projects for given date.
 *
 * @author Tomas Mrazek
 */
public final class WBPOProjectCrawler extends BaseDfidIncrementalCrawler {
    private static final String VERSION = "3";
    private static final String API_URL = "http://search.worldbank.org/api/v2/projects?" +
            "format=xml&source=IBRD&kw=N&rows=%d&os=%d&frmYear=%d&toYear=%d";
    /**
     * Count of tenders on resultset page.
     */
    private static final int RESULT_ROWS_COUNT = 500;
    /**
     * Maximum bytes to read from the (uncompressed) connection into the body, before the connection is closed, and the
     * input truncated.
     */
    private static final int MAX_BODY_SIZE = 20000000;

    private static final boolean VALIDATE_CERTIFICATES = false;
    private static final int DOWNLOAD_TIMEOUT = 180000; // XMLs can be big, so timeout is rather 3 minutes

    /**
     * Date format of the boardapprovaldate tag.
     */
    private static final DateTimeFormatter XML_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(1947, Month.JANUARY, 1);

    /**
     * Result page for given year and page number.
     */
    private Document yearResultsPage;
    /**
     * Contains information about parameters used for last currentYearResultPage generating. Is used for decision making
     * whether is need to download new file or use current.
     */
    private String yearResultsPageStamp;

    @Override
    protected void initialSetup() {
        // nothing to do
    }

    @Override
    protected void crawlSourceForDate(final LocalDate date) {
        int pageNumber = 1;
        do {
            setCurrentYearResultsPage(date.getYear(), pageNumber);

            final URL url;
            try {
                url = new URL(yearResultsPage.location());
            } catch (MalformedURLException e) {
                logger.error("Unable to load page #{} of tenders for {} because of the malformed url {}.", pageNumber,
                        yearResultsPage.location());
                throw new UnrecoverableException("Unable to load page for malformed url", e);
            }

            final Document doc = Jsoup.parse(yearResultsPage.html());
            final Elements projects = JsoupUtils.select("project", doc);
            boolean isDateFound = false;
            if (!projects.isEmpty()) {
                for (final Element project : projects) {
                    if (JsoupUtils.selectText("boardapprovaldate", project) == null) {
                        logger.error("Project with id {} doesn't contain 'boardapprovaldate' tag.", project.id());
                        continue;
                    }

                    final LocalDate approvalDate = LocalDate.parse(JsoupUtils.selectText("boardapprovaldate", project),
                            XML_DATE_FORMATTER);

                    if (approvalDate.isEqual(date)) {
                        isDateFound = true;
                        sendToDownloader(project.outerHtml(), url);
                        logger.info("New tender acquired for {} and sent to downloader.", date);
                    } else {
                        //data are ordered chronologically from the newest to oldest.
                        if (isDateFound || approvalDate.isBefore(date)) {
                            break;
                        }
                    }
                }
            }
            pageNumber++;
        } while (hasNextPage(yearResultsPage));
    }

    @Override
    protected void finalCleanup() {
        // nothing to do
    }

    @Override
    protected LocalDate getDefaultStartDate() {
        return DEFAULT_START_DATE;
    }

    @Override
    protected String getVersion() {
        return VERSION;
    }

    @Override
    protected ChronoUnit getIncrementUnit() {
        return ChronoUnit.DAYS;
    }

    /**
     * Returns n-th page of results for given year.
     *
     * @param year year for filtering
     * @param page page of result
     * @return n-th page of results for given year. If request fails returns null.
     */
    private Document getResultPageForYear(final int year, final int page) {
        final String url = getApiUrl(year, page);

        try {
            return Jsoup.connect(url)
                    .timeout(DOWNLOAD_TIMEOUT)
                    .validateTLSCertificates(VALIDATE_CERTIFICATES)
                    .maxBodySize(MAX_BODY_SIZE)
                    .get();
        } catch (Exception e) {
            logger.error("Request for results of year {} on page {} failed.", Integer.toString(year), url);
            throw new UnrecoverableException("Unable to load page", e);
        }

    }

    /**
     * Returns api url for given year.
     *
     * @param year year for filtering
     * @param page page of result set
     * @return api url
     */
    private String getApiUrl(final int year, final int page) {
        return String.format(API_URL, RESULT_ROWS_COUNT, (page - 1) * RESULT_ROWS_COUNT, year, year);
    }

    /**
     * For given result page determine whether exists next one.
     *
     * @param page current result page
     * @return decision whether exists next page
     */
    private boolean hasNextPage(final Document page) {
        if (!JsoupUtils.exists("projects", page)) {
            return false;
        }
        final int pageNumber = Integer.parseInt(JsoupUtils.selectAttribute("projects", "page", page), 10);
        final int totalRecordsCount = Integer.parseInt(JsoupUtils.selectAttribute("projects", "total", page), 10);
        final int rowsOnPageCount = Integer.parseInt(JsoupUtils.selectAttribute("projects", "rows", page), 10);

        return (pageNumber * rowsOnPageCount < totalRecordsCount);
    }

    /**
     * Send project to downloader and publishes appropriate message.
     *
     * @param sourceData source data of the project
     * @param url        url of the source xml file
     */
    private void sendToDownloader(final String sourceData, final URL url) {
        createAndPublishMessage(url.toString(), sourceData);
    }

    /**
     * Sets current result page for the given year and page number. If the result page from previous method calling was
     * generated with same parameters isn't need to download same file again and is returned this one. In other cases
     * is new file downloaded.
     *
     * @param year       year for filtering
     * @param pageNumber page of result
     */
    private void setCurrentYearResultsPage(final int year, final int pageNumber) {
        final String stamp = generateYearResultsPageStamp(year, pageNumber);
        if (!stamp.equals(yearResultsPageStamp)) {
            yearResultsPageStamp = stamp;
            yearResultsPage = getResultPageForYear(year, pageNumber);
        }
    }

    /**
     * Generates stamp for the given year and page number in form <year>@<pageNumber>.
     *
     * @param year       year for filtering
     * @param pageNumber page of result
     * @return year result page stamp
     */
    private String generateYearResultsPageStamp(final int year, final int pageNumber) {
        return year + "@" + pageNumber;
    }
}
