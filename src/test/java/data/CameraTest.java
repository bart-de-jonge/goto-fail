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
    }

    @Test
    public void testClone() {
        assertEquals(camera, camera.clone());
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
    public void ConstructorWithNoArgumentsTest() {
        Camera camera = new Camera();
        assertEquals("", camera.getName());
    }
    
    @Test
    public void toStringTest() {
        camera.setInstance(15);
        assertEquals("Camera(name=camera-1, description=A test camera, cameraType=CameraType(name=cameratype-1, description=A test cameratype, movementMargin=2.0), ip=, movementMargin=2.0, instance=15)", camera.toString());
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
    public void equalsTestEquals() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera2 = new Camera("camera-1", "A test camera", type2);
        camera2.setInstance(camera.getInstance());
        assertEquals(camera2, camera);
    }

    @Test
    public void equalsTestMovementMargin() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 3.00);
        Camera camera2 = new Camera("camera-1", "A test camera", type2);
        camera2.setInstance(camera.getInstance());

        assertNotEquals(camera2, camera);
    }

    @Test
    public void equalsTestSameObject() {
        assertEquals(camera, camera);
    }

    @Test
    public void equalsTestSameNull() {
        assertNotEquals(camera, null);
    }

    @Test
    public void equalsTestOtherClass() {
        assertNotEquals(camera, "dd");
    }

    @Test
    public void equalsTestOtherInstance() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera2 = new Camera("camera-1", "A test camera", type2);

        assertNotEquals(camera2, camera);
    }

    @Test
    public void equalsTestOtherName() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera2 = new Camera("camera-123", "A test camera", type2);
        camera2.setInstance(camera.getInstance());

        assertNotEquals(camera2, camera);
    }

    @Test
    public void equalsTestNameNull() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera2 = new Camera(null, "A test camera", type2);
        camera2.setInstance(camera.getInstance());

        assertNotEquals(camera2, camera);
    }

    @Test
    public void equalsTestNameNullBoth() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera2 = new Camera(null, "A test camera", type2);
        camera2.setInstance(camera.getInstance());
        camera.setName(null);

        assertEquals(camera2, camera);
    }

    @Test
    public void equalsTestOtherDescription() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera2 = new Camera("camera-1", "dsfA test camera", type2);
        camera2.setInstance(camera.getInstance());

        assertNotEquals(camera2, camera);
    }

    @Test
    public void equalsTestDescriptionNull() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera2 = new Camera("camera-1", null, type2);
        camera2.setInstance(camera.getInstance());

        assertNotEquals(camera2, camera);
    }

    @Test
    public void equalsTestDescriptionNullBoth() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera2 = new Camera("camera-1", null, type2);
        camera2.setInstance(camera.getInstance());
        camera.setDescription(null);

        assertEquals(camera2, camera);
    }

    @Test
    public void equalsTestOtherCameratype() {
        CameraType type2 = new CameraType("camerdatype-1", "A test cadmeratype", 2.00);
        Camera camera2 = new Camera("camera-1", "A test camera", type2);
        camera2.setInstance(camera.getInstance());

        assertNotEquals(camera2, camera);
    }

    @Test
    public void equalsTestCameratypeNull() {
        Camera camera2 = new Camera("camera-1", "A test camera", null);
        camera2.setMovementMargin(2.00);
        camera2.setInstance(camera.getInstance());

        assertNotEquals(camera2, camera);
    }

    @Test
    public void equalsTestCameratypeNullBoth() {
        Camera camera2 = new Camera("camera-1", "A test camera", null);
        camera2.setMovementMargin(2.00);
        camera.setMovementMargin(2.00);
        camera2.setInstance(camera.getInstance());
        camera.setCameraType(null);

        assertEquals(camera2, camera);
    }

    @Test
    public void equalsTestOtherIp() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera2 = new Camera("camera-1", "A test camera", type2);
        camera2.setInstance(camera.getInstance());
        camera2.setIp("eenip");

        assertNotEquals(camera2, camera);
    }

    @Test
    public void equalsTestIpNull() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera2 = new Camera("camera-1", "A test camera", type2);
        camera2.setInstance(camera.getInstance());
        camera2.setIp(null);

        assertNotEquals(camera2, camera);
    }

    @Test
    public void equalsTestIpNullBoth() {
        CameraType type2 = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera2 = new Camera("camera-1", "A test camera", type2);
        camera2.setIp(null);
        camera2.setInstance(camera.getInstance());
        camera.setIp(null);

        assertEquals(camera2, camera);
    }
    
    @Test
    public void hashCodeTestCameraTypeNull() {
        camera.setIp("someIP");
        camera.setCameraType(null);
        assertEquals(-446215953, camera.hashCode());
    }
    
    @Test
    public void hashCodeTestDescriptionNull() {
        camera.setIp("someIP");
        camera.setDescription(null);
        assertEquals(1225669491, camera.hashCode());
    }
    
    @Test
    public void hashCodeTestIpNull() {
        camera.setIp(null);
        assertEquals(1145154436, camera.hashCode());
    }
    
    @Test
    public void hashCodeTestNameNull() {
        camera.setIp("someIP");
        camera.setName(null);
        assertEquals(-1495740842, camera.hashCode());
    }
    
    @Test
    public void hashCodeTestNothingNull() {
        camera.setIp("someIP");
        assertEquals(-1645321761, camera.hashCode());
    }
}