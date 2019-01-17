package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.dataaccess.dto.generic.Address;
import eu.dl.dataaccess.dto.parsed.ParsedAddress;
import eu.dl.worker.clean.plugin.BaseCleaningPlugin;
import eu.dl.worker.utils.ArrayUtils;
import eu.dl.worker.clean.utils.StringUtils;

/**
 * Plugin that cleans project locations.
 *
 * @author Tomas Mrazek
 */
public class DFIDProjectLocationPlugin extends BaseCleaningPlugin<ParsedProject, DFIDCleanProject> {
    @Override
    public final DFIDCleanProject clean(final ParsedProject parsedItem, final DFIDCleanProject cleanItem) {
        cleanItem.setLocations(ArrayUtils.walk(parsedItem.getLocations(), this::cleanAddress));

        return cleanItem;
    }

    /**
     * Cleans location/address.
     *
     * @param address
     *          address to clean
     * @return clean address or null
     */
    private Address cleanAddress(final ParsedAddress address) {
        return new Address()
            .setCity(StringUtils.cleanShortString(address.getCity()))
            .setCountry(StringUtils.cleanShortString(address.getCountry()));
    }
}
