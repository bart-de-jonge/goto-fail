package data;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Bart.
 */
public class DirectorShotTest {
    DirectorShot shot;
    @Before
    public void initializeTest() {
        shot = new DirectorShot("directorshot-1", "A test director shot", 1, 2, 0, 0, new ArrayList<>());
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
        DirectorShot newShot = new DirectorShot("name", "description", 1, 2, 0, 0, new ArrayList<>());
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

    @Test
    public void getFrontShotPaddingTest() {
        shot.setFrontShotPadding(5.0);
        assertEquals(5.0, shot.getFrontShotPadding(), 0);
    }
    
    @Test
    public void setFrontShotPaddingTest() {
        shot.setFrontShotPadding(5.0);
        shot.setFrontShotPadding(6.0);
        assertEquals(6.0, shot.getFrontShotPadding(), 0);
    }
    
    
    @Test
    public void getEndShotPaddingTest() {
        shot.setEndShotPadding(5.0);
        assertEquals(5.0, shot.getEndShotPadding(), 0);
    }
    
    @Test
    public void setEndShotPaddingTest() {
        shot.setEndShotPadding(5.0);
        shot.setEndShotPadding(6.0);
        assertEquals(6.0, shot.getEndShotPadding(), 0);
    }
    
    @Test
    public void constructorWithNoArgumentsTest() {
        DirectorShot shot = new DirectorShot();
        assertEquals("", shot.getName());
    }
    
    @Test
    public void toStringTest() {
        assertEquals("DirectorShot(timelineIndices=[], cameraShots=[], frontShotPadding=0.0, endShotPadding=0.0, instance=" + shot.getInstance() + ")", shot.toString());
    }
}