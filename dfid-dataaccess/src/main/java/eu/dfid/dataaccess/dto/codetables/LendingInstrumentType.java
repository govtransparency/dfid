package eu.dfid.dataaccess.dto.codetables;

/**
 * Lending Instrument Type - investment project fin or development policy financing.
 */
public enum LendingInstrumentType {
    /**
     * WB - Investment Project Financing (IPF) .
     */
    IPF,

    /**
     * WB - Development Project Financing (DPF).
     */
    DPF,

    /**
     * Nonreimbursable.
     */
    NONREIMBURSABLE,

    /**
     * Investment Loan.
     */
    INVESTMENT_LOAN,

    /**
     * To be defined.
     */
    TBD,

    /**
     * Unknown purpose/meaning.
     */
    IN,

    /**
     * Unknown purpose/meaning.
     */
    AD,

    /**
     * Unknown purpose/meaning.
     */
    PR,

    /**
     * Unidentified dfid lending instrument type.
     */
    UNIDENTIFIED
}
