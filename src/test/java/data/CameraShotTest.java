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
    public void initialize() {
        cameraShot = new CameraShot("camerashot-1");
    }

    @Test
    public void getName() throws Exception {
        assertEquals("camerashot-1", cameraShot.getName());
    }

    @Test
    public void getInstance() throws Exception {
        assertEquals(0, cameraShot.getInstance());
    }

    @Test
    public void setName() throws Exception {
        cameraShot.setName("testname");
        assertEquals("testname", cameraShot.getName());
    }

}