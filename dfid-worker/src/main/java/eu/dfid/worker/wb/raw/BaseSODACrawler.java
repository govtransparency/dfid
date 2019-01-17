package eu.dfid.worker.wb.raw;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dfid.worker.raw.BaseDfidIncrementalCrawler;
import eu.dl.core.UnrecoverableException;
import eu.dl.worker.utils.ThreadUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Base crawler for SODA (Socrata Open Data API).
 *
 * @author Marek Mikes
 */
abstract class BaseSODACrawler extends BaseDfidIncrementalCrawler {
    private static final int API_WAITING_FOR_PROCESSING = 1000;
    private static final int API_REQUEST_ATTEMPTS_NUMBER_LIMIT = 10;

    /**
     * Returns the SODA connector of crawler worker.
     *
     * @return actual crawler worker SODA connector
     */
    protected abstract BaseSODAConnector getSODAConnector();

    /**
     * Returns the column of date (which is desired to filter) of crawler worker.
     *
     * @return column of date (which is desired to filter) of crawler worker
     */
    protected abstract String getFilteringDateColumn();

    /**
     * @return formatter of date (which is desired to filter) of crawler worker
     */
    protected abstract DateTimeFormatter getFilteringDateFormatter();

    @Override
    protected void initialSetup() {
        // nothing to do
    }

    @Override
    protected void finalCleanup() {
        // nothing to do
    }

    @Override
    protected final void crawlSourceForDate(final LocalDate date) {
        logger.info("Crawling for {} starts", date);
        final HashMap<String, Object> filter = new HashMap<>();
        filter.put("$select", "*, :id");
        filter.put(getFilteringDateColumn(), date.format(getFilteringDateFormatter()));

        int page = 1;
        int numberOfAttempts = 1;

        try {
            SODAResponse response = getResultSet(page, filter);
            while (!response.isEmpty()) {
                switch (response.getStatus()) {
                    case SUCCESS:
                        processSODAResponse(response);
                        break;
                    case PROCESSING:
                        if (numberOfAttempts > API_REQUEST_ATTEMPTS_NUMBER_LIMIT) {
                            logger.error(
                                "SODA request with url {} is still processed. After {} attempts crawling terminated.",
                                response.getUrl(), API_REQUEST_ATTEMPTS_NUMBER_LIMIT);
                            throw new UnrecoverableException("Request unable to finish. Forcefully terminated.");
                        } else {
                            ThreadUtils.sleep(API_WAITING_FOR_PROCESSING);
                            numberOfAttempts++;
                            continue;
                        }
                    case UNKNOWN:
                    default:
                        logger.error("SODA request with url {} returned unknown state with code {}.",
                                response.getUrl(), response.getStatus().getHttpCode());
                        throw new UnrecoverableException("API returned unknown state");
                }
                page++;
                response = getResultSet(page, filter);
            }
        } catch (IOException e) {
            logger.error("Downloading failed with exception {}", e);
            throw new UnrecoverableException("Downloading failed", e);
        }

        logger.info("Crawling for {} finish", date);
    }

    /**
     * Sends request to API endpoint and returns results list for the given page and filter.
     *
     * @param page
     *         page number
     * @param filter
     *         filter as set of field-value pairs
     *
     * @return API response
     * @throws UnrecoverableException
     *         in case that result set retrieving fails or response is one of error states
     */
    private SODAResponse getResultSet(final int page, final HashMap<String, Object> filter) {
        try {
            final SODAResponse response = getSODAConnector().getResultsList(page, filter);
            if (response.isError()) {
                logger.error("API request for page #{} fails with server error {}.", page, response.getErrorMessage());
                throw new UnrecoverableException("API request fails with server error",
                        new Exception(response.getErrorMessage()));
            }
            return response;
        } catch (IOException e) {
            logger.error("API request for page #{} fails with exception {}.", page, e);
            throw new UnrecoverableException("API request fails", e);
        }
    }

    /**
     * Parses the given JSON string and returns iterator of nodes collection.
     *
     * @param json
     *      JSON string
     * @return nodes list iterator
     */
    private Iterator<JsonNode> parseJSON(final String json) {
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());

        try {
            return mapper.readTree(json).elements();
        } catch (IOException e) {
            logger.error("Parsing of JSON fails with exception {}.", e);
            throw new UnrecoverableException("Unable to parse JSON");
        }
    }

    /**
     * Processes SODA response and create/publish message for each found element.
     *
     * @param response
     *      SODA response
     */
    private void processSODAResponse(final SODAResponse response) {
        final Iterator<JsonNode> elementsIterator = parseJSON(response.getContent());
        while (elementsIterator.hasNext()) {
            final JsonNode json = elementsIterator.next();
            createAndPublishMessage(getSODAConnector().getRowUrl(json.findValue(":id").asText()), json.toString());
        }
    }
}
