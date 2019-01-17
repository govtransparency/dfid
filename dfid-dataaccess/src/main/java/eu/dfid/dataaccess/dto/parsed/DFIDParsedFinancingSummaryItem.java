package eu.dfid.dataaccess.dto.parsed;

import eu.dl.dataaccess.dto.parsed.ParsedPrice;

/**
 * DFID Financial Activity.
 */
public final class DFIDParsedFinancingSummaryItem {
    private String loanNumber;
    private String approvalDate;
    private String closingDate;
    private ParsedPrice principal;
    private ParsedPrice disbursed;
    private ParsedPrice repayments;
    private ParsedPrice interestChargesFees;
    private ParsedPrice cancelledAmount;

    /**
     * Gets loanNumber.
     *
     * @return value of loanNumber
     */
    public String getLoanNumber() {
        return loanNumber;
    }

    /**
     * Sets loanNumber.
     *
     * @param loanNumber
     *         the loanNumber to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedFinancingSummaryItem setLoanNumber(final String loanNumber) {
        this.loanNumber = loanNumber;
        return this;
    }

    /**
     * Gets approvalDate.
     *
     * @return value of approvalDate
     */
    public String getApprovalDate() {
        return approvalDate;
    }

    /**
     * Sets approvalDate.
     *
     * @param approvalDate
     *         the approvalDate to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedFinancingSummaryItem setApprovalDate(final String approvalDate) {
        this.approvalDate = approvalDate;
        return this;
    }

    /**
     * Gets closingDate.
     *
     * @return value of closingDate
     */
    public String getClosingDate() {
        return closingDate;
    }

    /**
     * Sets closingDate.
     *
     * @param closingDate
     *         the closingDate to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedFinancingSummaryItem setClosingDate(final String closingDate) {
        this.closingDate = closingDate;
        return this;
    }

    /**
     * Gets principal.
     *
     * @return value of principal
     */
    public ParsedPrice getPrincipal() {
        return principal;
    }

    /**
     * Sets principal.
     *
     * @param principal
     *         the principal to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedFinancingSummaryItem setPrincipal(final ParsedPrice principal) {
        this.principal = principal;
        return this;
    }

    /**
     * Gets disbursed.
     *
     * @return value of disbursed
     */
    public ParsedPrice getDisbursed() {
        return disbursed;
    }

    /**
     * Sets disbursed.
     *
     * @param disbursed
     *         the disbursed to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedFinancingSummaryItem setDisbursed(final ParsedPrice disbursed) {
        this.disbursed = disbursed;
        return this;
    }

    /**
     * Gets repayments.
     *
     * @return value of repayments
     */
    public ParsedPrice getRepayments() {
        return repayments;
    }

    /**
     * Sets repayments.
     *
     * @param repayments
     *         the repayments to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedFinancingSummaryItem setRepayments(final ParsedPrice repayments) {
        this.repayments = repayments;
        return this;
    }

    /**
     * Gets interestChargesFees.
     *
     * @return value of interestChargesFees
     */
    public ParsedPrice getInterestChargesFees() {
        return interestChargesFees;
    }

    /**
     * Sets interestChargesFees.
     *
     * @param interestChargesFees
     *         the interestChargesFees to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedFinancingSummaryItem setInterestChargesFees(final ParsedPrice interestChargesFees) {
        this.interestChargesFees = interestChargesFees;
        return this;
    }

    /**
     * Gets cancelledAmount.
     *
     * @return value of cancelledAmount
     */
    public ParsedPrice getCancelledAmount() {
        return cancelledAmount;
    }

    /**
     * Sets cancelledAmount.
     *
     * @param cancelledAmount
     *         the cancelledAmount to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedFinancingSummaryItem setCancelledAmount(final ParsedPrice cancelledAmount) {
        this.cancelledAmount = cancelledAmount;
        return this;
    }
}
