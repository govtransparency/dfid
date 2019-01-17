package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.codetables.LendingInstrument;
import eu.dfid.dataaccess.dto.codetables.LendingInstrumentType;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.BaseCleaningPlugin;
import eu.dl.worker.utils.ArrayUtils;
import eu.dl.worker.clean.utils.CodeTableUtils;

import java.util.List;
import java.util.Map;

/**
 * DFIDLendingInstrumentPlugin.
 */
public class DFIDProjectLendingInstrumentPlugin extends BaseCleaningPlugin<ParsedProject, DFIDCleanProject> {
    private Map<Enum, List<String>> instrumentMapping;
    private Map<Enum, List<String>> instrumentTypeMapping;

    /**
     * Default constructor.
     *
     * @param instrumentMapping mapping for instrument mapping
     * @param instrumentTypeMapping mapping for instrument type mapping
     */
    public DFIDProjectLendingInstrumentPlugin(final Map<Enum, List<String>> instrumentMapping,
                                              final Map<Enum, List<String>> instrumentTypeMapping) {
        this.instrumentMapping = instrumentMapping;
        this.instrumentTypeMapping = instrumentTypeMapping;
    }

    @Override
    public final DFIDCleanProject clean(final ParsedProject parsedProject, final DFIDCleanProject cleanProject) {
        cleanProject.setLendingInstrument((LendingInstrument) CodeTableUtils
                .mapValue(parsedProject.getLendingInstrument(), instrumentMapping));

        cleanProject.setLendingInstrumentTypes(ArrayUtils.walk(parsedProject.getLendingInstrumentTypes(),
                instrumentType -> (LendingInstrumentType) CodeTableUtils.mapValue(instrumentType,
                        instrumentTypeMapping)));

        return cleanProject;
    }
}
