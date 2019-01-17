package eu.dfid.dataaccess.dto.parsed;

import eu.dl.dataaccess.dto.parsed.BaseParsedStorableDTO;
import eu.dl.dataaccess.dto.parsed.Parsable;
import eu.dl.dataaccess.dto.parsed.ParsedAddress;
import eu.dl.dataaccess.dto.parsed.ParsedBody;
import eu.dl.dataaccess.dto.parsed.ParsedFunding;
import eu.dl.dataaccess.dto.parsed.ParsedPrice;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * DFID Parsed Project.
 */
@Entity
@Table(name = "parsed_project")
public class ParsedProject extends BaseParsedStorableDTO implements Parsable {
    private String projectId;
    private List<ParsedPublication> publications;
    private List<DFIDParsedProjectOperation> operations;
    private String name;
    private String description;
    private String region;
    private String country;
    private String countryCode;
    private List<ParsedAddress> locations;
    private String productLine;
    private String lendingInstrument;
    private List<String> lendingInstrumentTypes;
    private String status;
    private String approvalFiscalYear;
    private String exitFiscalYear;
    private String approvalDate;
    private List<String> signatureDates;
    private String closingDate;
    private String deactivationDate;
    private ParsedPrice finalCost;
    private ParsedPrice estimatedCost;
    private List<ParsedPrice> donorFinancings;
    private ParsedPrice grantAmount;
    private ParsedPrice estimatedBorrowerFinancing;
    private ParsedPrice borrowerFinancing;
    private ParsedPrice cancelledAmount;
    private List<ParsedFunding> fundings;
    private List<DFIDParsedFinancing> financingPlan;
    private List<DFIDParsedFinancingSummaryItem> financingSummary;
    private String financingSummaryLastUpdate;
    private ParsedBody borrower;
    private ParsedBody implementingAgency;
    private String sectorBoard;
    private List<DFIDParsedWeightedAttribute> sectors;
    private List<DFIDParsedWeightedAttribute> majorSectors;
    private List<DFIDParsedWeightedAttribute> themes;
    private List<String> loansNumbers;
    private DFIDParsedBodyEvaluation donorEvaluation;
    private DFIDParsedProjectEvaluation evaluation;
    private String environmentalAndSocialCategory;
    private String teamLeader;
    private String agreementType;

    /**
     * Gets the project id.
     *
     * @return the project id
     */
    @Transient
    public final String getProjectId() {
        return projectId;
    }

    /**
     * Sets the project id.
     *
     * @param projectId
     *            the project id
     * @return the parsed project
     */
    public final ParsedProject setProjectId(final String projectId) {
        this.projectId = projectId;
        return this;
    }

    /**
     * Gets the publications.
     *
     * @return the publications
     */
    @Transient
    public final List<ParsedPublication> getPublications() {
        return publications;
    }

    /**
     * Sets the publications.
     *
     * @param publications
     *            the publications
     * @return the parsed project
     */
    public final ParsedProject setPublications(final List<ParsedPublication> publications) {
        this.publications = publications;
        return this;
    }

    /**
     * Adds the publication.
     *
     * @param publication
     *            the publication
     * @return the parsed project
     */
    public final ParsedProject addPublication(final ParsedPublication publication) {
        if (publication != null) {
            if (getPublications() == null) {
                setPublications(new ArrayList<>());
            }

            this.publications.add(publication);
        }

        return this;
    }

    /**
     * Gets the operations.
     *
     * @return the operations
     */
    @Transient
    public final List<DFIDParsedProjectOperation> getOperations() {
        return operations;
    }

    /**
     * Sets the operations.
     *
     * @param operations
     *            the operations
     * @return the parsed project
     */
    public final ParsedProject setOperations(final List<DFIDParsedProjectOperation> operations) {
        this.operations = operations;
        return this;
    }

