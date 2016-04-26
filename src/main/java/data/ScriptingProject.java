package data;

import lombok.Getter;
import lombok.Setter;

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
