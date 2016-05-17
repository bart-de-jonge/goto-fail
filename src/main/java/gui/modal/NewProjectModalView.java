package gui.modal;

import data.Camera;
import data.CameraTimeline;
import data.CameraType;
import gui.headerarea.NumberTextField;
import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledListview;
import gui.styling.StyledTextfield;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Class responsible for displaying a modal view for the creation of a project.
 */
@Log4j2
public class NewProjectModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 350 work very, very well.
    private static final int width = 820;
    private static final int height = 450;

    // variables for spacing
    private static final int topAreaHeight = 70;
    private static final int bottomAreaHeight = 60;

    // simple background styles of the three main areas.
    private String topStyle = "-fx-background-color: "
            + TweakingHelper.STRING_PRIMARY + ";"
            + "-fx-text-fill: white; -fx-font-size: 26;"
            + "-fx-font-family: helvetica neue; -fx-font-weight: lighter;"
            + "-fx-border-width: 0 0 10 0;"
            + "-fx-border-color: "
            + TweakingHelper.STRING_SECONDARY + ";";
    private String centerLeftStyle = "-fx-background-color: "
            + TweakingHelper.STRING_BACKGROUND_HIGH + ";";
    private String centerRightStyle = "-fx-background-color: "
            + TweakingHelper.STRING_BACKGROUND + ";";
    private String bottomStyle = "-fx-background-color: "
            + TweakingHelper.STRING_PRIMARY + ";";

    // variables for the Create and Cancel buttons
    private static final int buttonWidth = 90;
    private static final int buttonHeight = 25;
    private static final int buttonSpacing = 20;

    // variables for the title label
    private static final int titlelabelOffsetFromLeft = 20;

    /*
     * Other variables
     */

    // No touching these constants. They work well for all general cases,
    // and there is no reason to change them ever again.
    private static final int TEXT_AREA_MIN_WIDTH = 300;
    private static final int LISTS_AREA_MIN_WIDTH = 300;

    // General panes used
    @Getter
    private RootPane rootPane;
    @Getter
    private VBox viewPane;
    @Getter
    private HBox centerPane;
    @Getter
    private HBox buttonPane;
    @Getter
    private StyledListview<HBox> cameraList;
    @Getter
    private StyledListview<HBox> cameraTypeList;

    // Labels
    @Getter
    private Label titleLabel;

    // Text fields
    @Getter
    private StyledTextfield nameField;
    @Getter
    private StyledTextfield descriptionField;
    @Getter
    private NumberTextField secondsPerCountField;
    @Getter
    private StyledTextfield directorTimelineDescriptionField;

    // Buttons
    @Getter
    private StyledButton creationButton;
    @Getter
    private StyledButton cancelButton;
    @Getter
    private StyledButton addCameraTypeButton;
    @Getter
    private StyledButton addCameraButton;

    // Misc
    @Getter
    private ArrayList<CameraType> cameraTypes;
    @Getter
    private ArrayList<Camera> cameras;
    @Getter
    private ArrayList<CameraTimeline> timelines;

    /**
     * Construct a new NewProjectModalView.
     * @param rootPane the rootPane for this modal.
     */
    public NewProjectModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }
    
    /**
     * Construct a new NewProjectModalView.
     * @param rootPane the rootPane that calls this modal.
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
        // force minimum size
        getModalStage().setHeight(height);
        getModalStage().setWidth(width);
        getModalStage().setMinWidth(width);
        getModalStage().setMinHeight(height);

        // Create a new VBox for vertical layout
        this.viewPane = new VBox();

        // Add label at top
        initTitleLabel();

        // add space for textfields and lists
        this.centerPane = new HBox(40.0);
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(0, 0, 0, 0));
        this.centerPane.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        this.viewPane.getChildren().add(centerPane);

        // actually add textfields and lists
        initFields();
        initAdds();

        // add buttons at bottom.
        initButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }

    /**
     * Initialize title label.
     */
    private void initTitleLabel() {
        titleLabel = new Label("Create a new project...");
        titleLabel.setStyle(topStyle);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        titleLabel.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        titleLabel.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        titleLabel.setMinHeight(topAreaHeight);
        titleLabel.setPrefHeight(topAreaHeight);
        titleLabel.setMaxHeight(topAreaHeight);
        this.viewPane.getChildren().add(titleLabel);
    }

    /**
     * Initialize fields.
     */
    private void initFields() {
        VBox content = new VBox(TweakingHelper.GENERAL_SPACING);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinWidth(TEXT_AREA_MIN_WIDTH);
        content.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        content.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        content.setPadding(new Insets(TweakingHelper.GENERAL_PADDING));
        content.setStyle(centerLeftStyle);

        initNameDescriptionFields(content);
        innitTimelineFields(content);

        this.centerPane.getChildren().add(content);
    }

    /**
     * Initializes name and description textfields.
     * @param content pane in which to intiialize.
     */
    private void initNameDescriptionFields(VBox content) {
        // init name field
        final Label nameLabel = new Label("Project name: ");
        nameField = new StyledTextfield();
        nameField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        nameField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        nameField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        nameField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        HBox nameBox = new HBox(TweakingHelper.GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);

        // init description field
        final Label descriptionLabel = new Label("Project description: ");
        descriptionField = new StyledTextfield();
        descriptionField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        descriptionField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        descriptionField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        descriptionField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        HBox descriptionBox = new HBox(TweakingHelper.GENERAL_SPACING);
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);
        descriptionBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(nameBox, descriptionBox);
    }

    /**
     * Initializes timeline textfields.
     * @param content pane in which to intiialize.
     */
    private void innitTimelineFields(VBox content) {
        // init seconds per count field
        final Label secondsPerCountLabel = new Label("Seconds per count: ");
        secondsPerCountField = new NumberTextField();
        secondsPerCountField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        secondsPerCountField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        secondsPerCountField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        secondsPerCountField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        HBox secondsPerCountBox = new HBox(TweakingHelper.GENERAL_SPACING);
        secondsPerCountBox.getChildren().addAll(secondsPerCountLabel, secondsPerCountField);
        secondsPerCountBox.setAlignment(Pos.CENTER_RIGHT);

        // init timeline description field (this ought to die)
        final Label directorTimelineDescriptionLabel = new Label("Director Timeline Description: ");
        directorTimelineDescriptionField = new StyledTextfield();
        directorTimelineDescriptionField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        directorTimelineDescriptionField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        directorTimelineDescriptionField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        directorTimelineDescriptionField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        HBox directorTimelineDescriptionBox = new HBox(TweakingHelper.GENERAL_SPACING);
        directorTimelineDescriptionBox.getChildren().addAll(directorTimelineDescriptionLabel,
                directorTimelineDescriptionField);
        directorTimelineDescriptionBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(secondsPerCountBox, directorTimelineDescriptionBox);
    }

    /**
     * Initialize camera type and camera adding.
     */
    private void initAdds() {
        // vertical pane to hold content
        VBox content = new VBox(TweakingHelper.GENERAL_SPACING);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinWidth(LISTS_AREA_MIN_WIDTH);
        content.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        content.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        content.setPadding(new Insets(TweakingHelper.GENERAL_PADDING));
        content.setStyle(centerRightStyle);

        // add camera type
        addCameraTypeButton = new StyledButton("Add Camera Type");
        addCameraTypeButton.setFillColor(Color.WHITE);
        addCameraTypeButton.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        cameraTypeList = new StyledListview<HBox>();
        cameraTypeList.setMinHeight(75);
        content.getChildren().addAll(addCameraTypeButton, cameraTypeList);

        // add camera
        addCameraButton = new StyledButton("Add Camera");
        addCameraButton.setFillColor(Color.WHITE);
        addCameraButton.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        cameraList = new StyledListview<HBox>();
        cameraList.setMinHeight(75);
        content.getChildren().addAll(addCameraButton, cameraList);

        this.centerPane.getChildren().add(content);
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
        this.buttonPane.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        this.viewPane.getChildren().add(buttonPane);

        // Add cancel button
        cancelButton = new StyledButton("Cancel");
        cancelButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setAlignment(Pos.CENTER);
        cancelButton.setBorderColor(Color.WHITE);
        cancelButton.setFillColor(TweakingHelper.COLOR_PRIMARY);

        // Add creation button
        creationButton = new StyledButton("Create");
        creationButton.setPrefWidth(buttonWidth);
        creationButton.setPrefHeight(buttonHeight);
        creationButton.setAlignment(Pos.CENTER);
        creationButton.setBorderColor(Color.WHITE);
        creationButton.setFillColor(TweakingHelper.COLOR_PRIMARY);

        this.buttonPane.getChildren().addAll(creationButton, cancelButton);
    }

}
