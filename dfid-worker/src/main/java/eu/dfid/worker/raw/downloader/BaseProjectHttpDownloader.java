package eu.dfid.worker.raw.downloader;

import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dfid.dataaccess.dao.RawProjectDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.worker.raw.downloader.BaseHttpDownloader;

/**
 * Simple HTTP Downloader abstract class for Projects.
 */
public abstract class BaseProjectHttpDownloader extends BaseHttpDownloader {

    @Override
    public final RawProjectDAO getRawDataDao() {
        return DAOFactory.getDAOFactory().getRawProjectDAO(getName(), getVersion());
    }

    @Override
    protected final TransactionUtils getTransactionUtils() {
        return DAOFactory.getDAOFactory().getTransactionUtils();
    }
}
