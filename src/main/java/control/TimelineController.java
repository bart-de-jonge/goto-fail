package control;


import java.io.File;
import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.ScriptingProject;
import gui.CameraShotBlock;
import gui.CameraShotBlockUpdatedEvent;
import gui.RootPane;
import gui.ShotBlock;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
    private ControllerManager controllerManager;

    @Getter
    private ScriptingProject project;

    /**
     * Constructor.
     * @param controllerManager Root Pane.
     */
    public TimelineController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
        this.rootPane = controllerManager.getRootPane();
        this.project = controllerManager.getScriptingProject();
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
        controllerManager.setActiveShotBlock(shotBlock);
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

        controllerManager.setActiveShotBlock(changedBlock);

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
     * Remove a camera shot from both the display and the timeline.
     * @param shotBlock CameraShotBlock to be removed
     */
    public void removeCameraShot(CameraShotBlock shotBlock) {
        // If we are removing the active shot, then this must be updated accordingly
        if (this.controllerManager.getActiveShotBlock().equals(shotBlock)) {
            this.controllerManager.setActiveShotBlock(null);
        }
        System.out.println("Removing the shot");

        CameraTimeline cameraTimeline = this.project.getCameraTimelines()
                .get(shotBlock.getTimetableNumber());
        cameraTimeline.removeShot(shotBlock.getShot());
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
    
    /**
     * Save the current project state to file.
     * A file chooser window will be opened to select a file
     */
    public void save() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        ExtensionFilter extFilter = new ExtensionFilter("txt files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(rootPane.getPrimaryStage());
        if (file != null) {
            project.write(file);
        }
    }
    
    /**
     * Load a project from file.
     * A file chooser window will be opened to select the file.
     */
    public void load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        ExtensionFilter extFilter = new ExtensionFilter("txt files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(rootPane.getPrimaryStage());
        if (file != null) {
            ScriptingProject temp  = ScriptingProject.read(file);
            if (temp == null) {
                Alert alert = new Alert(AlertType.ERROR); 
                alert.setTitle("Load Failed");
                alert.setContentText("The format in the selected file was not recognized");
                alert.showAndWait();
            } else {
                project = temp;
            }
        }
    }
    
   
    
}
