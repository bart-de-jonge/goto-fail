package data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bart.
 */
public class CameraTest {
    Camera camera;
    @Before
    public void initializeTest() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        camera = new Camera("camera-1", "A test camera", type);
        camera.setMovementMargin(-0.5);
    }

    @Test
    public void resetMovementMarginTest() {
        camera.setMovementMargin(3.00);
        assertEquals(3.00, camera.getMovementMargin(), 0);
        camera.resetMovementMargin();
        assertEquals(2.0, camera.getMovementMargin(), 0);
    }

    @Test
    public void getNameTest() {
        assertEquals("camera-1", camera.getName());
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("A test camera", camera.getDescription());
    }

    @Test
    public void getCameraTypeTest() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        assertEquals(type, camera.getCameraType());
    }

    @Test
    public void getMovementMarginTest() {
        camera.setMovementMargin(5.5);
        assertEquals(5.5, camera.getMovementMargin(), 0);
    }

    @Test
    public void getDefaultMovementMarginTest() {
        assertEquals(2.00, camera.getMovementMargin(), 0);
    }

    @Test
    public void setNameTest() {
        camera.setName("testname");
        assertEquals("testname", camera.getName());
    }

    @Test
    public void setDescriptionTest() {
        camera.setDescription("testdescription");
        assertEquals("testdescription", camera.getDescription());
    }

    @Test
    public void setCameraTypeTest() {
        CameraType type = new CameraType("cameratype-2", "A second test cameratype", 3.00);
        camera.setCameraType(type);
        assertEquals(type, camera.getCameraType());
    }

    @Test
    public void setMovementMarginTest() {
        camera.setMovementMargin(3.00);
        assertEquals(3.00, camera.getMovementMargin(), 0);
    }

    @Test
    public void equalsTest() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 3.00);
        Camera camera2 = new Camera("camera-1", "A test camera", type2);
        CameraType type3 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera3 = new Camera("camera-1", "A test camera", type3);
        assertEquals(camera3, camera);
    }

    @Test
    public void testHashCodeTest() {
        assertEquals(88873304, camera.hashCode());
    }

    @Test
    public void canEqualTest() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 3.00);
        Camera camera2 = new Camera("camera-1", "A test camera", type2);
        assertTrue(camera.canEqual(camera2));
    }

}