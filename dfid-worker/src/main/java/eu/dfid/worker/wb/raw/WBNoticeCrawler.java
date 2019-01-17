package eu.dfid.worker.wb.raw;

import java.time.LocalDate;

import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dl.dataaccess.dao.TransactionUtils;

/**
 * Notice crawler for World Bank.
 *
 * @author Michal Riha
 */
public final class WBNoticeCrawler extends BaseWorldBankCrawler {
    private static final String VERSION = "1";

    @Override
    protected String getTemplatePageUrl() {
        return SOURCE_DOMAIN + "/p2e/procurement/procurementsearchpagination.html?" +
                "noOfRows=" + URL_NO_OF_ROWS +
                "&startIndex=" + URL_START_INDEX +
                "&lang=en&searchString=&notice_type_exact=&procurement_method_code_exact" +
                "=&procurement_method_name_exact=&procurement_type_exact=&project_ctry_code_exact" +
                "=&project_ctry_name_exact=&regionname_exact=&sectorcode_exact=&sector_exact=&rregioncode" +
                "=&submission_strdate=&submission_enddate=&deadline_strdate=&deadline_enddate=&showrecent=&clickIndex" +
                "=1&activeStartIndex=0&activeEndIndex=10&paramKey=srt&paramValue=submission_date&queryString=qterm%3D" +
                "%26srt%3Dsubmission_date+desc%2Cid+asc&sortOrder=desc";
    }

    @Override
    protected int getDateCellIndex() {
        return 5;
    }

    @Override
    protected LocalDate getFirstDateAvailable() {
        return LocalDate.of(2005, 9, 26);
    }

    @Override
    protected void extractUrlFromRow(final HtmlTableRow tableRow) {
        createAndPublishMessage(SOURCE_DOMAIN + tableRow.getCell(0)
                .getLastElementChild()
                .getFirstElementChild()
                .getAttribute("href"));
    }

    @Override
    protected String getVersion() {
        return VERSION;
    }

    @Override
    protected TransactionUtils getTransactionUtils() {
        return DAOFactory.getDAOFactory().getTransactionUtils();
    }
}
