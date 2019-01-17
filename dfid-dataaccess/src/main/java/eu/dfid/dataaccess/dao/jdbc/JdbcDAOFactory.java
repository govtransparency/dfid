package eu.dfid.dataaccess.dao.jdbc;

import eu.dfid.dataaccess.dao.CleanProjectDAO;
import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dfid.dataaccess.dao.ParsedProjectDAO;
import eu.dfid.dataaccess.dao.RawProjectDAO;
import eu.dl.dataaccess.dao.CleanTenderDAO;
import eu.dl.dataaccess.dao.CrawlerAuditDAO;
import eu.dl.dataaccess.dao.ParsedTenderDAO;
import eu.dl.dataaccess.dao.RawDataDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.dataaccess.dao.jdbc.JdbcCrawlerAuditDAO;
import eu.dl.dataaccess.dao.jdbc.JdbcRawDataDAO;
import eu.dl.dataaccess.dao.jdbc.JdbcTransactionUtils;

/**
 * DAO factory implementation for JDBC data sources.
 */
public final class JdbcDAOFactory extends DAOFactory {

    @Override
    public RawProjectDAO getRawProjectDAO(final String workerName, final String workerVersion) {
        return (RawProjectDAO) new JdbcRawProjectDAO().populateWithWorkerMetadata(workerName, workerVersion);
    }

    @Override
    public ParsedProjectDAO getParsedProjectDAO(final String workerName, final String workerVersion) {
        return (ParsedProjectDAO) new JdbcParsedProjectDAO().populateWithWorkerMetadata(workerName, workerVersion);
    }

    @Override
    public CleanProjectDAO getCleanProjectDAO(final String workerName, final String workerVersion) {
        return (CleanProjectDAO) new JdbcCleanProjectDAO().populateWithWorkerMetadata(workerName, workerVersion);
    }

    @Override
    public CrawlerAuditDAO getCrawlerAuditDAO(final String workerName, final String workerVersion) {
        return (CrawlerAuditDAO) new JdbcCrawlerAuditDAO().populateWithWorkerMetadata(workerName, workerVersion);
    }

    @Override
    public RawDataDAO getRawTenderDAO(final String workerName, final String workerVersion) {
        return (RawDataDAO) new JdbcRawDataDAO().populateWithWorkerMetadata(workerName, workerVersion);
    }

    @Override
    public ParsedTenderDAO getParsedTenderDAO(final String workerName, final String workerVersion) {
        return (ParsedTenderDAO) new JdbcParsedTenderDAO().populateWithWorkerMetadata(workerName, workerVersion);
    }

    @Override
    public CleanTenderDAO getCleanTenderDAO(final String workerName, final String workerVersion) {
        return (CleanTenderDAO) new JdbcCleanTenderDAO().populateWithWorkerMetadata(workerName, workerVersion);
    }

    @Override
    public TransactionUtils getTransactionUtils() {
        return JdbcTransactionUtils.getInstance();
    }
}
