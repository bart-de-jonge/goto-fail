package gui.modal;

import gui.headerarea.NumberTextField;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledCheckbox;
import gui.styling.StyledTextfield;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;


/**
 * Class responsible for displaying a modal view for the creation of director shots.
 * @author alex
 */
public class DirectorShotCreationModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 430 work very, very well.
    private static final int width = 680;
    private static final int height = 430;

    // simple background styles of the three main areas.
    private String topStyle = "-fx-background-color: rgb(60,190,255);"
            + "-fx-text-fill: white; -fx-font-size: 20;"
            + "-fx-border-width: 0 0 1px 0;"
            + "-fx-border-color: rgb(60, 190, 255);";
    private String centerStyle = "-fx-background-color: rgb(255, 255, 255);";
    private String bottomStyle = "-fx-background-color: rgb(255, 255, 255);"
            + "-fx-border-width: 1px 0 0 0;"
            + "-fx-border-color: rgb(60, 190, 255);";

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
    private double softShadowOpacity = 0;
    private double hardShadowRadius = 1;
    private double hardShadowCutoff = 1;
    private double hardShadowOpacity = 0;

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
    private static final int TOP_BOTTOM_AREA_HEIGHT = 60;

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
    @Getter
    private Label titleLabel;

    // Text fields
    @Getter
    private StyledTextfield descriptionField;
    @Getter
    private StyledTextfield nameField;
    @Getter
    private NumberTextField startField;
    @Getter
    private NumberTextField endField;

    @Getter
    private NumberTextField frontPaddingField;
    @Getter
    private NumberTextField endPaddingField;

    // Buttons
    @Getter
    private StyledButton creationButton;
    @Getter
    private StyledButton cancelButton;
    @Getter
    private List<StyledCheckbox> cameraCheckboxes;

    // Effects
    private InnerShadow topInnerShadow;
    private InnerShadow topOuterShadow;
    private DropShadow bottomOuterShadow;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     */
    public DirectorShotCreationModalView(RootPane rootPane, int numberOfCamerasInTimeline) {
        this(rootPane, numberOfCamerasInTimeline, width, height);
    }

    /**
     * Constructor.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     * @param modalWidth Modal display width
     * @param modalHeight Modal display height
     */
    public DirectorShotCreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                                 int modalWidth, int modalHeight) {
        super(rootPane, modalWidth, modalHeight);
        this.numberOfCameras = numberOfCamerasInTimeline;
        initializeCreationView();
    }

    /**
     * Initialize and display the modal view.
     */
    private void initializeCreationView() {
        getModalStage().setHeight(height);
        getModalStage().setWidth(width);
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
        //initEffects();

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
        titleLabel.setMinHeight(TOP_BOTTOM_AREA_HEIGHT);
        titleLabel.setPrefHeight(TOP_BOTTOM_AREA_HEIGHT);
        titleLabel.setMaxHeight(TOP_BOTTOM_AREA_HEIGHT);
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
            //checkBox.setMarkColor(checkboxColor);
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
        this.buttonPane.setMinHeight(TOP_BOTTOM_AREA_HEIGHT);
        this.buttonPane.setPrefHeight(TOP_BOTTOM_AREA_HEIGHT);
        this.buttonPane.setMaxHeight(TOP_BOTTOM_AREA_HEIGHT);
        this.buttonPane.setStyle(bottomStyle);
        this.buttonPane.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        this.rootPane.getChildren().add(buttonPane);

        // Add cancel button
        cancelButton = new StyledButton("Cancel");
        cancelButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setAlignment(Pos.CENTER);

        // Add creation button
        creationButton = new StyledButton("Create");
        creationButton.setPrefWidth(buttonWidth);
        creationButton.setPrefHeight(buttonHeight);
        creationButton.setAlignment(Pos.CENTER);

        this.buttonPane.getChildren().addAll(creationButton, cancelButton);
    }

    /**
     * Displays an error message in the view.
     * @param errorString Error to be displayed.
     */
    public void displayError(String errorString) {
        Text errText = new Text(errorString);
        errText.setFill(Color.RED);
        this.rootPane.getChildren().add(this.rootPane.getChildren().size() - 1, errText);
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
