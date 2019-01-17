package eu.dfid.worker.clean;

import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.dataaccess.dao.CleanDAO;
import eu.dl.dataaccess.dao.ParsedDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.worker.clean.BaseCleaner;

/**
 * This class covers the main functionality for the cleaners implementation that clean dfid tenders.
 *
 * @author Tomas Mrazek
 */
public abstract class BaseDfidProjectCleaner extends BaseCleaner<ParsedProject, DFIDCleanProject> {

    /**
     * Default constructor.
     */
    protected BaseDfidProjectCleaner() {
        super();
    }

    @Override
    protected final CleanDAO getCleanDAO() {
        return DAOFactory.getDAOFactory().getCleanProjectDAO(getName(), getVersion());
    }

    @Override
    protected final ParsedDAO getParsedDAO() {
        return DAOFactory.getDAOFactory().getParsedProjectDAO(getName(), getVersion());
    }

    @Override
    protected final TransactionUtils getTransactionUtils() {
        return DAOFactory.getDAOFactory().getTransactionUtils();
    }

    @Override
    protected final DFIDCleanProject postProcessCommonRules(final DFIDCleanProject cleanProject,
            final ParsedProject parsedItem) {
        return cleanProject;
    }

    @Override
    protected final void registerCommonPlugins() {
    }
}
