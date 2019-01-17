package eu.dfid.worker.raw;

import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dl.dataaccess.dao.CrawlerAuditDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.worker.raw.BaseIncrementalPagedSourceHttpCrawler;

/**
 * Base Digiwhist class for incremental crawlers for paged HTTP sources.
 */
public abstract class BaseDfidIncrementalPagedSourceHttpCrawler extends BaseIncrementalPagedSourceHttpCrawler {
    @Override
    protected final CrawlerAuditDAO getCrawlerAuditDAO() {
        return DAOFactory.getDAOFactory().getCrawlerAuditDAO(getName(), getVersion());
    }

    @Override
    protected final TransactionUtils getTransactionUtils() {
        return DAOFactory.getDAOFactory().getTransactionUtils();
    }
}
