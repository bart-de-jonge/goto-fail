package data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author martijn
 */
public class ShotTest {
    Shot shot1;
    Shot shot2;
    Shot shot3;

    @Before
    public void initialize() {
        shot1 = new CameraShot("s1", "d", 1, 3);
        shot2 = new CameraShot("s2", "d", 2, 4);
        shot3 = new CameraShot("s3", "d", 2, 3);
    }

    @Test
    public void getNameTest() {
        assertEquals("s1", shot1.getName());
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("d", shot1.getDescription());
    }

    @Test
    public void setInstanceTest() {
        int instance = shot3.getInstance();
        CameraShot shot = new CameraShot("test", "d", 1 ,2);
        assertEquals(instance + 1, shot.getInstance());
    }

    @Test
    public void getStartCountTest() {
        assertEquals(1, shot1.getStartCount(), 0);
    }

    @Test
    public void getEndCountTest() {
        assertEquals(3, shot1.getEndCount(), 0);
    }

    @Test
    public void smallerCompareTest() {
        assertEquals(-1, shot1.compareTo(shot2));
    }

    @Test
    public void biggerCompareTest() {
        assertEquals(1, shot2.compareTo(shot1));
    }

    @Test
    public void equalCompareTest() {
        shot3.setStartCount(1);
        assertEquals(0, shot1.compareTo(shot3));
    }

    @Test
    public void fullOverlapCompareTest() {
        shot1.setEndCount(4);
        assertEquals(-1, shot1.compareTo(shot3));
    }

    @Test
    public void oneSideOverlapTest() {
        assertTrue(shot1.areOverlapping(shot2));
        assertTrue(shot2.areOverlapping(shot1));
    }

    @Test
    public void fullOverlapTest() {
        shot2.setStartCount(0);
        assertTrue(shot1.areOverlapping(shot2));
        assertTrue(shot2.areOverlapping(shot1));
        assertTrue(shot1.areOverlapping(shot1));
    }

    @Test
    public void noOverlapTest() {
        shot2.setStartCount(3);
        assertFalse(shot1.areOverlapping(shot2));
        assertFalse(shot2.areOverlapping(shot1));
    }
}
