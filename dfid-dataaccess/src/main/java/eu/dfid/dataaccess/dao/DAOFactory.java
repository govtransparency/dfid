package eu.dfid.dataaccess.dao;

import eu.dfid.dataaccess.dao.jdbc.JdbcDAOFactory;
import eu.dl.core.config.Config;
import eu.dl.dataaccess.dao.CleanTenderDAO;
import eu.dl.dataaccess.dao.CrawlerAuditDAO;
import eu.dl.dataaccess.dao.ParsedTenderDAO;
import eu.dl.dataaccess.dao.RawDataDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract DAO factory class for creating DAO objects.
 */
public abstract class DAOFactory {

    /**
     * Default factory type.
     */
    private static final DAOFactoryType DEFAULT_FACTORY_TYPE = DAOFactoryType.JDBC;

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DAOFactory.class);

    /**
     * DAO types supported by the factory.
     */
    private enum DAOFactoryType {
        JDBC
    }

    /**
     * Creates and returns new default DAO factory.
     *
     * @return default DAO factory
     */
    public static DAOFactory getDefaultDAOFactory() {
        return getDAOFactory(DEFAULT_FACTORY_TYPE);
    }

    /**
     * Creates and returns new DAO factory of type specified in configuration.
     *
     *
     * @return new DAO factory of configured type
     */
    public static DAOFactory getDAOFactory() {
        return getDAOFactory(DAOFactoryType.valueOf(Config.getInstance().getParam("daofactory.type")));
    }

    /**
     * Creates and returns new DAO factory of specified type.
     *
     * @param factoryType
     *            type of requested DAO factory
     *
     * @return new DAO factory of specified type
     */
    public static DAOFactory getDAOFactory(final DAOFactoryType factoryType) {
        switch (factoryType) {
            case JDBC:
                return new JdbcDAOFactory();
            default:
                logger.error("Unknown factory type while trying to create DAO factory.");
                throw new IllegalArgumentException("Unknown factory type.");
        }
    }

    // There will be a method for each DAO that can be created. The concrete factories will have to implement these
    // methods.

    /**
     * Gets the Crawler Audit DAO.
     *
     * @param workerName
     *         name of the worker manipulating with data via this DAO
     * @param workerVersion
     *         version of the worker manipulating with data via this DAO
     *
     * @return DAO object for managing crawler audit log.
     */
    public abstract CrawlerAuditDAO getCrawlerAuditDAO(String workerName, String workerVersion);

    /**
     * Gets the Raw Tender DAO.
     *
     * @param workerName
     *         name of the worker manipulating with data via this DAO
     * @param workerVersion
     *         version of the worker manipulating with data via this DAO
     *
     * @return DAO object for managing raw tender data
     */
    public abstract RawDataDAO getRawTenderDAO(String workerName, String workerVersion);

    /**
     * Gets the Parsed Tender DAO.
     *
     * @param workerName
     *         name of the worker manipulating with data via this DAO
     * @param workerVersion
     *         version of the worker manipulating with data via this DAO
     *
     * @return DAO object for managing parsed tenders
     */
    public abstract ParsedTenderDAO getParsedTenderDAO(String workerName, String workerVersion);

    /**
     * Gets the Clean Tender DAO.
     *
     * @param workerName
     *         name of the worker manipulating with data via this DAO
     * @param workerVersion
     *         version of the worker manipulating with data via this DAO
     *
     * @return DAO object for managing clean tenders
     */
    public abstract CleanTenderDAO getCleanTenderDAO(String workerName, String workerVersion);



    /**
     * Gets the Raw Project DAO.
     *
     * @param workerName
     *         name of the worker manipulating with data via this DAO
     * @param workerVersion
     *         version of the worker manipulating with data via this DAO
     *
     * @return DAO object for managing raw project data
     */
    public abstract RawProjectDAO getRawProjectDAO(String workerName, String workerVersion);

    /**
     * Gets the Parsed Project DAO.
     *
     * @param workerName
     *         name of the worker manipulating with data via this DAO
     * @param workerVersion
     *         version of the worker manipulating with data via this DAO
     *
     * @return DAO object for managing parsed projects
     */
    public abstract ParsedProjectDAO getParsedProjectDAO(String workerName, String workerVersion);

    /**
     * Gets the Clean Project DAO.
     *
     * @param workerName
     *         name of the worker manipulating with data via this DAO
     * @param workerVersion
     *         version of the worker manipulating with data via this DAO
     *
     * @return DAO object for managing clean projects
     */
    public abstract CleanProjectDAO getCleanProjectDAO(String workerName, String workerVersion);

    /**
     * Gets transaction utils used to handle transactions.
     *
     * @return transaction utils
     */
    public abstract TransactionUtils getTransactionUtils();
}
