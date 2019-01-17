package eu.dfid.worker.wb.raw;

/**
 * Provides SODA API for Major contract awards data.
 *
 * @author Tomas Mrazek
 */
public final class WBMajorContractAwardsSODAConnector extends BaseSODAConnector {
    private static final String API_TOKEN = "";
    private static final String API_DATASET_URL = "https://finances.worldbank.org/";
    private static final String API_DATASET_IDENTIFIER = "45tv-a6qy";
    private static final SODAOutputFormatType API_OUTPUT_FORMAT = SODAOutputFormatType.JSON;

    @Override
    public String getToken() {
        return API_TOKEN;
    }

    @Override
    public String getDatasetUrl() {
        return API_DATASET_URL;
    }

    @Override
    public String getDatasetIdentifier() {
        return API_DATASET_IDENTIFIER;
    }

    @Override
    public SODAOutputFormatType getOutputFormat() {
        return API_OUTPUT_FORMAT;
    }
}
