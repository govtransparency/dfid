package eu.dfid.worker.idb.raw;

import eu.dfid.worker.raw.downloader.BaseTenderHttpDownloader;

/**
 * Procurement plans and notices base downloader for Inter-American Development Bank (IDB).
 * There can be downloaded many file types.
 *
 * @author Marek Mikes
 */
abstract class BaseIDBProcurementDownloader extends BaseTenderHttpDownloader {
    private static final int RETRY_COUNT_LIMIT = 19;

    /**
     * Default constructor.
     */
    BaseIDBProcurementDownloader() {
        super();

        setRetryCountLimit(RETRY_COUNT_LIMIT);
    }
}
