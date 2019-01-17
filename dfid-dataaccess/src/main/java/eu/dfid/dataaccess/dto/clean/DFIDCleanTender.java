package eu.dfid.dataaccess.dto.clean;

import eu.dl.dataaccess.dto.clean.CleanTender;
import eu.dl.dataaccess.dto.generic.Price;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * DFID Tender (contains couple of extra variables compared to basic tender class).
 */
@Entity
@Table(name = "clean_tender")
public final class DFIDCleanTender extends CleanTender {
    private String procurementType;
    private String purpose;
    private LocalDate estimatedInvitationDate;
    private LocalDate noObjectionDate;
    private List<String> majorSectors;
    private String legalBasis;
    private Boolean isDomesticPreferenceAllowed;
    private Boolean isDomesticPreferenceAffected;
    private Boolean hasDonorReview;
    private Integer fiscalYear;
    private Price donorFinancing;
    private DFIDCleanProject project;

    /**
     * Gets procurementType.
     *
     * @return value of procurementType
     */
    @Transient
    public String getProcurementType() {
        return procurementType;
    }

    /**
     * Sets procurementType.
     *
     * @param procurementType
     *         the procurementType to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanTender setProcurementType(final String procurementType) {
        this.procurementType = procurementType;
        return this;
    }

    /**
     * Gets purpose.
     *
     * @return value of purpose
     */
    @Transient
    public String getPurpose() {
        return purpose;
    }

    /**
     * Sets purpose.
     *
     * @param purpose
     *         the purpose to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanTender setPurpose(final String purpose) {
        this.purpose = purpose;
        return this;
    }

    /**
     * Gets noObjectionDate.
     *
     * @return value of noObjectionDate
     */
    @Transient
    public LocalDate getNoObjectionDate() {
        return noObjectionDate;
    }

    /**
     * Sets noObjectionDate.
     *
     * @param noObjectionDate
     *         the noObjectionDate to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanTender setNoObjectionDate(final LocalDate noObjectionDate) {
        this.noObjectionDate = noObjectionDate;
        return this;
    }

    /**
     * Gets majorSectors.
     *
     * @return value of majorSectors
     */
    @Transient
    public List<String> getMajorSectors() {
        return majorSectors;
    }

    /**
     * Sets majorSectors.
     *
     * @param majorSectors
     *         the majorSectors to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanTender setMajorSectors(final List<String> majorSectors) {
        this.majorSectors = majorSectors;
        return this;
    }

    /**
     * Gets legalBasis.
     *
     * @return value of legalBasis
     */
    @Transient
    public String getLegalBasis() {
        return legalBasis;
    }

    /**
     * Sets legalBasis.
     *
     * @param legalBasis
     *         the legalBasis to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanTender setLegalBasis(final String legalBasis) {
        this.legalBasis = legalBasis;
        return this;
    }

    /**
     * Gets isDomesticPreferenceAllowed.
     *
     * @return value of isDomesticPreferenceAllowed
     */
    @Transient
    public Boolean getIsDomesticPreferenceAllowed() {
        return isDomesticPreferenceAllowed;
    }

    /**
     * Sets isDomesticPreferenceAllowed.
     *
     * @param isDomesticPreferenceAllowed
     *         the isDomesticPreferenceAllowed to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanTender setIsDomesticPreferenceAllowed(final Boolean isDomesticPreferenceAllowed) {
        this.isDomesticPreferenceAllowed = isDomesticPreferenceAllowed;
        return this;
    }

    /**
     * Gets isDomesticPreferenceAffected.
     *
     * @return value of isDomesticPreferenceAffected
     */
    @Transient
    public Boolean getIsDomesticPreferenceAffected() {
        return isDomesticPreferenceAffected;
    }

    /**
     * Sets isDomesticPreferenceAffected.
     *
     * @param isDomesticPreferenceAffected
     *         the isDomesticPreferenceAffected to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanTender setIsDomesticPreferenceAffected(final Boolean isDomesticPreferenceAffected) {
        this.isDomesticPreferenceAffected = isDomesticPreferenceAffected;
        return this;
    }

    /**
     * Gets hasDonorReview.
     *
     * @return value of hasDonorReview
     */
    @Transient
    public Boolean getHasDonorReview() {
        return hasDonorReview;
    }

    /**
     * Sets hasDonorReview.
     *
     * @param hasDonorReview
     *         the hasDonorReview to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanTender setHasDonorReview(final Boolean hasDonorReview) {
        this.hasDonorReview = hasDonorReview;
        return this;
    }

    /**
     * Gets fiscalYear.
     *
     * @return value of fiscalYear
     */
    @Transient
    public Integer getFiscalYear() {
        return fiscalYear;
    }

    /**
     * Sets fiscalYear.
     *
     * @param fiscalYear
     *         the fiscalYear to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanTender setFiscalYear(final Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
        return this;
    }

    /**
     * Gets project.
     *
     * @return value of project
     */
    @Transient
    public DFIDCleanProject getProject() {
        return project;
    }

    /**
     * Sets project.
     *
     * @param project
     *         the project to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanTender setProject(final DFIDCleanProject project) {
        this.project = project;
        return this;
    }

    /**
     * @return donor financing price
     */
    @Transient
    public Price getDonorFinancing() {
        return donorFinancing;
    }

    /**
     * @param donorFinancing
     *      donor financing price to set
     * @return this instance for chaining
     */
    public DFIDCleanTender setDonorFinancing(final Price donorFinancing) {
        this.donorFinancing = donorFinancing;
        return this;
    }

    /**
     * @return estimated invitation date
     */
    @Transient
    public LocalDate getEstimatedInvitationDate() {
        return estimatedInvitationDate;
    }

    /**
     * @param estimatedInvitationDate
     *      estimated invitation date to set
     * @return this instance for chaining
     */
    public DFIDCleanTender setEstimatedInvitationDate(final LocalDate estimatedInvitationDate) {
        this.estimatedInvitationDate = estimatedInvitationDate;
        return this;
    }
}
