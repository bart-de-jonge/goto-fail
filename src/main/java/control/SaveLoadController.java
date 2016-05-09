package control;

import java.io.File;

import data.ScriptingProject;
import gui.root.RootCenterArea;
import gui.root.RootPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SaveLoadController {
    
    private RootPane rootPane;
    
    private ControllerManager controllerManager;
    
    public SaveLoadController(ControllerManager manager) {
        this.controllerManager = controllerManager;
        this.rootPane = controllerManager.getRootPane();
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
        File file = fileChooser.showSaveDialog(rootPane.getPrimaryStage());
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
        File file = fileChooser.showOpenDialog(rootPane.getPrimaryStage());
        if (file != null) {
            ScriptingProject temp  = ScriptingProject.read(file);
            if (temp == null) {
                Alert alert = new Alert(AlertType.ERROR); 
                alert.setTitle("Load Failed");
                alert.setContentText("The format in the selected file was not recognized");
                alert.showAndWait();
            } else {
                controllerManager.setScriptingProject(temp);
                rootPane.reInitRootCenterArea(new RootCenterArea(rootPane, controllerManager.getScriptingProject().getCameraTimelines().size(), false));
                controllerManager.getTimelineControl().addLoadedBlocks(controllerManager.getScriptingProject());
                controllerManager.getTimelineControl().setNumTimelines(controllerManager.getScriptingProject().getCameraTimelines().size());
            }
        }
    }

}
