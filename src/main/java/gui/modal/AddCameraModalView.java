package gui.modal;

import java.util.ArrayList;

import data.CameraType;
import gui.root.RootPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

public class AddCameraModalView extends ModalView {
    
    private static final int width = 300;
    private static final int height = 300;
    
    @Getter
    private TextField nameField;
    
    @Getter
    private TextField descriptionField;
    
    @Getter
    private ListView<Label> cameraTypes;
    
    @Getter
    private Button addCameraButton;
    
    @Getter
    private VBox viewPane;
    
    @Getter
    private Label errorLabel;
    
    @Getter
    private ArrayList<CameraType> cameraTypeList;
    
    
    public AddCameraModalView(RootPane rootPane,
                              ArrayList<CameraType> types) {
        this(rootPane, types, width, height);
    }
    
    /**
     * Construct a new AddCameraModalView.
     * @param rootPane the rootPane that uses this modal
     * @param types the camera types that can be used
     * @param width the width of the modal screen
     * @param height the height of the modal screen
     */
    public AddCameraModalView(RootPane rootPane,
                              ArrayList<CameraType> types, 
                              int width, 
                              int height) {
        super(rootPane, width, height);
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
        initErrorLabel();
        
        addCameraButton = new Button("Add Camera");
        this.viewPane.getChildren().add(addCameraButton);
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }
    
    private void initErrorLabel() {
        errorLabel = new Label("");
        viewPane.getChildren().add(errorLabel);
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
}
