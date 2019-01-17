package eu.dfid.dataaccess.dto.parsed;

/**
 * DFID Weighted Attribute.
 */
public final class DFIDParsedWeightedAttribute {
    private String name;
    private String weight;

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
    public DFIDParsedWeightedAttribute setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets weight.
     *
     * @return value of weight
     */
    public String getWeight() {
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
    public DFIDParsedWeightedAttribute setWeight(final String weight) {
        this.weight = weight;
        return this;
    }
}
