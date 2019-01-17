package eu.dfid.worker.parser;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for ParserUtils class.
 * 
 * @author Tomas Mrazek
 */
public final class ParserUtilsTest {

    /**
     * Tests for multiply function from ParserUtils class.
     */
    @Test
    public void multiplyTest() {
        int multiplier = 100;

        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.US);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');
        NumberFormat numberFormat = new DecimalFormat("#,##0.###", formatSymbols);

        Assert.assertTrue(ParserUtils.multiply("1.0", multiplier, numberFormat).equals("100.0"));
        Assert.assertTrue(ParserUtils.multiply("10000 EUR", multiplier, numberFormat).equals("1000000.0"));
        Assert.assertTrue(ParserUtils.multiply("100000", multiplier, numberFormat).equals("1.0E7"));
        Assert.assertTrue(ParserUtils.multiply("1", multiplier, numberFormat).equals("100.0"));
        Assert.assertTrue(ParserUtils.multiply("1.000", multiplier, numberFormat).equals("100.0"));
        Assert.assertTrue(ParserUtils.multiply("0.1", multiplier, numberFormat).equals("10.0"));
        Assert.assertTrue(ParserUtils.multiply("0.123", multiplier, numberFormat).equals("12.3"));
        Assert.assertTrue(ParserUtils.multiply("US$ 1", multiplier, numberFormat).equals("100.0"));
        Assert.assertTrue(ParserUtils.multiply("abc", multiplier, numberFormat).equals("abc"));
        Assert.assertNull(ParserUtils.multiply(null, multiplier, numberFormat));
    }
}
