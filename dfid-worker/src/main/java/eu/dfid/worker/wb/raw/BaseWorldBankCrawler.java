package eu.dfid.worker.wb.raw;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dl.core.UnrecoverableException;
import eu.dl.dataaccess.dao.CrawlerAuditDAO;
import eu.dl.worker.Message;
import eu.dl.worker.raw.BaseHttpCrawler;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

/**
 * Paged source crawler for World Bank.
 *
 * @author Michal Riha
 */
abstract class BaseWorldBankCrawler extends BaseHttpCrawler {
    protected static final String SOURCE_DOMAIN = "http://projects.worldbank.org";

    protected final CrawlerAuditDAO dao = DAOFactory.getDAOFactory().getCrawlerAuditDAO(getName(), getVersion());

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean requestedDataCrawled;

    private static final DateTimeFormatter DATA_DATE_FORMATTER = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("MMMM d, uuuu")
        .toFormatter(Locale.US)
        .withZone(ZoneId.systemDefault());

    private static final DateTimeFormatter MESSAGE_DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);

    static final String URL_NO_OF_ROWS = "URL_NO_OF_ROWS";
    static final String URL_START_INDEX = "URL_START_INDEX";
    private static final int NUMBER_OF_ROWS_TO_SHOW = 20;
    private long rowsParsed;

    private String templatePageUrl;
    private int dateCellIndex;
    private LocalDate firstDateAvailable;

    private static final String ERROR_MESSAGE = "//div[@id='projectsearchresult']/div[@id='superhead' and " +
            "contains(text(),'Sorry, there are no results that match your search.')]";
    private static final int INCORRECT_PAGE_COUNT_LIMIT = 20;

    /**
     * Default constructor for WorldBankCrawler. Sets url to crawl, table date cell to parse date from and checks if
     * url is in correct format.
     */
    BaseWorldBankCrawler() {
        super();
        getWebClient().getOptions().setJavaScriptEnabled(false);

        templatePageUrl = getTemplatePageUrl();
        dateCellIndex = getDateCellIndex();
        firstDateAvailable = getFirstDateAvailable();

        //check url is in correct format
        if (!templatePageUrl.contains(URL_NO_OF_ROWS) || !templatePageUrl.contains(URL_START_INDEX)) {
            logger.error(
                    "Variable templatePageUrl is not in correct format, please check how templatePageUrl is set in "
                            + "crawler.");
            throw new RuntimeException("Unable to start crawling, malformed URL.");
        }
    }

    @Override
    protected final void doWork(final Message message) {
        setCrawlingDateRange(message);
        HtmlPage page = getFirstPage();

        while (page != null) {
            extractDetails(page);
            page = getNextPage();
        }

        dao.updateLastCrawledDateForCrawler(endDate);
    }

    /**
     * Gets template url for page crawling.
     * Template should have URL_NO_OF_ROWS
     *
     * @return String, template url for crawling
     */
    protected abstract String getTemplatePageUrl();

    /**
     * Gets index in which table cell on page is publication/signing date.
     *
     * @return int
     */
    protected abstract int getDateCellIndex();

    /**
     * Set first first date data is available.
     *
     * @return LocalDate
     */
    protected abstract LocalDate getFirstDateAvailable();

    /**
     * Extracts url from given table row and prepares RabbitMQ message for downloader.
     *
     * @param tableRow tableRow to parse url from
     */
    protected abstract void extractUrlFromRow(HtmlTableRow tableRow);

    /**
     * Sets date range for crawling. Dates passed in message take priority over dates stored in crawler audit log.
     *
     * @param message message that may contain start and end dates
     */
    private void setCrawlingDateRange(final Message message) {
        final LocalDate yesterday = LocalDate.now().minusDays(1);

        // set the start date
        final String startDateString = message.getValue("startDate");
        if (startDateString != null) {
            // set dates from message
            startDate = LocalDate.parse(startDateString, MESSAGE_DATE_FORMATTER);
            if (startDate.isAfter(yesterday)) {
                logger.error("Crawler \"startDate\" date {} cannot be after yesterday ({})", startDate, yesterday);
                throw new UnrecoverableException("Crawling failed. Start date is after yesterday.");
            } else if (startDate.isBefore(getDefaultStartDate())) {
                startDate = getDefaultStartDate();
                logger.warn("\"startDate\" date is before default start date. Setting it to the default start date.");
            }

            final String endDateString = message.getValue("endDate");
            if (endDateString != null) {
                endDate = LocalDate.parse(endDateString, MESSAGE_DATE_FORMATTER);
                if (endDate.isAfter(yesterday)) {
                    endDate = yesterday;
                    logger.warn("\"endDate\" is today or later. Setting it to yesterday.");
                }
            } else {
                endDate = yesterday;
            }

            if (startDate.isAfter(endDate)) {
                logger.error("Crawler start date {} cannot be after end date {}", startDate, endDate);
                throw new UnrecoverableException("Crawling failed. Start date is after end date.");
            }
        } else {
            // set dates from crawler audit collection
            final LocalDate lastCrawledDate = dao.getLastCrawledDateByCrawler();
            if (lastCrawledDate != null) {
                startDate = lastCrawledDate.plusDays(1);
            } else {
                startDate = getDefaultStartDate();
            }
            endDate = yesterday;
        }
    }

    /**
     * Sets crawler to start crawling from beginning and returns first page.
     *
     * @return HtmlPage first page to crawl
     */
    private HtmlPage getFirstPage() {
        rowsParsed = 0;
        requestedDataCrawled = false;
        return getResultPage();
    }

    /**
     * Sets crawling value to crawl after already crawled data, and returns next page.
     *
     * @return HtmlPage next page to crawl
     */
    private HtmlPage getNextPage() {
        if (requestedDataCrawled) {
            return null;
        }
        rowsParsed = rowsParsed + NUMBER_OF_ROWS_TO_SHOW;
        return getResultPage();
    }

    /**
     * Get page with NUMBER_OF_ROWS_TO_SHOW number of rows, starting after URL_NO_OF_ROWS row number.
     *
     * @return HtmlPage
     */
    private HtmlPage getResultPage() {
        try {
            int incorrectPageCount = 0;
            // World Bank hosts responses are not always complete, so here are few checks that we have
            // correctly loaded page
            while (true) {
                // Create url from with template URL and try to load it.
                String url = templatePageUrl.replace(URL_NO_OF_ROWS, String.valueOf(NUMBER_OF_ROWS_TO_SHOW))
                        .replace(URL_START_INDEX, String.valueOf(rowsParsed));
                HtmlPage resultPage = getWebClient().getPage(url);

                // Check that
                // 1. some page was loaded
                // 2. page does not contain error message
                // 3. downloaded data is complete by checking date strings
                if (resultPage != null && resultPage.getByXPath(ERROR_MESSAGE).isEmpty()
                        && ((List<HtmlTableRow>) resultPage.getByXPath("//tbody/tr")).stream().anyMatch(
                                e -> !e.getCell(dateCellIndex).getTextContent().trim().isEmpty())) {
                    return resultPage;

                // if check does not comes through, try to load page again INCORRECT_PAGE_COUNT_LIMIT times
                } else {
                    if (++incorrectPageCount > INCORRECT_PAGE_COUNT_LIMIT) {
                        logger.error("Unable to load the page, url {}", url);
                        throw new UnrecoverableException(
                                "Unable to continue crawling, exceeded max page loading attempts.");
                    } else {
                        logger.warn("Attempt {} from {} to open page {} failed. Trying again.",
                                incorrectPageCount, INCORRECT_PAGE_COUNT_LIMIT, url);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Crawling failed with exception {}", e);
            throw new UnrecoverableException("Unable to crawl page", e);
        }
    }

    /**
     * Crawls data on page from new to old and sends links for detail page to RabbitMQ and returns true, when no
     * rows are found, or date range is exceeded, stop.
     *
     * @param page page to crawl
     */
    private void extractDetails(final HtmlPage page) {
        logger.info("Processing page with url {}", page.getUrl().toString());

        @SuppressWarnings("unchecked") List<HtmlTableRow> tableRows = (List<HtmlTableRow>) page.getByXPath(
                "//tbody/tr");

        // stop crawling if page is empty
        if (tableRows.isEmpty()) {
            logger.info("All data crawled, stop crawling. Page source is {}",
                    page.getWebResponse().getContentAsString());
            requestedDataCrawled = true;
            return;
        }

        // go through table, for each row, if date is in desired range, extractUrl
        for (HtmlTableRow tableRow : tableRows) {

            // get content of date cell in table
            String dateCellTextContent = tableRow.getCell(dateCellIndex).getTextContent();
            if (dateCellTextContent.isEmpty()) {
                logger.info("Date cell on page {} is empty. It's probably last document.",
                        page.getUrl().toString());
                continue;
            }

            // parse publicationDate from date cell content
            final LocalDate publicationDate;
            try {
                publicationDate = LocalDate.parse(dateCellTextContent.trim(), DATA_DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                logger.error("Parsing publication date failed for(trimmed): {} original: {} ",
                        dateCellTextContent.trim(), dateCellTextContent);
                throw new UnrecoverableException("Unable to crawl, publication date parsing failed.");
            }

            // 1. if publicationDate is in range of requested dates, extract and publish url
            // 2. if publicationDate is after requested date range, skip them (we are crawling from new to old)
            // 3. if publicationDate is before requested date range, stop crawling
            if (!(publicationDate.isBefore(startDate) || publicationDate.isAfter(endDate))) {
                extractUrlFromRow(tableRow);
            } else if (publicationDate.isAfter(endDate)) {
                continue;
            } else {
                // publication date is before start date
                logger.info("Crawling reached end of requested date range({} - {}), stopping.",
                        startDate.toString(), endDate.toString());
                requestedDataCrawled = true;
                return;
            }
        }
    }

    /**
     * First date data in source are available.
     *
     * @return LocalDate
     */
    protected LocalDate getDefaultStartDate() {
        return firstDateAvailable;
    }
}
