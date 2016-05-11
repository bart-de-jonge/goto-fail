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
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
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
 * Class responsible for displaying a modal view for the creation of director shots.
 * @author alex
 */
public class DirectorShotCreationModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 350 work very, very well.
    private static final int width = 680;
    private static final int height = 350;

    // simple background styles of the three main areas.
    private String topStyle = "-fx-background-color: rgb(240,240,240);"
            + "-fx-text-fill: black; -fx-font-size: 20;";
    private String centerStyle = "-fx-background-color: rgb(230, 230, 230);";
    private String bottomStyle = "-fx-background-color: rgb(240, 240, 240);";

    // variables for the Create and Cancel buttons
    private int buttonWidth = 90;
    private int buttonHeight = 25;
    private Point3D createButtonColor = new Point3D(200, 200, 200);
    private Point3D cancelButtonColor = new Point3D(200, 200, 200);
    private int buttonFontSize = 16;
    private int buttonSpacing = 20;

    // color of the "active" element of a checkbox
    private Point3D checkboxColor = new Point3D(250, 120, 50);

    // variables for the title label
    private int titlelabelOffsetFromLeft = 20;

    // variables for the shadow effects
    private double softShadowRadius = 15;
    private double softShadowCutoff = 0.2;
    private double softShadowOpacity = 0.05;
    private double hardShadowRadius = 1;
    private double hardShadowCutoff = 1;
    private double hardShadowOpacity = 0.15;

    /*
     * Other variables
     */

    // No touching these constants. They work well for all general cases,
    // and there is no reason to change them ever again.
    private static final int GENERAL_SIZE = 10000;
    private static final int GENERAL_SPACING = 10;
    private static final int GENERAL_PADDING = 20;
    private static final int TEXT_AREA_MIN_WIDTH = 350;
    private static final int CAMERA_AREA_MIN_WIDTH = 250;

    private int numberOfCameras;

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

        this.rootPane = new VBox();

        initTitleLabel();

        this.centerPane = new HBox(40.0);
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(0, GENERAL_PADDING, 0, 0));
        this.centerPane.setPrefHeight(GENERAL_SIZE);
        this.centerPane.setStyle(centerStyle);
        this.rootPane.getChildren().add(centerPane);

        initTextfields();
        initCamCheckBoxes();
        initButtons();

        initEffects();

        super.setModalView(this.rootPane);
        super.displayModal();
    }

    /**
     * Sets up effects and adds them to the appropriate panes.
     */
    private void initEffects() {
        topInnerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, hardShadowOpacity),
                hardShadowRadius, hardShadowCutoff, 0, -2);
        topOuterShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, softShadowOpacity),
                softShadowRadius, softShadowCutoff, 0, 1);
        bottomOuterShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, softShadowOpacity),
                softShadowRadius, softShadowCutoff, 0, -1);
        titleLabel.setEffect(topInnerShadow);
        centerPane.setEffect(topOuterShadow);
        buttonPane.setEffect(bottomOuterShadow);
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

        initInfoTextfields(content);
        initCountTextfields(content);
        initPaddingTextfields(content);

        this.centerPane.getChildren().add(content);
    }

    /**
     * Initialize the name and description text fields.
     * @param content the pane in which they are located.
     */
    private void initInfoTextfields(VBox content) {
        // init name field
        final Label nameLabel = new Label("Shot Name: ");
        nameField = new StyledTextfield();
        HBox nameBox = new HBox(GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);

        // init description field
        final Label descriptionLabel = new Label("Shot Description: ");
        descriptionField = new StyledTextfield();
        HBox descriptionBox = new HBox(GENERAL_SPACING);
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);
        descriptionBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(nameBox, descriptionBox);
    }

    /**
     * Initialize the start and end count text fields.
     * @param content the pane in which they are located.
     */
    private void initCountTextfields(VBox content) {
        // init start field
        final Label startLabel = new Label("Start:");
        startField = new NumberTextField();
        startField.setText(this.defaultStartCount);
        HBox startBox = new HBox(GENERAL_SPACING);
        startBox.getChildren().addAll(startLabel, startField);
        startBox.setAlignment(Pos.CENTER_RIGHT);

        // init end field
        final Label endLabel = new Label("End:");
        endField = new NumberTextField();
        endField.setText(this.defaultEndCount);
        HBox endBox = new HBox(GENERAL_SPACING);
        endBox.getChildren().addAll(endLabel, endField);
        endBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(startBox, endBox);
    }

    /**
     * Initialize the before and after padding text fields.
     * @param content the pane in which they are located.
     */
    private void initPaddingTextfields(VBox content) {
        // init padding before field
        final Label frontPaddingLabel = new Label("Padding before shot:");
        frontPaddingField = new NumberTextField();
        frontPaddingField.setText("0.0");
        HBox frontPaddingBox = new HBox(GENERAL_SPACING);
        frontPaddingBox.getChildren().addAll(frontPaddingLabel, frontPaddingField);
        frontPaddingBox.setAlignment(Pos.CENTER_RIGHT);

        // init padding after field
        final Label endPaddingLabel = new Label("Padding after shot:");
        endPaddingField = new NumberTextField();
        endPaddingField.setText("0.0");
        HBox endPaddingBox = new HBox(GENERAL_SPACING);
        endPaddingBox.getChildren().addAll(endPaddingLabel, endPaddingField);
        endPaddingBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(frontPaddingBox, endPaddingBox);
    }

    /**
     * Initialize the checkboxes with labels for each camera.
     */
    private void initCamCheckBoxes() {
        this.checkboxPane = new FlowPane();
        this.checkboxPane.setHgap(GENERAL_PADDING);
        this.checkboxPane.setVgap(GENERAL_PADDING);
        this.checkboxPane.setMinWidth(CAMERA_AREA_MIN_WIDTH);
        this.checkboxPane.setPrefWidth(GENERAL_SIZE);
        this.checkboxPane.setAlignment(Pos.CENTER);

        cameraCheckboxes = new ArrayList<>();
        for (int i = 0; i < numberOfCameras; i++) {
            String checkBoxString = "Camera " + (i + 1);
            StyledCheckbox checkBox = new StyledCheckbox(checkBoxString);
            checkBox.setMarkColor(checkboxColor);
            cameraCheckboxes.add(checkBox);
        }

        this.checkboxPane.getChildren().addAll(cameraCheckboxes);
        this.centerPane.getChildren().add(this.checkboxPane);
    }

    /**
     * Initializes pane with buttons at bottom.
     */
    private void initButtons() {
        // setup button pane
        this.buttonPane = new HBox();
        this.buttonPane.setSpacing(buttonSpacing);
        this.buttonPane.setAlignment(Pos.CENTER_LEFT);
        this.buttonPane.setPrefHeight(GENERAL_SIZE);
        this.buttonPane.setStyle(bottomStyle);
        this.buttonPane.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        this.rootPane.getChildren().add(buttonPane);

        // Add cancel button
        cancelButton = new StyledButton("Cancel");
        cancelButton.setOnMouseReleased(
            e -> {
                getModalStage().close(); // kill window
            }
        );
        cancelButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setFontSize(buttonFontSize);
        cancelButton.setButtonColor(createButtonColor);
        cancelButton.setAlignment(Pos.CENTER);

        // Add creation button
        creationButton = new StyledButton("Create");
        creationButton.setOnMouseClicked(this::createShot);
        creationButton.setPrefWidth(buttonWidth);
        creationButton.setPrefHeight(buttonHeight);
        creationButton.setFontSize(buttonFontSize);
        creationButton.setButtonColor(cancelButtonColor);
        creationButton.setAlignment(Pos.CENTER);

        this.buttonPane.getChildren().addAll(creationButton, cancelButton);
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
