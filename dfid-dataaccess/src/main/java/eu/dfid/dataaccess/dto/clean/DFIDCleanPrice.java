package eu.dfid.dataaccess.dto.clean;

import eu.dfid.dataaccess.dto.codetables.PriceValueType;
import eu.dl.dataaccess.dto.generic.Price;

/**
 * DFID Price.
 */
public final class DFIDCleanPrice extends Price {
    private PriceValueType valueType;

    /**
     * @return value of valueType
     */
    public PriceValueType getValueType() {
        return valueType;
    }

    /**
     * Sets valueType.
     *
     * @param valueType
     *         the valueType to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanPrice setValueType(final PriceValueType valueType) {
        this.valueType = valueType;
        return this;
    }
}
