import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.DirectorShot;
import data.DirectorTimeline;
import data.ScriptingProject;
import xml.XmlWriter;

public class XMLTester {

    public static void main(String[] args) {
        ScriptingProject project = new ScriptingProject("A test project", 2.00);
        XmlWriter writer = new XmlWriter("src/test/java/xml/test_files/test-write.xml");
        DirectorTimeline dt = new DirectorTimeline("A test director timeline", project);
        
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

        
        try {
            System.out.println("-----------------------------------------------------------------");
            System.out.println("CameraType");
            System.out.println();
            JAXBContext context = JAXBContext.newInstance(ScriptingProject.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            
            m.marshal(project, System.out);
           
            
            
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated method stub

    }

}
