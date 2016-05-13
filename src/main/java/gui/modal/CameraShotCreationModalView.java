package gui.modal;

import data.CameraTimeline;
import gui.headerarea.DoubleTextField;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledCheckbox;
import gui.styling.StyledTextfield;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Class responsible for displaying a modal view for the creation of shots.
 * @author alex
 */
public class CameraShotCreationModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 290 work very, very well.
    private static final int width = 680;
    private static final int height = 370;

    // three main colors used throughout window. Experiment a little!
    private static final Color mainColor = Color.rgb(255, 172, 70); // main bright color
    private static final Color secondaryColor = Color.rgb(255, 140, 0); // darker color
    private static final Color tertiaryColor = Color.rgb(255, 235, 190); // lighter color

    // variables for spacing
    private static final int topAreaHeight = 70;
    private static final int bottomAreaHeight = 60;

    // simple background styles of the three main areas.
    private String topStyle = "-fx-background-color: " + getStringFromColor(mainColor) + ";"
            + "-fx-text-fill: white; -fx-font-size: 26;"
            + "-fx-font-family: helvetica neue; -fx-font-weight: lighter;"
            + "-fx-border-width: 0 0 10 0;"
            + "-fx-border-color: " + getStringFromColor(secondaryColor) + ";";
    private String centerLeftStyle = "-fx-background-color: rgb(245, 245, 245);";
    private String centerRightStyle = "-fx-background-color: rgb(255, 255, 255);";
    private String bottomStyle = "-fx-background-color: " + getStringFromColor(mainColor) + ";";

    // variables for the Create and Cancel buttons
    private static final int buttonWidth = 90;
    private static final int buttonHeight = 25;
    private static final int buttonSpacing = 20;

    // variables for the title label
    private static final int titlelabelOffsetFromLeft = 20;

    /*
     * Other variables.
     */

    // No touching these constants. They work well for all general cases,
    // and there is no reason to change them ever again.
    private static final int GENERAL_SIZE = 10000;
    private static final int GENERAL_SPACING = 10;
    private static final int GENERAL_PADDING = 20;
    private static final int TEXT_AREA_MIN_WIDTH = 320;
    private static final int CAMERA_AREA_MIN_WIDTH = 250;

    private List<CameraTimeline> cameraTimelines;

    @Setter
    private String defaultStartCount = "0";
    @Setter
    private String defaultEndCount = "1";

    private VBox rootPane;
    private HBox centerPane;
    private HBox buttonPane;
    private FlowPane checkboxPane;
    @Getter
    private List<StyledCheckbox> cameraCheckboxes;

    @Getter
    private StyledTextfield descriptionField;
    @Getter
    private StyledTextfield nameField;
    @Getter
    private Label titleLabel;

    @Getter
    private StyledButton creationButton;
    @Getter
    private StyledButton cancelButton;

    @Getter
    private DoubleTextField startField;
    @Getter
    private DoubleTextField endField;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param cameraTimelines CameraTimelines to work with
     */
    public CameraShotCreationModalView(RootPane rootPane, List<CameraTimeline> cameraTimelines) {
        this(rootPane, cameraTimelines, width, height);
    }

    /**
     * Constructor.
     * @param rootPane Pane to display modal on top of
     * @param cameraTimelines CameraTimelines to work with
     * @param modalWidth Modal display width
     * @param modalHeight Modal display height
     */
    public CameraShotCreationModalView(RootPane rootPane, List<CameraTimeline> cameraTimelines,
                                       int modalWidth, int modalHeight) {
        super(rootPane, modalWidth, modalHeight);
        this.cameraTimelines = cameraTimelines;
        initializeCreationView();
    }

    /**
     * Initialize and display the modal view.
     */
    private void initializeCreationView() {
        // force minimum size
        getModalStage().setHeight(height);
        getModalStage().setWidth(width);
        getModalStage().setMinWidth(width);
        getModalStage().setMinHeight(height);

        // Create a new VBox for vertical layout
        this.rootPane = new VBox();

        // Add label at top
        initTitleLabel();

        // add space for textfields and checkboxes
        this.centerPane = new HBox();
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(0, GENERAL_PADDING, 0, 0));
        this.centerPane.setPrefHeight(GENERAL_SIZE);
        this.centerPane.setSpacing(40.0);
        this.rootPane.getChildren().add(centerPane);

        // actually add textfields and checkboxes
        initTextFields();
        initCamCheckBoxes();

        // add buttons at bottom.
        initButtons();

        super.setModalView(this.rootPane);
        super.displayModal();
    }

    /**
     * Initialize title label.
     */
    private void initTitleLabel() {
        titleLabel = new Label("Add a camerashot...");
        titleLabel.setStyle(topStyle);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        titleLabel.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        titleLabel.setPrefWidth(GENERAL_SIZE);
        titleLabel.setMinHeight(topAreaHeight);
        titleLabel.setPrefHeight(topAreaHeight);
        titleLabel.setMaxHeight(topAreaHeight);
        this.rootPane.getChildren().add(titleLabel);
    }

    /**
     * Initializes pane with buttons at bottom.
     */
    private void initButtons() {
        // setup button pane
        this.buttonPane = new HBox();
        this.buttonPane.setSpacing(buttonSpacing);
        this.buttonPane.setAlignment(Pos.CENTER_LEFT);
        this.buttonPane.setMinHeight(bottomAreaHeight);
        this.buttonPane.setPrefHeight(bottomAreaHeight);
        this.buttonPane.setMaxHeight(bottomAreaHeight);
        this.buttonPane.setStyle(bottomStyle);
        this.buttonPane.setPadding(new Insets(0, titlelabelOffsetFromLeft,
                0, titlelabelOffsetFromLeft));
        this.rootPane.getChildren().add(buttonPane);

        // Add cancel button
        cancelButton = new StyledButton("Cancel");
        cancelButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setAlignment(Pos.CENTER);
        cancelButton.setBorderColor(Color.WHITE);
        cancelButton.setFillColor(mainColor);

        // Add creation button
        creationButton = new StyledButton("Create");
        creationButton.setPrefWidth(buttonWidth);
        creationButton.setPrefHeight(buttonHeight);
        creationButton.setAlignment(Pos.CENTER);
        creationButton.setBorderColor(Color.WHITE);
        creationButton.setFillColor(mainColor);

        this.buttonPane.getChildren().addAll(creationButton, cancelButton);
    }

    /**
     * Initialize all textfields, add them to a left-central VBox.
     */
    private void initTextFields() {
        VBox content = new VBox(GENERAL_SPACING);
        content.setAlignment(Pos.CENTER);
        content.setMinWidth(TEXT_AREA_MIN_WIDTH);
        content.setPrefWidth(GENERAL_SIZE);
        content.setPrefHeight(GENERAL_SIZE);
        content.setPadding(new Insets(GENERAL_PADDING));
        content.setStyle(centerLeftStyle);

        initNameDescriptionFields(content);
        initCountFields(content);

        this.centerPane.getChildren().add(content);
    }

    /**
     * Initializes name and description textfields.
     * @param content pane in which to intiialize.
     */
    private void initNameDescriptionFields(VBox content) {
        // init name field
        final Label nameLabel = new Label("Shot Name: ");
        nameField = new StyledTextfield();
        nameField.setBorderColor(mainColor);
        nameField.setTextColor(mainColor);
        nameField.setTextActiveColor(secondaryColor);
        nameField.setFillActiveColor(tertiaryColor);
        HBox nameBox = new HBox(GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);

        // init description field
        final Label descripLabel = new Label("Shot Description: ");
        descriptionField = new StyledTextfield();
        descriptionField.setBorderColor(mainColor);
        descriptionField.setTextColor(mainColor);
        descriptionField.setTextActiveColor(secondaryColor);
        descriptionField.setFillActiveColor(tertiaryColor);
        HBox descriptionBox = new HBox(GENERAL_SPACING);
        descriptionBox.getChildren().addAll(descripLabel, descriptionField);
        descriptionBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(nameBox, descriptionBox);
    }

    /**
     * Initializes start and end count textfields.
     * @param content pane in which to intiialize.
     */
    private void initCountFields(VBox content) {
        // init start count field
        final Label startLabel = new Label("Start:");
        startField = new DoubleTextField(this.defaultStartCount);
        startField.setBorderColor(mainColor);
        startField.setTextColor(mainColor);
        startField.setTextActiveColor(secondaryColor);
        startField.setFillActiveColor(tertiaryColor);
        HBox startBox = new HBox(GENERAL_SPACING);
        startBox.getChildren().addAll(startLabel, startField);
        startBox.setAlignment(Pos.CENTER_RIGHT);

        // init end count field
        final Label endLabel = new Label("End:");
        endField = new DoubleTextField(this.defaultEndCount);
        endField.setBorderColor(mainColor);
        endField.setTextColor(mainColor);
        endField.setTextActiveColor(secondaryColor);
        endField.setFillActiveColor(tertiaryColor);
        HBox endBox = new HBox(GENERAL_SPACING);
        endBox.getChildren().addAll(endLabel, endField);
        endBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(startBox, endBox);
    }

    /**
     * Initialize the checkboxes with labels for each camera, in a flowpane.
     */
    private void initCamCheckBoxes() {
        // Create new FlowPane to hold the checkboxes.
        this.checkboxPane = new FlowPane();
        this.checkboxPane.setHgap(GENERAL_PADDING);
        this.checkboxPane.setVgap(GENERAL_PADDING);
        this.checkboxPane.setMinWidth(CAMERA_AREA_MIN_WIDTH);
        this.checkboxPane.setPrefWidth(GENERAL_SIZE);
        this.checkboxPane.setAlignment(Pos.CENTER);
        this.checkboxPane.setStyle(centerRightStyle);

        // add checkboxes
        cameraCheckboxes = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < this.cameraTimelines.size(); i++) {
            j = (j > 4) ? 0 : j + 1;
            String checkBoxString = this.cameraTimelines.get(i).getCamera().getName();
            StyledCheckbox checkBox = new StyledCheckbox(checkBoxString);
            checkBox.setBorderColor(mainColor);
            checkBox.setMarkColor(mainColor);
            checkBox.setFillColor(tertiaryColor);
            cameraCheckboxes.add(checkBox);
        }

        // add all to scene
        this.checkboxPane.getChildren().addAll(cameraCheckboxes);
        this.centerPane.getChildren().add(this.checkboxPane);
    }

    /**
     * Builds a list of which camera centerarea are in the shot.
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

    /**
     * Parses color from a Color object to javafx-css-compatible string.
     * @param color the color to parse.
     * @return a representative string.
     */
    private String getStringFromColor(Color color) {
        return "rgba(" + ((int) (color.getRed()   * 255)) + ","
                + ((int) (color.getGreen() * 255)) + ","
                + ((int) (color.getBlue()  * 255)) + ","
                + color.getOpacity() + ")";
    }
}
