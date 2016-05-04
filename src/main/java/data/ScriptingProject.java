package data;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xml.XmlReader;
import xml.XmlWriter;




/**
 * Created by Bart.
 * Class to store top-level properties of a scripting project.
 */
@XmlRootElement(name = "scriptingProject")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
public class ScriptingProject {

    // Description of this project
    @Getter @Setter
    private String description;

    // List of cameras that are available in this project
    @Getter @Setter
    @XmlElementWrapper(name = "cameraList")
    @XmlElement(name = "camera")
    private ArrayList<Camera> cameras;

    // The director timeline of this project
    @Getter @Setter
    private DirectorTimeline directorTimeline;

    // The camera centerarea of this project
    @Getter @Setter
    @XmlElementWrapper(name = "camera-centerarea")
    @XmlElement(name = "cameraTimeline")
    private ArrayList<CameraTimeline> cameraTimelines;

    // The number of seconds per count;
    @Getter @Setter
    private double secondsPerCount;
    
    /**
     * Default constructor.
     */
    public ScriptingProject() {
        description = "";
        secondsPerCount = 0;
        cameras = null;
        cameraTimelines = null;
        directorTimeline = new DirectorTimeline(description, this);
    }

    /**
     * Constructor.
     * @param description - the description of the project
     * @param secondsPerCount - the number of seconds each count takes
     */
    public ScriptingProject(String description, double secondsPerCount) {
        this.description = description;
        this.secondsPerCount = secondsPerCount;
        this.cameras = new ArrayList<Camera>();
        this.cameraTimelines = new ArrayList<CameraTimeline>();
        this.directorTimeline = new DirectorTimeline(description, this);
    }

    /**
     * Method to write the current project to a file.
     * @param fileName  - the file to write the project to
     * @return          - boolean indicating if the write was successful
     */
    public boolean write(String fileName) {
        File file = new File(fileName);
        return write(file);
    }
    
    /**
     * Write the current project to file.
     * @param file the file to write to
     * @return true if write succeeded, false otherwise
     */
    public boolean write(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(ScriptingProject.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(this, file);
            return true;
        } catch (JAXBException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to write the current project to a file.
     * @param fileName  - the file to write the project to
     * @return          - boolean indicating if the write was successful
     */
    public static ScriptingProject read(String fileName) {
        File file = new File(fileName);
        return read(file);
        
    }
    
    /**
     * Read a project from file.
     * @param file the file to read from
     * @return null if read failed, the read project otherwise
     */
    public static ScriptingProject read(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(ScriptingProject.class);
            Unmarshaller um = context.createUnmarshaller();
            return (ScriptingProject) um.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Compute the number of counts from the number of seconds.
     * @param seconds - the number of seconds to compute with
     * @return - the computed number of counts
     */
    public double secondsToCounts(double seconds) {
        return seconds / secondsPerCount;
    }

    /**
     * Compute the number of seconds from the number of counts.
     * @param counts - the number of counts
     * @return - the computed number of seconds
     */
    public double countsToSeconds(double counts) {
        return counts * secondsPerCount;
    }

    /**
     * Add a camera to the project.
     * @param camera - the camera to add
     */
    public void addCamera(Camera camera) {
        cameras.add(camera);
    }

    /**
     * Add a camera timeline to the project.
     * @param cameraTimeline - the cameraTimeline to add
     */
    public void addCameraTimeline(CameraTimeline cameraTimeline) {
        cameraTimeline.setProject(this);
        cameraTimelines.add(cameraTimeline);
    }
}
