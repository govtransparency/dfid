package eu.dfid.worker.wb.parsed;

import eu.dfid.dataaccess.dto.codetables.PublicationSources;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedPublication;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedTender;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dfid.worker.parser.BaseDFIDTenderParser;
import eu.dl.dataaccess.dto.parsed.ParsedAddress;
import eu.dl.dataaccess.dto.parsed.ParsedBody;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.dataaccess.dto.raw.RawData;
import eu.dl.worker.utils.jsoup.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Notice parser for World Bank.
 *
 * @author Marek Mikes
 */
public class WBNoticeParser extends BaseDFIDTenderParser {
    private static final String VERSION = "1";

    private static final String ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN =
            "div[id=noticeglance] tr:has(td:nth-child(1):containsOwn(%s)) > td:nth-child(2)";
    private static final String ELEMENT_IN_CONTACT_INFORMATION_TABLE_SELECTOR_PATTERN =
            "div[id=projectDetails] tr:has(td:nth-child(1):containsOwn(%s)) > td:nth-child(2)";

    @Override
    public final List<ParsedTender> parse(final RawData rawTender) {
        final Document form = Jsoup.parse(rawTender.getSourceData());

        DFIDParsedTender parsedTender = new DFIDParsedTender();

        // parse DFID attributes
        parsedTender
                .setProject(new ParsedProject()
                        .setProjectId(parseProjectId(form))
                        .setName(parseProjectName(form))
                        .setCountry(parseProjectCountry(form)))
                .addPublication(new DFIDParsedPublication()
                        .setNoticeStatus(parsePublicationNoticeStatus(form)));

        // parse common attributes
        parsedTender
                .setTitle(parseTenderTitle(form))
                .setBuyerAssignedId(parseTenderBuyerAssignedId(form))
                .setSelectionMethod(parseTenderSelectionMethod(form))
                .setBidDeadline(parseTenderBidDeadline(form))
                .setFurtherInformationProvider(new ParsedBody()
                        .setName(parseProviderName(form))
                        .setContactName(parseProviderContactName(form))
                        .setAddress(new ParsedAddress()
                                .setStreet(parseProviderStreet(form))
                                .setCity(parseProviderCity(form))
                                .setState(parseProviderState(form))
                                .setPostcode(parseProviderPostcode(form))
                                .setCountry(parseProviderCountry(form))
                                .setUrl(parseProviderWeb(form)))
                        .setPhone(parseProviderPhone(form))
                        .setEmail(parseProviderEmail(form)));
        parsedTender.getPublications().get(0)
                .setSourceId(parsePublicationSourceId(form))
                .setSourceFormType(parsePublicationSourceFormType(form))
                .setLanguage(parsePublicationLanguage(form))
                .setPublicationDate(parsePublicationDate(form))
                .setIsIncluded(true)
                .setSource(PublicationSources.WB)
                .setHumanReadableUrl(rawTender.getSourceUrl().toString());

        return new ArrayList<>(Collections.singletonList(parsedTender));
    }

    @Override
    public final String getVersion() {
        return VERSION;
    }

    /**
     * Parse tender title value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderTitle(final Document form) {
        return JsoupUtils.selectText("h2", form);
    }

    /**
     * Parse project Id value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProjectId(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN, "Project ID"), form);
    }

    /**
     * Parse project name value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProjectName(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN, "Project Title"), form);
    }

    /**
     * Parse project country value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProjectCountry(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN, "Country"), form);
    }

    /**
     * Parse publication source Id value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parsePublicationSourceId(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN, "Notice No"), form);
    }

    /**
     * Parse publication source form type value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parsePublicationSourceFormType(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN, "Notice Type"), form);
    }

    /**
     * Parse publication notice status value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parsePublicationNoticeStatus(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN, "Notice Status"), form);
    }

    /**
     * Parse tender buyer assigned Id value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderBuyerAssignedId(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN, "Borrower Bid Reference"), form);
    }

    /**
     * Parse tender selection method value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderSelectionMethod(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN, "Procurement Method"), form);
    }

    /**
     * Parse publication language value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parsePublicationLanguage(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN, "Language of Notice"), form);
    }

    /**
     * Parse tender bid deadline value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseTenderBidDeadline(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN, "Submission Deadline Date/Time"),
                form);
    }

    /**
     * Parse publication date value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parsePublicationDate(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_NOTICE_AT_A_GLANCE_TABLE_SELECTOR_PATTERN, "Published Date"), form);
    }

    /**
     * Parse provider name value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProviderName(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_CONTACT_INFORMATION_TABLE_SELECTOR_PATTERN, "Organization/Department"), form);
    }

    /**
     * Parse provider contact name value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProviderContactName(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_CONTACT_INFORMATION_TABLE_SELECTOR_PATTERN, "Name"), form);
    }

    /**
     * Parse provider street value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProviderStreet(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_CONTACT_INFORMATION_TABLE_SELECTOR_PATTERN, "Address"), form);
    }

    /**
     * Parse provider city value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProviderCity(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_CONTACT_INFORMATION_TABLE_SELECTOR_PATTERN, "City"), form);
    }

    /**
     * Parse provider state value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProviderState(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_CONTACT_INFORMATION_TABLE_SELECTOR_PATTERN, "Province/State"), form);
    }

    /**
     * Parse provider postcode value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProviderPostcode(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_CONTACT_INFORMATION_TABLE_SELECTOR_PATTERN, "Postal Code"), form);
    }

    /**
     * Parse provider country value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProviderCountry(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_CONTACT_INFORMATION_TABLE_SELECTOR_PATTERN, "Country"), form);
    }

    /**
     * Parse provider phone value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProviderPhone(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_CONTACT_INFORMATION_TABLE_SELECTOR_PATTERN, "Phone"), form);
    }

    /**
     * Parse provider email value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProviderEmail(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_CONTACT_INFORMATION_TABLE_SELECTOR_PATTERN, "Email"), form);
    }

    /**
     * Parse provider web value from document.
     *
     * @param form
     *         document to be parsed
     *
     * @return String or Null
     */
    private static String parseProviderWeb(final Document form) {
        return JsoupUtils.selectText(
                String.format(ELEMENT_IN_CONTACT_INFORMATION_TABLE_SELECTOR_PATTERN, "Website"), form);
    }

    @Override
    protected final String countryOfOrigin(final ParsedTender parsed, final RawData raw){
        return null;
    }
}
