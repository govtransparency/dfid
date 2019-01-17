package eu.dfid.worker.wb.parsed;

import org.jsoup.nodes.Document;

import eu.dfid.worker.parser.BaseDFIDProjectParser;
import eu.dl.worker.utils.jsoup.JsoupUtils;

/**
 * Base class for World Bank parsers which parse Documents & Reports pages.
 */
abstract class BaseWBDocumentsAndReportsProjectParser extends BaseDFIDProjectParser {
    private static final String ELEMENT_IN_DETAIL_TABLE_SELECTOR_PATTERN =
            "ul.detail > li:has(label:containsOwn(%s)) > span";

    /**
     * Parse publication date value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    protected static String parsePublicationDate(final Document form) {
        return parseDate(form, "Document Date");
    }

    /**
     * Parse publication last update value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    protected static String parsePublicationLastUpdate(final Document form) {
        return parseDate(form, "Disclosure Date");
    }

    /**
     * Parse publication source form type value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    protected static String parsePublicationSourceFormType(final Document form) {
        return JsoupUtils.selectText(String.format(ELEMENT_IN_DETAIL_TABLE_SELECTOR_PATTERN, "Document Type"), form);
    }

    /**
     * Parse publication source ID value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    protected static String parsePublicationSourceId(final Document form) {
        return JsoupUtils.selectText(String.format(ELEMENT_IN_DETAIL_TABLE_SELECTOR_PATTERN, "Report Number"), form);
    }

    /**
     * Parse publication language value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    protected static String parsePublicationLanguage(final Document form) {
        return JsoupUtils.selectText(String.format(ELEMENT_IN_DETAIL_TABLE_SELECTOR_PATTERN, "Language"), form);
    }

    /**
     * Parse project Id value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    protected static String parseProjectId(final Document form) {
        return JsoupUtils.selectText(String.format(ELEMENT_IN_DETAIL_TABLE_SELECTOR_PATTERN, "Rel. Proj ID"), form);
    }

    /**
     * Parse project operation number value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    protected static String parseProjectOperationNumber(final Document form) {
        return JsoupUtils.selectText(String.format(ELEMENT_IN_DETAIL_TABLE_SELECTOR_PATTERN, "Loan No"), form);
    }

    /**
     * Parse date value from document.
     *
     * @param form
     *         document to be parsed
     * @param dateTitle
     *         title of the date
     *
     * @return String or Null
     */
    private static String parseDate(final Document form, final String dateTitle) {
        final String publicationDateAndTime =
                JsoupUtils.selectText(String.format(ELEMENT_IN_DETAIL_TABLE_SELECTOR_PATTERN, dateTitle), form);
        if (publicationDateAndTime == null) {
            return null;
        } else {
            String[] publicationDateAndTimeArray = publicationDateAndTime.split(" ");
            // sometimes the date does contain time and sometimes does not.
            assert  publicationDateAndTimeArray.length == 1 || publicationDateAndTimeArray.length == 2;
            return publicationDateAndTimeArray[0];
        }
    }
}
