package data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Bart on 13/06/2016.
 */
public class InstrumentTest {
    private Instrument instrument;

    @Before
    public void setup() {
        instrument = new Instrument("testName", "test description");
    }

    @Test
    public void cloneTest() throws Exception {
        assertEquals(instrument, instrument.clone());
    }

    @Test
    public void setName() throws Exception {
        instrument.setName("testName2");
        assertEquals("testName2", instrument.getName());
    }

    @Test
    public void setDescription() throws Exception {
        instrument.setDescription("test description 2");
        assertEquals("test description 2", instrument.getDescription());
    }

    @Test
    public void equals() throws Exception {
        assertTrue(instrument.equals(new Instrument("testName", "test description")));
        assertFalse(instrument.equals(new Instrument("telkjasdf", "alkdf")));
    }

    @Test
    public void hashCodeTest() throws Exception {
        assertEquals(26067734, instrument.hashCode());
    }

    @Test
    public void canEqual() throws Exception {
        assertTrue(instrument.canEqual(new Instrument("alskdfjasdf", "laksdfjaskldjf")));
        assertFalse(instrument.canEqual(new ArrayList<Integer>()));
    }

    @Test
    public void getName() throws Exception {
        assertEquals("testName", instrument.getName());
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals("test description", instrument.getDescription());
    }

    @Test
    public void defaultConstructor() {
        Instrument myInstrument = new Instrument();
        assertEquals("", myInstrument.getName());
        assertEquals("", myInstrument.getDescription());
    }
}