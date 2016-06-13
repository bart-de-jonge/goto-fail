package data;

import control.ProjectController;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Class to store top-level properties of a scripting project.
 */
@XmlRootElement(name = "scriptingProject")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@Log4j2
public class ScriptingProject {
    
    @Getter @Setter
    private ArrayList<CameraType> cameraTypes;
    
    // Name of this project
    @Getter @Setter
    private String name;

    // Description of this project
    @Getter @Setter
    private String description;

    // List of cameras that are available in this project
    @Getter @Setter
    @XmlElementWrapper(name = "cameraList")
    @XmlElement(name = "camera")
    private ArrayList<Camera> cameras;
    
    // List of instruments that are available in this project
    @Getter @Setter
    @XmlElementWrapper(name = "instrumentList")
    @XmlElement(name = "instrument")
    private ArrayList<Instrument> instruments;

    // The director timeline of this project
    @Getter @Setter
    private DirectorTimeline directorTimeline;

    // The camera centerarea of this project
    @Getter @Setter
    @XmlElementWrapper(name = "camera-centerarea")
    @XmlElement(name = "cameraTimeline")
    private ArrayList<CameraTimeline> cameraTimelines;
    
    @Getter @Setter
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private ArrayList<User> users;

    // The number of seconds per count;
    @Getter @Setter
    private double secondsPerCount;
    
    @Getter @Setter
    private String filePath;
    
    @Getter @Setter
    private boolean changed;
    
    /**
     * Default constructor.
     */
    public ScriptingProject() {
        this("", "", 0);
    }

    /**
     * Constructor.
     * @param name the name of the project
     * @param description - the description of the project
     * @param secondsPerCount - the number of seconds each count takes
     */
    public ScriptingProject(String name, String description, double secondsPerCount) {
        log.debug("Initializing new ScriptingProject(description={}, secondsPerCount={})",
            description, secondsPerCount);

        this.name = name;
        this.description = description;
        this.secondsPerCount = secondsPerCount;
        this.cameras = new ArrayList<Camera>();
        this.cameraTimelines = new ArrayList<CameraTimeline>();
        this.directorTimeline = new DirectorTimeline(description, this);
        this.cameraTypes = new ArrayList<CameraType>();
        this.users = new ArrayList<User>();
        this.instruments = new ArrayList<Instrument>();
        this.changed = true;
    }
    
    /**
     * Add an instrument to this project.
     * @param instrument the instrument to add
     */
    public void addInstrument(Instrument instrument) {
        this.instruments.add(instrument);
    }
    
    
    /**
     * Add a user.
     * @param user the user to add
     */
    public void addUser(User user) {
        this.users.add(user);
    }
    
    
    /**
     * Project changed -> set changed variable to true.
     */
    public void changed() {
        this.changed = true;
    }
    
    /**
     * Changes saved -> reset changed variable.
     */
    public void saved() {
        this.changed = false;
    }
    
    @Setter @Getter
    private static int res = Integer.MIN_VALUE;
    
    /**
     * Get the maximum instance used so far.
     * @return the maximum instance used so far
     */
    public int getMaxInstance() {
        setRes(Integer.MIN_VALUE);
        directorTimeline.getShots().forEach(shot -> {
                if (shot.getInstance() > getRes()) {
                    setRes(shot.getInstance());
                }
            });
        for (int i = 0;i < cameraTimelines.size();i++) {
            CameraTimeline timeline = cameraTimelines.get(i);
            for (int j = 0;j < timeline.getShots().size();j++) {
                if (timeline.getShots().get(j).getInstance() > getRes()) {
                    setRes(timeline.getShots().get(j).getInstance());
                }
            }
        }
        return getRes();
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
    
    public boolean write() {
        return write(new File(filePath));
    }
    
    /**
     * Write the current project to file.
     * @param file the file to write to
     * @return true if write succeeded, false otherwise
     */
    public boolean write(File file) {
        log.info("Writing ScriptingProject to file {}", file.getAbsolutePath());
        try {
            JAXBContext context = JAXBContext.newInstance(ScriptingProject.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(this, file);
            saved();
            return true;
        } catch (JAXBException e) {
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
        log.info("Reading ScriptingProject from file {}", file.getAbsolutePath());
        try {
            JAXBContext context = JAXBContext.newInstance(ScriptingProject.class);
            Unmarshaller um = context.createUnmarshaller();
            ScriptingProject result = (ScriptingProject) um.unmarshal(file);
            result.getDirectorTimeline().setProject(result);
            result.getCameraTimelines().forEach(e -> e.setProject(result));
            result.setFilePath(file.getAbsolutePath());
            result.saved();
            return result;
        } catch (JAXBException e) {
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
     * Get a distinct list of camera types.
     * @return a list of camera types.
     */
    public Set<CameraType> getDistinctCameraTypes() {
        Set<CameraType> result = new HashSet<CameraType>();
        for (Camera c: this.cameras) {
            result.add(c.getCameraType());
        }
        
        return result;
    }
    
    /**
     * Removes the offsetted camera blocks from this project
     Needed because of loading functionality. 
     */
    public void removeOffsettedCameraBlocks() {
        for (CameraTimeline timeline : cameraTimelines) {
            Iterator<CameraShot> iterator = timeline.getShots().iterator();
            while (iterator.hasNext()) {
                CameraShot shot = iterator.next();
                if (shot.getBeginCount() == ProjectController.UNUSED_BLOCK_OFFSET) {
                    iterator.remove();
                }
            }
        }
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
