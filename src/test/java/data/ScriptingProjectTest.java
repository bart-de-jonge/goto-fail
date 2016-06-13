package data;

import control.ProjectController;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

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
    public void removeOffsettedCameraBlocks() {
        CameraType type = new CameraType("name", "descriptoin", 3);
        Camera camera = new Camera("name", "description", type);
        CameraTimeline timeline = new CameraTimeline(camera, project);
        timeline.addShot(new CameraShot("name", "description", ProjectController.UNUSED_BLOCK_OFFSET, ProjectController.UNUSED_BLOCK_OFFSET + 1));
        project.addCameraTimeline(timeline);
        project.removeOffsettedCameraBlocks();
        assertTrue(timeline.getShots().isEmpty());
    }

    @Test
    public void getDistinctCameraTypes() {
        CameraType type = new CameraType("name", "descriptoin", 3);
        Camera camera = new Camera("name", "description", type);
        CameraTimeline timeline = new CameraTimeline(camera, project);
        project.addCameraTimeline(timeline);
        project.addCamera(camera);
        assertEquals(1, project.getDistinctCameraTypes().size());
        assertTrue(project.getDistinctCameraTypes().contains(type));
    }

    @Test
    public void addUser() {
        User user = new User();
        project.addUser(user);
        assertEquals(1, project.getUsers().size());
        assertEquals(user, project.getUsers().get(0));
    }

    @Test
    public void addInstrument() {
        Instrument instrument = new Instrument();
        project.addInstrument(instrument);
        assertEquals(1, project.getInstruments().size());
        assertEquals(instrument, project.getInstruments().get(0));
    }

    @Test
    public void setRes() {
        ScriptingProject.setRes(5);
        assertEquals(5, ScriptingProject.getRes());
    }

    @Test
    public void getMaxInstance() {
        CameraType type = new CameraType("name", "descriptoin", 3);
        Camera camera = new Camera("name", "description", type);
        CameraTimeline timeline = new CameraTimeline(camera, project);
        project.addCameraTimeline(timeline);
        project.getDirectorTimeline().addShot(new DirectorShot());
        timeline.addShot(new CameraShot("name", "description", 1, 2));
        timeline.addShot(new CameraShot("name", "description", 1, 2));
        assertEquals(2, project.getMaxInstance());
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
        CameraTimeline timeline = new CameraTimeline(camera,
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
        CameraTimeline timeline = new CameraTimeline(camera,
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
        assertEquals("ScriptingProject(cameraTypes=[], name=, description=A test scripting project, cameras=[], instruments=[], directorTimeline=DirectorTimeline(shots=[], description=A test scripting project), cameraTimelines=[], users=[], secondsPerCount=2.0, filePath=null, changed=true)", project.toString());
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