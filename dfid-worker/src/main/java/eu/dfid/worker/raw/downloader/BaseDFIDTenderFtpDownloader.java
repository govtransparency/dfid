package eu.dfid.worker.raw.downloader;

import eu.dfid.dataaccess.dao.DAOFactory;
import eu.dl.dataaccess.dao.RawDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.dataaccess.dto.raw.RawData;
import eu.dl.worker.raw.downloader.BaseFtpDownloader;

/**
 * Base Tender FTP Downloader.
 */
public abstract class BaseDFIDTenderFtpDownloader extends BaseFtpDownloader<RawData> {

    @Override
    public final RawDAO getRawDataDao() {
        return DAOFactory.getDAOFactory().getRawTenderDAO(getName(), getVersion());
    }

    @Override
    protected final TransactionUtils getTransactionUtils() {
        return DAOFactory.getDAOFactory().getTransactionUtils();
    }
}
