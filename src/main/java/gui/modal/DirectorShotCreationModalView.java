package gui.modal;

import gui.headerarea.NumberTextField;
import gui.root.RootPane;
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
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for displaying a modal view for the creation of shots.
 * @author alex
 */
public class DirectorShotCreationModalView extends ModalView {

    private static final int width = 600;
    private static final int height = 600;

    private int numberOfCameras;

    @Setter
    private String defaultStartCount = "0";
    @Setter
    private String defaultEndCount = "1";

    private VBox viewPane;
    private List<CheckBox> cameraCheckboxes;
    @Getter
    private TextField descriptionField;
    @Getter
    private TextField nameField;
    @Getter
    private NumberTextField startField;
    @Getter
    private NumberTextField endField;
    private Button creationButton;

    @Getter
    private NumberTextField frontPaddingField;
    @Getter
    private NumberTextField endPaddingField;

    private EventHandler<MouseEvent> directorShotCreationEventEventHandler;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     * @param creationHandler Event handler for the creation of a shot
     */
    public DirectorShotCreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                                 EventHandler<MouseEvent> creationHandler) {
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
    public DirectorShotCreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                                 EventHandler<MouseEvent> creationHandler,
                                 int modalWidth, int modalHeight) {
        super(rootPane, modalWidth, modalHeight);
        this.numberOfCameras = numberOfCamerasInTimeline;
        this.directorShotCreationEventEventHandler = creationHandler;
        initializeCreationView();
    }

    /**
     * Initialize and display the modal view.
     */
    private void initializeCreationView() {
        // Create a new VBox with spacing between children of 20
        this.viewPane = new VBox(20);
        this.viewPane.getChildren().add(new Text("Add a new directorshot"));

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
        final Label nameLabel = new Label("Directorshot Name: ");
        nameField = new TextField();
        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setSpacing(10);

        final Label descripLabel = new Label("Directorshot Description: ");
        descriptionField = new TextField();
        HBox descripBox = new HBox();
        descripBox.getChildren().addAll(descripLabel, descriptionField);
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

        // Padding before and after the shot
        final Label frontPaddingLabel = new Label("Padding before the shot:");
        frontPaddingField = new NumberTextField();
        final Label endPaddingLabel = new Label("Padding after the shot:");
        endPaddingField = new NumberTextField();

        frontPaddingField.setText("0.0");
        endPaddingField.setText("0.0");

        HBox countBox = new HBox();
        countBox.getChildren()
                .addAll(startLabel, startField, endLabel, endField,
                        frontPaddingLabel, frontPaddingField, endPaddingLabel, endPaddingField);
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
            this.directorShotCreationEventEventHandler.handle(event);
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

        if (descriptionField.getText().isEmpty()) {
            errorString += "Please add a description.\n";
        }

        errorString = validateShotCounts(errorString);

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
     * Validates the fields with numbers in the CreationModalView.
     * @param errorString the string to add the error messages to
     * @return returns the appended errorstring
     */
    private String validateShotCounts(String errorString) {
        double startVal = Double.parseDouble(startField.getText());
        double endVal = Double.parseDouble(endField.getText());
        if (startVal >= endVal) {
            errorString += "Please make sure that the shot ends after it begins.\n";
        }

        double frontPadding = Double.parseDouble(frontPaddingField.getText());
        double endPadding = Double.parseDouble(endPaddingField.getText());
        if (frontPadding < 0 || endPadding < 0) {
            errorString += "Please make sure that the padding before "
                    + "and after the shot is positive.\n";
        }
        return errorString;
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
     * Builds a list of which cameras are in the shot.
     * @return list of cameras in shot
     */
    public List<Integer> getCamerasInShot() {
        List<Integer> camsInShot = new ArrayList<>();

        for (int i = 0; i < cameraCheckboxes.size(); i++) {
            if (cameraCheckboxes.get(i).isSelected()) {
                camsInShot.add(i);
            }
        }
        return camsInShot;
    }
}
