package control;

import java.io.File;

import gui.root.RootPane;
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

}