    /**
     * Adds the operation.
     *
     * @param operation
     *            the operation
     * @return the parsed project
     */
    public final ParsedProject addOperation(final DFIDParsedProjectOperation operation) {
        if (operation != null) {
            if (getOperations() == null) {
                setOperations(new ArrayList<>());
            }

            this.operations.add(operation);
        }

        return this;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Transient
    public final String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the name
     * @return the parsed project
     */
    public final ParsedProject setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    @Transient
    public final String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the description
     * @return the parsed project
     */
    public final ParsedProject setDescription(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Gets the region.
     *
     * @return the region
     */
    @Transient
    public final String getRegion() {
        return region;
    }

    /**
     * Sets the region.
     *
     * @param region
     *            the region
     * @return the parsed project
     */
    public final ParsedProject setRegion(final String region) {
        this.region = region;
        return this;
    }

    /**
     * Gets the country.
     *
     * @return the country
     */
    @Transient
    public final String getCountry() {
        return country;
    }

    /**
     * Sets the country.
     *
     * @param country
     *            the country
     * @return the parsed project
     */
    public final ParsedProject setCountry(final String country) {
        this.country = country;
        return this;
    }

    /**
     * Gets the country code.
     *
     * @return the country code
     */
    @Transient
    public final String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the country code.
     *
     * @param countryCode
     *            the country code
     * @return the parsed project
     */
    public final ParsedProject setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    /**
     * Gets the locations.
     *
     * @return the locations
     */
    @Transient
    public final List<ParsedAddress> getLocations() {
        return locations;
    }

    /**
     * Sets the locations.
     *
     * @param locations
     *            the locations
     * @return the parsed project
     */
    public final ParsedProject setLocations(final List<ParsedAddress> locations) {
        this.locations = locations;
        return this;
    }

    /**
     * Gets the product line.
     *
     * @return the product line
     */
    @Transient
    public final String getProductLine() {
        return productLine;
    }

    /**
     * Sets the product line.
     *
     * @param productLine
     *            the product line
     * @return the parsed project
     */
    public final ParsedProject setProductLine(final String productLine) {
        this.productLine = productLine;
        return this;
    }

    /**
     * Gets the lending instrument.
     *
     * @return the lending instrument
     */
    @Transient
    public final String getLendingInstrument() {
        return lendingInstrument;
    }

    /**
     * Sets the lending instrument.
     *
     * @param lendingInstrument
     *            the lending instrument
     * @return the parsed project
     */
    public final ParsedProject setLendingInstrument(final String lendingInstrument) {
        this.lendingInstrument = lendingInstrument;
        return this;
    }

    /**
     * Gets the lending instrument types.
     *
     * @return the lending instrument types
     */
    @Transient
    public final List<String> getLendingInstrumentTypes() {
        return lendingInstrumentTypes;
    }

    /**
     * Sets the lending instrument types.
     *
     * @param lendingInstrumentTypes
     *            the lending instrument types
     * @return the parsed project
     */
    public final ParsedProject setLendingInstrumentTypes(final List<String> lendingInstrumentTypes) {
        this.lendingInstrumentTypes = lendingInstrumentTypes;
        return this;
    }

    /**
     * Adds the lending instrument type.
     *
     * @param lendingInstrumentType
     *            the lending instrument type
     * @return the parsed project
     */
    public final ParsedProject addLendingInstrumentType(final String lendingInstrumentType) {
        if (lendingInstrumentType != null) {
            if (getLendingInstrumentTypes() == null) {
                setLendingInstrumentTypes(new ArrayList<>());
            }

            this.lendingInstrumentTypes.add(lendingInstrumentType);
        }

        return this;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    @Transient
    public final String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the status
     * @return the parsed project
     */
    public final ParsedProject setStatus(final String status) {
        this.status = status;
        return this;
    }

    /**
     * Gets the approval fiscal year.
     *
     * @return the approval fiscal year
     */
    @Transient
    public final String getApprovalFiscalYear() {
        return approvalFiscalYear;
    }

    /**
     * Sets the approval fiscal year.
     *
     * @param approvalFiscalYear
     *            the approval fiscal year
     * @return the parsed project
     */
    public final ParsedProject setApprovalFiscalYear(final String approvalFiscalYear) {
        this.approvalFiscalYear = approvalFiscalYear;
        return this;
    }

    /**
     * Gets the exit fiscal year.
     *
     * @return the exit fiscal year
     */
    @Transient
    public final String getExitFiscalYear() {
        return exitFiscalYear;
    }

    /**
     * Sets the exit fiscal year.
     *
     * @param exitFiscalYear
     *            the exit fiscal year
     * @return the parsed project
     */
    public final ParsedProject setExitFiscalYear(final String exitFiscalYear) {
        this.exitFiscalYear = exitFiscalYear;
        return this;
    }

    /**
     * Gets the approval date.
     *
     * @return the approval date
     */
    @Transient
    public final String getApprovalDate() {
        return approvalDate;
    }

    /**
     * Sets the approval date.
     *
     * @param approvalDate
     *            the approval date
     * @return the parsed project
     */
    public final ParsedProject setApprovalDate(final String approvalDate) {
        this.approvalDate = approvalDate;
        return this;
    }

    /**
     * Gets the signature dates.
     *
     * @return the signature dates
     */
    @Transient
    public final List<String> getSignatureDates() {
        return signatureDates;
    }

    /**
     * Sets the signature dates.
     *
     * @param signatureDates
     *            the signature dates
     * @return the parsed project
     */
    public final ParsedProject setSignatureDates(final List<String> signatureDates) {
        this.signatureDates = signatureDates;
        return this;
    }

    /**
     * Gets the closing date.
     *
     * @return the closing date
     */
    @Transient
    public final String getClosingDate() {
        return closingDate;
    }

    /**
     * Sets the closing date.
     *
     * @param closingDate
     *            the closing date
     * @return the parsed project
     */
    public final ParsedProject setClosingDate(final String closingDate) {
        this.closingDate = closingDate;
        return this;
    }

    /**
     * Gets the deactivation date.
     *
     * @return the deactivation date
     */
    @Transient
    public final String getDeactivationDate() {
        return deactivationDate;
    }

    /**
     * Sets the deactivation date.
     *
     * @param deactivationDate
     *            the deactivation date
     * @return the parsed project
     */
    public final ParsedProject setDeactivationDate(final String deactivationDate) {
        this.deactivationDate = deactivationDate;
        return this;
    }

    /**
     * Gets the final cost.
     *
     * @return the final cost
     */
    @Transient
    public final ParsedPrice getFinalCost() {
        return finalCost;
    }

    /**
     * Sets the final cost.
     *
     * @param finalCost
     *            the final cost
     * @return the parsed project
     */
    public final ParsedProject setFinalCost(final ParsedPrice finalCost) {
        this.finalCost = finalCost;
        return this;
    }

    /**
     * Gets the estimated cost.
     *
     * @return the estimated cost
     */
    @Transient
    public final ParsedPrice getEstimatedCost() {
        return estimatedCost;
    }

    /**
     * Sets the estimated cost.
     *
     * @param estimatedCost
     *            the estimated cost
     * @return the parsed project
     */
    public final ParsedProject setEstimatedCost(final ParsedPrice estimatedCost) {
        this.estimatedCost = estimatedCost;
        return this;
    }

    /**
     * Gets the donor financings.
     *
     * @return the donor financings
     */
    @Transient
    public final List<ParsedPrice> getDonorFinancings() {
        return donorFinancings;
    }

    /**
     * Sets the donor financings.
     *
     * @param donorFinancings
     *            the donor financings
     * @return the parsed project
     */
    public final ParsedProject setDonorFinancings(final List<ParsedPrice> donorFinancings) {
        this.donorFinancings = donorFinancings;
        return this;
    }

    /**
     * Adds the donor financing.
     *
     * @param donorFinancing
     *            the donor financing
     * @return the parsed project
     */
    public final ParsedProject addDonorFinancing(final ParsedPrice donorFinancing) {
        if (donorFinancing != null) {
            if (getDonorFinancings() == null) {
                setDonorFinancings(new ArrayList<>());
            }

            this.donorFinancings.add(donorFinancing);
        }

        return this;
    }

    /**
     * Gets the grant amount.
     *
     * @return the grant amount
     */
    @Transient
    public final ParsedPrice getGrantAmount() {
        return grantAmount;
    }

    /**
     * Sets the grant amount.
     *
     * @param grantAmount
     *            the grant amount
     * @return the parsed project
     */
    public final ParsedProject setGrantAmount(final ParsedPrice grantAmount) {
        this.grantAmount = grantAmount;
        return this;
    }

    /**
     * Gets the estimated borrower financing.
     *
     * @return the estimated borrower financing
     */
    @Transient
    public final ParsedPrice getEstimatedBorrowerFinancing() {
        return estimatedBorrowerFinancing;
    }

    /**
     * Sets the estimated borrower financing.
     *
     * @param estimatedBorrowerFinancing
     *            the estimated borrower financing
     * @return the parsed project
     */
    public final ParsedProject setEstimatedBorrowerFinancing(final ParsedPrice estimatedBorrowerFinancing) {
        this.estimatedBorrowerFinancing = estimatedBorrowerFinancing;
        return this;
    }

    /**
     * Gets the borrower financing.
     *
     * @return the borrower financing
     */
    @Transient
    public final ParsedPrice getBorrowerFinancing() {
        return borrowerFinancing;
    }

    /**
     * Sets the borrower financing.
     *
     * @param borrowerFinancing
     *            the borrower financing
     * @return the parsed project
     */
    public final ParsedProject setBorrowerFinancing(final ParsedPrice borrowerFinancing) {
        this.borrowerFinancing = borrowerFinancing;
        return this;
    }

    /**
     * Gets the cancelled amount.
     *
     * @return the cancelled amount
     */
    @Transient
    public final ParsedPrice getCancelledAmount() {
        return cancelledAmount;
    }

    /**
     * Sets the cancelled amount.
     *
     * @param cancelledAmount
     *            the cancelled amount
     * @return the parsed project
     */
    public final ParsedProject setCancelledAmount(final ParsedPrice cancelledAmount) {
        this.cancelledAmount = cancelledAmount;
        return this;
    }

    /**
     * Gets the fundings.
     *
     * @return the fundings
     */
    @Transient
    public final List<ParsedFunding> getFundings() {
        return fundings;
    }

    /**
     * Sets the fundings.
     *
     * @param fundings
     *            the fundings
     * @return the parsed project
     */
    public final ParsedProject setFundings(final List<ParsedFunding> fundings) {
        this.fundings = fundings;
        return this;
    }

    /**
     * Gets the financing plan.
     *
     * @return the financing plan
     */
    @Transient
    public final List<DFIDParsedFinancing> getFinancingPlan() {
        return financingPlan;
    }

    /**
     * Sets the financing plan.
     *
     * @param financingPlan
     *            the financing plan
     * @return the parsed project
     */
    public final ParsedProject setFinancingPlan(final List<DFIDParsedFinancing> financingPlan) {
        this.financingPlan = financingPlan;
        return this;
    }

    /**
     * Gets the financing summary.
     *
     * @return the financing summary
     */
    @Transient
    public final List<DFIDParsedFinancingSummaryItem> getFinancingSummary() {
        return financingSummary;
    }

    /**
     * Sets the financing summary.
     *
     * @param financingSummary
     *            the financing summary
     * @return the parsed project
     */
    public final ParsedProject setFinancingSummary(final List<DFIDParsedFinancingSummaryItem> financingSummary) {
        this.financingSummary = financingSummary;
        return this;
    }
    
    /**
     * Adds the financing summary item.
     *
     * @param financingSummaryItem
     *            the financing summary item
     * @return the parsed project
     */
    public final ParsedProject addFinancingSummaryItem(final DFIDParsedFinancingSummaryItem financingSummaryItem) {
        if (financingSummaryItem != null) {
            if (getFinancingSummary()== null) {
                setFinancingSummary(new ArrayList<>());
            }

            this.financingSummary.add(financingSummaryItem);
        }

        return this;
    }

    /**
     * Gets the financing summary last update.
     *
     * @return the financing summary last update
     */
    @Transient
    public final String getFinancingSummaryLastUpdate() {
        return financingSummaryLastUpdate;
    }

    /**
     * Sets the financing summary last update.
     *
     * @param financingSummaryLastUpdate
     *            the financing summary last update
     * @return the parsed project
     */
    public final ParsedProject setFinancingSummaryLastUpdate(final String financingSummaryLastUpdate) {
        this.financingSummaryLastUpdate = financingSummaryLastUpdate;
        return this;
    }

    /**
     * Gets the borrower.
     *
     * @return the borrower
     */
    @Transient
    public final ParsedBody getBorrower() {
        return borrower;
    }

    /**
     * Sets the borrower.
     *
     * @param borrower
     *            the borrower
     * @return the parsed project
     */
    public final ParsedProject setBorrower(final ParsedBody borrower) {
        this.borrower = borrower;
        return this;
    }

    /**
     * Gets the implementing agency.
     *
     * @return the implementing agency
     */
    @Transient
    public final ParsedBody getImplementingAgency() {
        return implementingAgency;
    }

    /**
     * Sets the implementing agency.
     *
     * @param implementingAgency
     *            the implementing agency
     * @return the parsed project
     */
    public final ParsedProject setImplementingAgency(final ParsedBody implementingAgency) {
        this.implementingAgency = implementingAgency;
        return this;
    }

    /**
     * Gets the sector board.
     *
     * @return the sector board
     */
    @Transient
    public final String getSectorBoard() {
        return sectorBoard;
    }

    /**
     * Sets the sector board.
     *
     * @param sectorBoard
     *            the sector board
     * @return the parsed project
     */
    public final ParsedProject setSectorBoard(final String sectorBoard) {
        this.sectorBoard = sectorBoard;
        return this;
    }

    /**
     * Gets the sectors.
     *
     * @return the sectors
     */
    @Transient
    public final List<DFIDParsedWeightedAttribute> getSectors() {
        return sectors;
    }

    /**
     * Sets the sectors.
     *
     * @param sectors
     *            the sectors
     * @return the parsed project
     */
    public final ParsedProject setSectors(final List<DFIDParsedWeightedAttribute> sectors) {
        this.sectors = sectors;
        return this;
    }

    /**
     * Adds the major sector.
     *
     * @param majorSector
     *            the major sector
     * @return the parsed project
     */
    public final ParsedProject addMajorSector(final DFIDParsedWeightedAttribute majorSector) {
        if (majorSector != null) {
            if (getMajorSectors() == null) {
                setMajorSectors(new ArrayList<>());
            }

            this.majorSectors.add(majorSector);
        }

        return this;
    }
    
    /**
     * Gets the major sectors.
     *
     * @return the major sectors
     */
    @Transient
    public final List<DFIDParsedWeightedAttribute> getMajorSectors() {
        return majorSectors;
    }

    /**
     * Sets the major sectors.
     *
     * @param majorSectors
     *            the major sectors
     * @return the parsed project
     */
    public final ParsedProject setMajorSectors(final List<DFIDParsedWeightedAttribute> majorSectors) {
        this.majorSectors = majorSectors;
        return this;
    }
    
    /**
     * Adds the sector.
     *
     * @param sector
     *            the sector
     * @return the parsed project
     */
    public final ParsedProject addSector(final DFIDParsedWeightedAttribute sector) {
        if (sector != null) {
            if (getSectors() == null) {
                setSectors(new ArrayList<>());
            }

            this.sectors.add(sector);
        }

        return this;
    }

    /**
     * Gets the themes.
     *
     * @return the themes
     */
    @Transient
    public final List<DFIDParsedWeightedAttribute> getThemes() {
        return themes;
    }

    /**
     * Sets the themes.
     *
     * @param themes
     *            the themes
     * @return the parsed project
     */
    public final ParsedProject setThemes(final List<DFIDParsedWeightedAttribute> themes) {
        this.themes = themes;
        return this;
    }

    /**
     * Gets the loans numbers.
     *
     * @return the loans numbers
     */
    @Transient
    public final List<String> getLoansNumbers() {
        return loansNumbers;
    }

    /**
     * Sets the loans numbers.
     *
     * @param loansNumbers
     *            the loans numbers
     * @return the parsed project
     */
    public final ParsedProject setLoansNumbers(final List<String> loansNumbers) {
        this.loansNumbers = loansNumbers;
        return this;
    }

    /**
     * Gets the donor evaluation.
     *
     * @return the donor evaluation
     */
    @Transient
    public final DFIDParsedBodyEvaluation getDonorEvaluation() {
        return donorEvaluation;
    }

    /**
     * Sets the donor evaluation.
     *
     * @param donorEvaluation
     *            the donor evaluation
     * @return the parsed project
     */
    public final ParsedProject setDonorEvaluation(final DFIDParsedBodyEvaluation donorEvaluation) {
        this.donorEvaluation = donorEvaluation;
        return this;
    }

    /**
     * Gets the evaluation.
     *
     * @return the evaluation
     */
    @Transient
    public final DFIDParsedProjectEvaluation getEvaluation() {
        return evaluation;
    }

    /**
     * Sets the evaluation.
     *
     * @param evaluation
     *            the evaluation
     * @return the parsed project
     */
    public final ParsedProject setEvaluation(final DFIDParsedProjectEvaluation evaluation) {
        this.evaluation = evaluation;
        return this;
    }

    /**
     * Gets the environmental and social category.
     *
     * @return the environmental and social category
     */
    @Transient
    public final String getEnvironmentalAndSocialCategory() {
        return environmentalAndSocialCategory;
    }

    /**
     * Sets the environmental and social category.
     *
     * @param environmentalAndSocialCategory
     *            the environmental and social category
     * @return the parsed project
     */
    public final ParsedProject setEnvironmentalAndSocialCategory(final String environmentalAndSocialCategory) {
        this.environmentalAndSocialCategory = environmentalAndSocialCategory;
        return this;
    }

    /**
     * Gets the team leader.
     *
     * @return the team leader
     */
    @Transient
    public final String getTeamLeader() {
        return teamLeader;
    }

    /**
     * Sets the team leader.
     *
     * @param teamLeader
     *            the team leader
     * @return the parsed project
     */
    public final ParsedProject setTeamLeader(final String teamLeader) {
        this.teamLeader = teamLeader;
        return this;
    }

    /**
     * Gets the agreement type.
     *
     * @return the agreement type
     */
    @Transient
    public final String getAgreementType() {
        return agreementType;
    }

    /**
     * Sets the agreement type.
     *
     * @param agreementType
     *            the agreement type
     * @return the parsed project
     */
    public final ParsedProject setAgreementType(final String agreementType) {
        this.agreementType = agreementType;
        return this;
    }

    /**
     * Adds the signature date.
     *
     * @param signatureDate
     *            the signature date
     * @return the parsed project
     */
    public final ParsedProject addSignatureDate(final String signatureDate) {
        if (signatureDate != null) {
            if (getSignatureDates() == null) {
                setSignatureDates(new ArrayList<>());
            }

            this.signatureDates.add(signatureDate);
        }

        return this;
    }
}
