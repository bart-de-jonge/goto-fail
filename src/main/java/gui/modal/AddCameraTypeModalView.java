package gui.modal;

import gui.events.AddCameraTypeEvent;
import gui.headerarea.DoubleTextField;
import gui.headerarea.NumberTextField;
import gui.root.RootPane;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AddCameraTypeModalView extends ModalView {
    
    private static final int width = 300;
    private static final int height = 300;
    
    private TextField nameField;
    private TextField descriptionField;
    private DoubleTextField movementMarginField;
    private Button addCameraTypeButton;
    private VBox viewPane;
    
    private EventHandler<AddCameraTypeEvent> eventHandler;
    
    public AddCameraTypeModalView(RootPane rootPane, EventHandler<AddCameraTypeEvent> handler) {
        this(rootPane, handler, width, height);
    }
    
    /**
     * Construct a new AddCameraTypeModalView
     * @param rootPane the rootPane that uses this modal.
     * @param handler the handler to handle the reuslt of the modal
     * @param width the width of the modal screen
     * @param height the height of the modal screen
     */
    public AddCameraTypeModalView(RootPane rootPane,
                                  EventHandler<AddCameraTypeEvent> handler,
                                  int width,
                                  int height) {
        super(rootPane, width, height);
        this.eventHandler = handler;
        initializeView();
    }
    
    /**
     * Initialize the view of this modal.
     */
    private void initializeView() {
        this.viewPane = new VBox(20);
        this.viewPane.getChildren().add(new Text("Create a camera type"));
        
        initFields();
        
        addCameraTypeButton = new Button("Add Camera Type");
        addCameraTypeButton.setOnMouseClicked(this::addCameraType);
        this.viewPane.getChildren().add(addCameraTypeButton);
        
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
        
        final Label marginLabel = new Label("Movement margin: ");
        movementMarginField = new DoubleTextField();
        HBox movementMarginBox = new HBox();
        movementMarginBox.getChildren().addAll(marginLabel, movementMarginField);
        movementMarginBox.setSpacing(10);
        
        this.viewPane.getChildren().addAll(nameBox, descriptionBox, movementMarginBox);
    }
    
    private void addCameraType(MouseEvent event) {
        super.hideModal();
        this.eventHandler.handle(this.buildEvent());
    }
    
    /**
     * Build an AddCameraTypeEvent from the data entered by the user.
     * @return an AddCameraTypeEvent that can be used to build a camera type.
     */
    private AddCameraTypeEvent buildEvent() {
        String name = this.nameField.getText();
        String description = this.descriptionField.getText();
        double movementMargin = Double.parseDouble(this.movementMarginField.getText());
        return new AddCameraTypeEvent(name, description, movementMargin);
    }

}
