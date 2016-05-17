package gui.modal;

import java.util.ArrayList;
import java.util.Set;

import data.Camera;
import data.CameraTimeline;
import data.CameraType;
import data.ScriptingProject;
import gui.headerarea.NumberTextField;
import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledListview;
import gui.styling.StyledTextfield;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;

/**
 * Class responsible for displaying a modal view for editing a project.
 */
public class EditProjectModalView extends ModalView {

    /*
     * Tweakable styling variables
     */

    // preferred width and height of screen.
    private static final int width = 800;
    private static final int height = 500;

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
    private ListView<HBox> cameraList;
    @Getter
    private ListView<HBox> cameraTypeList;

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
    private StyledTextfield directorDescriptionField;

    // Buttons
    @Getter
    private StyledButton addCameraButton;
    @Getter
    private StyledButton deleteCameraButton;
    @Getter
    private StyledButton addCameraTypeButton;
    @Getter
    private StyledButton deleteCameraTypeButton;
    @Getter
    private StyledButton saveButton;
    @Getter
    private StyledButton cancelButton;

    // Misc
    @Getter
    private ScriptingProject project;
    @Getter
    private ArrayList<CameraType> types;
    @Getter
    private ArrayList<Camera> cameras;
    @Getter
    private ArrayList<CameraTimeline> timelines;
    
    
    /**
     * Construct a new EditProjectModalView.
     * @param rootPane the rootPane for this modal.
     */
    public EditProjectModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }
    
    /**
     * Construct a new EditProjectModalView.
     * @param rootPane the rootPane for this modal.
     * @param width the width of this modal
     * @param height the height of this modal
     */
    public EditProjectModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);
        this.rootPane = rootPane;
        this.project = rootPane.getControllerManager().getScriptingProject();
        this.cameras = project.getCameras();
        this.types = project.getCameraTypes();
        this.timelines = project.getCameraTimelines();
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

        // Create a new VBox for vertical layout.
        this.viewPane = new VBox();

        // Add label at top
        initTitleLabel();

        // add space for textfields and lists
        this.centerPane = new HBox(40.0);
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(0, 0, 0, 0));
        this.centerPane.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        this.viewPane.getChildren().add(centerPane);
        
        initFields();
        initAdds();

        initButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }

    /**
     * Initialize title label.
     */
    private void initTitleLabel() {
        titleLabel = new Label("Edit the current project...");
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
     * Initialize the fields.
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
        initTimelineFields(content);

        this.centerPane.getChildren().add(content);
    }

    /**
     * Initializes name and description textfields.
     * @param content pane in which to intiialize.
     */
    private void initNameDescriptionFields(VBox content) {
        // init name field
        final Label nameLabel = new Label("Project name: ");
        nameField = new StyledTextfield(project.getName());
        nameField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        nameField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        nameField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        nameField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        HBox nameBox = new HBox(TweakingHelper.GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);

        // init description field
        final Label descriptionLabel = new Label("Project description: ");
        descriptionField = new StyledTextfield(project.getDescription());
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
     * Initialize timeline textfields.
     * @param content pane in which to initialize.
     */
    private void initTimelineFields(VBox content) {
        // init seconds per count field
        final Label secondsPerCountLabel = new Label("Seconds per count: ");
        secondsPerCountField = new NumberTextField(
                Double.toString(project.getSecondsPerCount()));
        secondsPerCountField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        secondsPerCountField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        secondsPerCountField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        secondsPerCountField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        HBox secondsPerCountBox = new HBox(TweakingHelper.GENERAL_SPACING);
        secondsPerCountBox.getChildren().addAll(secondsPerCountLabel, secondsPerCountField);
        secondsPerCountBox.setAlignment(Pos.CENTER_RIGHT);

        // init timeline description field (this ought to die)
        final Label directorTimelineDescriptionLabel = new Label("Director Timeline Description: ");
        directorDescriptionField = new StyledTextfield(
                project.getDirectorTimeline().getDescription());
        directorDescriptionField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        directorDescriptionField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        directorDescriptionField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        directorDescriptionField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        HBox directorTimelineDescriptionBox = new HBox(TweakingHelper.GENERAL_SPACING);
        directorTimelineDescriptionBox.getChildren().addAll(directorTimelineDescriptionLabel,
                directorDescriptionField);
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
        content.setStyle(centerRightStyle);

        // add camera type
        HBox cameraTypeContent = new HBox(TweakingHelper.GENERAL_SPACING);
        addCameraTypeButton = new StyledButton("Add Camera Type");
        addCameraTypeButton.setFillColor(Color.WHITE);
        addCameraTypeButton.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        deleteCameraTypeButton = new StyledButton("Delete Camera Type");
        deleteCameraTypeButton.setFillColor(Color.WHITE);
        deleteCameraTypeButton.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        cameraTypeContent.getChildren().addAll(addCameraTypeButton,
                deleteCameraTypeButton);
        cameraTypeList = new StyledListview<HBox>();
        cameraTypeList.setMinHeight(75);
        content.getChildren().addAll(cameraTypeContent, cameraTypeList);

        // add camera
        HBox cameraContent = new HBox(TweakingHelper.GENERAL_SPACING);
        addCameraButton = new StyledButton("Add Camera");
        addCameraButton.setFillColor(Color.WHITE);
        addCameraButton.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        deleteCameraButton = new StyledButton("Delete Camera");
        deleteCameraButton.setFillColor(Color.WHITE);
        deleteCameraButton.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        cameraContent.getChildren().addAll(addCameraButton, deleteCameraButton);
        cameraList = initCameraTypeList();
        content.getChildren().addAll(cameraContent, cameraList);

        this.centerPane.getChildren().add(content);
    }
    
    /**
     * Initialize the section for adding/deleting camera types.
     */
    private void initCameraTypeSection() {
        addCameraTypeButton = new StyledButton("Add Camera Type");
        deleteCameraTypeButton = new StyledButton("Delete Camera Type");
        cameraTypeList = initCameraTypeList();
        viewPane.getChildren().addAll(addCameraTypeButton, deleteCameraTypeButton, cameraTypeList);
    }
    
    /**
     * Initialize the camera type list.
     * @return the camera type list.
     */
    private StyledListview<HBox> initCameraTypeList() {
        StyledListview<HBox> result = new StyledListview<HBox>();
        result.setMinHeight(75);
        Set<CameraType> types = project.getDistinctCameraTypes();
        for (CameraType type: types) {
            HBox box = new HBox();
            box.getChildren().addAll(
                    new Label(type.getName()), new Label(" - "), new Label(type.getDescription()));
            result.getItems().add(box);
        }
        return result;
    }
    
    /**
     * Initialize the section for adding/deleting cameras.
     */
    private void initCameraSection() {
        addCameraButton = new StyledButton("Add Camera");
        deleteCameraButton = new StyledButton("Delete Camera");
        cameraList = initCameraList();
        viewPane.getChildren().addAll(addCameraButton, deleteCameraButton, cameraList);
    }
    
    /**
     * Initialize the camera list.
     * @return the camera list.
     */
    private ListView<HBox> initCameraList() {
        ListView<HBox> result = new ListView<HBox>();
        ArrayList<Camera> cameras = project.getCameras();
        for (Camera c: cameras) {
            HBox box = new HBox();
            box.getChildren().addAll(
                    new Label(c.getName()), new Label(" - "), new Label(c.getDescription()));
            result.getItems().add(box);
        }
        return result;
    }
    
    /**
     * Initialize the save/cancel buttons.
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
        saveButton = new StyledButton("Save");
        saveButton.setPrefWidth(buttonWidth);
        saveButton.setPrefHeight(buttonHeight);
        saveButton.setAlignment(Pos.CENTER);
        saveButton.setBorderColor(Color.WHITE);
        saveButton.setFillColor(TweakingHelper.COLOR_PRIMARY);

        this.buttonPane.getChildren().addAll(saveButton, cancelButton);
    }

}
