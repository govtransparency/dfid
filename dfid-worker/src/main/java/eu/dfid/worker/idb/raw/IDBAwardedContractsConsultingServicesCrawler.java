package eu.dfid.worker.idb.raw;

import java.time.LocalDate;
import java.time.Month;

/**
 * Awarded contracts (consulting services) crawler for Inter-American Development Bank (IDB).
 *
 * @author Marek Mikes
 */
public final class IDBAwardedContractsConsultingServicesCrawler extends BaseIDBAwardedContractsCrawler {
    private static final LocalDate OLDEST_NOTICE_DATE = LocalDate.of(1966, Month.SEPTEMBER, 4);
    private static final String SEARCH_TYPE_URL_VALUE = "consulting_services";

    @Override
    protected LocalDate getDefaultStartDate() {
        return OLDEST_NOTICE_DATE;
    }

    @Override
    protected String getSearchTypeUrlValue() {
        return SEARCH_TYPE_URL_VALUE;
    }
}
