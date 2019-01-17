package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanFinancingSummaryItem;
import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedFinancingSummaryItem;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.BaseDateTimePlugin;
import eu.dl.worker.clean.plugin.DatePlugin;
import eu.dl.worker.clean.utils.CleanUtils;
import eu.dl.worker.utils.ArrayUtils;
import eu.dl.worker.clean.utils.DateUtils;
import eu.dl.worker.clean.utils.PriceUtils;
import eu.dl.worker.clean.utils.StringUtils;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

/**
 * Created by michal on 21.12.16.
 */
public class DFIDProjectFinancingSummaryPlugin extends BaseDateTimePlugin<DatePlugin, ParsedProject, DFIDCleanProject> {
    protected NumberFormat format;

    /**
     * Default constructor.
     *
     * @param dateFormatters formatter
     * @param format format
     */
    public DFIDProjectFinancingSummaryPlugin(final DateTimeFormatter dateFormatters, final NumberFormat format) {
        super(dateFormatters);
        this.format = format;
    }

    @Override
    public final DFIDCleanProject clean(final ParsedProject parsedItem, final DFIDCleanProject cleanItem) {
        cleanItem.setFinancingSummary(ArrayUtils.walk(parsedItem.getFinancingSummary(),
                item -> cleanFinancingSummary(item, CleanUtils.getParsedItemCountry(parsedItem))));

        return cleanItem;
    }

    /**
     * Clean financing summary.
     *
     * @param item item to clean
     * @param country country
     * @return clean item
     */
    private DFIDCleanFinancingSummaryItem cleanFinancingSummary(final DFIDParsedFinancingSummaryItem item,
        final String country) {
        return new DFIDCleanFinancingSummaryItem()
                .setLoanNumber(StringUtils.cleanShortString(item.getLoanNumber()))
                .setApprovalDate(DateUtils.cleanDate(item.getApprovalDate(), formatters))
                .setClosingDate(DateUtils.cleanDate(item.getClosingDate(), formatters))
                .setPrincipal(PriceUtils.cleanPrice(item.getPrincipal(), format, country))
                .setDisbursed(PriceUtils.cleanPrice(item.getDisbursed(), format, country))
                .setRepayments(PriceUtils.cleanPrice(item.getRepayments(), format, country))
                .setInterestChargesFees(PriceUtils.cleanPrice(item.getInterestChargesFees(), format, country))
                .setCancelledAmount(PriceUtils.cleanPrice(item.getCancelledAmount(), format, country));
    }
}
