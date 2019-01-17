package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.clean.utils.CleanUtils;
import eu.dl.worker.clean.plugin.BaseCleaningPlugin;

/**
 * Plugin used to clean dfid project bodies.
 *
 * @author Tomas Mrazek
 */
public final class DFIDProjectBodyEvaluationPlugin extends BaseCleaningPlugin<ParsedProject, DFIDCleanProject> {

    /**
     * Cleans dfid project body fields.
     *
     * @param parsedProject
     *            project with source data
     * @param cleanProject
     *            project with clean data
     *
     * @return project with cleaned data
     */
    @Override
    public DFIDCleanProject clean(final ParsedProject parsedProject, final DFIDCleanProject cleanProject) {

        cleanProject.setDonorEvaluation(CleanUtils.cleanBodyEvaluation(parsedProject.getDonorEvaluation()));
        logger.debug("Cleaned donorEvaluation for parsed project {},", parsedProject.getId());

        return cleanProject;
    }
}
