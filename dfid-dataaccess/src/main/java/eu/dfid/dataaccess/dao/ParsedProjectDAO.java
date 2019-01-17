package eu.dfid.dataaccess.dao;

import java.util.List;

import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.dataaccess.dao.ParsedDAO;

/**
 * Parsed Project DAO interface. Specifies methods for manipulating data about
 * project that has been parsed (but not typed yet).
 *
 * @param <T>
 *         implementation class type that should be used for parsed tender
 */
public interface ParsedProjectDAO<T extends ParsedProject> extends ParsedDAO<T> {
    // signatures of methods for CRUD operations on parsed projects

    /**
     * Saves given project to persistent storage.
     *
     * @param parsedProject
     *            parsed project data to be saved
     * @return Id of saved project
     */
    String save(T parsedProject);

    /**
     * Returns the object by given id.
     *
     * @param id
     *            id to be searched
     * @return parsed project with given id
     */
    T getById(String id);

    /**
     * Returns objects which has been stored by the particular version of the
     * crawler/downloader.
     *
     * @param name
     *            downloader/crawler name
     * @param version
     *            downloader/crawler version
     * @param fromDate
     *            from date
     * @param toDate
     *            to date
     *
     * @return set of object with only one attribute id having set.
     */
    List<T> getMine(String name, String version, String fromDate, String toDate);
}
