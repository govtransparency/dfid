package eu.dfid.worker.wb.raw;

/**
 * API response statuses.
 *
 * @author Tomas Mrazek
 */
public enum SODAResponseStatusType {
    /** Request was successful. */
    SUCCESS(200),
    /** Processing. Is possible retry the request. */
    PROCESSING(202),
    /** Malformed request. */
    BAD_REQUEST(400),
    /** Attempt to authenticate fails. */
    UNAUTHORIZED(401),
    /** Unauthorized access to the resource. */
    FORBIDDEN(403),
    /** The requested resource doesn't exist. */
    NOT_FOUND(404),
    /** Limit of the requests count was reached. */
    TOO_MANY_REQUESTS(429),
    /** Something has gone wrong with Socrata’s platform. */
    SERVER_ERROR(500),
    /** Unknown state. */
    UNKNOWN(0);
    /**
     * Http response status code.
     */
    private final int httpCode;

    /**
     * @param httpCode
     *      http code for response status
     */
    SODAResponseStatusType(final int httpCode) {
        this.httpCode = httpCode;
    }
    /**
     * @return http status code
     */
    public int getHttpCode() {
        return httpCode;
    }

    /**
     * For given http code returns appropriate status.
     *
     * @param httpCode
     *      http status code
     * @return status or null if status doesn't exists for the given http code
     */
    public static SODAResponseStatusType getStatusForHttpCode(final int httpCode) {
        for (SODAResponseStatusType status : SODAResponseStatusType.values()) {
            if (status.getHttpCode() == httpCode) {
                return status;
            }
        }

        return UNKNOWN;
    }
}
