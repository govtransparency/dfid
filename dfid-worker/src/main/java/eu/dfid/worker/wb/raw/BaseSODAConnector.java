package eu.dfid.worker.wb.raw;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import eu.dl.worker.utils.http.URLUtils;

/**
 * Provides encapsulated functionality to all Socrata APIs v2.1 (SODA - The Socrata Open Data API). This class is able
 * to send requests to SODA API and process response.
 *
 * @author Tomas Mrazek
 */
abstract class BaseSODAConnector {
    private static final int DOWNLOAD_TIMEOUT = 30000;
    private static final boolean VALIDATE_CERTIFICATES = false;
    private static final boolean IGNORE_CONTENT_TYPE = true;
    private static final boolean IGNORE_HTTP_ERRORS = true;
    private static final int MAX_BODY_SIZE = 20000000;
    /**
     * Count of items per page. This number must be from 1000 to 50000.
     */
    private static final int PAGE_SIZE = 50000;

    /**
     * @return application token
     */
    public abstract String getToken();

    /**
     * @return API endpoint
     */
    public abstract String getDatasetUrl();

    /**
     * @return dataset identifier
     */
    public abstract String getDatasetIdentifier();

    /**
     * @return output format
     */
    public abstract SODAOutputFormatType getOutputFormat();

    /**
     * Sends request to API endpoint and returns response.
     *
     * @param options
     *      options of the request
     * @return API response
     * @throws IOException
     *      in case that response retrieving fails or encoding of the filter parameter value fails
     */
    public final SODAResponse get(final HashMap<String, Object> options) throws IOException {
        return getResponse(getUrl(options));
    }

    /**
     * Returns request url for the given options.
     *
     * @param options
     *  request options
     * @return request url
     * @throws UnsupportedEncodingException
     *      in case that encoding of the filter parameter value fails
     */
    public final String getUrl(final HashMap<String, Object> options) throws UnsupportedEncodingException {
        return URLUtils.addUrlParameter(getEndpointUrl(), options);
    }

    /**
     * @return API endpoint url
     */
    public final String getEndpointUrl() {
        final StringBuilder apiUrlBuilder = getEndpointUrlBase().append(getOutputFormat().getExtension());
        return apiUrlBuilder.toString();
    }

    /**
     * Sends request to API endpoint and returns results list response.
     *
     * @param page
     *      page number
     * @param filter
     *      filter as set of field-value pairs
     * @return API response
     * @throws IOException
     *      in case that response retrieving fails or encoding of the filter parameter value fails
     */
    public final SODAResponse getResultsList(final int page, final HashMap<String, Object> filter) throws IOException {
        return getResponse(getResultSetUrl(page, filter));
    }

    /**
     * Returns request url to API endpoint.
     *
     * @param page
     *      page number
     * @param filter
     *      filter as set of field-value pairs
     * @return request url
     * @throws UnsupportedEncodingException
     *      in case that encoding of the filter parameter value fails
     */
    public final String getResultSetUrl(final int page, final HashMap<String, Object> filter)
        throws UnsupportedEncodingException {
        final HashMap<String, Object> params = new HashMap<>(filter);
        params.put("$limit", PAGE_SIZE);
        params.put("$offset", (page - 1) * PAGE_SIZE);

        return getUrl(params);
    }

    /**
     * Returns StringBuilder initialized to base part of the API url.
     *
     * @return initialized StringBuilder of the API url
     */
    private StringBuilder getEndpointUrlBase() {
        return new StringBuilder()
            .append(getDatasetUrl())
            .append("resource/")
            .append(getDatasetIdentifier());
    }

    /**
     * Sends request to API endpoint and returns data of given row.
     *
     * @param rowId
     *      row id
     * @return API response
     * @throws IOException
     *      in case that response retrieving fails
     */
    public final SODAResponse getRow(final String rowId) throws IOException {
        return getResponse(getRowUrl(rowId));
    }

    /**
     * Returns API url of the request for downloading data of the given row.
     *
     * @param rowId
     *      row id
     * @return API url
     */
    public final String getRowUrl(final String rowId) {
        final StringBuilder apiUrlBuilder = getEndpointUrlBase()
            .append("/")
            .append(rowId)
            .append(getOutputFormat().getExtension());

        return apiUrlBuilder.toString();
    }

    /**
     * Sends request to API endpoint and returns response.
     *
     * @param url
     *      api request url
     * @return API response
     * @throws IOException
     *      in case that response retrieving fails
     */
    public final SODAResponse getResponse(final String url) throws IOException {
        final Connection.Response response = Jsoup
                    .connect(url)
                    .header("Accept", getOutputFormat().getMimeType())
                    .header("X-App-Token", getToken())
                    .timeout(DOWNLOAD_TIMEOUT)
                    .validateTLSCertificates(VALIDATE_CERTIFICATES)
                    .ignoreContentType(IGNORE_CONTENT_TYPE)
                    .ignoreHttpErrors(IGNORE_HTTP_ERRORS)
                    .maxBodySize(MAX_BODY_SIZE)
                    .execute();

        return new SODAResponse(response);
    }
}
