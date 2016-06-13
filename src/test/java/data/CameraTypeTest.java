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
    public void initializeTest() {
        type = new CameraType("type-1", "A test camera type", 2);
    }

    @Test
    public void getNameTest() {
        assertEquals("type-1", type.getName());
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("A test camera type", type.getDescription());
    }

    @Test
    public void getMovementMarginTest() {
        assertEquals(2, type.getMovementMargin(), 0);
    }

    @Test
    public void setNameTest() {
        type.setName("testname");
        assertEquals("testname", type.getName());
    }

    @Test
    public void setDescriptionTest() {
        type.setDescription("testdescription");
        assertEquals("testdescription", type.getDescription());
    }

    @Test
    public void setMovementMarginTest() {
        type.setMovementMargin(100.5);
        assertEquals(100.5, type.getMovementMargin(), 0);
    }

    @Test
    public void equalsTrueTest() {
        CameraType type2 = new CameraType("type-1", "A test camera type", 2);
        assertEquals(type2, type);
    }

    @Test
    public void equalsNameTest() {
        CameraType type2 = new CameraType("type-2", "A test camera type", 2);
        assertNotEquals(type2, type);
    }

    @Test
    public void equalsDescriptionTest() {
        CameraType type2 = new CameraType("type-1", "A test camera type 2", 2);
        assertNotEquals(type2, type);
    }

    @Test
    public void equalsMovementMarginTest() {
        CameraType type2 = new CameraType("type-1", "A test camera type", 3);
        assertNotEquals(type2, type);
    }

    @Test
    public void testHashCodeTest() {
        assertEquals(-245683693, type.hashCode());
    }

    @Test
    public void canEqualTest() {
        CameraType type2 = new CameraType("type-1", "A test camera type", 3);
        assertTrue(type.canEqual(type2));
    }

    @Test
    public void toStringTest() {
        assertEquals("CameraType(name=type-1, description=A test camera type, movementMargin=2.0)", type.toString());
    }

    @Test
    public void defaultConstructor() {
        CameraType myType = new CameraType();
        assertEquals("", myType.getName());
        assertEquals("", myType.getDescription());
        assertEquals(-1, myType.getMovementMargin(), 0);
    }

    @Test
    public void cloneTest() {
        assertEquals(type, type.clone());
    }
}