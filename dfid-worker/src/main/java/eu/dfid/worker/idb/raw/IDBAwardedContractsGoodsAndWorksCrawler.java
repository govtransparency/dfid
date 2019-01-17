package eu.dfid.worker.idb.raw;

import java.time.LocalDate;
import java.time.Month;

/**
 * Awarded contracts (goods & works) crawler for Inter-American Development Bank (IDB).
 *
 * @author Marek Mikes
 */
public final class IDBAwardedContractsGoodsAndWorksCrawler extends BaseIDBAwardedContractsCrawler {
    private static final LocalDate OLDEST_NOTICE_DATE = LocalDate.of(1961, Month.JANUARY, 1);
    private static final String SEARCH_TYPE_URL_VALUE = "goods_works";

    @Override
    protected LocalDate getDefaultStartDate() {
        return OLDEST_NOTICE_DATE;
    }

    @Override
    protected String getSearchTypeUrlValue() {
        return SEARCH_TYPE_URL_VALUE;
    }
}
