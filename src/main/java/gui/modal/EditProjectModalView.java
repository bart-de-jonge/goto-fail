package gui.modal;

import java.util.ArrayList;
import java.util.Set;

import data.Camera;
import data.CameraTimeline;
import data.CameraType;
import data.ScriptingProject;
import gui.headerarea.NumberTextField;
import gui.root.RootPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class EditProjectModalView extends ModalView {
    
    private static final int width = 600;
    private static final int height = 600;
    
    @Getter
    private RootPane rootPane;
    @Getter
    private VBox viewPane;
    @Getter
    private ScriptingProject project;
    @Getter
    private TextField nameField;
    @Getter
    private TextField descriptionField;
    @Getter
    private NumberTextField secondsPerCountField;
    @Getter
    private TextField directorDescriptionField;
    @Getter
    private Button addCameraButton;
    @Getter
    private Button deleteCameraButton;
    @Getter
    private Button addCameraTypeButton;
    @Getter
    private Button deleteCameraTypeButton;
    @Getter
    private Button saveButton;
    @Getter
    private Button cancelButton;
    @Getter
    private ListView<HBox> cameraList;
    @Getter
    private ListView<HBox> cameraTypeList;
    @Getter
    private ArrayList<CameraType> types;
    @Getter
    private ArrayList<Camera> cameras;
    @Getter
    private ArrayList<CameraTimeline> timelines;
    @Getter
    private Label errorLabel;
    
    
    
    public EditProjectModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }
    
    public EditProjectModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);
        this.rootPane = rootPane;
        this.project = rootPane.getControllerManager().getScriptingProject();
        this.cameras = project.getCameras();
        this.types = project.getCameraTypes();
        this.timelines = project.getCameraTimelines();
        initializeView();
    }
    
    private void initializeView() {
        viewPane = new VBox(20);
        errorLabel = new Label("");
        viewPane.getChildren().add(errorLabel);
        
        initFields();
        initCameraTypeSection();
        initCameraSection();
        initFinalButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }
    
    private void initFields() {
        nameField = new TextField(project.getName());
        descriptionField = new TextField(project.getDescription());
        secondsPerCountField = new NumberTextField(Double.toString(project.getSecondsPerCount()));
        directorDescriptionField = new TextField(project.getDirectorTimeline().getDescription());
        viewPane.getChildren().addAll(nameField, descriptionField, secondsPerCountField, directorDescriptionField);
    }
    
    private void initCameraTypeSection() {
        addCameraTypeButton = new Button("Add Camera Type");
        deleteCameraTypeButton = new Button("Delete Camera Type");
        cameraTypeList = initCameraTypeList();
        viewPane.getChildren().addAll(addCameraTypeButton, deleteCameraTypeButton, cameraTypeList);
    }
    
    
    
    private ListView<HBox> initCameraTypeList() {
        ListView<HBox> result = new ListView<HBox>();
        Set<CameraType> types = project.getDistinctCameraTypes();
        for (CameraType type: types) {
            HBox box = new HBox();
            box.getChildren().addAll(new Label(type.getName()), new Label(" - "), new Label(type.getDescription()));
            result.getItems().add(box);
        }
        return result;
    }
    
    private void initCameraSection() {
        addCameraButton = new Button("Add Camera");
        deleteCameraButton = new Button("Delete Camera");
        cameraList = initCameraList();
        viewPane.getChildren().addAll(addCameraButton, deleteCameraButton, cameraList);
    }
    
    private ListView<HBox> initCameraList() {
        ListView<HBox> result = new ListView<HBox>();
        ArrayList<Camera> cameras = project.getCameras();
        for (Camera c: cameras) {
            HBox box = new HBox();
            box.getChildren().addAll(new Label(c.getName()), new Label(" - "), new Label(c.getDescription()));
            result.getItems().add(box);
        }
        return result;
    }
    
    
    private void initFinalButtons() {
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        viewPane.getChildren().addAll(saveButton, cancelButton);
    }
    
    

}
