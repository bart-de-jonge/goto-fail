package xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;

import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.DirectorShot;
import data.DirectorTimeline;
import data.ScriptingProject;

/**
 * Created by Bart.
 */
public class XmlWriterTest {
    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @Test
    public void writeProject() throws Exception {
        ScriptingProject project = new ScriptingProject("A test project", 2.00);
        XmlWriter writer = new XmlWriter("src/test/java/xml/test_files/test-write.xml");
        DirectorTimeline dt = new DirectorTimeline("A test director timeline", new ScriptingProject("test", 1));
        DirectorShot directorShot = new DirectorShot("shot-1", "A test director shot", 1, 2);
        int directorShotInstance = directorShot.getInstance();
        dt.addShot(directorShot);
        project.setDirectorTimeline(dt);
        
        CameraTimeline ct1 = new CameraTimeline(new Camera("Some camera", "some camera description", new CameraType("a", "b", 4)), "Test camera line 1", new ScriptingProject("test", 1));
        CameraShot cs1 = new CameraShot("shot-2", "A camera shot", 1, 2);
        ct1.getShots().add(cs1);
        
        CameraTimeline ct2 = new CameraTimeline(new Camera("Another camera", "another description", new CameraType("c", "d", 5)), "Test camera line 2", new ScriptingProject("test", 1));
        CameraShot cs2 = new CameraShot("shot-3", "Another camera shot", 1, 2);
        ct2.getShots().add(cs2);
        int cameraShotInstance1 = cs1.getInstance();
        int cameraShotInstance2 = cs2.getInstance();
        
        ArrayList<CameraTimeline> timelines = new ArrayList<CameraTimeline>();
        timelines.add(ct1);
        timelines.add(ct2);
        project.setCameraTimelines(timelines);
        
        
        
        Camera c = new Camera("Camera", "Camera description", new CameraType("x", "y", 4));
        ArrayList<Camera> cameras = new ArrayList<Camera>();
        cameras.add(c);
        
        project.setCameras(cameras);

        writer.writeProject(project);
        

        String file = readFile("src/test/java/xml/test_files/test-write.xml", StandardCharsets.UTF_8);

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><scripting-project description=\"A test project\" seconds-per-count=\"2.0\"><director-timeline description=\"A test director timeline\"><director-shots number-of-shots=\"1\"><director-shot description=\"A test director shot\" instance=\"" + directorShotInstance + "\" name=\"shot-1\"/></director-shots></director-timeline><camera-timelines><camera-timeline description=\"Test camera line 1\"><camera description=\"some camera description\" movement-margin=\"4.0\" name=\"Some camera\"><camera-type description=\"b\" movement-margin=\"4.0\" name=\"a\"/></camera><camera-shots number-of-shots=\"1\"><camera-shot description=\"A camera shot\" instance=\"" + cameraShotInstance1 + "\" name=\"shot-2\"/></camera-shots></camera-timeline><camera-timeline description=\"Test camera line 2\"><camera description=\"another description\" movement-margin=\"5.0\" name=\"Another camera\"><camera-type description=\"d\" movement-margin=\"5.0\" name=\"c\"/></camera><camera-shots number-of-shots=\"1\"><camera-shot description=\"Another camera shot\" instance=\"" + cameraShotInstance2 + "\" name=\"shot-3\"/></camera-shots></camera-timeline></camera-timelines><cameras><camera description=\"Camera description\" movement-margin=\"4.0\" name=\"Camera\"><camera-type description=\"y\" movement-margin=\"4.0\" name=\"x\"/></camera></cameras></scripting-project>", file);

    }

    @Test
    public void getFileName() throws Exception {
        XmlWriter writer = new XmlWriter("src/test/java/xml/test_files/test-write.xml");
        assertEquals("src/test/java/xml/test_files/test-write.xml", writer.getFileName());
    }

    @Test
    public void setFileName() throws Exception {
        XmlWriter writer = new XmlWriter("src/test/java/xml/test_files/test-write.xml");
        writer.setFileName("new=file-name");
        assertEquals("new=file-name", writer.getFileName());
    }

}