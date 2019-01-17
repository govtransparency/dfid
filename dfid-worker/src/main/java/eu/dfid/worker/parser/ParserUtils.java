package eu.dfid.worker.parser;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class provides functions for parsing.
 *
 * @author Tomas Mrazek
 */
public final class ParserUtils {

    /**
     * Suppress default constructor for noninstantiability.
     */
    private ParserUtils() {
        throw new AssertionError();
    }

    /**
     * Attempts to find substring in the {@code input} that matches a regular expresion "[0-9\.,]". If such substring
     * exists tries to parse a number with the given {@code format} and multiply this one with {@code multiplier}. In
     * other cases returns original value of the {@code input}.
     *
     * @param input
     *      number string
     * @param multiplier
     *      multiplier
     * @param format
     *      number format
     * @return number as string, otherwise original input
     */
    public static String multiply(final String input, final double multiplier, final NumberFormat format) {
        if (input == null) {
            return null;
        }

        Pattern p = Pattern.compile("[0-9\\.,]+");
        Matcher m = p.matcher(input);

        if (m.find()) {
            try {
                double amount = format.parse(m.group()).doubleValue() * multiplier;
                return String.valueOf(amount);
            } catch (ParseException e) {
                return input;
            }
        }

        return input;
    }
}
