package eu.dfid.dataaccess.dao;

import eu.dl.dataaccess.dao.RawDataDAO;
import eu.dl.dataaccess.dto.raw.RawData;

/**
 * Raw Project DAO interface. Specifies methods for storing and loading project
 * raw data (source data like HTML or XML code).
 * 
 * @param <T>
 *            raw data type
 */
public interface RawProjectDAO<T extends RawData> extends RawDataDAO<T> {
}
