package control;

import java.io.File;

import data.CameraShot;
import data.CameraTimeline;
import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.root.RootCenterArea;
import gui.root.RootPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class FileMenuController {
    
    
    private ControllerManager controllerManager;
    
    public FileMenuController(ControllerManager manager) {
        this.controllerManager = manager;
    }
    
    /**
     * Save the current project state to file.
     * A file chooser window will be opened to select a file
     */
    public void save() {
        log.info("Saving Project to file");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        ExtensionFilter extFilter = new ExtensionFilter("txt files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(controllerManager.getRootPane().getPrimaryStage());
        if (file != null) {
            controllerManager.getScriptingProject().write(file);
        }
    }
    
    /**
     * Load a project from file.
     * A file chooser window will be opened to select the file.
     */
    public void load() {
        log.info("Loading Project from file");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        ExtensionFilter extFilter = new ExtensionFilter("txt files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(controllerManager.getRootPane().getPrimaryStage());
        if (file != null) {
            ScriptingProject temp  = ScriptingProject.read(file);
            if (temp == null) {
                Alert alert = new Alert(AlertType.ERROR); 
                alert.setTitle("Load Failed");
                alert.setContentText("The format in the selected file was not recognized");
                alert.showAndWait();
            } else {
                controllerManager.setScriptingProject(temp);
                controllerManager.getRootPane().reInitRootCenterArea(new RootCenterArea(controllerManager.getRootPane(), controllerManager.getScriptingProject().getCameraTimelines().size(), false));
                addLoadedBlocks(controllerManager.getScriptingProject());
                controllerManager.getTimelineControl().setNumTimelines(controllerManager.getScriptingProject().getCameraTimelines().size());
            }
        }
    }
    
    /**
     * Add the blocks that were loaded from file to the UI.
     * @param project the project that was loaded
     */
    public void addLoadedBlocks(ScriptingProject project) {
        log.info("Adding loaded blocks");
        for (int i = 0; i < project.getCameraTimelines().size();i++) {
            CameraTimeline timeline = project.getCameraTimelines().get(i);
            int amountShots = timeline.getShots().size();
            for (int j = 0; j < amountShots;j++) {
                CameraShot shot = timeline.getShots().get(j);
                addCameraShotForLoad(i, shot);
            }
        }
    }
    
    /**
     * Add a camera shot that is loaded from file.
     * @param cameraIndex the index of the camera timeline to use
     * @param shot the shot to add
     */
    private void addCameraShotForLoad(int cameraIndex, CameraShot shot) {
        CameraShotBlock shotBlock = new CameraShotBlock(shot, cameraIndex, 
                                                       controllerManager.getRootPane().getRootCenterArea(),
                                                       controllerManager.getTimelineControl()::shotChangedHandler);
        controllerManager.setActiveShotBlock(shotBlock);
        controllerManager.getTimelineControl().getCameraShotBlocks().add(shotBlock);
    }

}
