package eu.dfid.worker.idb.parsed;

import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedProjectOperation;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.BaseDFIDTenderParser;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.dataaccess.dto.raw.RawData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static eu.dfid.worker.idb.utils.IDBProcurementPlansConstants.COUNTRY_COLUMN_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementPlansConstants.DOCUMENT_URL_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementPlansConstants.PROJECT_NAME_COLUMN_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementPlansConstants.PUBLICATION_DATE_COLUMN_METADATA_KEY;

/**
 * Procurement plans parser for Inter-American Development Bank (IDB).
 *
 * @author Marek Mikes
 */
public class IDBProcurementPlansParser extends BaseDFIDTenderParser {
    private static final String VERSION = "1";

    @Override
    public final List<ParsedTender> parse(final RawData rawTender) {
        DFIDParsedTender parsedTender = new DFIDParsedTender();

        HashMap<String, Object> metadata = rawTender.getMetaData();

        // parse DFID attributes
        parsedTender
                .setProject(new ParsedProject()
                        .setCountry(metadata.get(COUNTRY_COLUMN_METADATA_KEY).toString())
                        .addOperation(new DFIDParsedProjectOperation()
                                .setOperationNumber(metadata.get(PROJECT_NAME_COLUMN_METADATA_KEY).toString())));

        // parse common attributes
        parsedTender
                .addPublication(new ParsedPublication()
                        .setSourceFormType("Procurement Plan")
                        .setHumanReadableUrl(metadata.get(DOCUMENT_URL_METADATA_KEY).toString())
                        .setPublicationDate(metadata.get(PUBLICATION_DATE_COLUMN_METADATA_KEY).toString())
                        .setIsIncluded(true)
                        .setSource(PublicationSources.IDB));

        return new ArrayList<>(Collections.singletonList(parsedTender));
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }

    @Override
    protected final String countryOfOrigin(final ParsedTender parsed, final RawData raw){
        return null;
    }
}
