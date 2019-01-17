package eu.dfid.worker.clean.plugin.project;

import eu.dfid.dataaccess.dto.clean.DFIDCleanProject;
import eu.dfid.dataaccess.dto.parsed.ParsedProject;
import eu.dl.worker.clean.plugin.BaseDateTimePlugin;
import eu.dl.worker.clean.plugin.DatePlugin;
import eu.dl.worker.utils.ArrayUtils;
import eu.dl.worker.clean.utils.DateUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Date cleaning plugin for DFID Projects.
 *
 * @author Michal Riha
 *
 * @param <T> parsedProject
 * @param <U> cleanProject
 */
public class DFIDProjectDatePlugin<T extends ParsedProject, U extends DFIDCleanProject>
        extends BaseDateTimePlugin<DatePlugin, T, U> {
    /**
     * DatePlugin should be initialised with the pattern of the date.
     *
     * @param formatter
     *            formatter used to parse the date/datetime fields
     */
    public DFIDProjectDatePlugin(final DateTimeFormatter formatter) {
        super(formatter);
    }

    /**
     * Plugin constructor with configuration.
     *
     * @param formatters
     *       list of datetime formatters
     */
    public DFIDProjectDatePlugin(final List<DateTimeFormatter> formatters) {
        super(formatters);
    }

    /**
     * Cleans date and date fields.
     *
     * @param parsedProject
     *            project with source data
     * @param cleanProject
     *            project with clean data
     *
     * @return project with cleaned data
     */
    @Override
    public final DFIDCleanProject clean(final ParsedProject parsedProject, final DFIDCleanProject cleanProject) {
        
        parsedProject.setSignatureDates(
                parsedProject.getSignatureDates().stream().filter(Objects::nonNull).map(this::justMapIt).collect(Collectors.toList()));
        
        List<LocalDate> walk = ArrayUtils.walk(parsedProject.getSignatureDates(),
                signatureDate -> DateUtils.cleanDate(signatureDate, formatters));
        cleanProject.setSignatureDates(walk);

        cleanProject.setApprovalDate(DateUtils.cleanDate(parsedProject.getApprovalDate(), formatters));

        cleanProject.setClosingDate(DateUtils.cleanDate(parsedProject.getClosingDate(), formatters));

        cleanProject.setDeactivationDate(DateUtils.cleanDate(parsedProject.getDeactivationDate(), formatters));

        return cleanProject;
    }
    
    /**
     * tries to convert dates likes May/01/01 to May/01/2001 and May/01/89 to May/01/1989
     * 
     * @param dateString the string which has to be analysed and converted
     * @return the enhanced/converted date string or the original if conversion is not possible
     */
    private String justMapIt(String dateString) {
        Pattern y2kPattern = Pattern.compile("\\w{3}[/]\\d\\d[/][0-1]\\d"); // probably 20xx date
        Pattern beforeY2kPattern = Pattern.compile("\\w{3}[/]\\d\\d[/][^0-1]\\d"); // probably 19xx date
        
        Matcher matcher = y2kPattern.matcher(dateString);
        String result = null;
        if (matcher.matches()) {
            result = dateString.substring(0,7) + "20" + dateString.substring(7);
        } else {
            matcher = beforeY2kPattern.matcher(dateString);
            if (matcher.matches()) {
                result = dateString.substring(0,7) + "19" + dateString.substring(7);
            }
        }
        
        return result!=null ? result : dateString;
    }


}
