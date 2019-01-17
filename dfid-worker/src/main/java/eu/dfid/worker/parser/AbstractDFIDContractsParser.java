
package eu.dfid.worker.parser;

import eu.dl.dataaccess.dto.parsed.ParsedAddress;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author pferenczy
 */
public abstract class AbstractDFIDContractsParser extends BaseDFIDTenderParser{

    /**
     * contract types
     */
    protected final String CONSULTING_SERVICES = "CONSULTING SERVICES";
    protected final String GOODS_AND_WORKS = "GOODS & WORKS";
    

    /**
     * convert raw data to a hashmap to allow easier handling and better code readability
     * @param row the result row
     * @return a map, contains all fields and values
     */
    protected Map<String, String> convertResultRowToData(Element row) {
        final Map<String, String> details = new HashMap<>();
        final Element headerData = row.getElementsByClass("table-row-a").first();
        final Element detailsData = row.getElementsByClass("table-row-b").first();
        final Elements headerFields = headerData.getElementsByClass("td-cell");
        for (Element e: detailsData.getElementsByClass("accordion-td-row")) {
            Integer count = e.getElementsByClass("accordion-td-cell").size();
            Integer startItem = 0;
            while (count > 0) {
                details.put(
                 e.getElementsByClass("accordion-td-cell").get(startItem++).text().toLowerCase().replaceAll(":", ""),
                 e.getElementsByClass("accordion-td-cell").get(startItem++).text()
                 );
                count -= 2;
            }
            
        }

        details.put("country", headerFields.get(0).text());
        details.put("type", headerFields.get(1).text());
        details.put("source id", headerFields.get(2).text());
        details.put("contract signature date", headerFields.get(3).text());
        return details;
    }
    
    /**
     * Parse bidder address value from raw data
     *
     * @param countryAndCityString the raw address information
     *
     * @return Parsed address or Null
     */
    protected static ParsedAddress parseBidderAddress(final String countryAndCityString) {
        if (countryAndCityString == null) {
            return null;
        }

        int commaIndex = countryAndCityString.indexOf(",");
        assert commaIndex != -1
                : "Comma should be filled always. Even if there is only country (comma is last character)";
        return new ParsedAddress()
                .setCountry(countryAndCityString.substring(0, commaIndex).trim())
                .setCity(countryAndCityString.length() - 1 == commaIndex
                        ? null
                        : countryAndCityString.substring(commaIndex + 1).trim());
    }
}
