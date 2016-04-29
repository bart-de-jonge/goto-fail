package control;

import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.ScriptingProject;
import gui.CameraShotBlock;
import gui.ShotblockUpdatedEvent;
import gui.TimelinesGridPane;
import gui.RootPane;
import gui.TimetableBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that controls the timeline.
 * @author alex
 */
public class TimelineController {

    private RootPane rootPane;

//    private List<CameraTimeline> cameraTimelines;

    // Placeholder camera type until GUI allows personalized entry
    // TODO: Replace Camera Type and Scripting Project when XML functionality is available
    private final CameraType defType = new CameraType("AW-HE130 HD PTZ", "It's an IP Camera", 0.5);

    // Placeholder project in lieu of XML loading
    private final ScriptingProject scriptingProject = new ScriptingProject("BOSS Project", 1.0);

    /**
     * Constructor.
     * @param rootPane Root Pane.
     */
    public TimelineController(RootPane rootPane) {
        this.rootPane = rootPane;
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
        this.scriptingProject.getCameraTimelines().get(cameraIndex).addShot(newShot);
        CameraShotBlock shotBlock = new CameraShotBlock(newShot.getInstance(), cameraIndex,
                rootPane.getRootCenterArea(), startCount, endCount,
                this.rootPane.getRootCenterArea().getGrid());
        shotBlock.attachEventHandler(this::shotChangedHandler);
    }

    /**
     * Handle updated camera shot. As original location is not passed using event,
     * the entire (flattened) list of shots must be searched for the corresponding shot.
     * The correct {@link CameraShot} is then updated using the latest {@link CameraShotBlock}
     * position and counts. As the event is unclear as to whether the shot has switched timelines,
     * it is removed from the previous timeline and added to the new one.
     * @param event Camera shot change event.
     */
    private void shotChangedHandler(ShotblockUpdatedEvent event) {
        // Tunnel through timetableblock to retrieve shotblock
        TimetableBlock timetableBlock = (TimetableBlock) event.getSource();
        CameraShotBlock changedBlock = (CameraShotBlock) timetableBlock.getParentBlock();
        List<CameraTimeline> camTimelines = this.scriptingProject.getCameraTimelines();
        // Locate shot to be updated using id
        CameraShot shot = camTimelines.stream()
                .flatMap(cameraTimeline -> cameraTimeline.getShots().stream())
                .filter(s -> s.getInstance() == changedBlock.getShotId())
                .findFirst()
                .get();

        // Adjust model
        shot.setStartCount(changedBlock.getBeginCount());
        shot.setEndCount(changedBlock.getEndCount());
        // Remove shot from previous timeline and add to new one
        camTimelines.forEach(tl -> tl.removeShot(shot));
        camTimelines.get(changedBlock.getTimetableNumber()).addShot(shot);
    }

    /**
     * Create camera timelines based on GUI. (Anti-pattern)
     * TODO: Replace this with proper XML based project creation
     */
    private void initializeCameraTimelines() {
        int timelinesN = this.rootPane.getRootCenterArea().getGrid().getNumberOfTimelines();
        for (int i = 0; i < timelinesN; i++) {
            Camera defCam = new Camera("IP Cam " + i, "", defType);
            CameraTimeline timelineN = new CameraTimeline(defCam, "", scriptingProject);
            scriptingProject.addCameraTimeline(timelineN);
        }
    }
}
