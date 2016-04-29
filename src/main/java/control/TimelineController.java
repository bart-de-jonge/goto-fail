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
        System.out.println("shot added?");
        CameraShot newShot = new CameraShot(name,description, startCount, endCount);
        this.cameraTimelines.get(cameraIndex).addShot(newShot);
        CameraShotBlock shotBlock = new CameraShotBlock(newShot.getInstance(), cameraIndex,
                                                        rootPane.getRootCenterArea(),
                                                        startCount, endCount);
        shotBlock.attachEventHandler(this::shotChangedHandler);
        timelinePane.addCamerShotBlock(shotBlock);
    }

    /**
     * Handle updated camera shot.
     * @param event Camera shot change event.
     */
    private void shotChangedHandler(ShotblockUpdatedEvent event) {
        // Tunnel through timetableblock to retrieve shotblock
        TimetableBlock timetableBlock = (TimetableBlock) event.getSource();
        CameraShotBlock changedBlock = (CameraShotBlock) timetableBlock.getParentBlock();
        // Locate shot to be updated using id
        CameraShot shot = this.cameraTimelines.stream()
                .flatMap(cameraTimeline -> cameraTimeline.getShots().stream())
                .filter(s -> s.getInstance() == changedBlock.getShotId())
                .findFirst()
                .get();

        // Adjust model
        shot.setStartCount(changedBlock.getBeginCount());
        shot.setEndCount(changedBlock.getEndCount());
        // Remove shot from previous timeline and add to new one
        this.cameraTimelines.forEach(tl -> tl.removeShot(shot));
        this.cameraTimelines.get(changedBlock.getTimetableNumber()).addShot(shot);
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
