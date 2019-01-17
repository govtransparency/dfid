package eu.dfid.dataaccess.dto.clean;

import eu.dl.dataaccess.dto.generic.Publication;

/**
 * DFID Publication.
 */
public final class DFIDCleanPublication extends Publication {
    private String noticeStatus;

    /**
     * Constructor which creates DFID publication.
     *
     */
    public DFIDCleanPublication() {
        super();
    }

    /**
     * Constructor which creates DFID copy of input publication.
     *
     * @param publication
     *            publication
     */
    public DFIDCleanPublication(final Publication publication) {
        super(publication);
    }

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
    public DFIDCleanPublication setNoticeStatus(final String noticeStatus) {
        this.noticeStatus = noticeStatus;
        return this;
    }
}
