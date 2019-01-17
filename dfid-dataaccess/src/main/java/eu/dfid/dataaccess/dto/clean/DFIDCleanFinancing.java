package eu.dfid.dataaccess.dto.clean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.dl.dataaccess.dto.clean.Validable;
import eu.dl.dataaccess.dto.generic.Price;
import eu.dl.dataaccess.utils.ClassUtils;
import eu.dl.dataaccess.utils.ValidationUtils;
import javax.persistence.Transient;

/**
 * DFID Financing. Information about financier and their commitment amount.
 */
public final class DFIDCleanFinancing implements Validable {
    private String financier;
    private Price commitment;

    /**
     * Gets financier.
     *
     * @return value of financier
     */
    public String getFinancier() {
        return financier;
    }

    /**
     * Sets financier.
     *
     * @param financier
     *         the financier to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanFinancing setFinancier(final String financier) {
        this.financier = financier;
        return this;
    }

    /**
     * Gets commitment.
     *
     * @return value of commitment
     */
    public Price getCommitment() {
        return commitment;
    }

    /**
     * Sets commitment.
     *
     * @param commitment
     *         the commitment to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanFinancing setCommitment(final Price commitment) {
        this.commitment = commitment;
        return this;
    }

    @Override
    @Transient
    @JsonIgnore
    public DFIDCleanFinancing getValid() {
        setCommitment(ClassUtils.removeNonsenses(commitment));

        return ValidationUtils.getValid(this, commitment, financier);
    }
}
