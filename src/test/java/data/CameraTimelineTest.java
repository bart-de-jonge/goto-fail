package data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bart.
 */
public class CameraTimelineTest {

    CameraTimeline timeline;
    @Before
    public void initialize() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        timeline = new CameraTimeline(camera, "A test cameratimeline", new ScriptingProject("test", "", 1));
    }

    @Test
    public void getCameraTest() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        assertEquals(camera, timeline.getCamera());
    }
    
    @Test
    public void setNameTest() {
        timeline.setName("Hai");
        assertEquals("Hai", timeline.getName());
    }
    
    @Test
    public void constructorWithNoArgumentsTest() {
        CameraTimeline timeline = new CameraTimeline();
        assertEquals(null, timeline.getDescription());
    }
    
    @Test
    public void constructorWithFourArgumentsTest() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        CameraTimeline timeline = new CameraTimeline("Name", camera, "Description", new ScriptingProject());
        assertEquals(camera, timeline.getCamera());
    }
    
    @Test
    public void toStringTest() {
        assertEquals("CameraTimeline(description=A test cameratimeline, name=null, camera=Camera(name=camera-1, description=A test camera, cameraType=CameraType(name=cameratype-1, description=A test cameratype, movementMargin=2.0), movementMargin=2.0), shots=[])", timeline.toString());
    }

    @Test
    public void setCameraTest() {
        CameraType type = new CameraType("cameratype-2", "A test cameratype 2", 3.00);
        Camera camera = new Camera("camera-2", "A test camera 2", type);
        timeline.setCamera(camera);
        assertEquals(camera, timeline.getCamera());
    }

    @Test
    public void addShotTest() {
        timeline.addShot("testname", "", 1, 2);
        assertEquals(1, timeline.getShots().size());
        assertEquals("testname", timeline.getShots().getFirst().getName());
    }

    @Test
    public void addCameraShotTest() {
        CameraShot shot = new CameraShot("testname", "", 1, 2);
        timeline.addShot(shot);
        assertEquals(1, timeline.getShots().size());
        assertEquals(shot, timeline.getShots().get(0));
    }

    @Test
    public void addBeforeCameraShotTest() {
        timeline.addShot("last", "", 2, 4);
        timeline.addShot("first", "", 1, 3);
        assertEquals("first", timeline.getShots().getFirst().getName());
    }

    @Test
    public void addAfterCameraShotTest() {
        timeline.addShot("first", "", 1, 3);
        timeline.addShot("last", "", 2, 4);
        assertEquals("last", timeline.getShots().getLast().getName());
    }

    @Test
    public void addFullOverlapCameraShotTest() {
        timeline.addShot("first", "", 2, 3);
        timeline.addShot("last", "", 1, 4);
        assertEquals("last", timeline.getShots().getFirst().getName());
    }

    @Test
    public void addSameTimeCameraShotTest() {
        timeline.addShot("first", "", 1, 3);
        timeline.addShot("last", "", 1, 3);
        assertEquals("last", timeline.getShots().getFirst().getName());
    }
    
    @Test
    public void removeShotTest() {
        CameraShot shot = new CameraShot("Name", "Description", 0, 0);
        timeline.addShot(shot);
        timeline.removeShot(shot);
        assertTrue(timeline.getShots().isEmpty());
    }
}