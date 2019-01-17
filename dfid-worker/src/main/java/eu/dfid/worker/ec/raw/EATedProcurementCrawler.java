package eu.dfid.worker.ec.raw;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import eu.dfid.worker.raw.BaseDfidIncrementalFtpCrawler;

/**
 * Finds all daily packages on the TED ftp.
 *
 * @author Tomas Mrazek
 */
public final class EATedProcurementCrawler extends BaseDfidIncrementalFtpCrawler {
    /**
     * Directory that contains daily package archives.
     */
    private static final String DAILY_PACKAGES_DIR = "daily-packages/";

    /**
     * Start date for crawling.
     */
    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(2011, Month.JANUARY, 4);

    private static final String VERSION = "1.0";

    private static final DateTimeFormatter FILE_NAME_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    protected void crawlSourceForDate(final LocalDate date) {
        final StringBuilder dir = new StringBuilder(DAILY_PACKAGES_DIR).append(date.getYear())
                .append("/")
                .append(String.format("%02d", date.getMonthValue()))
                .append("/");

        final String regex = String.format("^%s.+\\.tar\\.gz", date.format(FILE_NAME_DATE_FORMATTER));
        searchFiles(dir.toString(), regex);
    }

    @Override
    protected LocalDate getDefaultStartDate() {
        return DEFAULT_START_DATE;
    }

    @Override
    protected String getVersion() {
        return VERSION;
    }

    @Override
    protected ChronoUnit getIncrementUnit() {
        return ChronoUnit.DAYS;
    }

    @Override
    protected void sourceSpecificInitialFtpSetup() {
    }

    @Override
    protected void sourceSpecificFinalFtpCleanup() {
    }
}
