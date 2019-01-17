package eu.dfid.worker.raw;

import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dl.dataaccess.dao.CrawlerAuditDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.worker.raw.BaseIncrementalFtpCrawler;

/**
 * Base Digiwhist class for incremental crawlers for paged FTP sources.
 */
public abstract class BaseDfidIncrementalFtpCrawler extends BaseIncrementalFtpCrawler {
    @Override
    protected final CrawlerAuditDAO getCrawlerAuditDAO() {
        return DAOFactory.getDAOFactory().getCrawlerAuditDAO(getName(), getVersion());
    }

    @Override
    protected final TransactionUtils getTransactionUtils() {
        return DAOFactory.getDAOFactory().getTransactionUtils();
    }
}
