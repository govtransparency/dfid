package eu.dfid.dataaccess.dao.jdbc;

import eu.dfid.dataaccess.dao.CleanProjectDAO;
import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dl.dataaccess.dao.jdbc.GenericJdbcDAO;

/**
 * Jdbc DAO implementation for clean projects.
 */
public class JdbcCleanProjectDAO extends GenericJdbcDAO<DFIDCleanProject> implements CleanProjectDAO<DFIDCleanProject> {

    private static final String TABLE_NAME = "clean_project";

    @Override
    protected final String getTableWithSchema() {
        return schema + "." + TABLE_NAME;
    }

    @Override
    public final DFIDCleanProject getEmptyInstance() {
        return new DFIDCleanProject();
    }
}
