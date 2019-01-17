package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.BaseCleaningPlugin;
import eu.dl.worker.clean.utils.StringUtils;

/**
 * Created by michal on 21.12.16.
 */
public class DFIDProjectLongTextPlugin extends BaseCleaningPlugin<ParsedProject, DFIDCleanProject> {

    /**
     * Cleans dfid project ling string fields.
     *
     * @param parsedProject
     *            project with source data
     * @param cleanProject
     *            project with clean data
     *
     * @return project with cleaned data
     */
    @Override
    public final DFIDCleanProject clean(final ParsedProject parsedProject, final DFIDCleanProject cleanProject) {

        cleanProject.setDescription(StringUtils.cleanLongString(parsedProject.getDescription()));

        return cleanProject;
    }
}
