package gui.modal;

import gui.events.DirectorShotCreationEvent;
import gui.headerarea.NumberTextField;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledCheckbox;
import gui.styling.StyledTextfield;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Setter;


/**
 * Class responsible for displaying a modal view for the creation of shots.
 * @author alex
 */
public class DirectorShotCreationModalView extends ModalView {

    private static final int width = 600;
    private static final int height = 600;

    private String topStyle = "-fx-background-color: rgb(240,240,240);"
            + "-fx-text-fill: black; -fx-font-size: 20;";
    private String centerStyle = "-fx-background-color: rgb(230, 230, 230);";
    private String bottomStyle = "-fx-background-color: rgb(240, 240, 240);";

    private int titlelabelOffsetFromLeft = 20;

    private int numberOfCameras;

    // No touching these constants. They work well for all general cases,
    // and there is no reason to change them ever again.
    private static final int GENERAL_SIZE = 10000;
    private static final int GENERAL_SPACING = 10;
    private static final int GENERAL_PADDING = 20;
    private static final int TEXT_AREA_MIN_WIDTH = 350;
    private static final int CAMERA_AREA_MIN_WIDTH = 250;

    @Setter
    private String defaultStartCount = "0";
    @Setter
    private String defaultEndCount = "1";

    // General panes used
    private VBox rootPane;
    private HBox centerPane;
    private FlowPane checkboxPane;
    private HBox buttonPane;
    private Label titleLabel;

    // Text fields
    private StyledTextfield descriptionField;
    private StyledTextfield nameField;
    private NumberTextField startField;
    private NumberTextField endField;
    private NumberTextField frontPaddingField;
    private NumberTextField endPaddingField;

    // Buttons
    private StyledButton creationButton;
    private StyledButton cancelButton;
    private List<StyledCheckbox> cameraCheckboxes;

    // Effects
    private InnerShadow topInnerShadow;
    private InnerShadow topOuterShadow;
    private DropShadow bottomOuterShadow;

    private EventHandler<DirectorShotCreationEvent> directorShotCreationEventEventHandler;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     * @param creationHandler Event handler for the creation of a shot
     */
    public DirectorShotCreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
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
    public DirectorShotCreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                                 EventHandler<DirectorShotCreationEvent> creationHandler,
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
        getModalStage().setMinWidth(width);
        getModalStage().setMinHeight(height);

        // Create a new VBox with spacing between children of 20
        this.rootPane = new VBox();
        initTitleLabel();

