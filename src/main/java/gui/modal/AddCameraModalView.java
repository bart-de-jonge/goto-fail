package gui.modal;

import java.util.ArrayList;

import data.CameraType;
import gui.events.AddCameraEvent;
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

public class AddCameraModalView extends ModalView {
    
    private static final int width = 300;
    private static final int height = 300;
    
    private TextField nameField;
    private TextField descriptionField;
    private ListView<Label> cameraTypes;
    private Button addCameraButton;
    private VBox viewPane;
    private ArrayList<CameraType> cameraTypeList;
    
    private EventHandler<AddCameraEvent> eventHandler;
    
    public AddCameraModalView(RootPane rootPane,
                              EventHandler<AddCameraEvent> handler,
                              ArrayList<CameraType> types) {
        this(rootPane, handler, types, width, height);
    }
    
    /**
     * Construct a new AddCameraModalView.
     * @param rootPane the rootPane that uses this modal
     * @param handler the handler to handle the result of the modal
     * @param types the camera types that can be used
     * @param width the width of the modal screen
     * @param height the height of the modal screen
     */
    public AddCameraModalView(RootPane rootPane,
                              EventHandler<AddCameraEvent> handler,
                              ArrayList<CameraType> types, 
                              int width, 
                              int height) {
        super(rootPane, width, height);
        this.eventHandler = handler;
        this.cameraTypeList = types;
        initializeView();
    }
    
    /**
     * Initialize the view of this modal.
     */
    private void initializeView() {
        this.viewPane = new VBox(20);
        this.viewPane.getChildren().add(new Text("Create a camera"));
        
        initFields();
        initTypeList();
        
        addCameraButton = new Button("Add Camera");
        addCameraButton.setOnMouseClicked(this::addCamera);
        this.viewPane.getChildren().add(addCameraButton);
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }
    
    /**
     * Initialize the fields.
     */
    private void initFields() {
        final Label nameLabel = new Label("Name:  ");
        nameField = new TextField();
        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setSpacing(10);
        
        final Label descriptionLabel = new Label("Description: ");
        descriptionField = new TextField();
        HBox descriptionBox = new HBox();
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);
        descriptionBox.setSpacing(10);
        
        this.viewPane.getChildren().addAll(nameBox, descriptionBox);
    }
    
    /**
     * Initialize the list of camera types.
     */
    private void initTypeList() {
        final Label cameraTypeLabel = new Label("Camera Type: ");
        cameraTypes = new ListView<Label>();
        cameraTypes.setMaxHeight(100);
        for (CameraType type: cameraTypeList) {
            cameraTypes.getItems().add(new Label(type.getName()));
        }
        
        this.viewPane.getChildren().addAll(cameraTypeLabel, cameraTypes);
        
    }
    
    private void addCamera(MouseEvent event) {
        super.hideModal();
        this.eventHandler.handle(this.buildEvent());
    }
    
    /**
     * Build a AddCameraEvent from the data entered by the user.
     * @return an AddCameraEvent that can be used to construct a Camera
     */
    private AddCameraEvent buildEvent() {
        String name = this.nameField.getText();
        String description = this.descriptionField.getText();
        int selectedIndex = cameraTypes.getSelectionModel().getSelectedIndex();
        CameraType type = cameraTypeList.get(selectedIndex);
        return new AddCameraEvent(name, description, type);
    }

}
