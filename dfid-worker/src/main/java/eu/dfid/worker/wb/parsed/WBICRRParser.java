package eu.dfid.worker.wb.parsed;

import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedProjectOperation;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.raw.RawData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation Completion and Results Report documents parser for World Bank.
 *
 * @author Marek Mikes
 */
public class WBICRRParser extends BaseWBDocumentsAndReportsProjectParser {
    private static final String VERSION = "1";

    @Override
    public final List<ParsedProject> parse(final RawData rawProject) {
        final Document form = Jsoup.parse(rawProject.getSourceData());

        ParsedProject parsedProject = new ParsedProject();

        // parse DFID attributes
        parsedProject
                .setProjectId(parseProjectId(form))
                .addOperation(new DFIDParsedProjectOperation()
                        .setOperationNumber(parseProjectOperationNumber(form)))
                .addPublication(new ParsedPublication()
                        .setPublicationDate(parsePublicationDate(form))
                        .setSourceFormType(parsePublicationSourceFormType(form))
                        .setSourceId(parsePublicationSourceId(form))
                        .setLastUpdate(parsePublicationLastUpdate(form))
                        .setLanguage(parsePublicationLanguage(form))
                        .setIsIncluded(true)
                        .setSource(PublicationSources.WB)
                        .setHumanReadableUrl(rawProject.getSourceUrl().toString()));

        return new ArrayList<>(Collections.singletonList(parsedProject));
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }
}
