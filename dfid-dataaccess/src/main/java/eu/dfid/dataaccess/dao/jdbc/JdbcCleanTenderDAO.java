package eu.dfid.dataaccess.dao.jdbc;

import eu.dfid.dataaccess.dto.clean.DFIDCleanTender;
import eu.dl.dataaccess.dao.CleanTenderDAO;
import eu.dl.dataaccess.dao.jdbc.GenericJdbcDAO;

import java.time.LocalDate;
import java.util.List;

/**
 * Jdbc DAO implementation for clean tenders.
 */
public class JdbcCleanTenderDAO extends GenericJdbcDAO<DFIDCleanTender> implements CleanTenderDAO<DFIDCleanTender> {
    private static final String TABLE_NAME = "clean_tender";

    @Override
    protected final String getTableWithSchema() {
        return schema + "." + TABLE_NAME;
    }

    @Override
    public final DFIDCleanTender getEmptyInstance() {
        return new DFIDCleanTender();
    }

    @Override
    public final List<String> getIncludedPublicationSourceIds(final LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
