package eu.dfid.worker.clean.plugin;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.clean.DFIDCleanProjectOperation;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.dataaccess.dto.clean.Cleanable;
import eu.dl.dataaccess.dto.parsed.Parsable;
import eu.dl.worker.clean.plugin.BaseCleaningPlugin;
import eu.dl.worker.utils.ArrayUtils;

/**
 * Base class used to clean DFID project operations.
 *
 *    @param <T>
 *            parsable item
 *    @param <U>
 *            cleanable item
 */
public abstract class BaseDFIDProjectOperationPlugin<T extends Parsable, U extends Cleanable>
        extends BaseCleaningPlugin<T, U> {
    /**
     * Cleans DFID project operations.
     *
     * @param parsed
     *            project with source data
     * @param clean
     *            project with clean data
     *
     * @return project with cleaned data
     */
    protected final DFIDCleanProject cleanProjectOperation(final ParsedProject parsed, final DFIDCleanProject clean) {
        logger.debug("Cleaning operations for parsed project {} starts", parsed.getId());

        clean.setOperations(ArrayUtils.walk(parsed.getOperations(),
                (parsedOperation) -> {
                    DFIDCleanProjectOperation cleanOperation = new DFIDCleanProjectOperation();

                    cleanOperation
                            .setOperationNumber(parsedOperation.getOperationNumber());

                    return cleanOperation;
                }));

        logger.debug("Cleaning operations for parsed project {} starts", parsed.getId());

        return clean;
    }
}
