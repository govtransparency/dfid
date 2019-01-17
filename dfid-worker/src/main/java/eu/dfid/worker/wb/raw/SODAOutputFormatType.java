package eu.dfid.worker.wb.raw;

/**
 * Output formats of the api response.
 *
 * @author Tomas Mrazek
 */
public enum SODAOutputFormatType {
    /** Common JSON. */
    JSON("application/json", ".json"),
    /** JSON used for geospatial data. */
    GEO_JSON("application/json", ".json"),
    /** Comma separated csv. */
    CSV("text/csv", ".csv");

    private final String mimeType;

    private final String extension;

    /**
     * @param mimeType
     *      mimetype of the output format
     * @param extension
     *      extension of the output format
     */
    SODAOutputFormatType(final String mimeType, final String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }
    /**
     * @return mime type of the output format
     */
    public String getMimeType() {
        return mimeType;
    }
    /**
     * @return extension of the output format
     */
    public String getExtension() {
        return extension;
    }
    /**
     * Returns output format for the given mime type.
     *
     * @param mimeType
     *      MIME Type
     * @return output format
     */
    public static SODAOutputFormatType getFormatForMimeType(final String mimeType) {
        for (SODAOutputFormatType format : SODAOutputFormatType.values()) {
            // Content-Type header regex type/subtype(;<parameter_name>=<parameter_value>)*
            if (mimeType.startsWith(format.getMimeType())) {
                return format;
            }
        }

        return null;
    }
}
