package eu.dfid.worker.raw.downloader;

import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dl.dataaccess.dao.RawDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.worker.raw.downloader.BaseHttpDownloader;

/**
 * Simple HTTP Downloader abstract class for Tenders.
 */
public abstract class BaseTenderHttpDownloader extends BaseHttpDownloader {

    @Override
    public final RawDAO getRawDataDao() {
        return DAOFactory.getDAOFactory().getRawTenderDAO(getName(), getVersion());
    }

    @Override
    protected final TransactionUtils getTransactionUtils() {
        return DAOFactory.getDAOFactory().getTransactionUtils();
    }
}
