package eu.dfid.dataaccess.dto.parsed;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import eu.dl.dataaccess.dto.parsed.ParsedPrice;
import eu.dl.dataaccess.dto.parsed.ParsedTender;

/**
 * DFID Tender (contains couple of extra variables compared to basic tender class).
 */
@Entity
@Table(name = "parsed_tender")
public class DFIDParsedTender extends ParsedTender {
    private String procurementType;
    private String purpose;
    private String estimatedInvitationDate;
    private String noObjectionDate;
    private List<String> majorSectors;
    private String legalBasis;
    private String isDomesticPreferenceAllowed;
    private String isDomesticPreferenceAffected;
    private String hasDonorReview;
    private String fiscalYear;
    private ParsedPrice donorFinancing;
    private ParsedProject project;

    /**
     * Gets procurementType.
     *
     * @return value of procurementType
     */
    @Transient
    public final String getProcurementType() {
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
    public final DFIDParsedTender setProcurementType(final String procurementType) {
        this.procurementType = procurementType;
        return this;
    }

    /**
     * Gets purpose.
     *
     * @return value of purpose
     */
    @Transient
    public final String getPurpose() {
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
    public final DFIDParsedTender setPurpose(final String purpose) {
        this.purpose = purpose;
        return this;
    }

    /**
     * Gets noObjectionDate.
     *
     * @return value of noObjectionDate
     */
    @Transient
    public final String getNoObjectionDate() {
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
    public final DFIDParsedTender setNoObjectionDate(final String noObjectionDate) {
        this.noObjectionDate = noObjectionDate;
        return this;
    }

    /**
     * Gets majorSectors.
     *
     * @return value of majorSectors
     */
    @Transient
    public final List<String> getMajorSectors() {
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
    public final DFIDParsedTender setMajorSectors(final List<String> majorSectors) {
        this.majorSectors = majorSectors;
        return this;
    }

    /**
     * @param majorSector
     *         the majorSector to add
     *
     * @return this instance for chaining
     */
    public final ParsedTender addMajorSector(final String majorSector) {
        if (majorSector != null) {
            if (getMajorSectors() == null) {
                setMajorSectors(new ArrayList<>());
            }

            this.majorSectors.add(majorSector);
        }

        return this;
    }

    /**
     * Gets legalBasis.
     *
     * @return value of legalBasis
     */
    @Transient
    public final String getLegalBasis() {
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
    public final DFIDParsedTender setLegalBasis(final String legalBasis) {
        this.legalBasis = legalBasis;
        return this;
    }

    /**
     * Gets isDomesticPreferenceAllowed.
     *
     * @return value of isDomesticPreferenceAllowed
     */
    @Transient
    public final String getIsDomesticPreferenceAllowed() {
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
    public final DFIDParsedTender setIsDomesticPreferenceAllowed(final String isDomesticPreferenceAllowed) {
        this.isDomesticPreferenceAllowed = isDomesticPreferenceAllowed;
        return this;
    }

    /**
     * Gets isDomesticPreferenceAffected.
     *
     * @return value of isDomesticPreferenceAffected
     */
    @Transient
    public final String getIsDomesticPreferenceAffected() {
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
    public final DFIDParsedTender setIsDomesticPreferenceAffected(final String isDomesticPreferenceAffected) {
        this.isDomesticPreferenceAffected = isDomesticPreferenceAffected;
        return this;
    }

    /**
     * Gets hasDonorReview.
     *
     * @return value of hasDonorReview
     */
    @Transient
    public final String getHasDonorReview() {
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
    public final DFIDParsedTender setHasDonorReview(final String hasDonorReview) {
        this.hasDonorReview = hasDonorReview;
        return this;
    }

    /**
     * Gets fiscalYear.
     *
     * @return value of fiscalYear
     */
    @Transient
    public final String getFiscalYear() {
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
    public final DFIDParsedTender setFiscalYear(final String fiscalYear) {
        this.fiscalYear = fiscalYear;
        return this;
    }

    /**
     * Gets project.
     *
     * @return value of project
     */
    @Transient
    public final ParsedProject getProject() {
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
    public final DFIDParsedTender setProject(final ParsedProject project) {
        this.project = project;
        return this;
    }

    /**
     * @return donor financing price
     */
    @Transient
    public final ParsedPrice getDonorFinancing() {
        return donorFinancing;
    }

    /**
     * @param donorFinancing
     *      donor financing price to set
     * @return this instance for chaining
     */
    public final DFIDParsedTender setDonorFinancing(final ParsedPrice donorFinancing) {
        this.donorFinancing = donorFinancing;
        return this;
    }

    /**
     * @return estimated invitation date
     */
    @Transient
    public final String getEstimatedInvitationDate() {
        return estimatedInvitationDate;
    }

    /**
     * @param estimatedInvitationDate
     *      estimated invitation date to set
     * @return this instance for chaining
     */
    public final DFIDParsedTender setEstimatedInvitationDate(final String estimatedInvitationDate) {
        this.estimatedInvitationDate = estimatedInvitationDate;
        return this;
    }
}
