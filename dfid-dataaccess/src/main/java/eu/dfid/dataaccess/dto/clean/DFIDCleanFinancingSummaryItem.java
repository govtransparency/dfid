package eu.dfid.dataaccess.dto.clean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.dl.dataaccess.dto.clean.Validable;
import java.time.LocalDate;

import eu.dl.dataaccess.dto.generic.Price;
import eu.dl.dataaccess.utils.ClassUtils;
import eu.dl.dataaccess.utils.ValidationUtils;
import javax.persistence.Transient;

/**
 * DFID Financial Activity.
 */
public final class DFIDCleanFinancingSummaryItem implements Validable {
    private String loanNumber;
    private LocalDate approvalDate;
    private LocalDate closingDate;
    private Price principal;
    private Price disbursed;
    private Price repayments;
    private Price interestChargesFees;
    private Price cancelledAmount;

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
    public DFIDCleanFinancingSummaryItem setLoanNumber(final String loanNumber) {
        this.loanNumber = loanNumber;
        return this;
    }

    /**
     * Gets approvalDate.
     *
     * @return value of approvalDate
     */
    public LocalDate getApprovalDate() {
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
    public DFIDCleanFinancingSummaryItem setApprovalDate(final LocalDate approvalDate) {
        this.approvalDate = approvalDate;
        return this;
    }

    /**
     * Gets closingDate.
     *
     * @return value of closingDate
     */
    public LocalDate getClosingDate() {
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
    public DFIDCleanFinancingSummaryItem setClosingDate(final LocalDate closingDate) {
        this.closingDate = closingDate;
        return this;
    }

    /**
     * Gets principal.
     *
     * @return value of principal
     */
    public Price getPrincipal() {
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
    public DFIDCleanFinancingSummaryItem setPrincipal(final Price principal) {
        this.principal = principal;
        return this;
    }

    /**
     * Gets disbursed.
     *
     * @return value of disbursed
     */
    public Price getDisbursed() {
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
    public DFIDCleanFinancingSummaryItem setDisbursed(final Price disbursed) {
        this.disbursed = disbursed;
        return this;
    }

    /**
     * Gets repayments.
     *
     * @return value of repayments
     */
    public Price getRepayments() {
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
    public DFIDCleanFinancingSummaryItem setRepayments(final Price repayments) {
        this.repayments = repayments;
        return this;
    }

    /**
     * Gets interestChargesFees.
     *
     * @return value of interestChargesFees
     */
    public Price getInterestChargesFees() {
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
    public DFIDCleanFinancingSummaryItem setInterestChargesFees(final Price interestChargesFees) {
        this.interestChargesFees = interestChargesFees;
        return this;
    }

    /**
     * Gets cancelledAmount.
     *
     * @return value of cancelledAmount
     */
    public Price getCancelledAmount() {
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
    public DFIDCleanFinancingSummaryItem setCancelledAmount(final Price cancelledAmount) {
        this.cancelledAmount = cancelledAmount;
        return this;
    }

    @Override
    @Transient
    @JsonIgnore
    public DFIDCleanFinancingSummaryItem getValid() {
        setCancelledAmount(ClassUtils.removeNonsenses(cancelledAmount));
        setDisbursed(ClassUtils.removeNonsenses(disbursed));
        setInterestChargesFees(ClassUtils.removeNonsenses(interestChargesFees));
        setPrincipal(ClassUtils.removeNonsenses(principal));
        setRepayments(ClassUtils.removeNonsenses(repayments));

        return ValidationUtils.getValid(this, approvalDate, cancelledAmount, closingDate, disbursed,
            interestChargesFees, loanNumber, principal, repayments);
    }
}
