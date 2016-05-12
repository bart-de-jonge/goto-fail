package control;


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
import gui.root.RootPane;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
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
    
    
    /**
     * Construct a new FileMenuController.
     * @param manager the controllermanager that manages this controller
     */
    public FileMenuController(ControllerManager manager) {
        this.controllerManager = manager;
        controllerManager.getRootPane().getRootCenterArea().getNewButton()
                         .setOnMouseClicked(this::newProject);
        controllerManager.getRootPane().getRootCenterArea().getLoadButton()
                         .setOnMouseClicked(this::loadProject);
    }
    
    /**
     * Save the current project state to file.
     * A file chooser window will be opened to select a file
     */
    public void saveAs() {
        log.info("Saving Project to file");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        ExtensionFilter scpFilter = new ExtensionFilter("Scripting Project", "*.scp");
        ExtensionFilter allFilter = new ExtensionFilter("All files", "*.*");
        fileChooser.getExtensionFilters().addAll(scpFilter, allFilter);
        File file = fileChooser.showSaveDialog(controllerManager.getRootPane().getPrimaryStage());
        if (file != null) {
            controllerManager.getScriptingProject().setFilePath(file.getAbsolutePath());
            controllerManager.getScriptingProject().write(file);
            this.changeConfigFile(controllerManager.getScriptingProject());
        } else {
            log.info("User did not select a file");
        }
    }
    
    /**
     * Save the current project.
     If the file path of this project is already known, write to that file
     Otherwise, treat as if save as was clicked
     */
    public void save() {
        if (controllerManager.getScriptingProject().getFilePath() == null) {
            saveAs();
        } else {
            controllerManager.getScriptingProject().write();
        }
    }
    
    /**
     * Load a project from the given path.
     * @param projectPath the path to load from
     */
    public void load(String projectPath) {
        ScriptingProject temp  = ScriptingProject.read(new File(projectPath));
        changeConfigFile(temp);
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
    
    /**
     * Load a project from file.
     * A file chooser window will be opened to select the file.
     */
    public void load() {
        log.info("Loading Project from file");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        ExtensionFilter scpFilter = new ExtensionFilter("Scripting Project", "*.scp");
        ExtensionFilter allFilter = new ExtensionFilter("All files", "*.*");
        fileChooser.getExtensionFilters().addAll(scpFilter, allFilter);
        File file = fileChooser.showOpenDialog(controllerManager.getRootPane().getPrimaryStage());
        if (file != null) {
            ScriptingProject temp  = ScriptingProject.read(file);
            changeConfigFile(temp);
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
        } else {
            log.info("User did not select a file");
        }
    }
    
    /**
     * Overwrite most recent project path in config file.
     * @param project the project to write the path from
     */
    private void changeConfigFile(ScriptingProject project) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(RootPane.getCONFIG_FILEPATH()));
            writer.write(project.getFilePath());
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
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
    }
    
    /**
     * Method to show the modal to create a new project.
     Has MouseEvent so it can be used in an onclick for a button.
     * @param event the MouseEvent, e.g. when clicking the new project button
     */
    public void newProject(MouseEvent event) {
        newProject();
    }

    /**
     * Event handler for when the add camera button in the NewProjectModalView is clicked.
     * @param event the mouse event related to this event
     */
    private void addCamera(MouseEvent event) {
        cameraModal = new AddCameraModalView(controllerManager.getRootPane(),
                                             newProjectModal.getCameraTypes());
        cameraModal.getAddCameraButton().setOnMouseClicked(this::cameraAdded);
    }
    
    /**
     * Event handler for when the add camera button is clicked in the AddCameraModalView.
     * This adds a camera, and a timeline, all in one.
     * @param event the mouse event related to this event
     */
    private void cameraAdded(MouseEvent event) {
        if (validateCameraData()) {
            // add camera
            cameraModal.hideModal();
            String name = cameraModal.getNameField().getText();
            String description = cameraModal.getDescriptionField().getText();
            int selectedIndex = cameraModal.getCameraTypes().getSelectionModel().getSelectedIndex();
            CameraType type = cameraModal.getCameraTypeList().get(selectedIndex);
            Camera camera = new Camera(name, description, type);
            newProjectModal.getCameras().add(camera);
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(" - "), new Label(description));
            newProjectModal.getCameraList().getItems().add(box);
            // add timeline
            CameraTimeline timeline = new CameraTimeline(name, camera, description, null);
            newProjectModal.getTimelines().add(timeline);
        }
    }
    
    /**
     * Validate the data entered by the user in the modal to add a camera.
     * @return true is the data is legit, false otherwise
     */
    private boolean validateCameraData() {
        String errorString = "";
        String name = cameraModal.getNameField().getText();
        String description = cameraModal.getDescriptionField().getText();
        int selectedIndex = cameraModal.getCameraTypes().getSelectionModel().getSelectedIndex();
        
        if (name.isEmpty()) {
            errorString += "Please enter a camera name\n";
        }
        
        if (description.isEmpty()) {
            errorString += "Please enter a camera description\n";
        }
        
        if (selectedIndex == -1) {
            errorString += "Please select a camera type\n";
        }
        
        cameraModal.getErrorLabel().setText(errorString);
        cameraModal.getErrorLabel().setTextFill(Color.RED);
        
        return errorString.isEmpty();
    }
    
    /**
     * Event handler for when the add camera type button in the NewProjectModalView is clicked.
     * @param event the mouse event related to this event
     */
    private void addCameraType(MouseEvent event) {
        cameraTypeModal = new AddCameraTypeModalView(controllerManager.getRootPane());
        cameraTypeModal.getAddCameraTypeButton().setOnMouseClicked(this::typeAdded);
    }
    
    /**
     * Event handler for when the add camera type button in the AddCameraTypeModalView is clicked.
     * @param event the the mouse event related to this event
     */
    private void typeAdded(MouseEvent event) {
        if (validateCameraTypeData()) {
            cameraTypeModal.hideModal();
            String name = cameraTypeModal.getNameField().getText();
            String description = cameraTypeModal.getDescriptionField().getText();
            double movementMargin = Double.parseDouble(
                    cameraTypeModal.getMovementMarginField().getText());
            CameraType type = new CameraType(name, description, movementMargin);
            newProjectModal.getCameraTypes().add(type);
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(" - "), new Label(description));
            newProjectModal.getCameraTypeList().getItems().add(box);
        }
    }
    
    /**
     * Validate the data entered by the user in the modal to add a camera type.
     * @return true is the data is legit, false otherwise
     */
    private boolean validateCameraTypeData() {
        String errorString = "";
        String name = cameraTypeModal.getNameField().getText();
        String description = cameraTypeModal.getDescriptionField().getText();
        String movementMargin = cameraTypeModal.getMovementMarginField().getText();

        if (movementMargin.isEmpty()) {
            errorString = "Please enter a movement margin\n";
        }

        if (description.isEmpty()) {
            errorString = "Please enter a description\n";
        }

        if (name.isEmpty()) {
            errorString = "Please enter a name\n";
        }

        cameraTypeModal.getTitleLabel().setText(errorString);
        cameraTypeModal.getTitleLabel().setTextFill(Color.RED);
        
        return errorString.isEmpty();
    }
    
    /**
     * Validate the data entered by the user in the modal to add a timeline.
     * @return true is the data is legit, false otherwise
     */
    private boolean validateTimelineData() {
        String errorString = "";
        String name = timelineModal.getNameField().getText();
        String description = timelineModal.getDescriptionField().getText();
        int selectedIndex = timelineModal.getCameraList().getSelectionModel().getSelectedIndex();
        
        if (name.isEmpty()) {
            errorString += "Please enter a timeline name\n";
        }
        
        if (description.isEmpty()) {
            errorString += "Please enter a timeline description\n";
        }
        
        if (selectedIndex == -1) {
            errorString += "Please select a camera\n";
        }
        
        timelineModal.getErrorLabel().setText(errorString);
        timelineModal.getErrorLabel().setTextFill(Color.RED);
        
        return errorString.isEmpty();
    }
    
    /**
     * Event handler for when the create project button is clicked in the NewProjectModalView.
     * @param event the mouse event related to this event
     */
    private void createProject(MouseEvent event) {
        if (validateProjectData()) {
            newProjectModal.hideModal();
            
            // Fetch necessary data
            String name = newProjectModal.getNameField().getText();
            String description = newProjectModal.getDescriptionField().getText();
            String directorTimelineDescription = 
                    newProjectModal.getDirectorTimelineDescriptionField().getText();
            double secondsPerCount = Double.parseDouble(newProjectModal.getSecondsPerCountField()
                                                                        .getText());
            
            // Construct ScriptingProject from entered data
            ScriptingProject project = new ScriptingProject(name, description, secondsPerCount);
            project.setDirectorTimeline(new DirectorTimeline(directorTimelineDescription, null));
            project.setCameras(newProjectModal.getCameras());
            project.setCameraTimelines(newProjectModal.getTimelines());
            project.getDirectorTimeline().setProject(project);
            for (CameraTimeline timeline : project.getCameraTimelines()) {
                timeline.setProject(project);
            }
            
            // Set title of program to be the title of the current project
            controllerManager.getRootPane().getPrimaryStage().setTitle(name);

            // Set constructed project to be *the* project currently used
            controllerManager.setScriptingProject(project);
            
            // Re-init RootCenterArea with new number of timelines
            RootCenterArea area = new RootCenterArea(controllerManager.getRootPane(),
                                                     newProjectModal.getTimelines().size(),
                                                     false);
            controllerManager.getRootPane().reInitRootCenterArea(area);
        }
    }
    
    /**
     * Validate the data entered by the user in the modal to create a project.
     * @return true is the data is legit, false otherwise
     */
    private boolean validateProjectData() {
        String errorString = "";

        String directorTimelineDescription = newProjectModal.getDirectorTimelineDescriptionField()
                                                            .getText();
        if (directorTimelineDescription.isEmpty()) {
            errorString = "Please enter a director timeline description\n";
        }

        String secondsPerCount = newProjectModal.getSecondsPerCountField()
                .getText();
        if (secondsPerCount.isEmpty()) {
            errorString = "Please enter the seconds per count\n";
        }

        String description = newProjectModal.getDescriptionField().getText();
        if (description.isEmpty()) {
            errorString = "Please enter a project description\n";
        }

        String name = newProjectModal.getNameField().getText();
        if (name.isEmpty()) {
            errorString = "Please enter a project name\n";
        }

        if (!errorString.equals((""))) {
            newProjectModal.getTitleLabel().setText(errorString);
            newProjectModal.getTitleLabel().setTextFill(Color.RED);
        }

        return errorString.isEmpty();
    }
     
    public void loadProject(MouseEvent event) {
        load();
    }

}
