package gui.modal;

import data.Camera;
import data.CameraTimeline;
import data.CameraType;
import gui.headerarea.NumberTextField;
import gui.root.RootPane;
import gui.styling.StyledButton;
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
 * @author Menno
 */
@Log4j2
public class NewProjectModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 350 work very, very well.
    private static final int width = 680;
    private static final int height = 450;

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
    private static final int TEXT_AREA_MIN_WIDTH = 300;
    private static final int LISTS_AREA_MIN_WIDTH = 300;
    private static final int TOP_BOTTOM_AREA_HEIGHT = 60;

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
    private ListView<HBox> timelineList;
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

    // Effects
    private InnerShadow topInnerShadow;
    private InnerShadow topOuterShadow;
    private DropShadow bottomOuterShadow;

    // Misc
    @Getter
    private ArrayList<CameraType> cameraTypes;
    @Getter
    private ArrayList<Camera> cameras;
    @Getter
    private ArrayList<CameraTimeline> timelines;

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
        getModalStage().setHeight(height);
        getModalStage().setWidth(width);
        getModalStage().setMinWidth(width);
        getModalStage().setMinHeight(height);

        this.viewPane = new VBox();

        initTitleLabel();

        this.centerPane = new HBox(40.0);
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(0, 0, 0, 0));
        this.centerPane.setPrefHeight(GENERAL_SIZE);
        this.centerPane.setStyle(centerStyle);
        this.viewPane.getChildren().add(centerPane);

        initFields();
        initAdds();

        initButtons();

        initEffects();
        
        super.setModalView(this.viewPane);
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
        this.titleLabel.setEffect(topInnerShadow);
        this.centerPane.setEffect(topOuterShadow);
        this.buttonPane.setEffect(bottomOuterShadow);
    }

    /**
     * Initialize title label.
     */
    private void initTitleLabel() {
        titleLabel = new Label("Create a new project...");
        titleLabel.setStyle(topStyle);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        titleLabel.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        titleLabel.setPrefWidth(GENERAL_SIZE);
        titleLabel.setMinHeight(TOP_BOTTOM_AREA_HEIGHT);
        titleLabel.setPrefHeight(TOP_BOTTOM_AREA_HEIGHT);
        titleLabel.setMaxHeight(TOP_BOTTOM_AREA_HEIGHT);
        this.viewPane.getChildren().add(titleLabel);
    }

    /**
     * Initialize fields.
     */
    private void initFields() {
        VBox content = new VBox(GENERAL_SPACING);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinWidth(TEXT_AREA_MIN_WIDTH);
        content.setPrefWidth(GENERAL_SIZE);
        content.setPrefHeight(GENERAL_SIZE);
        content.setPadding(new Insets(GENERAL_PADDING));

        final Label nameLabel = new Label("Project name: ");
        nameField = new StyledTextfield();
        HBox nameBox = new HBox(GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);

        final Label descriptionLabel = new Label("Project description: ");
        descriptionField = new StyledTextfield();
        HBox descriptionBox = new HBox(GENERAL_SPACING);
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);
        descriptionBox.setAlignment(Pos.CENTER_RIGHT);

        final Label secondsPerCountLabel = new Label("Seconds per count: ");
        secondsPerCountField = new NumberTextField();
        HBox secondsPerCountBox = new HBox(GENERAL_SPACING);
        secondsPerCountBox.getChildren().addAll(secondsPerCountLabel, secondsPerCountField);
        secondsPerCountBox.setAlignment(Pos.CENTER_RIGHT);

        final Label directorTimelineDescriptionLabel = new Label("Director Timeline Description: ");
        directorTimelineDescriptionField = new StyledTextfield();
        HBox directorTimelineDescriptionBox = new HBox(GENERAL_SPACING);
        directorTimelineDescriptionBox.getChildren().addAll(directorTimelineDescriptionLabel,
                directorTimelineDescriptionField);
        directorTimelineDescriptionBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(nameBox, descriptionBox,
                secondsPerCountBox, directorTimelineDescriptionBox);
        this.centerPane.getChildren().add(content);
    }

    /**
     * Initialize camera type and camera adding.
     */
    private void initAdds() {
        // vertical pane to hold content
        VBox content = new VBox(GENERAL_SPACING);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinWidth(LISTS_AREA_MIN_WIDTH);
        content.setPrefWidth(GENERAL_SIZE);
        content.setPrefHeight(GENERAL_SIZE);
        content.setPadding(new Insets(GENERAL_PADDING));

        // add camera type
        addCameraTypeButton = new StyledButton("Add Camera Type");
        cameraTypeList = new ListView<HBox>();
        cameraTypeList.setMinHeight(75);
        content.getChildren().addAll(addCameraTypeButton, cameraTypeList);

        // add camera
        addCameraButton = new StyledButton("Add Camera");
        cameraList = new ListView<HBox>();
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
        this.buttonPane.setMinHeight(TOP_BOTTOM_AREA_HEIGHT);
        this.buttonPane.setPrefHeight(TOP_BOTTOM_AREA_HEIGHT);
        this.buttonPane.setMaxHeight(TOP_BOTTOM_AREA_HEIGHT);
        this.buttonPane.setStyle(bottomStyle);
        this.buttonPane.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        this.viewPane.getChildren().add(buttonPane);

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



}
