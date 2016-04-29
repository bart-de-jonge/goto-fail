package data;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import xml.XmlReader;
import xml.XmlWriter;




/**
 * Created by Bart.
 * Class to store top-level properties of a scripting project.
 */
public class ScriptingProject {

    // Description of this project
    @Getter @Setter
    private String description;

    // List of cameras that are available in this project
    @Getter @Setter
    private ArrayList<Camera> cameras;

    // The director timeline of this project
    @Getter @Setter
    private DirectorTimeline directorTimeline;

    // The camera timelines of this project
    @Getter @Setter
    private ArrayList<CameraTimeline> cameraTimelines;

    // The number of seconds per count;
    @Getter @Setter
    private double secondsPerCount;

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
    }

    /**
     * Method to write the current project to a file.
     * @param fileName  - the file to write the project to
     * @return          - boolean indicating if the write was successful
     */
    public boolean write(String fileName) {

        // Write using XML, change if writing with another
        // module is necessary
        XmlWriter writer = new XmlWriter(fileName);
        return writer.writeProject(this);
    }

    /**
     * Method to write the current project to a file.
     * @param fileName  - the file to write the project to
     * @return          - boolean indicating if the write was successful
     */
    public static ScriptingProject read(String fileName) {

        // Read using XML, change if reading with another
        // module is necessary
        XmlReader reader = new XmlReader(fileName);
        return reader.readProject();
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
