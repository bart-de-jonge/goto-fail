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
 * @author Menno
 */
@Log4j2
public class NewProjectModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 350 work very, very well.
    private static final int width = 820;
    private static final int height = 450;

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
    private StyledListview<HBox> timelineList;
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
        nameField.setBorderColor(mainColor);
        nameField.setTextColor(mainColor);
        nameField.setTextActiveColor(secondaryColor);
        nameField.setFillActiveColor(tertiaryColor);
        HBox nameBox = new HBox(TweakingHelper.GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);

        // init description field
        final Label descriptionLabel = new Label("Project description: ");
        descriptionField = new StyledTextfield();
        descriptionField.setBorderColor(mainColor);
        descriptionField.setTextColor(mainColor);
        descriptionField.setTextActiveColor(secondaryColor);
        descriptionField.setFillActiveColor(tertiaryColor);
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
        secondsPerCountField.setBorderColor(mainColor);
        secondsPerCountField.setTextColor(mainColor);
        secondsPerCountField.setTextActiveColor(secondaryColor);
        secondsPerCountField.setFillActiveColor(tertiaryColor);
        HBox secondsPerCountBox = new HBox(TweakingHelper.GENERAL_SPACING);
        secondsPerCountBox.getChildren().addAll(secondsPerCountLabel, secondsPerCountField);
        secondsPerCountBox.setAlignment(Pos.CENTER_RIGHT);

        // init timeline description field (this ought to die)
        final Label directorTimelineDescriptionLabel = new Label("Director Timeline Description: ");
        directorTimelineDescriptionField = new StyledTextfield();
        directorTimelineDescriptionField.setBorderColor(mainColor);
        directorTimelineDescriptionField.setTextColor(mainColor);
        directorTimelineDescriptionField.setTextActiveColor(secondaryColor);
        directorTimelineDescriptionField.setFillActiveColor(tertiaryColor);
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
        addCameraTypeButton.setBorderColor(mainColor);
        cameraTypeList = new StyledListview<HBox>();
        cameraTypeList.setMinHeight(75);
        content.getChildren().addAll(addCameraTypeButton, cameraTypeList);

        // add camera
        addCameraButton = new StyledButton("Add Camera");
        addCameraButton.setFillColor(Color.WHITE);
        addCameraButton.setBorderColor(mainColor);
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
