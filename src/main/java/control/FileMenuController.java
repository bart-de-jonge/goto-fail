package control;

import java.io.File;

import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.DirectorTimeline;
import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.modal.AddCameraModalView;
import gui.modal.AddCameraTypeModalView;
import gui.modal.AddTimelineModalView;
import gui.modal.NewProjectModalView;
import gui.root.RootCenterArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class FileMenuController {
    
    
    private ControllerManager controllerManager;
    
    private NewProjectModalView newProjectModal;
    private AddCameraModalView cameraModal;
    private AddCameraTypeModalView cameraTypeModal;
    private AddTimelineModalView timelineModal;
    
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
        ExtensionFilter extFilter = new ExtensionFilter("Scripting Project", "*.scp");
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
        ExtensionFilter extFilter = new ExtensionFilter("Scripting Project", "*.scp");
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
                controllerManager.getRootPane()
                                 .reInitRootCenterArea(
                                         new RootCenterArea(
                                                 controllerManager.getRootPane(),
                                                 controllerManager.getScriptingProject()
                                                                  .getCameraTimelines()
                                                                  .size(),
                                                 false));
                addLoadedBlocks(controllerManager.getScriptingProject());
                controllerManager.getTimelineControl()
                                 .setNumTimelines(controllerManager.getScriptingProject()
                                                                   .getCameraTimelines()
                                                                   .size());
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
                addCameraShotForLoad(i, timeline.getShots().get(j));
            }
        }
    }
    
    /**
     * Add a camera shot that is loaded from file.
     * @param cameraIndex the index of the camera timeline to use
     * @param shot the shot to add
     */
    private void addCameraShotForLoad(int cameraIndex, CameraShot shot) {
        CameraShotBlock shotBlock = 
                new CameraShotBlock(shot,
                                    cameraIndex, 
                                    controllerManager.getRootPane()
                                                     .getRootCenterArea(),
                                    controllerManager.getTimelineControl()
                                                     ::shotChangedHandler);
        controllerManager.setActiveShotBlock(shotBlock);
        controllerManager.getTimelineControl().getCameraShotBlocks().add(shotBlock);
    }
    
    /**
     * Method to show the modal to create a new project.
     */
    public void newProject() {
        newProjectModal = new NewProjectModalView(controllerManager.getRootPane());
        newProjectModal.getCreationButton().setOnMouseClicked(this::createProject);
        newProjectModal.getAddCameraButton().setOnMouseClicked(this::addCamera);
        newProjectModal.getAddCameraTypeButton().setOnMouseClicked(this::addCameraType);
        newProjectModal.getAddTimelineButton().setOnMouseClicked(this::addTimeline);
    }
    
    private void addCamera(MouseEvent event) {
        cameraModal = new AddCameraModalView(controllerManager.getRootPane(), newProjectModal.getCameraTypes());
        cameraModal.getAddCameraButton().setOnMouseClicked(this::cameraAdded);
    }
    
    private void cameraAdded(MouseEvent event) {
        cameraModal.hideModal();
        String name = cameraModal.getNameField().getText();
        String description = cameraModal.getDescriptionField().getText();
        int selectedIndex = cameraModal.getCameraTypes().getSelectionModel().getSelectedIndex();
        CameraType type = cameraModal.getCameraTypeList().get(selectedIndex);
        Camera camera = new Camera(name, description, type);
        newProjectModal.getCameras().add(camera);
        newProjectModal.getCameraList().getItems().add(new Label(camera.getName()));
    }
    
    private void addCameraType(MouseEvent event) {
        cameraTypeModal = new AddCameraTypeModalView(controllerManager.getRootPane());
        cameraTypeModal.getAddCameraTypeButton().setOnMouseClicked(this::typeAdded);
    }
    
    private void typeAdded(MouseEvent event) {
        cameraTypeModal.hideModal();
        String name = cameraTypeModal.getNameField().getText();
        String description = cameraTypeModal.getDescriptionField().getText();
        double movementMargin = Double.parseDouble(cameraTypeModal.getMovementMarginField().getText());
        CameraType type = new CameraType(name, description, movementMargin);
        newProjectModal.getCameraTypes().add(type);
        newProjectModal.getCameraTypeList().getItems().add(new Label(type.getName()));
        
    }
    
    private void addTimeline(MouseEvent event) {
        timelineModal = new AddTimelineModalView(controllerManager.getRootPane(), newProjectModal.getCameras());
        timelineModal.getAddTimelineButton().setOnMouseClicked(this::timelineAdded);
    }
    
    private void timelineAdded(MouseEvent event) {
        timelineModal.hideModal();
        String description = timelineModal.getDescriptionField().getText();
        int selectedIndex = timelineModal.getCameraList().getSelectionModel().getSelectedIndex();
        Camera camera = timelineModal.getCameras().get(selectedIndex);
        CameraTimeline timeline = new CameraTimeline(camera, description, null);
        newProjectModal.getTimelines().add(timeline);
        newProjectModal.getTimelineList().getItems().add(new Label(description));
    }
    
    
    
    private void createProject(MouseEvent event) {
        newProjectModal.hideModal();
        String description = newProjectModal.getDescriptionField().getText();
        String directorTimelineDescription = newProjectModal.getDirectorTimelineDescriptionField().getText();
        double secondsPerCount = Double.parseDouble(newProjectModal.getSecondsPerCountField().getText());
        ScriptingProject project = new ScriptingProject(description, secondsPerCount);
        project.setDirectorTimeline(new DirectorTimeline(directorTimelineDescription, null));
        project.setCameras(newProjectModal.getCameras());
        project.setCameraTimelines(newProjectModal.getTimelines());
        project.getDirectorTimeline().setProject(project);
        for (CameraTimeline timeline : project.getCameraTimelines()) {
            timeline.setProject(project);
        }
        controllerManager.setScriptingProject(project);
        RootCenterArea area = new RootCenterArea(controllerManager.getRootPane(), newProjectModal.getTimelines().size(), false);
        controllerManager.getRootPane().reInitRootCenterArea(area);
    }
    
    /**
     * Method to show the modal to create a new project.
     Has MouseEvent so it can be used in an onclick for a button.
     * @param event the MouseEvent, e.g. when clicking the new project button
     */
    public void newProject(MouseEvent event) {
        newProject();
    }
    
   
    
    public void loadProject(MouseEvent event) {
        load();
    }

}
