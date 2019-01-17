package eu.dfid.worker.ec.raw;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import eu.dfid.worker.raw.BaseDfidIncrementalPagedSourceHttpCrawler;
import eu.dl.core.UnrecoverableException;
import eu.dl.worker.raw.utils.CrawlerUtils;

/**
 * Contract (this keyword is used on web) crawler for EuropeAid of European Commission. Crawls by years (crawling by
 * days not possible).
 *
 * @author Marek Mikes
 */
public final class EAContractCrawler extends BaseDfidIncrementalPagedSourceHttpCrawler {
    private static final String VERSION = "4";

    private static final String SEARCH_FORM_URL = "http://ec.europa.eu/europeaid/work/funding/beneficiaries";

    private static final String YEAR_SELECT_XPATH = "//form[@name='searchContracts']//select[@name='year']";
    private static final String SEARCH_BUTTON_XPATH = "//form[@name='searchContracts']//input[@type='submit']";
    private static final String NEXT_BUTTON_XPATH = "//a[child::strong[contains(text(),'Next')]]";

    private static final int OLDEST_CONTRACT_YEAR = 2007;

    private Year actualYear = null;

    /**
     * Default constructor.
     */
    public EAContractCrawler() {
        super();
        getWebClient().getOptions().setJavaScriptEnabled(false);
    }

    @Override
    protected HtmlPage getSearchResultsStartPageForDate(final LocalDate incrementDate) {
        actualYear = Year.of(incrementDate.getYear());
        try {
            final HtmlPage page = getWebClient().getPage(SEARCH_FORM_URL);
            final HtmlSelect yearSelect = page.getFirstByXPath(YEAR_SELECT_XPATH);

            final Year latestYear = Year.of(Integer.parseInt(yearSelect.getOption(0).getText()));
            if (latestYear.isBefore(actualYear)) {
                logger.info("There are not contracts for year {}", actualYear);
                return null;
            } else {
                List<HtmlOption> yearOptions = yearSelect.getOptions();
                for (int i = 0; i < yearOptions.size(); ++i) {
                    final HtmlOption yearOption = yearOptions.get(i);
                    if (yearOption.getText().equals(actualYear.toString())) {
                        yearSelect.setSelectedIndex(i);
                        break;
                    }
                }

                assert yearSelect.getOption(yearSelect.getSelectedIndex()).getText().equals(actualYear.toString());

                final HtmlSubmitInput submit = page.getFirstByXPath(SEARCH_BUTTON_XPATH);
                return submit.click();
            }
        } catch (Exception e) {
            logger.error("Searching results for year {} fails.", actualYear, e);
            throw new UnrecoverableException("Searching results for year fails", e);
        }
    }

    @Override
    public HtmlPage extractDetailsFromPage(final HtmlPage page) {
        final HashMap<String, Object> metaData = new HashMap<>();
        metaData.put("publicationYear", actualYear);
        createAndPublishMessage(page.getUrl().toString(), page.getWebResponse().getContentAsString(), metaData);
        return page;
    }

    @Override
    protected LocalDate getDefaultStartDate() {
        return LocalDate.of(OLDEST_CONTRACT_YEAR, Month.JANUARY, 1);
    }

    @Override
    public HtmlPage getNextPage(final HtmlPage actualPage) {
        return CrawlerUtils.clickElement(actualPage, NEXT_BUTTON_XPATH);
    }

    @Override
    public boolean isPageValid(final HtmlPage page) {
        return true;
    }

    @Override
    protected String getVersion() {
        return VERSION;
    }

    @Override
    protected ChronoUnit getIncrementUnit() {
        return ChronoUnit.YEARS;
    }
}
