package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Bart.
 */
public class ScriptingProjectTest {
    
    private static final String TEST_PATH = "src/test/java/data/project_write_test.txt";

    ScriptingProject project;

    @Before
    public void setUpTest() {
        project = new ScriptingProject("", "A test scripting project", 2);
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
        CameraTimeline timeline = new CameraTimeline(camera, "A test cameratimeline",
                new ScriptingProject("", "test", 1));
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
        DirectorTimeline timeline = new DirectorTimeline("A test director timeline",
                new ScriptingProject("", "test", 1));
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
        CameraTimeline timeline = new CameraTimeline(camera, "A test cameratimeline",
                new ScriptingProject("", "test", 1));
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
    
    @Test
    public void toStringTest() {
        assertEquals("ScriptingProject(name=, description=A test scripting project, cameras=[], directorTimeline=DirectorTimeline(shots=[]), cameraTimelines=[], secondsPerCount=2.0, filePath=null, changed=true)", project.toString());
    }
    
    @Test
    public void setNameTest() {
        project.setName("Kek");
        assertEquals("Kek", project.getName());
    }
    
    @Test
    public void getFilePathTest() {
        project.setFilePath("path/to/file");
        assertEquals("path/to/file", project.getFilePath());
    }
    
    @Test
    public void setFilePathTest() {
        project.setFilePath("path/to/file");
        project.setFilePath("other/path");
        assertEquals("other/path", project.getFilePath());
    }
    
    @Test
    public void savedTest() {
        project.setChanged(true);
        project.saved();
        assertFalse(project.isChanged());
    }
    
    @Test
    public void changedTest() {
        project.setChanged(false);
        project.changed();
        assertTrue(project.isChanged());
    }
    
    @Test
    public void constructorWithNoArgumentsTest() {
        ScriptingProject project = new ScriptingProject();
        assertEquals("", project.getName());
    }
    
    @Test
    public void constructorWithThreeArgumentsTest() {
        ScriptingProject project = new ScriptingProject("Name", "Description", 0);
        assertEquals("Name", project.getName());
    }
    
    @Test
    public void writeTest() {
        project.setFilePath("project_write_test.txt");
        assertTrue(project.write());
    }
    
    @Test
    public void writeFileTest() {
        assertTrue(project.write(new File(TEST_PATH)));
    }
    
    @Test
    public void writeStringTest() {
        assertTrue(project.write(new String(TEST_PATH)));
    }
    
    @Test
    public void readTest() {
        project.write(new File(TEST_PATH));
        ScriptingProject project = ScriptingProject.read(new File("project_write_test.txt"));
        assertEquals("A test scripting project", project.getDescription());
    }
    
    @Test
    public void readStringTest() {
        project.write(new File(TEST_PATH));
        ScriptingProject project = ScriptingProject.read("project_write_test.txt");
        assertEquals("A test scripting project", project.getDescription());
    }

}