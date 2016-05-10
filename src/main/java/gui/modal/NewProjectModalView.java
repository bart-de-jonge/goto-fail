package gui.modal;

import java.util.ArrayList;

import data.Camera;
import data.CameraTimeline;
import data.CameraType;
import gui.events.AddCameraEvent;
import gui.events.AddCameraTypeEvent;
import gui.events.AddTimelineEvent;
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
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class NewProjectModalView extends ModalView {
    
    private static final int width = 700;
    private static final int height = 700;
    
    @Getter
    private TextField descriptionField;
    
    @Getter
    private NumberTextField secondsPerCountField;
    
    @Getter
    private TextField directorTimelineDescriptionField;
    
    @Getter
    private Button addCameraButton;
    
    @Getter
    private Button addTimelineButton;
    
    @Getter
    private Button creationButton;
    
    @Getter
    private Button addCameraTypeButton;
    
    @Getter
    private VBox viewPane;
    
    @Getter
    private ListView<Label> cameraList;
    
    @Getter
    private ListView<Label> timelineList;
    
    @Getter
    private ListView<Label> cameraTypeList;
    
    @Getter
    private RootPane rootPane;
    
    @Getter
    private ArrayList<CameraType> cameraTypes;
    
    @Getter
    private ArrayList<Camera> cameras;
    
    @Getter
    private ArrayList<CameraTimeline> timelines;
    
    
    public NewProjectModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }
    
    /**
     * Construct a new NewProjectModalView.
     * @param rootPane the rootPane that calls this modal.
     * @param creationHandler the handler to handle the result of this modal
     * @param width the modal screen width
     * @param height the modal screen height
     */
    public NewProjectModalView(RootPane rootPane,
                               int width,
                               int height) {
        super(rootPane, width, height);
        this.rootPane = rootPane;
        this.cameraTypes = new ArrayList<CameraType>();
        this.cameras = new ArrayList<Camera>();
        this.timelines = new ArrayList<CameraTimeline>();
        initializeView();
    }
    
    /**
     * Initialize the view of this modal.
     */
    private void initializeView() {
        this.viewPane = new VBox(20);
        this.viewPane.getChildren().add(new Text("Create a new project"));
        
        initFields();
        initAddCameraType();
        initAddCamera();
        initAddTimeline();
        
        creationButton = new Button("Create new project");
        this.viewPane.getChildren().add(creationButton);
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }

    /**
     * Initialize the section to add timelines.
     */
    private void initAddTimeline() {
        addTimelineButton = new Button("Add Camera Timeline");
        
        timelineList = new ListView<Label>();
        timelineList.setMaxHeight(100);
        
        this.viewPane.getChildren().addAll(addTimelineButton, timelineList); 
    }
    
    /**
     * Initialize the section to add camera types.
     */
    private void initAddCameraType() {
        addCameraTypeButton = new Button("Add Camera Type");
        
        cameraTypeList = new ListView<Label>();
        cameraTypeList.setMaxHeight(100);
        
        this.viewPane.getChildren().addAll(addCameraTypeButton, cameraTypeList);
    }

    /**
     * Initialize the section to add cameras.
     */
    private void initAddCamera() {
        addCameraButton = new Button("Add Camera");
        
        cameraList = new ListView<Label>();
        cameraList.setMaxHeight(100);
        
        this.viewPane.getChildren().addAll(addCameraButton, cameraList);
    }

    /**
     * Initialize fields.
     */
    private void initFields() {
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
        
        final Label directorTimelineDescriptionLabel = new Label("Director Timeline Description: ");
        directorTimelineDescriptionField = new TextField();
        HBox directorTimelineDescriptionBox = new HBox();
        directorTimelineDescriptionBox.getChildren().addAll(directorTimelineDescriptionLabel,
                                                            directorTimelineDescriptionField);
        directorTimelineDescriptionBox.setSpacing(10);
        
        this.viewPane.getChildren().addAll(descriptionBox,
                                           secondsPerCountBox,
                                           directorTimelineDescriptionBox);
    }
    
   
   
    
    
    
    /**
     * Handle an added Timeline.
     * @param event the event thrown by the AddTimelineModalView
     */
    private void handleAddTimeline(AddTimelineEvent event) {
        CameraTimeline timeline = new CameraTimeline(event.getCamera(),
                                                     event.getDescription(),
                                                     null);
        timelines.add(timeline);
        timelineList.getItems().add(new Label(timeline.getDescription()));
    }
    
    
    /**
     * Handle an added camera type.
     * @param event the event thrown by the AddCameraTypeModalView
     */
    private void handleAddCameraType(AddCameraTypeEvent event) {
        CameraType type = new CameraType(event.getName(),
                                         event.getDescription(), 
                                         event.getMovementMargin());
        cameraTypes.add(type);
        cameraTypeList.getItems().add(new Label(type.getName()));
    }
    
    /**
     * Handle an added camera.
     * @param event the event thrown by the AddCameraModalView
     */
    private void handleAddCamera(AddCameraEvent event) {
        Camera camera = new Camera(event.getName(), event.getDescription(), event.getType());
        cameras.add(camera);
        cameraList.getItems().add(new Label(camera.getName()));
    }
    

}