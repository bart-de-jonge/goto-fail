package data;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Created by Bart.
 */
public class DirectorShotTest {
    DirectorShot shot;
    @Before
    public void initializeTest() {
        shot = new DirectorShot("directorshot-1", "A test director shot", 1, 2, 0, 0);
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("A test director shot", shot.getDescription());
    }

    @Test
    public void setDescriptionTest() {
        shot.setDescription("testdescription");
        assertEquals("testdescription", shot.getDescription());
    }

    @Test
    public void getNameTest() {
        assertEquals("directorshot-1", shot.getName());
    }

    @Test
    public void getInstanceTest() {
        int oldInstance = shot.getInstance();
        DirectorShot newShot = new DirectorShot("name", "description", 1, 2, 0, 0);
        assertEquals(oldInstance + 1, newShot.getInstance());
    }

    @Test
    public void setNameTest() {
        shot.setName("testname");
        assertEquals("testname", shot.getName());
    }

    @Test
    public void getTimelineIndicesTest() {
        assertEquals(Collections.<Integer>emptySet(), shot.getTimelineIndices());
    }

    @Test
    public void getCameraShotsTest() {
        assertEquals(Collections.<CameraShot>emptySet(), shot.getCameraShots());
    }

    @Test
    public void addCameraTimelineIndexTest() {
        shot.addCameraTimelineIndex(0);
        assertEquals(true, shot.getTimelineIndices().contains(0));
    }

    @Test
    public void addCameraShotTest() {
        CameraShot cameraShot = new CameraShot("Cam shot", "description", 0, 1);
        shot.addCameraShot(cameraShot);
        assertEquals(true, shot.getCameraShots().contains(cameraShot));
    }

    @Test
    public void removeCameraShotTest() {
        CameraShot cameraShot = new CameraShot("Cam shot", "description", 0, 1);
        shot.addCameraTimelineIndex(0);
        shot.addCameraShot(cameraShot);
        shot.removeCameraShot(cameraShot, 0);
        assertEquals(false, shot.getCameraShots().contains(cameraShot));
        assertEquals(false, shot.getTimelineIndices().contains(0));
    }
}