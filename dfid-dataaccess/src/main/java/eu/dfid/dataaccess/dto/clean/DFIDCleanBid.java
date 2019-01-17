package eu.dfid.dataaccess.dto.clean;

import eu.dl.dataaccess.dto.clean.CleanBid;

/**
 * DFID Bid.
 */
public final class DFIDCleanBid extends CleanBid {
    private Integer score;

    /**
     * Gets score (tenderer's score if the criteria is MEAT).
     *
     * @return value of score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Sets score (tenderer's score if the criteria is MEAT).
     *
     * @param score
     *         the score to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanBid setScore(final Integer score) {
        this.score = score;
        return this;
    }
}
