package eu.dfid.worker.wb.raw;

import java.time.LocalDate;

import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dl.dataaccess.dao.TransactionUtils;

/**
 * Contract crawler for World Bank.
 *
 * @author Michal Riha
 */
public final class WBContractCrawler extends BaseWorldBankCrawler {
    private static final String VERSION = "1";

    @Override
    protected String getTemplatePageUrl() {
        return SOURCE_DOMAIN + "/p2e/procurement/procurementcontractsearchpagination.html?" +
                "noOfRows=" + URL_NO_OF_ROWS +
                "&startIndex=" + URL_START_INDEX +
                "&lang=en&supp_name_exact=&supp_id=&procu_meth_text_exact=&procu_type_text_exact" +
                "=&procurement_group_desc=&countryshortname_exact=&regionname_exact=&supplier_countryshortname_exact" +
                "=&mjsecname_exact=&sector_exact=&sectorcode_exact=&mjsector_exact=&countrycode=&procu_meth" +
                "=&procu_type=&procurement_group=&supplier_country_exact=&rregioncode=&contr_sgn_strdate" +
                "=&contr_sgn_enddate=&supp_name=&searchString=&queryString=lang%3Den%26lang%3Den%26srce%3Dboth";
    }

    @Override
    protected int getDateCellIndex() {
        return 4;
    }

    @Override
    protected LocalDate getFirstDateAvailable() {
        return LocalDate.of(2000, 7, 1);
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
