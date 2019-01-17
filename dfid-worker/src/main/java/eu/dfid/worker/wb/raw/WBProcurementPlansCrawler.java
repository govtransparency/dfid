package eu.dfid.worker.wb.raw;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.HashMap;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dl.core.UnrecoverableException;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.worker.Message;
import eu.dl.worker.MessageFactory;

/**
 * Procurement plans crawler for World Bank.
 *
 * @author Michal Riha
 */
public final class WBProcurementPlansCrawler extends BaseWorldBankCrawler {
    private static final String VERSION = "1";

    private static final String DOCUMENTS_DOMAIN = "http://documents.worldbank.org";

    @Override
    protected String getTemplatePageUrl() {
        return SOURCE_DOMAIN + "/p2e/procurement/procurementPlanSearchPagination.html?" + "noOfRows=" +
                URL_NO_OF_ROWS + "&startIndex=" + URL_START_INDEX +
                "&lang=en&searchString=&count_exact=&admreg_exact=&sectr_exact=&showrecent=&queryString=lang%3Den" +
                "%26lang%3Den%26srce%3Dboth";
    }

    @Override
    protected int getDateCellIndex() {
        return 1;
    }

    @Override
    protected LocalDate getFirstDateAvailable() {
        return LocalDate.of(1980, Month.MARCH, 1);
    }

    @Override
    protected void extractUrlFromRow(final HtmlTableRow tableRow) {
        final HtmlAnchor documentDetailPageLink = (HtmlAnchor) tableRow.getCell(0)
                .getLastElementChild()
                .getFirstElementChild();
        HtmlPage documentDetailPage;
        try {
            documentDetailPage = documentDetailPageLink.click();
        } catch (IOException e) {
            logger.error("Getting detail page failed ", e);
            throw new UnrecoverableException("Unable to get detail page", e);
        }

        final Message outgoingMessage = MessageFactory.getMessage();
        outgoingMessage.setValue("url", documentDetailPage.getUrl().toString());
        outgoingMessage.setValue("sourceData", documentDetailPage.getWebResponse().getContentAsString());
        HtmlAnchor txtFileLink = documentDetailPage.getFirstByXPath(
                "//ul[@class='documentLnks']/li[@class='textdoc']/a");
        // TXT file can be unavailable - see
        // http://documents.worldbank.org/curated/en/2016/07/26572774/angola-learning-all-project-procurement-plan
        if (txtFileLink != null) {
            final HashMap<String, Object> metaData = new HashMap<>();
            metaData.put("additionalUrls",
                    Collections.singletonList(DOCUMENTS_DOMAIN + txtFileLink.getHrefAttribute()));
            outgoingMessage.setMetaData(metaData);
        }
        publishMessage(outgoingMessage);
        logger.info("New message sent to be processed: {}", outgoingMessage);
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
