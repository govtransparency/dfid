package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.codetables.DFIDRegion;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.CodeTablePlugin;
import eu.dl.worker.clean.utils.CodeTableUtils;
import java.util.List;
import java.util.Map;

/**
 * Plugin used to clean dfid project bodies.
 *
 * @author Tomas Mrazek
 */
public final class DFIDProjectRegionPlugin extends CodeTablePlugin<ParsedProject, DFIDCleanProject> {
    /**
     * DFIDProjectRegionPlugin should be initialised with the region mapping and region mapping.
     *
     * @param mapping
     *      project region mapping mapping
     */
    public DFIDProjectRegionPlugin(final Map<Enum, List<String>> mapping){
        super(mapping);
    }

    /**
     * Cleans dfid project region.
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
        DFIDRegion region = (DFIDRegion) CodeTableUtils.mapValue(parsedProject.getRegion(), mapping);
        
        cleanProject.setRegion(region);
        logger.debug("Cleaned region in parsed project {}, clean value \"{}\"", parsedProject.getId(), region);

        return cleanProject;
    }
}
