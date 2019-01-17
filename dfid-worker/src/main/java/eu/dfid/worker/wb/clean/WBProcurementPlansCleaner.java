package eu.dfid.worker.wb.clean;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.clean.BaseDfidProjectCleaner;
import eu.dfid.worker.clean.plugin.project.DFIDProjectPublicationPlugin;
import eu.dfid.worker.clean.plugin.project.DFIDProjectShortTextPlugin;
import eu.dfid.worker.wb.clean.utils.WBCleanUtils;

/**
 * World Bank procurement plans cleaner.
 *
 * @author Tomas Mrazek
 */
public class WBProcurementPlansCleaner extends BaseDfidProjectCleaner {

    private static final String VERSION = "1.0";

    private static final Locale LOCALE = new Locale("en");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu/MM/dd", LOCALE);

    @Override
    protected final void registerSpecificPlugins() {
        pluginRegistry
            .registerPlugin("dfid-publications",
                new DFIDProjectPublicationPlugin(null, DATE_FORMATTER, null))
            .registerPlugin("dfid-shortstrings", new DFIDProjectShortTextPlugin());
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }

    @Override
    protected final DFIDCleanProject postProcessSourceSpecificRules(final ParsedProject parsedItem,
        final DFIDCleanProject cleanItem) {

        return cleanItem;
    }

    @Override
    protected final ParsedProject preProcessParsedItem(final ParsedProject parsedItem) {
        return (ParsedProject) WBCleanUtils.replaceNAWithNull(parsedItem);
    }
}
