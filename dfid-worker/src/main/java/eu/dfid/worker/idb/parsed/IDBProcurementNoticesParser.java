package eu.dfid.worker.idb.parsed;

import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.BaseDFIDTenderParser;
import eu.dl.dataaccess.dto.parsed.ParsedAddress;
import eu.dl.dataaccess.dto.parsed.ParsedBody;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.dataaccess.dto.raw.RawData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static eu.dfid.worker.idb.utils.IDBProcurementNoticesConstants.COUNTRY_COLUMN_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementNoticesConstants.DUE_DATE_COLUMN_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementNoticesConstants.NOTICE_TITLE_COLUMN_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementNoticesConstants.PROJECT_ID_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementNoticesConstants.PROJECT_NAME_COLUMN_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementNoticesConstants.PUBLICATION_DATE_COLUMN_METADATA_KEY;
import static eu.dfid.worker.idb.utils.IDBProcurementNoticesConstants.TYPE_COLUMN_METADATA_KEY;

/**
 * Procurement notices parser for Inter-American Development Bank (IDB).
 *
 * @author Marek Mikes
 */
public class IDBProcurementNoticesParser extends BaseDFIDTenderParser {
    private static final String VERSION = "1";

    @Override
    public final List<ParsedTender> parse(final RawData rawTender) {
        DFIDParsedTender parsedTender = new DFIDParsedTender();

        HashMap<String, Object> metadata = rawTender.getMetaData();

        // parse DFID attributes
        parsedTender
                .setProject(new ParsedProject()
                        .setName(metadata.get(PROJECT_NAME_COLUMN_METADATA_KEY).toString()));
        if (metadata.get(PROJECT_ID_METADATA_KEY) != null) {
            parsedTender
                    .setProject(parsedTender.getProject()
                            .setProjectId(metadata.get(PROJECT_ID_METADATA_KEY).toString()));
        }

        // parse common attributes
        parsedTender
                .addPublication(new ParsedPublication()
                        .setSourceFormType(metadata.get(TYPE_COLUMN_METADATA_KEY).toString())
                        .setPublicationDate(metadata.get(PUBLICATION_DATE_COLUMN_METADATA_KEY).toString())
                        .setIsIncluded(true)
                        .setSource(PublicationSources.IDB)
                        .setHumanReadableUrl(rawTender.getSourceUrl().toString()))
                .addBuyer(new ParsedBody()
                        .setAddress(new ParsedAddress()
                                .setCountry(metadata.get(COUNTRY_COLUMN_METADATA_KEY).toString())))
                .setTitle(metadata.get(NOTICE_TITLE_COLUMN_METADATA_KEY).toString());
                
        
        if (metadata.get(DUE_DATE_COLUMN_METADATA_KEY) != null) { // DUE can be null!
            parsedTender.setBidDeadline(metadata.get(DUE_DATE_COLUMN_METADATA_KEY).toString());
        }

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
