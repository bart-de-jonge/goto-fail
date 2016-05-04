package gui;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for displaying a modal view for the creation of shots.
 * @author alex
 */
public class CreationModalView extends ModalView {

    private static final int width = 600;
    private static final int height = 600;

    private int numberOfCameras;

    @Setter
    private String defaultStartCount = "0";
    @Setter
    private String defaultEndCount = "1";

    private VBox viewPane;
    private List<CheckBox> cameraCheckboxes;
    private TextField descripField;
    private TextField nameField;
    private NumberTextField startField;
    private NumberTextField endField;
    private Button creationButton;

    private EventHandler<DirectorShotCreationEvent> shotCreationEventHandler;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     * @param creationHandler Event handler for the creation of a shot
     */
    public CreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                             EventHandler<DirectorShotCreationEvent> creationHandler) {
        this(rootPane, numberOfCamerasInTimeline, creationHandler, width, height);
    }

    /**
     * Constructor.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     * @param creationHandler Event handler for the creation of a shot
     * @param modalWidth Modal display width
     * @param modalHeight Modal display height
     */
    public CreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                             EventHandler<DirectorShotCreationEvent> creationHandler,
                             int modalWidth, int modalHeight) {
        super(rootPane, modalWidth, modalHeight);
        this.numberOfCameras = numberOfCamerasInTimeline;
        this.shotCreationEventHandler = creationHandler;
        initializeCreationView();
    }

    /**
     * Initialize and display the modal view.
     */
    private void initializeCreationView() {
        // Create a new VBox with spacing between children of 20
        this.viewPane = new VBox(20);
        this.viewPane.getChildren().add(new Text("Add a new shot"));

        initNameDescriptionFields();
        initCountFields();
        initCamCheckBoxes();

        creationButton = new Button("Create");
        creationButton.setOnMouseClicked(this::createShot);
        this.viewPane.getChildren().add(creationButton);

        super.setModalView(this.viewPane);
        super.displayModal();
    }

    /**
     * Initialize the shot name and description fields.
     */
    private void initNameDescriptionFields() {
        // Add the textfields for shot's name & description
        final Label nameLabel = new Label("Shot Name: ");
        nameField = new TextField();
        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setSpacing(10);

        final Label descripLabel = new Label("Shot Description: ");
        descripField = new TextField();
        HBox descripBox = new HBox();
        descripBox.getChildren().addAll(descripLabel, descripField);
        descripBox.setSpacing(10);

        this.viewPane.getChildren().addAll(nameBox, descripBox);
    }

    /**
     * Initialize the start and end count fields.
     */
    private void initCountFields() {
        // Start and end points
        final Label startLabel = new Label("Start:");
        startField = new NumberTextField();
        final Label endLabel = new Label("End:");
        endField = new NumberTextField();

        // Add default values as a cue
        startField.setText(this.defaultStartCount);
        endField.setText(this.defaultEndCount);

        HBox countBox = new HBox();
        countBox.getChildren()
                .addAll(startLabel, startField, endLabel, endField);
        this.viewPane.getChildren().add(countBox);
    }

    /**
     * Initialize the checkboxes with labels for each camera.
     */
    private void initCamCheckBoxes() {
        cameraCheckboxes = new ArrayList<>();
        for (int i = 0; i < numberOfCameras; i++) {
            String checkBoxString = "Camera " + (i + 1);
            CheckBox checkBox = new CheckBox(checkBoxString);
            cameraCheckboxes.add(checkBox);
        }
        this.viewPane.getChildren().addAll(cameraCheckboxes);
    }

    /**
     * Validate and then pass shot information along.
     * @param event Creation button event
     */
    private void createShot(MouseEvent event) {
        if (validateShot()) {
            super.hideModal();
            this.shotCreationEventHandler.handle(this.buildCreationEvent());
        }
    }

    /**
     * Validates that the fields are correctly filled, and if not, displays
     * a corresponding error message.
     * @return whether or not the fields are valid
     */
    private boolean validateShot() {
        String errorString = "";
        if (nameField.getText().isEmpty()) {
            errorString += "Please name your shot.\n";
        }

        if (descripField.getText().isEmpty()) {
            errorString += "Please add a description.\n";
        }

        double startVal = Double.parseDouble(startField.getText());
        double endVal = Double.parseDouble(endField.getText());
        if (startVal >= endVal) {
            errorString += "Please make sure that the shot ends after it begins.\n";
        }

        boolean aCameraSelected = false;
        for (CheckBox cb : this.cameraCheckboxes) {
            if (cb.isSelected()) {
                aCameraSelected = true;
            }
        }

        if (!aCameraSelected) {
            errorString += "Please select at least one camera for this shot.";
        }

        if (errorString.isEmpty()) {
            return true;
        } else {
            displayError(errorString);
            return false;
        }
    }

    /**
     * Displays an error message in the view.
     * @param errorString Error to be displayed.
     */
    private void displayError(String errorString) {
        Text errText = new Text(errorString);
        errText.setFill(Color.RED);
        this.viewPane.getChildren().add(this.viewPane.getChildren().size() - 1, errText);
    }

    /**
     * Build the shot creation event.
     * @return the shot creation event
     */
    private DirectorShotCreationEvent buildCreationEvent() {
        String shotName = this.nameField.getText();
        String shotDescrip = this.descripField.getText();
        List<Integer> camerasInShot = getCamerasInShot();
        double startPoint = Double.parseDouble(this.startField.getText());
        double endPoint = Double.parseDouble(this.endField.getText());

        return new DirectorShotCreationEvent(shotName, shotDescrip, camerasInShot,
                                             startPoint, endPoint);
    }

    /**
     * Builds a list of which camera timelines are in the shot.
     * @return list of cameras in shot
     */
    private List<Integer> getCamerasInShot() {
        List<Integer> camsInShot = new ArrayList<>();

        for (int i = 0; i < cameraCheckboxes.size(); i++) {
            if (cameraCheckboxes.get(i).isSelected()) {
                camsInShot.add(i);
            }
        }

        return camsInShot;
    }
}
