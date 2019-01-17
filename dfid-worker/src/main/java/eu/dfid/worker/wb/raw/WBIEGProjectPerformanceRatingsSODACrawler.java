package eu.dfid.worker.wb.raw;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * IEG (Independent Evaluation Group) World Bank Project Performance Ratings crawler.
 *
 * @author Marek Mikes
 */
public final class WBIEGProjectPerformanceRatingsSODACrawler extends BaseSODACrawler {
    private static final String VERSION = "2";

    private final WBIEGProjectPerformanceRatingsSODAConnector apiConnector =
            new WBIEGProjectPerformanceRatingsSODAConnector();

    private static final String FILTERING_DATE_COLUMN = "ieg_evaldate_2";
    private static final DateTimeFormatter FILTERING_DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yy");

    private static final LocalDate OLDEST_ASSESSMENT_DATE = LocalDate.of(1972, Month.OCTOBER, 20);

    @Override
    public String getVersion() {
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
    public LocalDate getDefaultStartDate() {
        return OLDEST_ASSESSMENT_DATE;
    }

    @Override
    protected ChronoUnit getIncrementUnit() {
        return ChronoUnit.DAYS;
    }
}
