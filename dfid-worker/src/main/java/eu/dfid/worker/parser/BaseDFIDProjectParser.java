package eu.dfid.worker.parser;

import java.util.List;

import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.dataaccess.dao.ParsedDAO;
import eu.dl.dataaccess.dao.RawDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.dataaccess.dto.raw.RawData;
import eu.dl.worker.parsed.BaseParser;

/**
 * Base class for DFID project parsers. Waits for raw project data, parses it and saves to database.
 */
public abstract class BaseDFIDProjectParser extends BaseParser<RawData, ParsedProject> {
    @Override
    protected final RawDAO<RawData> getRawDAO() {
        return DAOFactory.getDAOFactory().getRawProjectDAO(getName(), getVersion());
    }

    @Override
    protected final ParsedDAO<ParsedProject> getParsedDAO() {
        return DAOFactory.getDAOFactory().getParsedProjectDAO(getName(), getVersion());
    }

    @Override
    protected final TransactionUtils getTransactionUtils() {
        return DAOFactory.getDAOFactory().getTransactionUtils();
    }

    @Override
    protected final List<ParsedProject> postProcess(final List<ParsedProject> parsedItems, final RawData raw) {
        return parsedItems;
    }
}
