package eu.dfid.dataaccess.dto.codetables;

/**
 * Type of value (historical or revalued) for IDB prices.
 */
public enum PriceValueType {
    /**
     * It was originally agreed, approved by the board.
     */
    HISTORICAL,

    /**
     * Cancelled amount is subtracted, so new values need to be revalued.
     */
    REVALUED,
}
