package control;

import java.util.LinkedList;

import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.DirectorTimeline;
import data.ScriptingProject;
import gui.modal.AddCameraModalView;
import gui.modal.AddCameraTypeModalView;
import gui.modal.EditProjectModalView;
import gui.root.RootCenterArea;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class EditMenuController {
    
    private ControllerManager controllerManager;
    
    private EditProjectModalView editModal;
    private AddCameraTypeModalView typeModal;
    private AddCameraModalView cameraModal;
    
    /**
     * Construct a new EditMenuController.
     * @param manager the ControllerManager that controls this EditMenuController
     */
    public EditMenuController(ControllerManager manager) {
        this.controllerManager = manager;
    }
    
    /**
     * Start the edit project modal.
     */
    public void editProject() {
        editModal = new EditProjectModalView(controllerManager.getRootPane());
        editModal.getAddCameraButton().setOnMouseClicked(this::addCamera);
        editModal.getDeleteCameraButton().setOnMouseClicked(this::deleteCamera);
        editModal.getAddCameraTypeButton().setOnMouseClicked(this::addCameraType);
        editModal.getDeleteCameraTypeButton().setOnMouseClicked(this::deleteCameraType);
        editModal.getCancelButton().setOnMouseClicked(this::cancel);
        editModal.getSaveButton().setOnMouseClicked(this::save);
    }
    
    /**
     * Handler for adding a camera.
     * @param event the MouseEvent for this handler
     */
    private void addCamera(MouseEvent event) {
        cameraModal = new AddCameraModalView(controllerManager.getRootPane(), editModal.getTypes());
        cameraModal.getAddCameraButton().setOnMouseClicked(this::cameraAdded);
    }
    
    /**
     * Handler for when a camera is added.
     * @param event the MouseEvent for this handler
     */
    private void cameraAdded(MouseEvent event) {
        if (validateCameraData()) {
            // Hide modal
            cameraModal.hideModal();
            // Get data
            String name = cameraModal.getNameField().getText();
            String description = cameraModal.getDescriptionField().getText();
            int selectedIndex = cameraModal.getCameraTypes().getSelectionModel().getSelectedIndex();
            // Construct objects from data
            CameraType type = cameraModal.getCameraTypeList().get(selectedIndex);
            Camera camera = new Camera(name, description, type);
            // Add camera to camera list
            editModal.getCameras().add(camera);
            // Add camera to list of cameras that is shown
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(" - "), new Label(description));
            editModal.getCameraList().getItems().add(box);
            // Add camera timeline to list of timelines
            CameraTimeline timeline = new CameraTimeline(name, camera, description, null);
            editModal.getTimelines().add(timeline);
        }
    }
    
    /**
     * Validate the entered camera data.
     * @return true if the data is legit, false otherwise
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
     * Handler for deleting a camera.
     * @param event the MouseEvent for this handler
     */
    private void deleteCamera(MouseEvent event) {
        // TODO: Show prompt if there are shots on the timeline.
        int selectedIndex = editModal.getCameraList().getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            editModal.getCameras().remove(selectedIndex);
            editModal.getTimelines().remove(selectedIndex);
            editModal.getCameraList().getItems().remove(selectedIndex);
        } else {
            // TODO: Error message: should select camera
        }
    }
    
    private void addCameraType(MouseEvent event) {
        typeModal = new AddCameraTypeModalView(controllerManager.getRootPane());
        typeModal.getAddCameraTypeButton().setOnMouseClicked(this::typeAdded);
    }
    
    /**
     * Handler for adding a camera type.
     * @param event the MouseEvent for this handler
     */
    private void typeAdded(MouseEvent event) {
        if (validateCameraTypeData()) {
            typeModal.hideModal();
            String name = typeModal.getNameField().getText();
            String description = typeModal.getDescriptionField().getText();
            double movementMargin = Double.parseDouble(
                    typeModal.getMovementMarginField().getText());
            CameraType type = new CameraType(name, description, movementMargin);
            editModal.getTypes().add(type);
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(" - "), new Label(description));
            editModal.getCameraTypeList().getItems().add(box);
        }
    }
    
    /**
     * Validate the data for adding a camera type.
     * @return true if the data is legit, false otherwise
     */
    private boolean validateCameraTypeData() {
        String errorString = "";
        String name = typeModal.getNameField().getText();
        String description = typeModal.getDescriptionField().getText();
        String movementMargin = typeModal.getMovementMarginField().getText();
        
        if (movementMargin.isEmpty()) {
            errorString = "Please enter a movement margin\n";
        }

        if (description.isEmpty()) {
            errorString = "Please enter a description\n";
        }

        if (name.isEmpty()) {
            errorString = "Please enter a name\n";
        }
        
        typeModal.getTitleLabel().setText(errorString);
        typeModal.getTitleLabel().setTextFill(Color.RED);
        
        return errorString.isEmpty();
    }
    
    /**
     * Handler for deleting a camera type.
     * @param event the MouseEvent for this handler
     */
    private void deleteCameraType(MouseEvent event) {
        int selectedIndex = editModal.getCameraTypeList().getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            editModal.getTypes().remove(selectedIndex);
            editModal.getCameraTypeList().getItems().remove(selectedIndex);
        } else {
            // TODO: Error message, select camera type
        }
    }
    
    /**
     * Handler for when the cancel button is pressed.
     * @param event the MouseEvent for this handler
     */
    private void cancel(MouseEvent event) {
        editModal.hideModal();
    }
    
    /**
     * Handler for when the save button is clicked.
     * @param event the MouseEvent for this handler
     */
    private void save(MouseEvent event) {
        if (validateProjectData()) {
            editModal.hideModal();
            
            String name = editModal.getNameField().getText();
            String description = editModal.getDescriptionField().getText();
            String directorTimelineDescription = editModal.getDirectorDescriptionField().getText();
            double secondsPerCount = Double.parseDouble(
                    editModal.getSecondsPerCountField().getText());
            
            ScriptingProject project = new ScriptingProject(name, description, secondsPerCount);
            project.setDirectorTimeline(new DirectorTimeline(directorTimelineDescription, null));
            project.setCameraTypes(editModal.getTypes());
            project.setCameras(editModal.getCameras());
            project.setCameraTimelines(editModal.getTimelines());
            project.getDirectorTimeline().setProject(project);
            project.setFilePath(editModal.getProject().getFilePath());
            for (CameraTimeline timeline : project.getCameraTimelines()) {
                timeline.setProject(project);
            }
            controllerManager.setScriptingProject(project);
            controllerManager.updateWindowTitle();
            RootCenterArea area = new RootCenterArea(
                    controllerManager.getRootPane(), editModal.getTimelines().size(), false);
            controllerManager.getRootPane().reInitRootCenterArea(area);
            
            for (int i = 0;i < project.getCameraTimelines().size();i++) {
                CameraTimeline newLine = project.getCameraTimelines().get(i);
                CameraTimeline oldLine = editModal.getProject().getCameraTimelines().get(i);
                LinkedList<CameraShot> shots = new LinkedList<CameraShot>();
                for (CameraShot shot: oldLine.getShots()) {
                    shots.add(shot);
                }
                for (CameraShot shot: shots) {
                    newLine.addShot(shot);
                    controllerManager.getTimelineControl().addCameraShot(i, shot);
                }
            }  
        }
    }
    
    /**
     * Validate the data entered for the project.
     * @return true if the data is legit, false otherwise
     */
    private boolean validateProjectData() {
        String errorString = "";
        String directorTimelineDescription = editModal.getDirectorDescriptionField().getText();
        
        if (directorTimelineDescription.isEmpty()) {
            errorString = "Please enter a director timeline description\n";
        }
        
        String secondsPerCount = editModal.getSecondsPerCountField().getText();
        
        if (secondsPerCount.isEmpty()) {
            errorString = "Please enter the seconds per count\n";
        }
        
        String description = editModal.getDescriptionField().getText();
        if (description.isEmpty()) {
            errorString = "Please enter a project description\n";
        }
        
        String name = editModal.getNameField().getText();
        if (name.isEmpty()) {
            errorString = "Please enter a project name\n";
        }
        
        editModal.getErrorLabel().setText(errorString);
        editModal.getErrorLabel().setTextFill(Color.RED);
        
        return errorString.isEmpty();
    }

}
