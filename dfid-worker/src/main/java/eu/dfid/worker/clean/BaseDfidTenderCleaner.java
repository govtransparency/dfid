package eu.dfid.worker.clean;

import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dl.dataaccess.dao.CleanDAO;
import eu.dl.dataaccess.dao.ParsedDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.worker.clean.BaseTenderCleaner;

/**
 * This class covers the main functionality for the cleaners implementation that clean dfid tenders.
 *
 * @author Tomas Mrazek
 */
public abstract class BaseDfidTenderCleaner extends BaseTenderCleaner {

    /**
     * Default constructor.
     */
    protected BaseDfidTenderCleaner() {
        super();
    }

    @Override
    protected final CleanDAO getCleanDAO() {
        return DAOFactory.getDAOFactory().getCleanTenderDAO(getName(), getVersion());
    }

    @Override
    protected final ParsedDAO getParsedDAO() {
        return DAOFactory.getDAOFactory().getParsedTenderDAO(getName(), getVersion());
    }

    @Override
    protected final TransactionUtils getTransactionUtils() {
        return DAOFactory.getDAOFactory().getTransactionUtils();
    }
}