        this.centerPane = new HBox(40.0);
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(0, GENERAL_PADDING, 0, 0));
        this.centerPane.setPrefHeight(GENERAL_SIZE);
        this.centerPane.setStyle(centerStyle);
        this.rootPane.getChildren().add(centerPane);

        initTextfields();

        //initNameDescriptionFields();
        //initCountFields();
        //initCamCheckBoxes();

        creationButton = new StyledButton("Create");
        creationButton.setOnMouseClicked(this::createShot);
        this.rootPane.getChildren().add(creationButton);

        super.setModalView(this.rootPane);
        super.displayModal();
    }

    /**
     * Initialize title label.
     */
    private void initTitleLabel() {
        titleLabel = new Label("Add a directorshot...");
        titleLabel.setStyle(topStyle);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        titleLabel.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        titleLabel.setPrefWidth(GENERAL_SIZE);
        titleLabel.setPrefHeight(GENERAL_SIZE);
        this.rootPane.getChildren().add(titleLabel);
    }

    /**
     * Initialize all six of the text fields.
     */
    private void initTextfields() {
        VBox content = new VBox(GENERAL_SPACING);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinWidth(TEXT_AREA_MIN_WIDTH);
        content.setPrefWidth(GENERAL_SIZE);
        content.setPrefHeight(GENERAL_SIZE);
        content.setPadding(new Insets(GENERAL_PADDING));

        // init name field
        final Label nameLabel = new Label("Shot Name: ");
        nameField = new StyledTextfield();
        HBox nameBox = new HBox(GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setSpacing(GENERAL_SPACING);

        // init description field
        final Label descriptionLabel = new Label("Shot Description: ");
        descriptionField = new StyledTextfield();
        HBox descriptionBox = new HBox(GENERAL_SPACING);
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);
        descriptionBox.setSpacing(GENERAL_SPACING);

        // init start field
        final Label startLabel = new Label("Start:");
        startField = new NumberTextField();
        startField.setText(this.defaultStartCount);
        HBox startBox = new HBox(GENERAL_SPACING);
        startBox.getChildren().addAll(startLabel, startField);
        startBox.setSpacing(GENERAL_SPACING);

        // init end field
        final Label endLabel = new Label("End:");
        endField = new NumberTextField();
        endField.setText(this.defaultEndCount);
        HBox endBox = new HBox(GENERAL_SPACING);
        endBox.getChildren().addAll(endLabel, endField);
        endBox.setSpacing(GENERAL_SPACING);

        // init padding before field
        final Label frontPaddingLabel = new Label("Padding before shot:");
        frontPaddingField = new NumberTextField();
        frontPaddingField.setText("0.0");
        HBox frontPaddingBox = new HBox(GENERAL_SPACING);
        frontPaddingBox.getChildren().addAll(frontPaddingLabel, frontPaddingField);
        frontPaddingBox.setSpacing(GENERAL_SPACING);

        // init padding after field
        final Label endPaddingLabel = new Label("Padding after shot:");
        endPaddingField = new NumberTextField();
        endPaddingField.setText("0.0");
        HBox endPaddingBox = new HBox(GENERAL_SPACING);
        endPaddingBox.getChildren().addAll(endPaddingLabel, endPaddingField);
        endPaddingBox.setSpacing(GENERAL_SPACING);

        content.getChildren().addAll(nameBox, descriptionBox, startBox, endBox,
                frontPaddingBox, endPaddingBox);
        this.centerPane.getChildren().add(content);
    }

    /**
     * Initialize the shot name and description fields.
     */
    private void initNameDescriptionFields() {
        // Add the textfields for shot's name & description
        final Label nameLabel = new Label("Shot Name: ");
        nameField = new StyledTextfield();
        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setSpacing(GENERAL_SPACING);

        final Label descriptionLabel = new Label("Shot Description: ");
        descriptionField = new StyledTextfield();
        HBox descriptionBox = new HBox();
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);
        descriptionBox.setSpacing(GENERAL_SPACING);

        this.rootPane.getChildren().addAll(nameBox, descriptionBox);
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
        this.rootPane.getChildren().add(countBox);
    }

    /**
     * Initialize the checkboxes with labels for each camera.
     */
    private void initCamCheckBoxes() {
        cameraCheckboxes = new ArrayList<>();
        for (int i = 0; i < numberOfCameras; i++) {
            String checkBoxString = "Camera " + (i + 1);
            StyledCheckbox checkBox = new StyledCheckbox(checkBoxString);
            cameraCheckboxes.add(checkBox);
        }
        this.rootPane.getChildren().addAll(cameraCheckboxes);
    }

    /**
     * Validate and then pass shot information along.
     * @param event Creation button event
     */
    private void createShot(MouseEvent event) {
        if (validateShot()) {
            super.hideModal();
            this.directorShotCreationEventEventHandler.handle(this.buildCreationEvent());
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
        this.rootPane.getChildren().add(this.rootPane.getChildren().size() - 1, errText);
    }

    /**
     * Build the shot creation event.
     * @return the shot creation event
     */
    private DirectorShotCreationEvent buildCreationEvent() {
        String shotName = this.nameField.getText();
        String shotDescrip = this.descriptionField.getText();
        List<Integer> camerasInShot = getCamerasInShot();
        double startPoint = Double.parseDouble(this.startField.getText());
        double endPoint = Double.parseDouble(this.endField.getText());
        double frontPadding = Double.parseDouble(this.frontPaddingField.getText());
        double endPadding = Double.parseDouble(this.endPaddingField.getText());

        return new DirectorShotCreationEvent(shotName, shotDescrip, camerasInShot,
                startPoint, endPoint, frontPadding, endPadding);
    }

    /**
     * Builds a list of which cameras are in the shot.
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
