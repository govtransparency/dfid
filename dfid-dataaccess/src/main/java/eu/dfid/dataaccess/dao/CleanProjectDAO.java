package eu.dfid.dataaccess.dao;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dl.dataaccess.dao.CleanDAO;

/**
 * Clean project DAO interface. Specifies methods for manipulating data about
 * projects that has been parsed and converted to correct data types.
 *
 * @param <T>
 *         implementation class type that should be used for clean project
 */
public interface CleanProjectDAO<T extends DFIDCleanProject> extends CleanDAO<T> {
}
