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
    public void setUpTest() {
        project = new ScriptingProject("A test scripting project", 2);
    }

    @Test
    public void secondsToCountsTest() {
        assertEquals(2, project.secondsToCounts(4), 0);
    }

    @Test
    public void countsToSecondsTest() {
        assertEquals(4, project.countsToSeconds(2), 0);
    }

    @Test
    public void addCameraTest() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        project.addCamera(camera);
        assertEquals(1, project.getCameras().size());
        assertEquals(camera, project.getCameras().get(0));
    }

    @Test
    public void addAndGetCameraTimelineTest() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        CameraTimeline timeline = new CameraTimeline(camera, "A test cameratimeline");
        project.addCameraTimeline(timeline);
        assertEquals(1, project.getCameraTimelines().size());
        assertEquals(timeline, project.getCameraTimelines().get(0));
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("A test scripting project", project.getDescription());
    }

    @Test
    public void getCamerasTest() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        project.addCamera(camera);
        ArrayList<Camera> cameras = new ArrayList<Camera>();
        cameras.add(camera);
        assertEquals(cameras, project.getCameras());
    }

    @Test
    public void getAndSetDirectorTimelineTest() {
        DirectorTimeline timeline = new DirectorTimeline("A test director timeline");
        project.setDirectorTimeline(timeline);
        assertEquals(timeline, project.getDirectorTimeline());
    }

    @Test
    public void getSecondsPerCountTest() {
        assertEquals(2, project.getSecondsPerCount(), 0);
    }

    @Test
    public void setDescriptionTest() {
        project.setDescription("testdescription");
        assertEquals("testdescription", project.getDescription());
    }

    @Test
    public void setCamerasTest() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        ArrayList<Camera> cameras = new ArrayList<Camera>();
        cameras.add(camera);
        project.setCameras(cameras);
        assertEquals(cameras, project.getCameras());
    }

    @Test
    public void setCameraTimelinesTest() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        CameraTimeline timeline = new CameraTimeline(camera, "A test cameratimeline");
        ArrayList<CameraTimeline> timelines = new ArrayList<CameraTimeline>();
        timelines.add(timeline);
        project.setCameraTimelines(timelines);
        assertEquals(timelines, project.getCameraTimelines());
    }

    @Test
    public void setSecondsPerCountTest() {
        project.setSecondsPerCount(4.5);
        assertEquals(4.5, project.getSecondsPerCount(), 0);
    }

}