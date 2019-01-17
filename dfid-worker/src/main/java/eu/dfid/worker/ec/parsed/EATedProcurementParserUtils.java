package eu.dfid.worker.ec.parsed;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import eu.dl.worker.utils.jsoup.JsoupUtils;

/**
 * Utils class for EuropeAid of European Commission that are published in TED.
 *
 * @author Tomas Mrazek
 */
public final class EATedProcurementParserUtils {

    /**
     * Tries to find element TXT_MARK that follows after the elemnt TI_MARK which includes own text matching
     * {@code regex}. If such element exists, returns its text.
     *
     * @param regex
     *      label regex
     * @param context
     *      context
     * @return text of the labeled node or null
     */
    public static String selectTextByLabel(final String regex, final Element context) {
        return JsoupUtils.selectText("TI_MARK:matchesOwn(" + regex + ") + TXT_MARK", context);
    }

    /**
     * Suppress default constructor for noninstantiability.
     */
    private EATedProcurementParserUtils() {
        throw new AssertionError();
    }
    
    /**
     * @param document
     *      parsed document
     * @return CODED_DATA_SECTION node or null
     */
    public static Element parseCodeDataSectionNode(final Document document) {
        return JsoupUtils.selectFirst("TED_EXPORT > CODED_DATA_SECTION", document);
    }
    
    /**
     * @param document
     *      parsed document
     * @return TRANSLATION_SECTION node or null
     */
    public static Element parseTranslationSectionNode(final Document document) {
        return JsoupUtils.selectFirst("TED_EXPORT > TRANSLATION_SECTION", document);
    }
    
    /**
     * @param document
     *      parsed document
     * @return FORM_SECTION node or null
     */
    public static Element parseFormSectionNode(final Document document) {
        return JsoupUtils.selectFirst("TED_EXPORT > FORM_SECTION", document);
    }
    
    /**
     * @param formSectionNode
     *      FORM_SECTION node
     * @return origin node for form data parsing
     */
    public static Element parseOriginNode(final Element formSectionNode) {
        return JsoupUtils.selectFirst("OTH_NOT[LG=EN]", formSectionNode);
    }
    
    /**
     * @param codedDataSectionNode
     *      CODED_DATA_SECTION node
     * @return original language of the form
     */
    public static String parseOriginalLanguage(final Element codedDataSectionNode) {
        final Element node = JsoupUtils.selectFirst("NOTICE_DATA > LG_ORIG", codedDataSectionNode);
        return (node == null ? "EN" : node.text());
    }
}
