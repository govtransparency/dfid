package eu.dfid.dataaccess.dto.clean;

import eu.dl.dataaccess.dto.clean.CleanBody;

/**
 * DFID Body.
 */
public final class DFIDCleanBody extends CleanBody {
    private DFIDCleanBodyEvaluation bodyEvaluation;
    
    /**
     * Default constructor.
     */
    public DFIDCleanBody() {
        super();
    }

    /**
     * Constructor that creates DFIDCleanBody from CleanBody instance.
     * 
     * @param cleanBody
     *      CleanBody instance
     */
    public DFIDCleanBody(final CleanBody cleanBody) {
        if (cleanBody == null) {
            return;
        }

        setAddress(cleanBody.getAddress());
        setBodyIds(cleanBody.getBodyIds());
        setBuyerType(cleanBody.getBuyerType());
        setContactName(cleanBody.getContactName());
        setContactPoint(cleanBody.getContactPoint());
        setEmail(cleanBody.getEmail());
        setIsLeader(cleanBody.getIsLeader());
        setIsPublic(cleanBody.getIsPublic());
        setIsSectoral(cleanBody.getIsSectoral());
        setIsSme(cleanBody.getIsSme());
        setIsSubsidized(cleanBody.getIsSubsidized());
        setMainActivities(cleanBody.getMainActivities());
        setName(cleanBody.getName());
        setPhone(cleanBody.getPhone());
    }

    /**
     * Gets bodyEvaluation.
     *
     * @return value of bodyEvaluation
     */
    public DFIDCleanBodyEvaluation getBodyEvaluation() {
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
    public DFIDCleanBody setBodyEvaluation(final DFIDCleanBodyEvaluation bodyEvaluation) {
        this.bodyEvaluation = bodyEvaluation;
        return this;
    }
}
