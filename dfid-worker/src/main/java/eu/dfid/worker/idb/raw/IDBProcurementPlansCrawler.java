package eu.dfid.worker.idb.raw;

import static eu.dfid.worker.idb.utils.IDBProcurementPlansConstants.COUNTRY_COLUMN_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementPlansConstants.DOCUMENT_TITLE_COLUMN_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementPlansConstants.DOCUMENT_URL_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementPlansConstants.PROJECT_NAME_COLUMN_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementPlansConstants.PUBLICATION_DATE_COLUMN_METADATA_KEY;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import eu.dfid.worker.raw.BaseDfidIncrementalPagedSourceHttpCrawler;
import eu.dl.core.UnrecoverableException;
import eu.dl.worker.Message;
import eu.dl.worker.MessageFactory;
import eu.dl.worker.raw.utils.CrawlerUtils;

/**
 * Procurement plans crawler for Inter-American Development Bank (IDB).
 *
 * @author Marek Mikes
 */
public final class IDBProcurementPlansCrawler extends BaseDfidIncrementalPagedSourceHttpCrawler {
    private static final String VERSION = "2";

    private static final String RESULT_PAGE_URL_PATTERN = "https://www.iadb.org/en/procurement-plans-search?query[dateFrom_plan]=%s&query[dateTo_plan]=%s";
    private static final String NEXT_BUTTON_XPATH = "//a[contains(@title, 'Go to next page')]";

    private static final LocalDate OLDEST_NOTICE_DATE = LocalDate.of(2014, Month.JANUARY, 6);
    private static final DateTimeFormatter URL_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM'%2F'dd'%2F'yyyy");

    /**
     * Initialization of the crawler.
     */
    public IDBProcurementPlansCrawler() {
        super();
        getWebClient().getOptions().setJavaScriptEnabled(false);
    }

    @Override
    protected HtmlPage getSearchResultsStartPageForDate(final LocalDate incrementDate) {
        String dateString = incrementDate.format(URL_DATE_FORMATTER);
        String resultPageUrl = String.format(RESULT_PAGE_URL_PATTERN, dateString, dateString);
        try {
            return getWebClient().getPage(resultPageUrl);
        } catch (IOException e) {
            logger.error("Getting result for date {} failed", dateString, e);
            throw new UnrecoverableException("Unable to get result page", e);
        }
    }

    @Override
    public HtmlPage extractDetailsFromPage(final HtmlPage page) {
        @SuppressWarnings("unchecked") final List<HtmlTableRow> procurementPlanRows = (List<HtmlTableRow>) page
                .getByXPath(
                "//table/tbody/tr");

        for (HtmlTableRow procurementPlanRow : procurementPlanRows) {
            final HtmlAnchor documentLink = procurementPlanRow.getFirstByXPath("td[2]/a");
            if (documentLink == null) {
                continue;
            }

            final String documentUrl = documentLink.getHrefAttribute();
            // add additional information
            final HashMap<String, Object> metaData = new HashMap<>();
            metaData.put(COUNTRY_COLUMN_METADATA_KEY, procurementPlanRow.getCell(0).getTextContent().trim());
            metaData.put(DOCUMENT_TITLE_COLUMN_METADATA_KEY, procurementPlanRow.getCell(1).getTextContent().trim());
            metaData.put(DOCUMENT_URL_METADATA_KEY, documentUrl);
            metaData.put(PROJECT_NAME_COLUMN_METADATA_KEY, procurementPlanRow.getCell(2).getTextContent().trim());
            metaData.put(PUBLICATION_DATE_COLUMN_METADATA_KEY, procurementPlanRow.getCell(3).getTextContent().trim());

            final Message outgoingMessage = MessageFactory.getMessage();
            outgoingMessage.setValue("binaryDataUrl", documentUrl);
            outgoingMessage.setMetaData(metaData);
            publishMessage(outgoingMessage);
            logger.info("New message {} with url {} sent to be processed", outgoingMessage, documentUrl);
        }
        return page;
    }

    @Override
    public HtmlPage getNextPage(final HtmlPage actualPage) {
        return CrawlerUtils.clickElement(actualPage, NEXT_BUTTON_XPATH);
    }

    @Override
    protected ChronoUnit getIncrementUnit() {
        return ChronoUnit.DAYS;
    }

    @Override
    public boolean isPageValid(final HtmlPage page) {
        return true;
    }

    @Override
    protected String getVersion() {
        return VERSION;
    }

    @Override
    protected LocalDate getDefaultStartDate() {
        return OLDEST_NOTICE_DATE;
    }
}
