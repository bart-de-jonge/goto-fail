package gui.modal;

import java.util.ArrayList;

import data.Camera;
import data.CameraType;
import gui.events.AddCameraTypeEvent;
import gui.events.NewProjectCreationEvent;
import gui.headerarea.NumberTextField;
import gui.root.RootPane;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class NewProjectModalView extends ModalView {
    
    private static final int width = 600;
    private static final int height = 600;
    
    private TextField descriptionField;
    private NumberTextField secondsPerCountField;
    private Button addCameraButton;
    private Button addTimelineButton;
    private Button creationButton;
    private Button addCameraTypeButton;
    private VBox viewPane;
    private ListView<Label> cameraList;
    private ListView<Label> timelineList;
    private ListView<Label> cameraTypeList;
    private RootPane rootPane;
    
    private ArrayList<CameraType> cameraTypes;
    private ArrayList<Camera> cameras;
    
    private EventHandler<NewProjectCreationEvent> creationEventHandler;
    
    public NewProjectModalView(RootPane rootPane, EventHandler<NewProjectCreationEvent> creationHandler) {
        this(rootPane, creationHandler, width, height);
    }
    
    public NewProjectModalView(RootPane rootPane, EventHandler<NewProjectCreationEvent> creationHandler,
            int width, int height) {
        super(rootPane, width, height);
        this.rootPane = rootPane;
        this.creationEventHandler = creationHandler;
        this.cameraTypes = new ArrayList<CameraType>();
        this.cameras = new ArrayList<Camera>();
        initializeView();
    }
    
    private void initializeView() {
        this.viewPane = new VBox(20);
        this.viewPane.getChildren().add(new Text("Create a new project"));
        
        initDescriptionCountFields();
        initAddCameraType();
        initAddCamera();
        initAddTimeline();
        
        creationButton = new Button("Create new project");
        creationButton.setOnMouseClicked(this::createProject);
        this.viewPane.getChildren().add(creationButton);
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }

    private void initAddTimeline() {
        addTimelineButton = new Button("Add Camera Timeline");
        addTimelineButton.setOnMouseClicked(this::addTimeline);
        
        timelineList = new ListView<Label>();
        timelineList.setMaxHeight(100);
        
       
        this.viewPane.getChildren().addAll(addTimelineButton, timelineList);
        
    }
    
    private void initAddCameraType() {
        addCameraTypeButton = new Button("Add Camera Type");
        addCameraTypeButton.setOnMouseClicked(this::addCameraType);
        
        cameraTypeList = new ListView<Label>();
        cameraTypeList.setMaxHeight(100);
        
        this.viewPane.getChildren().addAll(addCameraTypeButton, cameraTypeList);
    }

    private void initAddCamera() {
        addCameraButton = new Button("Add Camera");
        addCameraButton.setOnMouseClicked(this::addCamera);
        
        cameraList = new ListView<Label>();
        cameraList.setMaxHeight(100);
        
        this.viewPane.getChildren().addAll(addCameraButton, cameraList);

        
    }

    private void initDescriptionCountFields() {
        final Label descriptionLabel = new Label("Project description: ");
        descriptionField = new TextField();
        HBox descriptionBox = new HBox();
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);
        descriptionBox.setSpacing(10);
        
        final Label secondsPerCountLabel = new Label("Seconds per count: ");
        secondsPerCountField = new NumberTextField();
        HBox secondsPerCountBox = new HBox();
        secondsPerCountBox.getChildren().addAll(secondsPerCountLabel, secondsPerCountField);
        secondsPerCountBox.setSpacing(10);
        
        this.viewPane.getChildren().addAll(descriptionBox, secondsPerCountBox);
    }
    
    private void addCamera(MouseEvent event) {
        // TODO: Do stuff
        log.error("Adding camera");
    }
    
    private void addTimeline(MouseEvent event) {
        // TODO: Do stuff
        log.error("Adding timeline");
    }
    
    private void createProject(MouseEvent event) {
        // TODO: Do stuff
        log.error("Creating project");
    }
    
    private void addCameraType(MouseEvent event) {
        new AddCameraTypeModalView(rootPane, this::handleAddCameraType);
    }
    
    private void handleAddCameraType(AddCameraTypeEvent event) {
        CameraType type = new CameraType(event.getName(), event.getDescription(), event.getMovementMargin());
        cameraTypes.add(type);
        cameraTypeList.getItems().add(new Label(type.getName()));
    }
    

}
