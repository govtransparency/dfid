package eu.dfid.worker.clean.utils;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import eu.dfid.dataaccess.dto.clean.DFIDCleanBody;
import eu.dfid.dataaccess.dto.clean.DFIDCleanBodyEvaluation;
import eu.dfid.dataaccess.dto.clean.DFIDCleanPublication;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedBody;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedBodyEvaluation;
import eu.dfid.dataaccess.dto.parsed.DFIDParsedPublication;
import eu.dl.dataaccess.dto.clean.CleanBody;
import eu.dl.dataaccess.dto.generic.Publication;
import eu.dl.dataaccess.dto.parsed.ParsedBody;
import eu.dl.dataaccess.dto.parsed.ParsedPublication;
import eu.dl.worker.clean.utils.BodyUtils;
import eu.dl.worker.clean.utils.PublicationUtils;
import eu.dl.worker.clean.utils.StringUtils;
import eu.dl.worker.utils.ArrayUtils;

/**
 *
 * @author Tomas Mrazek
 */
public final class CleanUtils {
    /**
     * Supress default constructor for noninstantiability.
     */
    private CleanUtils() {
        throw new AssertionError();
    }

    /**
     * Cleans the given list of DFID parsed publication.
     *
     * @param parsedPublications
     *      list of DFID parsed publications
     * @param numberFormats
     *      number formats
     * @param formatters
     *      datetime formatters
     * @param formTypeMapping
     *      source form type mapping
     * @return list of clean DFID publications
     */
    public static List<Publication> cleanPublications(final List<ParsedPublication> parsedPublications,
        final List<NumberFormat> numberFormats, final List<DateTimeFormatter> formatters,
        final Map<Enum, List<String>> formTypeMapping) {

        return ArrayUtils.walk(parsedPublications,
            (parsedPublication) -> {
                Publication cleanPublication = PublicationUtils
                    .cleanPublication(parsedPublication, numberFormats, formatters, formTypeMapping);

                DFIDCleanPublication dfidCleanPublication = new DFIDCleanPublication(cleanPublication);

                //dfid specific fields
                if (parsedPublication instanceof DFIDParsedPublication) {
                    dfidCleanPublication.setNoticeStatus(StringUtils.cleanShortString(
                            ((DFIDParsedPublication) parsedPublication).getNoticeStatus()));
                }

                return dfidCleanPublication;
            });
    }

    /**
     * Cleans the given DFID parsed body.
     *
     * @param parsedBody
     *      DFID parsed body
     * @param countryMapping
     *      country mapping
     * @return clean DFID body
     */
    public static CleanBody cleanBody(final ParsedBody parsedBody, final Map<Enum, List<String>> countryMapping) {
        if (parsedBody == null) {
            return null;
        }

        DFIDCleanBody dfidCleanBody = new DFIDCleanBody(BodyUtils.cleanBody(parsedBody, null, null, countryMapping));

        //DFID BODY SPECIFIC FIELDS
        if (parsedBody instanceof DFIDParsedBody) {
            dfidCleanBody.setBodyEvaluation(cleanBodyEvaluation(((DFIDParsedBody) parsedBody).getBodyEvaluation()));
        }
        
        return dfidCleanBody;
    }

    /**
     * Cleans the given DFID parsed body evaluation.
     *
     * @param parsedEvaluation
     *      DFID parsed body evaluation
     * @return clean DFID body evaluation
     */
    public static DFIDCleanBodyEvaluation cleanBodyEvaluation(final DFIDParsedBodyEvaluation parsedEvaluation) {
        if (parsedEvaluation == null) {
            return null;
        }
        
        return new DFIDCleanBodyEvaluation()
            .setGovernmentPerformance(StringUtils.cleanShortString(parsedEvaluation.getGovernmentPerformance()))
            .setImplementingAgencyPerformance(
                StringUtils.cleanShortString(parsedEvaluation.getImplementingAgencyPerformance()))
            .setOverallPerformance(StringUtils.cleanShortString(parsedEvaluation.getOverallPerformance()))
            .setQualityExAnte(StringUtils.cleanShortString(parsedEvaluation.getQualityExAnte()))
            .setSupervisionQuality(StringUtils.cleanShortString(parsedEvaluation.getSupervisionQuality()));
    }
}
