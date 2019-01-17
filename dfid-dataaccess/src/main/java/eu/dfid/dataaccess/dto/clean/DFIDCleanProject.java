package eu.dfid.dataaccess.dto.clean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.dfid.dataaccess.dto.codetables.DFIDRegion;
import eu.dfid.dataaccess.dto.codetables.LendingInstrument;
import eu.dfid.dataaccess.dto.codetables.LendingInstrumentType;
import eu.dfid.dataaccess.dto.codetables.ProjectStatus;
import eu.dl.dataaccess.dto.clean.CleanBody;
import eu.dl.dataaccess.dto.clean.CleanStorableDTO;
import eu.dl.dataaccess.dto.clean.Cleanable;
import eu.dl.dataaccess.dto.generic.Address;
import eu.dl.dataaccess.dto.generic.Funding;
import eu.dl.dataaccess.dto.generic.Price;
import eu.dl.dataaccess.dto.generic.Publication;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static eu.dl.dataaccess.utils.ClassUtils.removeNonsenses;
import eu.dl.dataaccess.utils.ValidationUtils;
/**
 * DFID Clean Project.
 */
@Entity
@Table(name = "clean_project")
public class DFIDCleanProject extends CleanStorableDTO implements Cleanable {
    private String projectId;
    private List<Publication> publications;
    private List<DFIDCleanProjectOperation> operations;
    private String name;
    private String description;
    private DFIDRegion region;
    private String country;
    private String countryCode;
    private List<Address> locations;
    private String productLine;
    private LendingInstrument lendingInstrument;
    private List<LendingInstrumentType> lendingInstrumentTypes;
    private ProjectStatus status;
    private Integer approvalFiscalYear;
    private Integer exitFiscalYear;
    private LocalDate approvalDate;
    private List<LocalDate> signatureDates;
    private LocalDate closingDate;
    private LocalDate deactivationDate;
    private Price finalCost;
    private Price estimatedCost;
    private List<Price> donorFinancings;
    private Price grantAmount;
    private Price estimatedBorrowerFinancing;
    private Price borrowerFinancing;
    private Price cancelledAmount;
    private List<Funding> fundings;
    private List<DFIDCleanFinancing> financingPlan;
    private List<DFIDCleanFinancingSummaryItem> financingSummary;
    private LocalDate financingSummaryLastUpdate;
    private CleanBody borrower;
    private CleanBody implementingAgency;
    private String sectorBoard;
    private List<DFIDCleanWeightedAttribute> sectors;
    private List<DFIDCleanWeightedAttribute> majorSectors;
    private List<DFIDCleanWeightedAttribute> themes;
    private List<String> loansNumbers;
    private DFIDCleanBodyEvaluation donorEvaluation;
    private DFIDCleanProjectEvaluation evaluation;
    private String environmentalAndSocialCategory;
    private String teamLeader;
    private String agreementType;

    /**
     * Gets projectId.
     *
     * @return value of projectId
     */
    @Transient
    public final String getProjectId() {
        return projectId;
    }

    /**
     * Sets projectId.
     *
     * @param projectId
     *         the projectId to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setProjectId(final String projectId) {
        this.projectId = projectId;
        return this;
    }

    /**
     * Gets publications.
     *
     * @return value of publications
     */
    @Transient
    public final List<Publication> getPublications() {
        return publications;
    }

    /**
     * Sets publications.
     *
     * @param publications
     *         the publications to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setPublications(final List<Publication> publications) {
        this.publications = publications;
        return this;
    }

    /**
     * Adds an publication to the publication list.
     *
     * @param publication
     *         publication
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject addPublication(final Publication publication) {
        if (publication != null) {
            if (getPublications() == null) {
                setPublications(new ArrayList<>());
            }

            this.publications.add(publication);
        }

        return this;
    }

    /**
     * Gets operations.
     *
     * @return value of operations
     */
    @Transient
    public final List<DFIDCleanProjectOperation> getOperations() {
        return operations;
    }

