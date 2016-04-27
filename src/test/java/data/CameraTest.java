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
    public void initialize() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        camera = new Camera("camera-1", "A test camera", type);
    }

    @Test
    public void resetMovementMargin() throws Exception {
        camera.setMovementMargin(3.00);
        assertEquals(3.00, camera.getMovementMargin(), 0);
        camera.resetMovementMargin();
        assertEquals(-1, camera.getMovementMargin(), 0);
    }

    @Test
    public void getName() throws Exception {
        assertEquals("camera-1", camera.getName());
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals("A test camera", camera.getDescription());
    }

    @Test
    public void getCameraType() throws Exception {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        assertEquals(type, camera.getCameraType());
    }

    @Test
    public void getMovementMargin() throws Exception {
        assertEquals(-1, camera.getMovementMargin(), 0);
    }

    @Test
    public void setName() throws Exception {
        camera.setName("testname");
        assertEquals("testname", camera.getName());
    }

    @Test
    public void setDescription() throws Exception {
        camera.setDescription("testdescription");
        assertEquals("testdescription", camera.getDescription());
    }

    @Test
    public void setCameraType() throws Exception {
        CameraType type = new CameraType("cameratype-2", "A second test cameratype", 3.00);
        camera.setCameraType(type);
        assertEquals(type, camera.getCameraType());
    }

    @Test
    public void setMovementMargin() throws Exception {
        camera.setMovementMargin(3.00);
        assertEquals(3.00, camera.getMovementMargin(), 0);
    }

    @Test
    public void equals() throws Exception {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 3.00);
        Camera camera2 = new Camera("camera-1", "A test camera", type2);
        CameraType type3 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera3 = new Camera("camera-1", "A test camera", type3);
        assertEquals(camera3, camera);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(-2059658920, camera.hashCode());
    }

    @Test
    public void canEqual() throws Exception {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 3.00);
        Camera camera2 = new Camera("camera-1", "A test camera", type2);
        assertTrue(camera.canEqual(camera2));
    }

}