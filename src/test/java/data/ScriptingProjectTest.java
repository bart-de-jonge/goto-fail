package data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Bart.
 */
public class ScriptingProjectTest {

    ScriptingProject project;

    @Before
    public void setUp() throws Exception {
        project = new ScriptingProject("A test scripting project", 2);
    }

    @Test
    public void secondsToCounts() throws Exception {
        assertEquals(2, project.secondsToCounts(4), 0);
    }

    @Test
    public void countsToSeconds() throws Exception {
        assertEquals(4, project.countsToSeconds(2), 0);
    }

    @Test
    public void addCamera() throws Exception {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        project.addCamera(camera);
        assertEquals(1, project.getCameras().size());
        assertEquals(camera, project.getCameras().get(0));
    }

    @Test
    public void addAndGetCameraTimeline() throws Exception {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        CameraTimeline timeline = new CameraTimeline(camera, "A test cameratimeline");
        project.addCameraTimeline(timeline);
        assertEquals(1, project.getCameraTimelines().size());
        assertEquals(timeline, project.getCameraTimelines().get(0));
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals("A test scripting project", project.getDescription());
    }

    @Test
    public void getCameras() throws Exception {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        project.addCamera(camera);
        ArrayList<Camera> cameras = new ArrayList<Camera>();
        cameras.add(camera);
        assertEquals(cameras, project.getCameras());
    }

    @Test
    public void getAndSetDirectorTimeline() throws Exception {
        DirectorTimeline timeline = new DirectorTimeline("A test director timeline");
        project.setDirectorTimeline(timeline);
        assertEquals(timeline, project.getDirectorTimeline());
    }

    @Test
    public void getSecondsPerCount() throws Exception {
        assertEquals(2, project.getSecondsPerCount(), 0);
    }

    @Test
    public void setDescription() throws Exception {
        project.setDescription("testdescription");
        assertEquals("testdescription", project.getDescription());
    }

    @Test
    public void setCameras() throws Exception {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        ArrayList<Camera> cameras = new ArrayList<Camera>();
        cameras.add(camera);
        project.setCameras(cameras);
        assertEquals(cameras, project.getCameras());
    }

    @Test
    public void setCameraTimelines() throws Exception {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        CameraTimeline timeline = new CameraTimeline(camera, "A test cameratimeline");
        ArrayList<CameraTimeline> timelines = new ArrayList<CameraTimeline>();
        timelines.add(timeline);
        project.setCameraTimelines(timelines);
        assertEquals(timelines, project.getCameraTimelines());
    }

    @Test
    public void setSecondsPerCount() throws Exception {
        project.setSecondsPerCount(4.5);
        assertEquals(4.5, project.getSecondsPerCount(), 0);
    }

}