    /**
     * Sets operations.
     *
     * @param operations
     *         the operations to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setOperations(final List<DFIDCleanProjectOperation> operations) {
        this.operations = operations;
        return this;
    }

    /**
     * Adds an operation to the operation list.
     *
     * @param operation
     *         operation
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject addOperation(final DFIDCleanProjectOperation operation) {
        if (operation != null) {
            if (getOperations() == null) {
                setOperations(new ArrayList<>());
            }

            this.operations.add(operation);
        }

        return this;
    }

    /**
     * Gets name.
     *
     * @return value of name
     */
    @Transient
    public final String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name
     *         the name to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets description.
     *
     * @return value of description
     */
    @Transient
    public final String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description
     *         the description to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setDescription(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Gets region.
     *
     * @return value of region
     */
    @Transient
    public final DFIDRegion getRegion() {
        return region;
    }

    /**
     * Sets region.
     *
     * @param region
     *         the region to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setRegion(final DFIDRegion region) {
        this.region = region;
        return this;
    }

    /**
     * Gets country.
     *
     * @return value of country
     */
    @Transient
    public final String getCountry() {
        return country;
    }

    /**
     * Sets country.
     *
     * @param country
     *         the country to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setCountry(final String country) {
        this.country = country;
        return this;
    }

    /**
     * Gets countryCode.
     *
     * @return value of countryCode
     */
    @Transient
    public final String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets countryCode.
     *
     * @param countryCode
     *         the countryCode to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    /**
     * Gets locations.
     *
     * @return value of locations
     */
    @Transient
    public final List<Address> getLocations() {
        return locations;
    }

    /**
     * Sets locations.
     *
     * @param locations
     *         the locations to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setLocations(final List<Address> locations) {
        this.locations = locations;
        return this;
    }

    /**
     * Gets productLine.
     *
     * @return value of productLine
     */
    @Transient
    public final String getProductLine() {
        return productLine;
    }

    /**
     * Sets productLine.
     *
     * @param productLine
     *         the productLine to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setProductLine(final String productLine) {
        this.productLine = productLine;
        return this;
    }

    /**
     * Gets lendingInstrument.
     *
     * @return value of lendingInstrument
     */
    @Transient
    public final LendingInstrument getLendingInstrument() {
        return lendingInstrument;
    }

    /**
     * Sets lendingInstrument.
     *
     * @param lendingInstrument
     *         the lendingInstrument to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setLendingInstrument(final LendingInstrument lendingInstrument) {
        this.lendingInstrument = lendingInstrument;
        return this;
    }

    /**
     * Gets lendingInstrumentTypes.
     *
     * @return value of lendingInstrumentTypes
     */
    @Transient
    public final List<LendingInstrumentType> getLendingInstrumentTypes() {
        return lendingInstrumentTypes;
    }

    /**
     * Sets lendingInstrumentTypes.
     *
     * @param lendingInstrumentTypes
     *         the lendingInstrumentTypes to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setLendingInstrumentTypes(final List<LendingInstrumentType> lendingInstrumentTypes) {
        this.lendingInstrumentTypes = lendingInstrumentTypes;
        return this;
    }

    /**
     * Adds an lendingInstrumentType to the list.
     *
     * @param lendingInstrumentType
     *         the lendingInstrumentType to add to the list
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject addLendingInstrumentType(final LendingInstrumentType lendingInstrumentType) {
        if (lendingInstrumentType != null) {
            if (this.lendingInstrumentTypes == null) {
                setLendingInstrumentTypes(new ArrayList<>());
            }

            this.lendingInstrumentTypes.add(lendingInstrumentType);
        }

        return this;
    }

    /**
     * Gets status.
     *
     * @return value of status
     */
    @Transient
    public final ProjectStatus getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status
     *         the status to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setStatus(final ProjectStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Gets approvalFiscalYear.
     *
     * @return value of approvalFiscalYear
     */
    @Transient
    public final Integer getApprovalFiscalYear() {
        return approvalFiscalYear;
    }

    /**
     * Sets approvalFiscalYear.
     *
     * @param approvalFiscalYear
     *         the approvalFiscalYear to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setApprovalFiscalYear(final Integer approvalFiscalYear) {
        this.approvalFiscalYear = approvalFiscalYear;
        return this;
    }

    /**
     * Gets exitFiscalYear.
     *
     * @return value of exitFiscalYear
     */
    @Transient
    public final Integer getExitFiscalYear() {
        return exitFiscalYear;
    }

    /**
     * Sets exitFiscalYear.
     *
     * @param exitFiscalYear
     *         the exitFiscalYear to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setExitFiscalYear(final Integer exitFiscalYear) {
        this.exitFiscalYear = exitFiscalYear;
        return this;
    }

    /**
     * Gets approvalDate.
     *
     * @return value of approvalDate
     */
    @Transient
    public final LocalDate getApprovalDate() {
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
    public final DFIDCleanProject setApprovalDate(final LocalDate approvalDate) {
        this.approvalDate = approvalDate;
        return this;
    }

    /**
     * Gets signatureDates.
     *
     * @return value of signatureDates
     */
    @Transient
    public final List<LocalDate> getSignatureDates() {
        return signatureDates;
    }

    /**
     * Sets signatureDates.
     *
     * @param signatureDates
     *         the signatureDates to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setSignatureDates(final List<LocalDate> signatureDates) {
        this.signatureDates = signatureDates;
        return this;
    }

    /**
     * Gets closingDate.
     *
     * @return value of closingDate
     */
    @Transient
    public final LocalDate getClosingDate() {
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
    public final DFIDCleanProject setClosingDate(final LocalDate closingDate) {
        this.closingDate = closingDate;
        return this;
    }

    /**
     * Gets deactivationDate.
     *
     * @return value of deactivationDate
     */
    @Transient
    public final LocalDate getDeactivationDate() {
        return deactivationDate;
    }

    /**
     * Sets deactivationDate.
     *
     * @param deactivationDate
     *         the deactivationDate to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setDeactivationDate(final LocalDate deactivationDate) {
        this.deactivationDate = deactivationDate;
        return this;
    }

    /**
     * Gets finalCost.
     *
     * @return value of finalCost
     */
    @Transient
    public final Price getFinalCost() {
        return finalCost;
    }

    /**
     * Sets finalCost.
     *
     * @param finalCost
     *         the finalCost to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setFinalCost(final Price finalCost) {
        this.finalCost = finalCost;
        return this;
    }

    /**
     * Gets estimatedCost.
     *
     * @return value of estimatedCost
     */
    @Transient
    public final Price getEstimatedCost() {
        return estimatedCost;
    }

    /**
     * Sets estimatedCost.
     *
     * @param estimatedCost
     *         the estimatedCost to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setEstimatedCost(final Price estimatedCost) {
        this.estimatedCost = estimatedCost;
        return this;
    }

    /**
     * Gets donorFinancings.
     *
     * @return value of donorFinancings
     */
    @Transient
    public final List<Price> getDonorFinancings() {
        return donorFinancings;
    }

    /**
     * Sets donorFinancings.
     *
     * @param donorFinancings
     *         the donorFinancings to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setDonorFinancings(final List<Price> donorFinancings) {
        this.donorFinancings = donorFinancings;
        return this;
    }

    /**
     * Adds an donorFinancing to the list.
     *
     * @param donorFinancing
     *         the donorFinancing to add to the list
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject addDonorFinancing(final Price donorFinancing) {
        if (donorFinancing != null) {
            if (getDonorFinancings() == null) {
                setDonorFinancings(new ArrayList<>());
            }

            this.donorFinancings.add(donorFinancing);
        }

        return this;
    }

    /**
     * Gets grantAmount.
     *
     * @return value of grantAmount
     */
    @Transient
    public final Price getGrantAmount() {
        return grantAmount;
    }

    /**
     * Sets grantAmount.
     *
     * @param grantAmount
     *         the grantAmount to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setGrantAmount(final Price grantAmount) {
        this.grantAmount = grantAmount;
        return this;
    }

    /**
     * Gets estimatedBorrowerFinancing.
     *
     * @return value of estimatedBorrowerFinancing
     */
    @Transient
    public final Price getEstimatedBorrowerFinancing() {
        return estimatedBorrowerFinancing;
    }

    /**
     * Sets estimatedBorrowerFinancing.
     *
     * @param estimatedBorrowerFinancing
     *         the estimatedBorrowerFinancing to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setEstimatedBorrowerFinancing(final Price estimatedBorrowerFinancing) {
        this.estimatedBorrowerFinancing = estimatedBorrowerFinancing;
        return this;
    }

    /**
     * Gets borrowerFinancing.
     *
     * @return value of borrowerFinancing
     */
    @Transient
    public final Price getBorrowerFinancing() {
        return borrowerFinancing;
    }

    /**
     * Sets borrowerFinancing.
     *
     * @param borrowerFinancing
     *         the borrowerFinancing to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setBorrowerFinancing(final Price borrowerFinancing) {
        this.borrowerFinancing = borrowerFinancing;
        return this;
    }

    /**
     * Gets cancelledAmount.
     *
     * @return value of cancelledAmount
     */
    @Transient
    public final Price getCancelledAmount() {
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
    public final DFIDCleanProject setCancelledAmount(final Price cancelledAmount) {
        this.cancelledAmount = cancelledAmount;
        return this;
    }

    /**
     * Gets fundings.
     *
     * @return value of fundings
     */
    @Transient
    public final List<Funding> getFundings() {
        return fundings;
    }

    /**
     * Sets fundings.
     *
     * @param fundings
     *         the fundings to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setFundings(final List<Funding> fundings) {
        this.fundings = fundings;
        return this;
    }

    /**
     * Gets financingPlan.
     *
     * @return value of financingPlan
     */
    @Transient
    public final List<DFIDCleanFinancing> getFinancingPlan() {
        return financingPlan;
    }

    /**
     * Sets financingPlan.
     *
     * @param financingPlan
     *         the financingPlan to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setFinancingPlan(final List<DFIDCleanFinancing> financingPlan) {
        this.financingPlan = financingPlan;
        return this;
    }

    /**
     * Gets financingSummary.
     *
     * @return value of financingSummary
     */
    @Transient
    public final List<DFIDCleanFinancingSummaryItem> getFinancingSummary() {
        return financingSummary;
    }

    /**
     * Sets financingSummary.
     *
     * @param financingSummary
     *         the financingSummary to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setFinancingSummary(final List<DFIDCleanFinancingSummaryItem> financingSummary) {
        this.financingSummary = financingSummary;
        return this;
    }

    /**
     * Gets financingSummaryLastUpdate.
     *
     * @return value of financingSummaryLastUpdate
     */
    @Transient
    public final LocalDate getFinancingSummaryLastUpdate() {
        return financingSummaryLastUpdate;
    }

    /**
     * Sets financingSummaryLastUpdate.
     *
     * @param financingSummaryLastUpdate
     *         the financingSummaryLastUpdate to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setFinancingSummaryLastUpdate(final LocalDate financingSummaryLastUpdate) {
        this.financingSummaryLastUpdate = financingSummaryLastUpdate;
        return this;
    }

    /**
     * Gets borrower.
     *
     * @return value of borrower
     */
    @Transient
    public final CleanBody getBorrower() {
        return borrower;
    }

    /**
     * Sets borrower.
     *
     * @param borrower
     *         the borrower to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setBorrower(final CleanBody borrower) {
        this.borrower = borrower;
        return this;
    }

    /**
     * Gets implementingAgency.
     *
     * @return value of implementingAgency
     */
    @Transient
    public final CleanBody getImplementingAgency() {
        return implementingAgency;
    }

    /**
     * Sets implementingAgency.
     *
     * @param implementingAgency
     *         the implementingAgency to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setImplementingAgency(final CleanBody implementingAgency) {
        this.implementingAgency = implementingAgency;
        return this;
    }

    /**
     * Gets sectorBoard.
     *
     * @return value of sectorBoard
     */
    @Transient
    public final String getSectorBoard() {
        return sectorBoard;
    }

    /**
     * Sets sectorBoard.
     *
     * @param sectorBoard
     *         the sectorBoard to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setSectorBoard(final String sectorBoard) {
        this.sectorBoard = sectorBoard;
        return this;
    }

    /**
     * Gets sectors.
     *
     * @return value of sectors
     */
    @Transient
    public final List<DFIDCleanWeightedAttribute> getSectors() {
        return sectors;
    }

    /**
     * Sets sectors.
     *
     * @param sectors
     *         the sectors to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setSectors(final List<DFIDCleanWeightedAttribute> sectors) {
        this.sectors = sectors;
        return this;
    }

    /**
     * Gets majorSectors.
     *
     * @return value of majorSectors
     */
    @Transient
    public final List<DFIDCleanWeightedAttribute> getMajorSectors() {
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
    public final DFIDCleanProject setMajorSectors(final List<DFIDCleanWeightedAttribute> majorSectors) {
        this.majorSectors = majorSectors;
        return this;
    }

    /**
     * Gets themes.
     *
     * @return value of themes
     */
    @Transient
    public final List<DFIDCleanWeightedAttribute> getThemes() {
        return themes;
    }

    /**
     * Sets themes.
     *
     * @param themes
     *         the themes to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setThemes(final List<DFIDCleanWeightedAttribute> themes) {
        this.themes = themes;
        return this;
    }

    /**
     * Gets loansNumbers.
     *
     * @return value of loansNumbers
     */
    @Transient
    public final List<String> getLoansNumbers() {
        return loansNumbers;
    }

    /**
     * Sets loansNumbers.
     *
     * @param loansNumbers
     *         the loansNumbers to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setLoansNumbers(final List<String> loansNumbers) {
        this.loansNumbers = loansNumbers;
        return this;
    }

    /**
     * Gets donorEvaluation.
     *
     * @return value of donorEvaluation
     */
    @Transient
    public final DFIDCleanBodyEvaluation getDonorEvaluation() {
        return donorEvaluation;
    }

    /**
     * Sets donorEvaluation.
     *
     * @param donorEvaluation
     *         the donorEvaluation to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setDonorEvaluation(final DFIDCleanBodyEvaluation donorEvaluation) {
        this.donorEvaluation = donorEvaluation;
        return this;
    }

    /**
     * Gets evaluation.
     *
     * @return value of evaluation
     */
    @Transient
    public final DFIDCleanProjectEvaluation getEvaluation() {
        return evaluation;
    }

    /**
     * Sets evaluation.
     *
     * @param evaluation
     *         the evaluation to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setEvaluation(final DFIDCleanProjectEvaluation evaluation) {
        this.evaluation = evaluation;
        return this;
    }

    /**
     * Gets environmentalAndSocialCategory.
     *
     * @return value of environmentalAndSocialCategory
     */
    @Transient
    public final String getEnvironmentalAndSocialCategory() {
        return environmentalAndSocialCategory;
    }

    /**
     * Sets environmentalAndSocialCategory.
     *
     * @param environmentalAndSocialCategory
     *         the environmentalAndSocialCategory to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setEnvironmentalAndSocialCategory(final String environmentalAndSocialCategory) {
        this.environmentalAndSocialCategory = environmentalAndSocialCategory;
        return this;
    }

    /**
     * Gets teamLeader.
     *
     * @return value of teamLeader
     */
    @Transient
    public final String getTeamLeader() {
        return teamLeader;
    }

    /**
     * Sets teamLeader.
     *
     * @param teamLeader
     *         the teamLeader to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setTeamLeader(final String teamLeader) {
        this.teamLeader = teamLeader;
        return this;
    }

    /**
     * Gets agreementType.
     *
     * @return value of agreementType
     */
    @Transient
    public final String getAgreementType() {
        return agreementType;
    }

    /**
     * Sets agreementType.
     *
     * @param agreementType
     *         the agreementType to set
     *
     * @return this instance for chaining
     */
    public final DFIDCleanProject setAgreementType(final String agreementType) {
        this.agreementType = agreementType;
        return this;
    }

    @Override
    @Transient
    @JsonIgnore
    public final DFIDCleanProject getValid() {
        setBorrower(removeNonsenses(borrower));
        setBorrowerFinancing(removeNonsenses(borrowerFinancing));
        setCancelledAmount(removeNonsenses(cancelledAmount));
        setDonorEvaluation(removeNonsenses(donorEvaluation));
        setDonorFinancings(ValidationUtils.getValid(donorFinancings));
        setEstimatedBorrowerFinancing(removeNonsenses(estimatedBorrowerFinancing));
        setEstimatedCost(removeNonsenses(estimatedCost));
        setEvaluation(removeNonsenses(evaluation));
        setFinalCost(removeNonsenses(finalCost));
        setFinancingPlan(ValidationUtils.getValid(financingPlan));
        setFinancingSummary(ValidationUtils.getValid(financingSummary));
        setFundings(ValidationUtils.getValid(fundings));
        setGrantAmount(removeNonsenses(grantAmount));
        setImplementingAgency(removeNonsenses(implementingAgency));
        setLendingInstrumentTypes(ValidationUtils.getValid(lendingInstrumentTypes));
        setLoansNumbers(ValidationUtils.getValid(loansNumbers));
        setLocations(ValidationUtils.getValid(locations));
        setMajorSectors(ValidationUtils.getValid(majorSectors));
        setOperations(ValidationUtils.getValid(operations));
        setPublications(ValidationUtils.getValid(publications));
        setSectors(ValidationUtils.getValid(sectors));
        setSignatureDates(ValidationUtils.getValid(signatureDates));
        setThemes(ValidationUtils.getValid(themes));

        return ValidationUtils.getValid(this, agreementType, approvalDate, approvalFiscalYear, borrower,
            borrowerFinancing, cancelledAmount, closingDate, country, countryCode, deactivationDate, description,
            donorEvaluation, donorFinancings, environmentalAndSocialCategory, estimatedBorrowerFinancing, estimatedCost,
            evaluation, exitFiscalYear, finalCost, financingPlan, financingSummary, financingSummaryLastUpdate,
            fundings, grantAmount, implementingAgency, lendingInstrument, lendingInstrumentTypes, loansNumbers,
            locations, majorSectors, name, operations, productLine, projectId, publications, region, sectorBoard,
            sectors, signatureDates, status, teamLeader, themes);
    }
}
