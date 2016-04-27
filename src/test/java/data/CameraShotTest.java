package data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bart.
 */
public class CameraShotTest {
    CameraShot cameraShot;
    @Before
    public void initializeTest() {
        cameraShot = new CameraShot("camerashot-1");
    }

    @Test
    public void getNameTest() {
        assertEquals("camerashot-1", cameraShot.getName());
    }

    @Test
    public void getInstanceTest() {
        int oldInstance = cameraShot.getInstance();
        CameraShot newShot = new CameraShot("name");
        assertEquals(oldInstance + 1, newShot.getInstance());
    }

    @Test
    public void setNameTest() {
        cameraShot.setName("testname");
        assertEquals("testname", cameraShot.getName());
    }

}