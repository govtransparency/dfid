package eu.dfid.dataaccess.dto.parsed;

import eu.dl.dataaccess.dto.parsed.ParsedBody;

/**
 * DFID Body.
 */
public final class DFIDParsedBody extends ParsedBody {
    private DFIDParsedBodyEvaluation bodyEvaluation;

    /**
     * Gets bodyEvaluation.
     *
     * @return value of bodyEvaluation
     */
    public DFIDParsedBodyEvaluation getBodyEvaluation() {
        return bodyEvaluation;
    }

    /**
     * Sets bodyEvaluation.
     *
     * @param bodyEvaluation
     *         the bodyEvaluation to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedBody setBodyEvaluation(final DFIDParsedBodyEvaluation bodyEvaluation) {
        this.bodyEvaluation = bodyEvaluation;
        return this;
    }
}
