package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.codetables.ProjectStatus;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.CodeTablePlugin;
import eu.dl.worker.clean.utils.CodeTableUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by michal on 21.12.16.
 */
public class DFIDProjectStatusPlugin extends CodeTablePlugin<ParsedProject, DFIDCleanProject> {
    /**
     * Default constructor.
     *
     * @param mapping mapping for project statuses
     */
    public DFIDProjectStatusPlugin(final Map<Enum, List<String>> mapping) {
        super(mapping);
    }

    @Override
    public final DFIDCleanProject clean(final ParsedProject parsedProject, final DFIDCleanProject cleanProject) {
        cleanProject.setStatus((ProjectStatus) CodeTableUtils.mapValue(parsedProject.getStatus(), mapping));

        return cleanProject;
    }
}
