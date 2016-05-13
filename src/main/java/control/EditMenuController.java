package control;

import data.Camera;
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
    
    public EditMenuController(ControllerManager manager) {
        this.controllerManager = manager;
    }
    
    public void editProject() {
        editModal = new EditProjectModalView(controllerManager.getRootPane());
        editModal.getAddCameraButton().setOnMouseClicked(this::addCamera);
        editModal.getDeleteCameraButton().setOnMouseClicked(this::deleteCamera);
        editModal.getAddCameraTypeButton().setOnMouseClicked(this::addCameraType);
        editModal.getDeleteCameraTypeButton().setOnMouseClicked(this::deleteCameraType);
        editModal.getCancelButton().setOnMouseClicked(this::cancel);
        editModal.getSaveButton().setOnMouseClicked(this::save);
    }
    
    private void addCamera(MouseEvent event) {
        cameraModal = new AddCameraModalView(controllerManager.getRootPane(), editModal.getTypes());
        cameraModal.getAddCameraButton().setOnMouseClicked(this::cameraAdded);
    }
    
    private void cameraAdded(MouseEvent event) {
        if (validateCameraData()) {
            cameraModal.hideModal();
            String name = cameraModal.getNameField().getText();
            String description = cameraModal.getDescriptionField().getText();
            int selectedIndex = cameraModal.getCameraTypes().getSelectionModel().getSelectedIndex();
            CameraType type = cameraModal.getCameraTypeList().get(selectedIndex);
            Camera camera = new Camera(name, description, type);
            editModal.getCameras().add(camera);
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(" - "), new Label(description));
            editModal.getCameraList().getItems().add(box);
            CameraTimeline timeline = new CameraTimeline(name, camera, description, null);
            editModal.getTimelines().add(timeline);
        }
    }
    
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
    
    private void typeAdded(MouseEvent event) {
        if (validateCameraTypeData()) {
            typeModal.hideModal();
            String name = typeModal.getNameField().getText();
            String description = typeModal.getDescriptionField().getText();
            double movementMargin = Double.parseDouble(typeModal.getMovementMarginField().getText());
            CameraType type = new CameraType(name, description, movementMargin);
            editModal.getTypes().add(type);
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(" - "), new Label(description));
            editModal.getCameraTypeList().getItems().add(box);
        }
    }
    
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
    
    private void deleteCameraType(MouseEvent event) {
        int selectedIndex = editModal.getCameraTypeList().getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            editModal.getTypes().remove(selectedIndex);
            editModal.getCameraTypeList().getItems().remove(selectedIndex);
        } else {
            // TODO: Error message, select camera type
        }
    }
    
    private void cancel(MouseEvent event) {
        editModal.hideModal();
    }
    
    private void save(MouseEvent event) {
        if (validateProjectData()) {
            editModal.hideModal();
            
            String name = editModal.getNameField().getText();
            String description = editModal.getDescriptionField().getText();
            String directorTimelineDescription = editModal.getDirectorDescriptionField().getText();
            double secondsPerCount = Double.parseDouble(editModal.getSecondsPerCountField().getText());
            
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
            
            RootCenterArea area = new RootCenterArea(controllerManager.getRootPane(), editModal.getTimelines().size(), false);
            controllerManager.getRootPane().reInitRootCenterArea(area);
        }
    }
    
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
