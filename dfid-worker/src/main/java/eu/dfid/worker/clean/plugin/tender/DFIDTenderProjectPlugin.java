package eu.dfid.worker.clean.plugin.tender;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.clean.DFIDCleanTender;
import eu.dfid.dataaccess.dto.codetables.DFIDRegion;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.clean.utils.CleanUtils;
import eu.dl.worker.clean.plugin.BaseCleaningPlugin;
import eu.dl.worker.clean.utils.CodeTableUtils;
import eu.dl.worker.clean.utils.StringUtils;
import java.util.List;
import java.util.Map;

/**
 * Plugin used to clean dfid project.
 *
 * @author Tomas Mrazek
 */
public final class DFIDTenderProjectPlugin extends BaseCleaningPlugin<DFIDParsedTender, DFIDCleanTender> {

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
    public DFIDTenderProjectPlugin(final Map<Enum, List<String>> regionMapping,
        final Map<Enum, List<String>> countryMapping){
        
        this.regionMapping = regionMapping;
        this.countryMapping = countryMapping;
    }

    /**
     * Cleans dfid tender project fields.
     *
     * @param parsedTender
     *            tender with source data
     * @param cleanTender
     *            tender with clean data
     *
     * @return tender with cleaned data
     */
    @Override
    public DFIDCleanTender clean(final DFIDParsedTender parsedTender, final DFIDCleanTender cleanTender) {
        final ParsedProject parsedProject = parsedTender.getProject();
        if (parsedProject == null) {
            return cleanTender;
        }

        DFIDCleanProject cleanProject = cleanTender.getProject();
        if (cleanProject == null) {
            cleanProject = new DFIDCleanProject();
        }
        
        String projectId = StringUtils.cleanShortString(parsedProject.getProjectId());
        cleanProject.setProjectId(projectId);
        logger.debug("Cleaned projectId for parsed tender project {}, clean value \"{}\"", parsedProject.getId(),
            projectId);

        String name = StringUtils.cleanShortString(parsedProject.getName());
        cleanProject.setName(name);
        logger.debug("Cleaned name for parsed tender project {}, clean value \"{}\"", parsedProject.getId(), name);

        String country = StringUtils.cleanShortString(parsedProject.getCountry());
        cleanProject.setCountry(country);
        logger.debug("Cleaned country for parsed tender project {}, clean value \"{}\"", parsedProject.getId(),
            country);

        String teamLeader = StringUtils.cleanShortString(parsedProject.getTeamLeader());
        cleanProject.setTeamLeader(teamLeader);
        logger.debug("Cleaned teamLeader for parsed tender project {}, clean value \"{}\"", parsedProject.getId(),
            teamLeader);

        DFIDRegion region = (DFIDRegion) CodeTableUtils.mapValue(parsedProject.getRegion(), regionMapping);
        cleanProject.setRegion(region);
        logger.debug("Cleaned region for parsed tender project {}, clean value \"{}\"", parsedProject.getId(), region);

        cleanProject.setBorrower(CleanUtils.cleanBody(parsedProject.getBorrower(), countryMapping));
        logger.debug("Cleaned borrower for parsed tender project {},", parsedProject.getId());

        String productLine = StringUtils.cleanShortString(parsedProject.getProductLine());
        cleanProject.setProductLine(productLine);
        logger.debug("Cleaned productLine for parsed tender project {}, clean value \"{}\"", parsedProject.getId(),
            productLine);

        cleanTender.setProject(cleanProject);

        return cleanTender;
    }
}
