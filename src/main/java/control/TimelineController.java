package control;

import data.Camera;
import data.CameraTimeline;
import data.CameraType;
import data.ScriptingProject;
import gui.CameraShotBlock;
import gui.TimelinesGridPane;
import gui.RootPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that controls the timeline.
 * @author alex
 */
public class TimelineController {

    private TimelinesGridPane timelinePane;
    private RootPane rootPane;

    private List<CameraTimeline> cameraTimelines;

    // Placeholder camera type until GUI allows personalized entry
    private final CameraType defType = new CameraType("AW-HE130 HD PTZ", "It's an IP Camera", 0.5);

    // Placeholder project in lieu of XML loading
    private final ScriptingProject scriptingProject = new ScriptingProject("BOSS Project", 1.0);

    /**
     * Constructor.
     * @param timelinePane Timeline view to be controlled.
     * @param rootPane Root Pane.
     */
    public TimelineController(TimelinesGridPane timelinePane, RootPane rootPane) {
        this.timelinePane = timelinePane;
        this.rootPane = rootPane;

        initializeCameraTimelines();
    }

    /**
     * Add a camera shot to the corresponding timeline
     * @param cameraIndex Index of the camera track.
     * @param name Name of the shot.
     * @param description Shot description.
     * @param startCount Start count.
     * @param endCount End count.
     */
    public void addCameraShot(int cameraIndex, String name, String description,
                              int startCount, int endCount) {
        this.cameraTimelines.get(cameraIndex).addShot(name, description, startCount, endCount);
    }

    /**
     * Create camera timelines based on GUI. (Anti-pattern)
     * TODO: Replace this with proper XML based project creation
     */
    private void initializeCameraTimelines() {
        cameraTimelines = new ArrayList<>();

        for (int i = 0; i < this.timelinePane.getNumberOfTimelines(); i++) {
            Camera defCam = new Camera("IP Cam " + i, "", defType);
            CameraTimeline timelineN = new CameraTimeline(defCam, "", scriptingProject);
            cameraTimelines.add(timelineN);
        }
    }
}
