package eu.dfid.dataaccess.dao.jdbc;

import eu.dfid.dataaccess.dao.ParsedProjectDAO;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.dataaccess.dao.jdbc.GenericJdbcDAO;

/**
 * Jdbc DAO implementation for pased projects.
 */
public class JdbcParsedProjectDAO extends GenericJdbcDAO<ParsedProject> implements ParsedProjectDAO<ParsedProject> {

    private static final String TABLE_NAME = "parsed_project";

    @Override
    protected final String getTableWithSchema() {
        return schema + "." + TABLE_NAME;
    }

    @Override
    public final ParsedProject getEmptyInstance() {
        return new ParsedProject();
    }
}
