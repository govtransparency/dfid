package eu.dfid.dataaccess.dto.parsed;

import eu.dl.dataaccess.dto.parsed.ParsedPrice;

/**
 * DFID Financing. Information about financier and their commitment amount.
 */
public final class DFIDParsedFinancing {
    private String financier;
    private ParsedPrice commitment;

    /**
     * Gets financier.
     *
     * @return value of financier
     */
    public String getFinancier() {
        return financier;
    }

    /**
     * Sets financier.
     *
     * @param financier
     *         the financier to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedFinancing setFinancier(final String financier) {
        this.financier = financier;
        return this;
    }

    /**
     * Gets commitment.
     *
     * @return value of commitment
     */
    public ParsedPrice getCommitment() {
        return commitment;
    }

    /**
     * Sets commitment.
     *
     * @param commitment
     *         the commitment to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedFinancing setCommitment(final ParsedPrice commitment) {
        this.commitment = commitment;
        return this;
    }
}
