package eu.dfid.worker.idb.utils;

/**
 * Constants for Inter-American Development Bank (IDB) procurement notices.
 */
public final class IDBProcurementNoticesConstants {
    /**
     * Private constructor to make this class static.
     */
    private IDBProcurementNoticesConstants() {
    }

    /**
     * Metadata key for string in TYPE column.
     */
    public static final String TYPE_COLUMN_METADATA_KEY = "type";
    /**
     * Metadata key for string in COUNTRY column.
     */
    public static final String COUNTRY_COLUMN_METADATA_KEY = "country";
    /**
     * Metadata key for string in NOTICE TITLE column.
     */
    public static final String NOTICE_TITLE_COLUMN_METADATA_KEY = "noticeTitle";
    /**
     * Metadata key for string in PROJECT NAME column.
     */
    public static final String PROJECT_NAME_COLUMN_METADATA_KEY = "projectName";
    /**
     * Metadata key for project ID.
     */
    public static final String PROJECT_ID_METADATA_KEY = "projectId";
    /**
     * Metadata key for string in PUBLICATION DATE column.
     */
    public static final String PUBLICATION_DATE_COLUMN_METADATA_KEY = "publicationDate";
    /**
     * Metadata key for string in DUE DATE column.
     */
    public static final String DUE_DATE_COLUMN_METADATA_KEY = "dueDate";
}
