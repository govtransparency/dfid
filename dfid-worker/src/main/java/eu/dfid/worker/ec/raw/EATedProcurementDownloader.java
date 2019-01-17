package eu.dfid.worker.ec.raw;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import eu.dfid.worker.raw.downloader.BaseDFIDTenderFtpDownloader;
import eu.dl.core.UnrecoverableException;
import eu.dl.dataaccess.dto.raw.RawData;
import eu.dl.worker.Message;
import eu.dl.worker.utils.archive.ArchiveUtils;
import eu.dl.worker.utils.ftp.FTPFactory;
import eu.dl.worker.utils.jsoup.JsoupUtils;

/**
 * Tender downloader for calls for proposals & Procurement notices of EuropeAid (European Commission) stored in TED.
 *
 * @author Tomas Mrazek
 */
public final class EATedProcurementDownloader extends BaseDFIDTenderFtpDownloader {
    private static final String VERSION = "1.0";
    
    /**
     * Downloads and unpacks TED daily package and extract all files in archive.
     *
     * @param message
     *         RabbitMQ message
     *
     * @return raw data
     */
    @Override
    protected List<RawData> downloadAndPopulateRawDataFromFtpServer(final Message message) {
        final List<RawData> rawData = new ArrayList<>();
        final String fileUrl = message.getValue("url");
        URL url = null;

        try {
            if (FilenameUtils.getExtension(fileUrl.toLowerCase()).equals(CompressorStreamFactory.GZIP)) {
                url = new URL(FTPFactory.getFtpUrl(this.getName()) + "/" + fileUrl);

                logger.info("Unpacking daily package {}.", fileUrl);
                final InputStream dailyPackageStream = getFtpClient().retrieveFileStream(fileUrl);
                final HashMap<String, String> files = ArchiveUtils.extract(dailyPackageStream,
                        FilenameUtils.getName(fileUrl), ArchiveStreamFactory.TAR, CompressorStreamFactory.GZIP);

                for (final Map.Entry<String, String> file : files.entrySet()) {
                    if (!isEuropeAid(file.getValue())) {
                        continue;
                    }

                    logger.info("Extracting file {}.", file.getKey());

                    // init tender
                    final RawData tender = new RawData();
                    tender.setSourceData(file.getValue());
                    tender.setSourceUrl(url);
                    tender.setSourceFileName(file.getKey());

                    rawData.add(tender);

                    logger.info("New tender downloaded from url {}.", url);
                }
            }
        } catch (final Exception e) {
            logger.error("Downloading failed for daily package {}.", url, e);
            throw new UnrecoverableException("Daily package downloading failed.", e);
        }

        return rawData;
    }

    /**
     * Checks whether {@code sourceData} contains EuropeAid indetifier.
     *
     * @param sourceData
     *      file content 
     * @return true only and only if file contains EuropeAid data
     */
    private boolean isEuropeAid(final String sourceData) {
        final Document resultPage = Jsoup.parse(sourceData);
        
        final String regulationCode =
            JsoupUtils.selectText("TED_EXPORT > CODED_DATA_SECTION > CODIF_DATA > RP_REGULATION", resultPage);
        
        return regulationCode != null && regulationCode.equalsIgnoreCase("External aid and European Development Fund");
    }
    
    @Override
    public String getVersion() {
        return VERSION;
    }
}
