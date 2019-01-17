package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.clean.plugin.BaseDFIDProjectOperationPlugin;

/**
 * Plugin used to clean DFID project operations.
 *
 * @author Tomas Mrazek
 */
public class DFIDProjectOperationPlugin extends BaseDFIDProjectOperationPlugin<ParsedProject, DFIDCleanProject> {
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
    @Override
    public final DFIDCleanProject clean(final ParsedProject parsed, final DFIDCleanProject clean) {
        return cleanProjectOperation(parsed, clean);
    }
}
