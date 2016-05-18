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
    private TextField directorTimelineDescriptionField;
    @Getter
    private Button addCameraButton;
    @Getter
    private Button deleteCameraButton;
    @Getter
    private Button addCameraTypeButton;
    @Getter
    private Button deleteCameraTypeButton;
    @Getter
    private Button creationButton;
    @Getter
    private Button cancelButton;
    @Getter
    private ListView<HBox> cameraList;
    @Getter
    private ListView<HBox> cameraTypeList;
    @Getter
    private ArrayList<CameraType> cameraTypes;
    @Getter
    private ArrayList<Camera> cameras;
    @Getter
    private ArrayList<CameraTimeline> timelines;
    @Getter
    private Label titleLabel;
    
    
    /**
     * Construct a new EditProjectModalView.
     * @param rootPane the rootPane for this modal.
     */
    public EditProjectModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }
    
    /**
     * Construct a new EditProjectModalView.
     * @param rootPane the rootPane for this modal.
     * @param width the width of this modal
     * @param height the height of this modal
     */
    public EditProjectModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);
        this.rootPane = rootPane;
        this.project = rootPane.getControllerManager().getScriptingProject();
        this.cameras = project.getCameras();
        this.cameraTypes = project.getCameraTypes();
        this.timelines = project.getCameraTimelines();
        initializeView();
    }
    
    /**
     * Initialize the view of this modal.
     */
    private void initializeView() {
        viewPane = new VBox(20);
        titleLabel = new Label("");
        viewPane.getChildren().add(titleLabel);
        
        initFields();
        initCameraTypeSection();
        initCameraSection();
        initFinalButtons();
       // fillInformation();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }
    
    private void fillInformation() {
        nameField.setText(project.getName());
        descriptionField.setText(project.getDescription());
        secondsPerCountField.setText(Double.toString(project.getSecondsPerCount()));
        directorTimelineDescriptionField.setText(project.getDirectorTimeline().getDescription());
        cameraTypeList = initCameraTypeList();
        cameraList = initCameraList();
    }
    
    /**
     * Initialize the fields.
     */
    private void initFields() {
        nameField = new TextField();
        descriptionField = new TextField();
        secondsPerCountField = new NumberTextField();
        directorTimelineDescriptionField = new TextField();
        viewPane.getChildren().addAll(nameField, descriptionField, 
                secondsPerCountField, directorTimelineDescriptionField);
    }
    
    /**
     * Initialize the section for adding/deleting camera types.
     */
    private void initCameraTypeSection() {
        addCameraTypeButton = new Button("Add Camera Type");
        deleteCameraTypeButton = new Button("Delete Camera Type");
        cameraTypeList = new ListView<HBox>();
        viewPane.getChildren().addAll(addCameraTypeButton, deleteCameraTypeButton, cameraTypeList);
    }
    
    /**
     * Initialize the camera type list.
     * @return the camera type list.
     */
    private ListView<HBox> initCameraTypeList() {
        ListView<HBox> result = new ListView<HBox>();
        Set<CameraType> types = project.getDistinctCameraTypes();
        for (CameraType type: types) {
            HBox box = new HBox();
            box.getChildren().addAll(
                    new Label(type.getName()), new Label(" - "), new Label(type.getDescription()));
            result.getItems().add(box);
        }
        return result;
    }
    
    /**
     * Initialize the section for adding/deleting cameras.
     */
    private void initCameraSection() {
        addCameraButton = new Button("Add Camera");
        deleteCameraButton = new Button("Delete Camera");
        cameraList = new ListView<HBox>();
        viewPane.getChildren().addAll(addCameraButton, deleteCameraButton, cameraList);
    }
    
    /**
     * Initialize the camera list.
     * @return the camera list.
     */
    private ListView<HBox> initCameraList() {
        ListView<HBox> result = new ListView<HBox>();
        ArrayList<Camera> cameras = project.getCameras();
        for (Camera c: cameras) {
            HBox box = new HBox();
            box.getChildren().addAll(
                    new Label(c.getName()), new Label(" - "), new Label(c.getDescription()));
            result.getItems().add(box);
        }
        return result;
    }
    
    /**
     * Initialize the save/cancel buttons.
     */
    private void initFinalButtons() {
        creationButton = new Button("Save");
        cancelButton = new Button("Cancel");
        viewPane.getChildren().addAll(creationButton, cancelButton);
    }
    
    

}
