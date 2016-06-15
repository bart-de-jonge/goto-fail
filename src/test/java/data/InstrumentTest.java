package data;

import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void testEqualsNull() {
        assertNotEquals(this.instrument, null);
    }

    @Test
    public void testEqualsOtherClass() {
        assertNotEquals(this.instrument, "");
    }

    @Test
    public void testEqualsName() {
        Instrument instrument = new Instrument("testNameasdf", "test description");
        assertNotEquals(instrument, this.instrument);
    }

    @Test
    public void testEqualsNameNull() {
        Instrument instrument = new Instrument(null, "test description");
        assertNotEquals(instrument, this.instrument);
    }

    @Test
    public void testEqualsNameBotNull() {
        Instrument instrument = new Instrument(null, "test description");
        this.instrument.setName(null);
        assertEquals(instrument, this.instrument);
    }

    @Test
    public void testEqualsDescription() {
        Instrument instrument = new Instrument("testName", "test descasdfasdfription");
        assertNotEquals(instrument, this.instrument);
    }

    @Test
    public void testEqualsDescriptionNull() {
        Instrument instrument = new Instrument("testName", null);
        assertNotEquals(instrument, this.instrument);
    }

    @Test
    public void testEqualsDescriptionBotNull() {
        Instrument instrument = new Instrument("testName", null);
        this.instrument.setDescription(null);
        assertEquals(instrument, this.instrument);
    }
}