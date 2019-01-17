package eu.dfid.worker.clean.plugin.tender;

import eu.dfid.dataaccess.dto.clean.DFIDCleanTender;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dfid.worker.clean.plugin.BaseDFIDProjectOperationPlugin;

/**
 * Plugin used to clean DFID project operations of tender.
 *
 * @author Marek Mikes
 */
public class DFIDTenderProjectOperationPlugin
        extends BaseDFIDProjectOperationPlugin<DFIDParsedTender, DFIDCleanTender> {
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
    public final DFIDCleanTender clean(final DFIDParsedTender parsed, final DFIDCleanTender clean) {
        clean.setProject(cleanProjectOperation(parsed.getProject(), clean.getProject()));
        return clean;
    }
}
