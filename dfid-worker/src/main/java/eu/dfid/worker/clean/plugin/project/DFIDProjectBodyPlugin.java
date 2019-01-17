package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.clean.utils.CleanUtils;
import eu.dl.worker.clean.plugin.BaseCleaningPlugin;
import java.util.List;
import java.util.Map;

/**
 * Plugin used to clean dfid project bodies.
 *
 * @author Tomas Mrazek
 */
public final class DFIDProjectBodyPlugin extends BaseCleaningPlugin<ParsedProject, DFIDCleanProject> {

    private final Map<Enum, List<String>> regionMapping;

    private final Map<Enum, List<String>> countryMapping;

    /**
     * DFIDProjectPlugin should be initialised with the region mapping and country mapping.
     *
     * @param regionMapping
     *      project region mapping
     * @param countryMapping
     *      project bodies country mapping
     */
    public DFIDProjectBodyPlugin(final Map<Enum, List<String>> regionMapping,
        final Map<Enum, List<String>> countryMapping){
        
        this.regionMapping = regionMapping;
        this.countryMapping = countryMapping;
    }

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

        cleanProject.setBorrower(CleanUtils.cleanBody(parsedProject.getBorrower(), countryMapping));
        logger.debug("Cleaned borrower for parsed project {},", parsedProject.getId());

        cleanProject.setImplementingAgency(CleanUtils.cleanBody(parsedProject.getImplementingAgency(),
            countryMapping));
        logger.debug("Cleaned implementing agency for parsed project {},", parsedProject.getId());

        return cleanProject;
    }
}
