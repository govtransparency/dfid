package eu.dfid.dataaccess.dto.parsed;

import eu.dl.dataaccess.dto.parsed.ParsedPrice;

/**
 * DFID Price.
 */
public final class DFIDParsedPrice extends ParsedPrice {
    private String valueType;

    /**
     * @return value of valueType
     */
    public String getValueType() {
        return valueType;
    }

    /**
     * Sets valueType.
     *
     * @param valueType
     *            the valueType to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedPrice setValueType(final String valueType) {
        this.valueType = valueType;
        return this;
    }
}
