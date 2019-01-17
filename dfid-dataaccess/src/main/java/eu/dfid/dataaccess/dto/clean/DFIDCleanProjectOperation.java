package eu.dfid.dataaccess.dto.clean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

import eu.dfid.dataaccess.dto.codetables.LendingInstrumentType;
import eu.dl.dataaccess.dto.clean.Validable;
import eu.dl.dataaccess.dto.generic.Funding;
import eu.dl.dataaccess.utils.ClassUtils;
import eu.dl.dataaccess.utils.ValidationUtils;
import javax.persistence.Transient;

/**
 * DFID Project Operation.
 */
public final class DFIDCleanProjectOperation implements Validable {
    private String operationNumber;
    private LendingInstrumentType lendingInstrumentType;
    private Funding funding;
    private DFIDCleanFinancingSummaryItem financing;
    private LocalDate signatureDate;

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
    public DFIDCleanProjectOperation setOperationNumber(final String operationNumber) {
        this.operationNumber = operationNumber;
        return this;
    }

    /**
     * Gets lendingInstrumentType.
     *
     * @return value of lendingInstrumentType
     */
    public LendingInstrumentType getLendingInstrumentType() {
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
    public DFIDCleanProjectOperation setLendingInstrumentType(final LendingInstrumentType lendingInstrumentType) {
        this.lendingInstrumentType = lendingInstrumentType;
        return this;
    }

    /**
     * Gets funding.
     *
     * @return value of funding
     */
    public Funding getFunding() {
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
    public DFIDCleanProjectOperation setFunding(final Funding funding) {
        this.funding = funding;
        return this;
    }

    /**
     * Gets financing.
     *
     * @return value of financing
     */
    public DFIDCleanFinancingSummaryItem getFinancing() {
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
    public DFIDCleanProjectOperation setFinancing(final DFIDCleanFinancingSummaryItem financing) {
        this.financing = financing;
        return this;
    }

    /**
     * Gets signatureDate.
     *
     * @return value of signatureDate
     */
    public LocalDate getSignatureDate() {
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
    public DFIDCleanProjectOperation setSignatureDate(final LocalDate signatureDate) {
        this.signatureDate = signatureDate;
        return this;
    }

    @Override
    @Transient
    @JsonIgnore
    public DFIDCleanProjectOperation getValid() {
        setFinancing(ClassUtils.removeNonsenses(financing));
        setFunding(ClassUtils.removeNonsenses(funding));

        return ValidationUtils.getValid(this, financing, funding, lendingInstrumentType, operationNumber,
            signatureDate);
    }
}
