package eu.dfid.dataaccess.dto.parsed;

import eu.dl.dataaccess.dto.parsed.ParsedFunding;

/**
 * DFID Project Operation.
 */
public final class DFIDParsedProjectOperation {
    private String operationNumber;
    private String lendingInstrumentType;
    private ParsedFunding funding;
    private DFIDParsedFinancingSummaryItem financing;
    private String signatureDate;

    /**
     * Gets operationNumber.
     *
     * @return value of operationNumber
     */
    public String getOperationNumber() {
        return operationNumber;
    }

    /**
     * Sets operationNumber.
     *
     * @param operationNumber
     *         the operationNumber to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedProjectOperation setOperationNumber(final String operationNumber) {
        this.operationNumber = operationNumber;
        return this;
    }

    /**
     * Gets lendingInstrumentType.
     *
     * @return value of lendingInstrumentType
     */
    public String getLendingInstrumentType() {
        return lendingInstrumentType;
    }

    /**
     * Sets lendingInstrumentType.
     *
     * @param lendingInstrumentType
     *         the lendingInstrumentType to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedProjectOperation setLendingInstrumentType(final String lendingInstrumentType) {
        this.lendingInstrumentType = lendingInstrumentType;
        return this;
    }

    /**
     * Gets funding.
     *
     * @return value of funding
     */
    public ParsedFunding getFunding() {
        return funding;
    }

    /**
     * Sets funding.
     *
     * @param funding
     *         the funding to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedProjectOperation setFunding(final ParsedFunding funding) {
        this.funding = funding;
        return this;
    }

    /**
     * Gets financing.
     *
     * @return value of financing
     */
    public DFIDParsedFinancingSummaryItem getFinancing() {
        return financing;
    }

    /**
     * Sets financing.
     *
     * @param financing
     *         the financing to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedProjectOperation setFinancing(final DFIDParsedFinancingSummaryItem financing) {
        this.financing = financing;
        return this;
    }

    /**
     * Gets signatureDate.
     *
     * @return value of signatureDate
     */
    public String getSignatureDate() {
        return signatureDate;
    }

    /**
     * Sets signatureDate.
     *
     * @param signatureDate
     *         the signatureDate to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedProjectOperation setSignatureDate(final String signatureDate) {
        this.signatureDate = signatureDate;
        return this;
    }
}
