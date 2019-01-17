package eu.dfid.dataaccess.dto.parsed;

import eu.dl.dataaccess.dto.parsed.ParsedBid;

/**
 * DFID Bid.
 */
public final class DFIDParsedBid extends ParsedBid {
    private String score;

    /**
     * Gets score.
     *
     * @return value of score
     */
    public String getScore() {
        return score;
    }

    /**
     * Sets score.
     *
     * @param score
     *         the score to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedBid setScore(final String score) {
        this.score = score;
        return this;
    }
}
