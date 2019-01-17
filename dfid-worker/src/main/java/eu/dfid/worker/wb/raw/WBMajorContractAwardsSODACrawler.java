package eu.dfid.worker.wb.raw;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Generates Socrata Open Data API request URL for downloading Major Contract Awards data.
 *
 * @author Tomas Mrazek
 */
public final class WBMajorContractAwardsSODACrawler extends BaseSODACrawler {
    private static final String VERSION = "2";

    private final WBMajorContractAwardsSODAConnector apiConnector = new WBMajorContractAwardsSODAConnector();

    private static final String FILTERING_DATE_COLUMN = "contract_signing_date";
    private static final DateTimeFormatter FILTERING_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00.000");

    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(1993, Month.NOVEMBER, 12);

    @Override
    protected String getVersion() {
        return VERSION;
    }

    @Override
    protected BaseSODAConnector getSODAConnector() {
        return apiConnector;
    }

    @Override
    protected String getFilteringDateColumn() {
        return FILTERING_DATE_COLUMN;
    }

    @Override
    protected DateTimeFormatter getFilteringDateFormatter() {
        return FILTERING_DATE_FORMATTER;
    }

    @Override
    protected LocalDate getDefaultStartDate() {
        return DEFAULT_START_DATE;
    }

    @Override
    protected ChronoUnit getIncrementUnit() {
        return ChronoUnit.DAYS;
    }
}
