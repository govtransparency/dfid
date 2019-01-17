package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.clean.DFIDCleanWeightedAttribute;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedWeightedAttribute;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.BaseNumberPlugin;
import eu.dl.worker.utils.ArrayUtils;
import eu.dl.worker.clean.utils.NumberUtils;
import eu.dl.worker.clean.utils.StringUtils;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by michal on 21.12.16.
 */
public class DFIDProjectWeightedAttributesPlugin extends BaseNumberPlugin<ParsedProject, DFIDCleanProject> {

    /**
     * Default constructor.
     *
     * @param format format
     */
    public DFIDProjectWeightedAttributesPlugin(final NumberFormat format) {
        super(format);
    }

    @Override
    public final DFIDCleanProject clean(final ParsedProject parsedItem, final DFIDCleanProject cleanItem) {
        cleanItem.setMajorSectors(ArrayUtils.walk(parsedItem.getMajorSectors(), this::cleanWeightedAttribute));
        cleanItem.setSectors(ArrayUtils.walk(parsedItem.getSectors(), this::cleanWeightedAttribute));
        cleanItem.setThemes(ArrayUtils.walk(parsedItem.getThemes(), this::cleanWeightedAttribute));

        return cleanItem;
    }

    /**
     * Clean WeihtedAttributes.
     *
     * @param sector
     *      sector to clean
     * @return DFIDCleanWeightedAttribute
     */
    private DFIDCleanWeightedAttribute cleanWeightedAttribute(final DFIDParsedWeightedAttribute sector) {

        BigDecimal weight = NumberUtils.cleanBigDecimal(sector.getWeight(), formats);
        
        return new DFIDCleanWeightedAttribute()
                .setName(StringUtils.cleanShortString(sector.getName()))
                .setWeight(weight != null ? weight.doubleValue() : null);
    }
}
