package control;

import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.ScriptingProject;
import gui.CameraShotBlock;
import gui.CameraShotBlockUpdatedEvent;
import gui.RootPane;
import lombok.Getter;

/**
 * Class that controls the timeline.
 * @author alex
 */
public class TimelineController {

    private RootPane rootPane;

    // TODO: replace number of timelines with xml data
    private final int numTimelines = 8;

    // Placeholder camera type until GUI allows personalized entry
    // TODO: Replace Camera Type and Scripting Project when XML functionality is available
    private final CameraType defType = new CameraType("AW-HE130 HD PTZ", "It's an IP Camera", 0.5);

    @Getter
    private ControllerManager manager;

    @Getter
    private ScriptingProject project;

    /**
     * Constructor.
     * @param manager Root Pane.
     */
    public TimelineController(ControllerManager manager) {
        this.manager = manager;
        this.rootPane = manager.getRootPane();
        this.project = manager.getScriptingProject();
        initializeCameraTimelines();
    }

    /**
     * Add a camera shot to the corresponding timeline.
     * @param cameraIndex Index of the camera track.
     * @param name Name of the shot.
     * @param description Shot description.
     * @param startCount Start count.
     * @param endCount End count.
     */
    public void addCameraShot(int cameraIndex, String name, String description,
                              int startCount, int endCount) {
        CameraShot newShot = new CameraShot(name,description, startCount, endCount);
        this.project.getCameraTimelines().get(cameraIndex).addShot(newShot);
        CameraShotBlock shotBlock = new CameraShotBlock(newShot.getInstance(), cameraIndex,
                rootPane.getRootCenterArea(), startCount, endCount, description,
                name, this::shotChangedHandler, newShot);

        manager.setActiveBlock(shotBlock);
    }


    /**
     * Handle updated camera shot. The previous timeline is used to retrieve the corresponding
     * shot. The correct {@link CameraShot} is then updated using the latest {@link CameraShotBlock}
     * position and counts. As the event is unclear as to whether the shot has switched timelines,
     * it is removed from the previous timeline and added to the new one.
     * @param event Camera shot change event.
     */
    public void shotChangedHandler(CameraShotBlockUpdatedEvent event) {
        CameraShotBlock changedBlock = event.getCameraShotBlock();

        manager.setActiveBlock(changedBlock);

        CameraTimeline previousTimeline = this.project.getCameraTimelines()
                .get(event.getOldTimelineNumber());
        // Locate shot to be updated using id
        CameraShot shot = changedBlock.getShot();

        // Adjust model
        shot.setBeginCount(changedBlock.getBeginCount());
        shot.setEndCount(changedBlock.getEndCount());
        // Remove shot from previous timeline and add to new one
        previousTimeline.removeShot(shot);
        this.project.getCameraTimelines()
                .get(changedBlock.getTimetableNumber()).addShot(shot);
    }

    /**
     * Create camera timelines based on GUI. (Anti-pattern)
     * TODO: Replace this with proper XML based project creation
     */
    private void initializeCameraTimelines() {
        for (int i = 0; i < numTimelines; i++) {
            Camera defCam = new Camera("IP Cam " + i, "", defType);
            CameraTimeline timelineN = new CameraTimeline(defCam, "", project);
            project.addCameraTimeline(timelineN);
        }
    }
}
