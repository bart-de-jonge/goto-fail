package data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bart.
 */
public class CameraTypeTest {
    CameraType type;
    @Before
    public void initialize() {
        type = new CameraType("type-1", "A test camera type", 2);
    }

    @Test
    public void getName() throws Exception {
        assertEquals("type-1", type.getName());
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals("A test camera type", type.getDescription());
    }

    @Test
    public void getMovementMargin() throws Exception {
        assertEquals(2, type.getMovementMargin(), 0);
    }

    @Test
    public void setName() throws Exception {
        type.setName("testname");
        assertEquals("testname", type.getName());
    }

    @Test
    public void setDescription() throws Exception {
        type.setDescription("testdescription");
        assertEquals("testdescription", type.getDescription());
    }

    @Test
    public void setMovementMargin() throws Exception {
        type.setMovementMargin(100.5);
        assertEquals(100.5, type.getMovementMargin(), 0);
    }

    @Test
    public void equalsTrue() throws Exception {
        CameraType type2 = new CameraType("type-1", "A test camera type", 2);
        assertEquals(type2, type);
    }

    @Test
    public void equalsName() throws Exception {
        CameraType type2 = new CameraType("type-2", "A test camera type", 2);
        assertNotEquals(type2, type);
    }

    @Test
    public void equalsDescription() throws Exception {
        CameraType type2 = new CameraType("type-1", "A test camera type 2", 2);
        assertNotEquals(type2, type);
    }

    @Test
    public void equalsMovementMargin() throws Exception {
        CameraType type2 = new CameraType("type-1", "A test camera type", 3);
        assertNotEquals(type2, type);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(-245683693, type.hashCode());
    }

    @Test
    public void canEqual() throws Exception {
        CameraType type2 = new CameraType("type-1", "A test camera type", 3);
        assertTrue(type.canEqual(type2));
    }

}