package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.BaseNumberPlugin;
import eu.dl.worker.clean.utils.NumberUtils;
import java.text.NumberFormat;
import java.util.List;

/**
 * Plugin used for cleaning dfid integer fields.
 *
 * @author Tomas Mrazek
 */
public final class DFIDProjectIntegerPlugin extends BaseNumberPlugin<ParsedProject, DFIDCleanProject> {

    /**
     * DFIDIntegerPlugin initialization by the number format.
     *
     * @param format
     *            number format
     */
    public DFIDProjectIntegerPlugin(final NumberFormat format) {
        super(format);
    }

    /**
     * DFIDIntegerPlugin initialization by the number formats.
     *
     * @param formats
     *            list of number formats
     */
    public DFIDProjectIntegerPlugin(final List<NumberFormat> formats) {
        super(formats);
    }

    /**
     * Cleans integer fields.
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
        final Integer fiscalYear = NumberUtils.cleanInteger(parsedProject.getApprovalFiscalYear(), formats);
        cleanProject.setApprovalFiscalYear(fiscalYear);
        logger.debug("Cleaned approvalFiscalYear for parsed project {}, clean value \"{}\"", parsedProject.getId(),
            fiscalYear);

        final Integer exitFiscalYear = NumberUtils.cleanInteger(parsedProject.getExitFiscalYear(), formats);
        cleanProject.setExitFiscalYear(exitFiscalYear);
        logger.debug("Cleaned exitFiscalYear for parsed project {}, clean value \"{}\"", parsedProject.getId(),
            exitFiscalYear);

        return cleanProject;
    }
}
