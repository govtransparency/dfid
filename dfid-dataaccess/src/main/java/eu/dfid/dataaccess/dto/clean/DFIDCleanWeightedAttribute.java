package eu.dfid.dataaccess.dto.clean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.dl.dataaccess.dto.clean.Validable;
import eu.dl.dataaccess.utils.ValidationUtils;
import javax.persistence.Transient;

/**
 * DFID Weighted Attribute.
 */
public final class DFIDCleanWeightedAttribute implements Validable {
    private String name;
    private Double weight;

    /**
     * Gets name.
     *
     * @return value of name
     */
    public String getName() {
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
    public DFIDCleanWeightedAttribute setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets weight.
     *
     * @return value of weight
     */
    public Double getWeight() {
        return weight;
    }

    /**
     * Sets weight.
     *
     * @param weight
     *         the weight to set
     *
     * @return this instance for chaining
     */
    public DFIDCleanWeightedAttribute setWeight(final Double weight) {
        this.weight = weight;
        return this;
    }

    @Override
    @Transient
    @JsonIgnore
    public DFIDCleanWeightedAttribute getValid() {
        return ValidationUtils.getValid(this, name, weight);
    }
}
