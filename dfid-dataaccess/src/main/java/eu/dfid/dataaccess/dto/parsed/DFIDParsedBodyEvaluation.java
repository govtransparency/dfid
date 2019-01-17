package eu.dfid.dataaccess.dto.parsed;

/**
 * DFID Body Evaluation.
 */
public final class DFIDParsedBodyEvaluation {
    private String qualityExAnte;
    private String supervisionQuality;
    private String overallPerformance;
    private String implementingAgencyPerformance;
    private String governmentPerformance;

    /**
     * Gets qualityExAnte.
     *
     * @return value of qualityExAnte
     */
    public String getQualityExAnte() {
        return qualityExAnte;
    }

    /**
     * Sets qualityExAnte.
     *
     * @param qualityExAnte
     *         the qualityExAnte to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedBodyEvaluation setQualityExAnte(final String qualityExAnte) {
        this.qualityExAnte = qualityExAnte;
        return this;
    }

    /**
     * Gets supervisionQuality.
     *
     * @return value of supervisionQuality
     */
    public String getSupervisionQuality() {
        return supervisionQuality;
    }

    /**
     * Sets supervisionQuality.
     *
     * @param supervisionQuality
     *         the supervisionQuality to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedBodyEvaluation setSupervisionQuality(final String supervisionQuality) {
        this.supervisionQuality = supervisionQuality;
        return this;
    }

    /**
     * Gets overallPerformance.
     *
     * @return value of overallPerformance
     */
    public String getOverallPerformance() {
        return overallPerformance;
    }

    /**
     * Sets overallPerformance.
     *
     * @param overallPerformance
     *         the overallPerformance to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedBodyEvaluation setOverallPerformance(final String overallPerformance) {
        this.overallPerformance = overallPerformance;
        return this;
    }

    /**
     * Gets implementingAgencyPerformance.
     *
     * @return value of implementingAgencyPerformance
     */
    public String getImplementingAgencyPerformance() {
        return implementingAgencyPerformance;
    }

    /**
     * Sets implementingAgencyPerformance.
     *
     * @param implementingAgencyPerformance
     *         the implementingAgencyPerformance to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedBodyEvaluation setImplementingAgencyPerformance(final String implementingAgencyPerformance) {
        this.implementingAgencyPerformance = implementingAgencyPerformance;
        return this;
    }

    /**
     * Gets governmentPerformance.
     *
     * @return value of governmentPerformance
     */
    public String getGovernmentPerformance() {
        return governmentPerformance;
    }

    /**
     * Sets governmentPerformance.
     *
     * @param governmentPerformance
     *         the governmentPerformance to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedBodyEvaluation setGovernmentPerformance(final String governmentPerformance) {
        this.governmentPerformance = governmentPerformance;
        return this;
    }
}
