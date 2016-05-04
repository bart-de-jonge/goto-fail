package control;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import data.*;
import gui.CameraShotBlock;
import gui.CameraShotBlockUpdatedEvent;
import gui.RootPane;
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
    private final CameraType defType = new CameraType("AW-HE130 HD PTZ", "It's an IP Camera", 0);

    @Getter
    private ControllerManager controllerManager;

    @Getter
    private ScriptingProject project;

    // List of all camerashotblocks in this timelinecontroller
    private ArrayList<CameraShotBlock> cameraShotBlocks;

    // List of all currently overlapping camerashotblocks
    private ArrayList<CameraShotBlock> overlappingCameraShotBlocks;

    /**
     * Constructor.
     * @param controllerManager Root Pane.
     */
    public TimelineController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
        this.rootPane = controllerManager.getRootPane();
        this.project = controllerManager.getScriptingProject();
        initializeCameraTimelines();
        this.cameraShotBlocks = new ArrayList<>();
        this.overlappingCameraShotBlocks = new ArrayList<>();
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
        this.cameraShotBlocks.add(shotBlock);

        // Check for collisions
        checkCollisions(cameraIndex, shotBlock);
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

        CameraTimeline newCameraTimeline = this.project.getCameraTimelines().get(changedBlock.getTimetableNumber());

        // Remove shot from previous timeline and add to new one if changed
        if(event.getOldTimelineNumber() != changedBlock.getTimetableNumber()) {
            previousTimeline.removeShot(shot);
            newCameraTimeline.addShot(shot);
        }

        // check for collisions
        checkCollisions(changedBlock.getTimetableNumber(), event.getOldTimelineNumber(), changedBlock);
    }

    /**
     * Check collisions for new camerablock
     * @param timelineNumber - the timelinenumber where the camerashotblock is added
     * @param cameraShotBlock - the camerashotblock to check collisions with
     */
    private void checkCollisions(int timelineNumber, CameraShotBlock cameraShotBlock) {
        checkCollisions(timelineNumber, -1, cameraShotBlock);
    }

    /**
     * Check for collisions (added and removed).
     * @param timelineNumber - the current timeline number to check new collisions
     * @param oldTimelineNumber - the old timeline number to check which collisions are not valid anymore
     *                          should be negative when adding new block
     * @param cameraShotBlock - the shotblock to check collisions with
     */
    private void checkCollisions(int timelineNumber, int oldTimelineNumber, CameraShotBlock cameraShotBlock) {
        // Get the correct timeline
        CameraTimeline timeline = project.getCameraTimelines().get(timelineNumber);

        // Check for collisions
        ArrayList<CameraShot> overlappingShots = timeline.getOverlappingShots(cameraShotBlock.getShot());
        if (overlappingShots.size() > 1) {

            // Collision detected
            System.out.println("COLLISION BITCH");

            ArrayList<CameraShotBlock> overlappingShotBlocks = new ArrayList<>();

            Supplier<ArrayList<Integer>> supplier = () -> new ArrayList<>();
            ArrayList<Integer> myInts = overlappingShots.stream().map(s -> s.getInstance()).collect(Collectors.toCollection(supplier));
            System.out.println(myInts);

            // Get CameraShotBlock
            for (CameraShotBlock shotBlock : this.cameraShotBlocks) {
                if (myInts.contains(shotBlock.getShotId())) {
                    overlappingShotBlocks.add(shotBlock);
                    if (!this.overlappingCameraShotBlocks.contains(shotBlock)) {
                        this.overlappingCameraShotBlocks.add(shotBlock);
                    }
                }
            }

            // Make camerashotblocks red
            for (CameraShotBlock shotBlock : overlappingShotBlocks) {
                shotBlock.getTimetableBlock().setStyle("-fx-background-color: red");
            }
        } else {
            System.out.println("NO OVERLAPPING SHOTS?>!");
        }

        // Remove overlaps for non-overlapping shotblocks
        System.out.println(this.overlappingCameraShotBlocks);
        ArrayList<CameraShotBlock> toRemove = new ArrayList<>();
        for (CameraShotBlock shotBlock : this.overlappingCameraShotBlocks) {
            if (shotBlock.getShot().getCollidesWith().isEmpty()) {
//                this.overlappingCameraShotBlocks.remove(shotBlock);
                toRemove.add(shotBlock);
                shotBlock.setStyle("-fx-background-color: none");
            } else {
                System.out.println(shotBlock.getShot().getCollidesWith().size());
            }
        }
        System.out.println("Removing " + toRemove.size() + " shots things");
        this.overlappingCameraShotBlocks.removeAll(toRemove);
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
