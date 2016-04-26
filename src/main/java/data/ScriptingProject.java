package data;

import lombok.Getter;
import lombok.Setter;
import xml.XmlReader;

import java.util.ArrayList;




/**
 * Created by Bart.
 * Class to store top-level properties of a scripting project.
 */
public class ScriptingProject {

    // Description of this project
    @Getter
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

    /**
     * Constructor.
     * @param description - the description of the project
     */
    public ScriptingProject(String description) {
        this.description = description;
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
        cameraTimelines.add(cameraTimeline);
    }
}
