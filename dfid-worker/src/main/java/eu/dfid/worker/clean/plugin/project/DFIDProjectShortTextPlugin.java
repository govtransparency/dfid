package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.BaseCleaningPlugin;
import eu.dl.worker.clean.utils.StringUtils;

/**
 * Plugin used to clean dfid project short string.
 *
 * @author Tomas Mrazek
 */
public final class DFIDProjectShortTextPlugin extends BaseCleaningPlugin<ParsedProject, DFIDCleanProject> {

    /**
     * Cleans dfid project short string fields.
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
        
        String projectId = StringUtils.cleanShortString(parsedProject.getProjectId());
        cleanProject.setProjectId(projectId);
        logger.debug("Cleaned projectId for parsed project {}, clean value \"{}\"", parsedProject.getId(),
            projectId);

        String name = StringUtils.cleanShortString(parsedProject.getName());
        cleanProject.setName(name);
        logger.debug("Cleaned name for parsed project {}, clean value \"{}\"", parsedProject.getId(), name);

        String country = StringUtils.cleanShortString(parsedProject.getCountry());
        cleanProject.setCountry(country);
        logger.debug("Cleaned country for parsed project {}, clean value \"{}\"", parsedProject.getId(), country);

        String teamLeader = StringUtils.cleanShortString(parsedProject.getTeamLeader());
        cleanProject.setTeamLeader(teamLeader);
        logger.debug("Cleaned teamLeader for parsed project {}, clean value \"{}\"", parsedProject.getId(), teamLeader);

        String productLine = StringUtils.cleanShortString(parsedProject.getProductLine());
        cleanProject.setProductLine(productLine);
        logger.debug("Cleaned productLine for parsed project {}, clean value \"{}\"", parsedProject.getId(),
            productLine);

        String environmentalAndSocialCategory = StringUtils.cleanShortString(parsedProject
                .getEnvironmentalAndSocialCategory());
        cleanProject.setEnvironmentalAndSocialCategory(environmentalAndSocialCategory);
        logger.debug("Cleaned environmentalAndSocialCategory for parsed project {}, clean value \"{}\"",
                parsedProject.getId(), productLine);

        String countryCode = StringUtils.cleanShortString(parsedProject.getCountryCode());
        cleanProject.setCountryCode(countryCode);
        logger.debug("Cleaned countryCode for parsed project {}, clean value \"{}\"", parsedProject.getId(),
            countryCode);

        String sectorBoard = StringUtils.cleanShortString(parsedProject.getSectorBoard());
        cleanProject.setSectorBoard(sectorBoard);
        logger.debug("Cleaned sectorBoard for parsed project {}, clean value \"{}\"", parsedProject.getId(),
            sectorBoard);

        String agreementType = StringUtils.cleanShortString(parsedProject.getAgreementType());
        cleanProject.setAgreementType(agreementType);
        logger.debug("Cleaned agreementType for parsed project {}, clean value \"{}\"", parsedProject.getId(),
            agreementType);

        return cleanProject;
    }
}
