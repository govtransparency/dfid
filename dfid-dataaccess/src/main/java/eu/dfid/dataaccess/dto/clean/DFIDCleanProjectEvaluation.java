package eu.dfid.dataaccess.dto.clean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.dl.dataaccess.dto.clean.Validable;
import eu.dl.dataaccess.utils.ValidationUtils;
import java.net.URL;
import java.time.LocalDate;
import javax.persistence.Transient;

/**
 * DFID Project Evaluation.
 */
public final class DFIDCleanProjectEvaluation implements Validable {
    private Integer evaluationFiscalYear;
    private LocalDate evaluationDate;
    private String evaluationType;
    private Integer projectErrExAnte;
    private Integer projectErrExPost;
    private String projectOutcome;
    private String projectImpact;
    private String sustainabilityRating;
    private URL reportUrl;
    private String riskToDevelopment;
    private String icrQuality;
    private String meQuality;

    /**
     * Gets evaluationFiscalYear.
     *
     * @return value of evaluationFiscalYear
     */
    public Integer getEvaluationFiscalYear() {
        return evaluationFiscalYear;
    }

    /**
     * Sets evaluationFiscalYear.
     *
     * @param evaluationFiscalYear
     *         the evaluationFiscalYear to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setEvaluationFiscalYear(final Integer evaluationFiscalYear) {
        this.evaluationFiscalYear = evaluationFiscalYear;
        return this;
    }

    /**
     * Gets evaluationDate.
     *
     * @return value of evaluationDate
     */
    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    /**
     * Sets evaluationDate.
     *
     * @param evaluationDate
     *         the evaluationDate to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setEvaluationDate(final LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
        return this;
    }

    /**
     * Gets evaluationType.
     *
     * @return value of evaluationType
     */
    public String getEvaluationType() {
        return evaluationType;
    }

    /**
     * Sets evaluationType.
     *
     * @param evaluationType
     *         the evaluationType to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setEvaluationType(final String evaluationType) {
        this.evaluationType = evaluationType;
        return this;
    }

    /**
     * Gets projectErrExAnte.
     *
     * @return value of projectErrExAnte
     */
    public Integer getProjectErrExAnte() {
        return projectErrExAnte;
    }

    /**
     * Sets projectErrExAnte.
     *
     * @param projectErrExAnte
     *         the projectErrExAnte to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setProjectErrExAnte(final Integer projectErrExAnte) {
        this.projectErrExAnte = projectErrExAnte;
        return this;
    }

    /**
     * Gets projectErrExPost.
     *
     * @return value of projectErrExPost
     */
    public Integer getProjectErrExPost() {
        return projectErrExPost;
    }

    /**
     * Sets projectErrExPost.
     *
     * @param projectErrExPost
     *         the projectErrExPost to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setProjectErrExPost(final Integer projectErrExPost) {
        this.projectErrExPost = projectErrExPost;
        return this;
    }

    /**
     * Gets projectOutcome.
     *
     * @return value of projectOutcome
     */
    public String getProjectOutcome() {
        return projectOutcome;
    }

    /**
     * Sets projectOutcome.
     *
     * @param projectOutcome
     *         the projectOutcome to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setProjectOutcome(final String projectOutcome) {
        this.projectOutcome = projectOutcome;
        return this;
    }

    /**
     * Gets projectImpact.
     *
     * @return value of projectImpact
     */
    public String getProjectImpact() {
        return projectImpact;
    }

    /**
     * Sets projectImpact.
     *
     * @param projectImpact
     *         the projectImpact to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setProjectImpact(final String projectImpact) {
        this.projectImpact = projectImpact;
        return this;
    }

    /**
     * Gets sustainabilityRating.
     *
     * @return value of sustainabilityRating
     */
    public String getSustainabilityRating() {
        return sustainabilityRating;
    }

    /**
     * Sets sustainabilityRating.
     *
     * @param sustainabilityRating
     *         the sustainabilityRating to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setSustainabilityRating(final String sustainabilityRating) {
        this.sustainabilityRating = sustainabilityRating;
        return this;
    }

    /**
     * Gets reportUrl.
     *
     * @return value of reportUrl
     */
    public URL getReportUrl() {
        return reportUrl;
    }

    /**
     * Sets reportUrl.
     *
     * @param reportUrl
     *         the reportUrl to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setReportUrl(final URL reportUrl) {
        this.reportUrl = reportUrl;
        return this;
    }

    /**
     * Gets riskToDevelopment.
     *
     * @return value of riskToDevelopment
     */
    public String getRiskToDevelopment() {
        return riskToDevelopment;
    }

    /**
     * Sets riskToDevelopment.
     *
     * @param riskToDevelopment
     *         the riskToDevelopment to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setRiskToDevelopment(final String riskToDevelopment) {
        this.riskToDevelopment = riskToDevelopment;
        return this;
    }

        /**
     * Gets icrQuality.
     *
     * @return value of icrQuality
     */
    public String getIcrQuality() {
        return icrQuality;
    }

    /**
     * Sets icrQuality.
     *
     * @param icrQuality
     *         the icrQuality to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setIcrQuality(final String icrQuality) {
        this.icrQuality = icrQuality;
        return this;
    }

    /**
     * Gets meQuality.
     *
     * @return value of meQuality
     */
    public String getMeQuality() {
        return meQuality;
    }

    /**
     * Sets meQuality.
     *
     * @param meQuality
     *         the meQuality to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanProjectEvaluation setMeQuality(final String meQuality) {
        this.meQuality = meQuality;
        return this;
    }

    @Override
    @Transient
    @JsonIgnore
    public DFIDCleanProjectEvaluation getValid() {
        return ValidationUtils.getValid(this, evaluationDate, evaluationFiscalYear, evaluationType, icrQuality,
            meQuality, projectErrExAnte, projectErrExPost, projectImpact, projectOutcome, reportUrl, riskToDevelopment,
            sustainabilityRating);
    }
}
