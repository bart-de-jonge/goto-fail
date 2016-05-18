package control;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.DirectorTimeline;
import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.modal.AddCameraModalView;
import gui.modal.AddCameraTypeModalView;
import gui.modal.EditProjectModalView;
import gui.root.RootCenterArea;
import gui.root.RootPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProjectController {
    
    private ControllerManager controllerManager;
    
    private EditProjectModalView editProjectModal;
    private AddCameraModalView cameraModal;
    private AddCameraTypeModalView cameraTypeModal;
    
    /**
     * Construct a new FileMenuController.
     * @param manager the controllermanager that manages this controller
     */
    public ProjectController(ControllerManager manager) {
        this.controllerManager = manager;
        controllerManager.getRootPane().getStartupModalView().getNewButton()
                         .setOnMouseClicked(this::newProject);
        controllerManager.getRootPane().getStartupModalView().getLoadButton()
                         .setOnMouseClicked(this::loadProject);
        controllerManager.getRootPane().getStartupModalView().getExitButton()
                         .setOnMouseClicked(this::exit);
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
     * Handler for when the save button is clicked.
     * @param event the MouseEvent for this handler
     */
    private void save(MouseEvent event) {
        if (validateProjectData()) {
            editProjectModal.hideModal();
            
            String name = editProjectModal.getNameField().getText();
            String description = editProjectModal.getDescriptionField().getText();
            String directorTimelineDescription = editProjectModal
                    .getDirectorTimelineDescriptionField().getText();
            double secondsPerCount = Double.parseDouble(
                    editProjectModal.getSecondsPerCountField().getText());
            
            ScriptingProject project = new ScriptingProject(name, description, secondsPerCount);
            project.setDirectorTimeline(new DirectorTimeline(directorTimelineDescription, null));
            project.setCameraTypes(editProjectModal.getCameraTypes());
            project.setCameras(editProjectModal.getCameras());
            project.setCameraTimelines(editProjectModal.getTimelines());
            project.getDirectorTimeline().setProject(project);
            project.setFilePath(editProjectModal.getProject().getFilePath());
            for (CameraTimeline timeline : project.getCameraTimelines()) {
                timeline.setProject(project);
            }
            controllerManager.setScriptingProject(project);
            controllerManager.updateWindowTitle();
            RootCenterArea area = new RootCenterArea(
                    controllerManager.getRootPane(), editProjectModal.getTimelines().size(), false);
            controllerManager.getRootPane().reInitRootCenterArea(area);
            for (int i = 0;i < project.getCameraTimelines().size();i++) {
                CameraTimeline newLine = project.getCameraTimelines().get(i);
                CameraTimeline oldLine = editProjectModal.getProject().getCameraTimelines().get(i);
                LinkedList<CameraShot> shots = new LinkedList<CameraShot>();
                oldLine.getShots().forEach(shots::add);
                int j = i;
                shots.forEach(shot -> {
                        newLine.addShot(shot);
                        controllerManager.getTimelineControl().addCameraShot(j, shot);
                    });
            }  
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
     * @param file the file to load from
     */
    public void load(File file) {
        ScriptingProject temp = null;
        try {
            temp = ScriptingProject.read(file);
        } catch (Exception e) {
            log.error("previously opened file could not be found.");
        }
        if (temp == null) {
            controllerManager.getRootPane().getPrimaryStage().close();
            controllerManager.getRootPane().initStartupScreen(true);
        } else {
            controllerManager.getRootPane().closeStartupScreen();
            controllerManager.setScriptingProject(temp);
            controllerManager.getRootPane()
                    .reInitRootCenterArea(new RootCenterArea(
                            controllerManager.getRootPane(),
                            controllerManager.getScriptingProject()
                                    .getCameraTimelines()
                                    .size(), false));
            addLoadedBlocks(controllerManager.getScriptingProject());
            controllerManager.getTimelineControl()
                    .setNumTimelines(controllerManager.getScriptingProject()
                            .getCameraTimelines()
                            .size());
            changeConfigFile(temp);
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
            load(file);
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
            writer = new PrintWriter(new File(RootPane.getCONFIG_FILEPATH()), "UTF-8");
            writer.write(project.getFilePath());
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
                controllerManager.updateWindowTitle();
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
     * Start the edit project modal with information of the current project filled in.
     */
    public void editProject() {
        editProjectModal = new EditProjectModalView(controllerManager.getRootPane(), true);
        editProjectModal.getAddCameraButton().setOnMouseClicked(this::addCamera);
        editProjectModal.getDeleteCameraButton().setOnMouseClicked(this::deleteCamera);
        editProjectModal.getAddCameraTypeButton().setOnMouseClicked(this::addCameraType);
        editProjectModal.getDeleteCameraTypeButton().setOnMouseClicked(this::deleteCameraType);
        editProjectModal.getCancelButton().setOnMouseClicked(this::cancel);
        editProjectModal.getSaveButton().setOnMouseClicked(this::save);
    }
    
    /**
     * Handler for deleting a camera type.
     * @param event the MouseEvent for this handler
     */
    private void deleteCameraType(MouseEvent event) {
        int selectedIndex = editProjectModal.getCameraTypeList()
                                            .getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            editProjectModal.getCameraTypes().remove(selectedIndex);
            editProjectModal.getCameraTypeList().getItems().remove(selectedIndex);
        } else {
            log.debug("TODO: Error message select camera");
            // TODO: Error message, select camera type
        }
    }
    
    /**
     * Handler for when the cancel button is pressed.
     * @param event the MouseEvent for this handler
     */
    private void cancel(MouseEvent event) {
        editProjectModal.hideModal();
    }
    
    /**
     * Handler for deleting a camera.
     * @param event the MouseEvent for this handler
     */
    private void deleteCamera(MouseEvent event) {
        // TODO: Show prompt if there are shots on the timeline.
        int selectedIndex = editProjectModal.getCameraList().getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            editProjectModal.getCameras().remove(selectedIndex);
            editProjectModal.getTimelines().remove(selectedIndex);
            editProjectModal.getCameraList().getItems().remove(selectedIndex);
        } else {
            log.debug("TODO: Error message should select camera");
            // TODO: Error message: should select camera
        }
    }
    
    /**
     * Method to show the modal to create a new project.
     */
    public void newProject() {
        editProjectModal = new EditProjectModalView(controllerManager.getRootPane(), false);
        editProjectModal.getSaveButton().setOnMouseClicked(this::save);
        editProjectModal.getAddCameraButton().setOnMouseClicked(this::addCamera);
        editProjectModal.getAddCameraTypeButton().setOnMouseClicked(this::addCameraType);
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
                                             editProjectModal.getCameraTypes());
        cameraModal.getAddCameraButton().setOnMouseClicked(this::cameraAdded);
        cameraModal.getCancelButton().setOnMouseClicked(this::cancelAddCamera);
    }
    
    private void cancelAddCamera(MouseEvent event) {
        cameraModal.hideModal();
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
            editProjectModal.getCameras().add(camera);
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(" - "), new Label(description));
            editProjectModal.getCameraList().getItems().add(box);
            // add timeline
            CameraTimeline timeline = new CameraTimeline(name, camera, description, null);
            editProjectModal.getTimelines().add(timeline);
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

        if (selectedIndex == -1) {
            errorString = "Please select a camera type\n";
        }
        
        if (description.isEmpty()) {
            errorString = "Please enter a camera description\n";
        }

        if (name.isEmpty()) {
            errorString = "Please enter a camera name\n";
        }

        
        cameraModal.getTitleLabel().setText(errorString);
        cameraModal.getTitleLabel().setTextFill(Color.RED);
        
        return errorString.isEmpty();
    }
    
    /**
     * Event handler for when the add camera type button in the NewProjectModalView is clicked.
     * @param event the mouse event related to this event
     */
    private void addCameraType(MouseEvent event) {
        cameraTypeModal = new AddCameraTypeModalView(controllerManager.getRootPane());
        cameraTypeModal.getAddCameraTypeButton().setOnMouseClicked(this::typeAdded);
        cameraTypeModal.getCancelButton().setOnMouseClicked(this::cancelAddCameraType);
    }
    
    private void cancelAddCameraType(MouseEvent event) {
        cameraTypeModal.hideModal();
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
            editProjectModal.getCameraTypes().add(type);
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(" - "), new Label(description));
            editProjectModal.getCameraTypeList().getItems().add(box);
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
     * Validate the data entered by the user in the modal to create a project.
     * @return true is the data is legit, false otherwise
     */
    private boolean validateProjectData() {
        String errorString = "";

        String directorTimelineDescription = editProjectModal.getDirectorTimelineDescriptionField()
                                                            .getText();
        if (directorTimelineDescription.isEmpty()) {
            errorString = "Please enter a director timeline description\n";
        }

        String secondsPerCount = editProjectModal.getSecondsPerCountField()
                .getText();
        if (secondsPerCount.isEmpty()) {
            errorString = "Please enter the seconds per count\n";
        }

        String description = editProjectModal.getDescriptionField().getText();
        if (description.isEmpty()) {
            errorString = "Please enter a project description\n";
        }

        String name = editProjectModal.getNameField().getText();
        if (name.isEmpty()) {
            errorString = "Please enter a project name\n";
        }

        if (!errorString.equals((""))) {
            editProjectModal.getTitleLabel().setText(errorString);
            editProjectModal.getTitleLabel().setTextFill(Color.RED);
        }

        return errorString.isEmpty();
    }
     
    public void loadProject(MouseEvent event) {
        load();
    }


    private void exit(MouseEvent event) {
        controllerManager.getRootPane().closeStartupScreen();
    }

}
