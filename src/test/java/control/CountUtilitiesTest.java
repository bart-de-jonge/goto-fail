package control;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by alexandergeenen on 12/05/16.
 */
public class CountUtilitiesTest {
    @Test
    public void formatDoubleTest() {
        double toFormat = 3.14159265;
        String formatted = CountUtilities.formatDouble(toFormat);
        assertEquals("3.14159265", formatted);
    }

    @Test
    public void emptyParseCountNumberTest() {
        String parsed = CountUtilities.parseCountNumber("");
        assertEquals("0", parsed);
    }

    @Test
    public void roundParseCountNumberTest() {
        String toFormat = "3.14159265";
        String parsed = CountUtilities.parseCountNumber(toFormat);
        assertEquals("3.25", parsed);
    }
}
