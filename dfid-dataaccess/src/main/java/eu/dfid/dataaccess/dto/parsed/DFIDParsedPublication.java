package eu.dfid.dataaccess.dto.parsed;

import eu.dl.dataaccess.dto.parsed.ParsedPublication;

/**
 * DFID Publication.
 */
public final class DFIDParsedPublication extends ParsedPublication {
    private String noticeStatus;

    /**
     * Gets noticeStatus.
     *
     * @return value of noticeStatus
     */
    public String getNoticeStatus() {
        return noticeStatus;
    }

    /**
     * Sets noticeStatus.
     *
     * @param noticeStatus
     *         the noticeStatus to set
     *
     * @return this instance for chaining
     */
    public DFIDParsedPublication setNoticeStatus(final String noticeStatus) {
        this.noticeStatus = noticeStatus;
        return this;
    }
}
