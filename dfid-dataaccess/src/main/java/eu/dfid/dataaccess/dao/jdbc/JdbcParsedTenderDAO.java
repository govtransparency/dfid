package eu.dfid.dataaccess.dao.jdbc;

import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dl.dataaccess.dao.ParsedTenderDAO;
import eu.dl.dataaccess.dao.jdbc.GenericJdbcDAO;

/**
 * Jdbc DAO implementation for clean parsed tenders.
 */
public class JdbcParsedTenderDAO extends GenericJdbcDAO<DFIDParsedTender>
        implements ParsedTenderDAO<DFIDParsedTender> {
    private static final String TABLE_NAME = "parsed_tender";

    @Override
    protected final String getTableWithSchema() {
        return schema + "." + TABLE_NAME;
    }

    @Override
    public final DFIDParsedTender getEmptyInstance() {
        return new DFIDParsedTender();
    }
}
