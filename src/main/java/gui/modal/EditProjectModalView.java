package gui.modal;

import java.util.ArrayList;

import data.Camera;
import data.CameraTimeline;
import data.CameraType;
import data.Instrument;
import data.ScriptingProject;
import gui.headerarea.DoubleTextField;
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
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Class responsible for displaying a modal view for editing a project.
 */
@Log4j2
public class EditProjectModalView extends ModalView {

    /*
     * Tweakable styling variables
     */

    // preferred width and height of screen.
    private static final int width = 900;
    private static final int height = 700;

    // variables for spacing
    private static final int topAreaHeight = 70;
    private static final int bottomAreaHeight = 60;

    // simple background styles of the three main areas.
    private String topStyle = ModalUtilities.constructDefaultModalTopStyle(26);
    private String centerLeftStyle = BACKGROUND_COLOR_STRING
            + TweakingHelper.getBackgroundHighString() + ";";
    private String centerRightStyle = BACKGROUND_COLOR_STRING
            + TweakingHelper.getBackgroundString() + ";";
    private String bottomStyle = BACKGROUND_COLOR_STRING
            + TweakingHelper.getColorString(0) + ";";

    // variables for the Create and Cancel buttons
    private static final int buttonWidth = 155;
    private static final int buttonSpacing = 20;

    // variables for the title label
    private static final int titlelabelOffsetFromLeft = 20;

    /*
     * Other variables
     */

    // No touching these constants. They work well for all general cases,
    // and there is no reason to change them ever again.
    private static final int TEXT_AREA_MIN_WIDTH = 300;
    private static final int LISTS_AREA_MIN_WIDTH = 500;

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
    private StyledTextfield directorTimelineDescriptionField;


    // Text fields

    @Getter
    private StyledTextfield nameField;
    @Getter
    private StyledTextfield descriptionField;
    @Getter
    private DoubleTextField secondsPerCountField;
    

    // buttons
    @Getter
    private StyledButton addCameraButton;
    @Getter
    private StyledButton editCameraButton;
    @Getter
    private StyledButton deleteCameraButton;
    @Getter
    private StyledButton addCameraTypeButton;
    @Getter
    private StyledButton editCameraTypeButton;
    @Getter
    private StyledButton deleteCameraTypeButton;
    @Getter
    private StyledButton applyButton;
    @Getter
    private StyledButton cancelButton;
    
    @Getter
    private Label titleLabel;

    // Misc
    @Getter
    private ScriptingProject project;
    @Getter
    private ArrayList<CameraType> cameraTypes;
    @Getter
    private ArrayList<Camera> cameras;
    @Getter
    private ArrayList<CameraTimeline> timelines;
    @Getter
    private ArrayList<Instrument> instruments;

    private boolean fillWithCurrentProjectInfo;
    @Getter
    private StyledButton addInstrumentButton;
    @Getter
    private StyledButton editInstrumentButton;
    @Getter
    private StyledButton deleteInstrumentButton;
    @Getter
    private StyledListview<HBox> instrumentList;
    
    private static final String BACKGROUND_COLOR_STRING = "-fx-background-color: ";
    
    /**
     * Construct a new EditProjectModalView.
     * @param rootPane the rootPane for this modal.
     * @param fillWithCurrentProjectInfo as variable name says.
     */
    public EditProjectModalView(RootPane rootPane, boolean fillWithCurrentProjectInfo) {
        this(rootPane, fillWithCurrentProjectInfo, width, height);
    }
    
    /**
     * Construct a new EditProjectModalView.
     * @param rootPane the rootPane for this modal.
     * @param fillWithCurrentProjectInfo if the modal should be filled with current project info
     * @param width the width of this modal
     * @param height the height of this modal
     */
    public EditProjectModalView(RootPane rootPane, 
            boolean fillWithCurrentProjectInfo, int width, int height) {
        super(rootPane, width, height);
        this.fillWithCurrentProjectInfo = fillWithCurrentProjectInfo;
        this.rootPane = rootPane;
        this.project = rootPane.getControllerManager().getScriptingProject();
        this.cameras =  new ArrayList<>();
        this.cameraTypes = new ArrayList<>();
        this.timelines = new ArrayList<>();
        this.instruments = new ArrayList<>();

        if (fillWithCurrentProjectInfo) {
            ArrayList<Camera> projectCameras = project.getCameras();
            projectCameras.forEach(e -> cameras.add(e.clone()));
            ArrayList<CameraType> projectTypes = project.getCameraTypes();
            projectTypes.forEach(e -> cameraTypes.add(e.clone()));
            ArrayList<CameraTimeline> projectTimelines = project.getCameraTimelines();
            projectTimelines.forEach(e -> timelines.add(e.clone()));
            ArrayList<Instrument> projectInstruments = project.getInstruments();
            projectInstruments.forEach(e -> instruments.add(e.clone()));
        }
        initializeView();
    }

    /**
     * Initialize the view of this modal.
     */
    private void initializeView() {

        // force minimum size
        forceBounds(height, width);

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
        
        if (fillWithCurrentProjectInfo) {
            fillInformation();
        }

        super.setModalView(this.viewPane);
        super.displayModal();
    }

    /**
     * Fill in the information of the current project.
     */
    private void fillInformation() {
        nameField.setText(project.getName());
        descriptionField.setText(project.getDescription());
        secondsPerCountField.setText(Double.toString(project.getSecondsPerCount()));
        
        directorTimelineDescriptionField.setText(project.getDirectorTimeline().getDescription());
        initCameraTypeList(cameraTypeList);
        initCameraList(cameraList);
        initInstrumentList(instrumentList);
    }
    
    
    /**
     * Initialize title label.
     */
    private void initTitleLabel() {
        titleLabel = ModalUtilities.constructTitleLabel(topStyle, topAreaHeight);
        titleLabel.setText( fillWithCurrentProjectInfo
                ? "Edit the current project..." : "Create a new project...");
        this.viewPane.getChildren().add(titleLabel);
    }


    /**
     * Initialize the fields.
     */
    private void initFields() {
        VBox content = ModalUtilities.constructFieldsPane();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinWidth(TEXT_AREA_MIN_WIDTH);
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
        nameField = new StyledTextfield();
        HBox nameBox = new HBox(TweakingHelper.GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);

        // init description field
        final Label descriptionLabel = new Label("Project description: ");
        descriptionField = new StyledTextfield();
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
        secondsPerCountField = new DoubleTextField();
        HBox secondsPerCountBox = new HBox(TweakingHelper.GENERAL_SPACING);
        secondsPerCountBox.getChildren().addAll(secondsPerCountLabel, secondsPerCountField);
        secondsPerCountBox.setAlignment(Pos.CENTER_RIGHT);

        // init timeline description field (this ought to die)
        final Label directorTimelineDescriptionLabel = new Label("Director Timeline Description: ");
        directorTimelineDescriptionField = new StyledTextfield();
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
        content.setStyle(centerRightStyle);

        // add camera type
        initCameraTypeAdd(content);

        // add camera
        initCameraAdd(content);
        
        initInstrumentAdd(content);

        this.centerPane.getChildren().add(content);
    }
    
    /**
     * Initialize the instrument add.
     * @param content the content to put this in
     */
    private void initInstrumentAdd(VBox content) {
        addInstrumentButton = createButton("Add Instrument", true);
        editInstrumentButton = createButton("Edit Instrument", true);
        deleteInstrumentButton = createButton("Delete Instrument", true);
        addInstrumentButton.setPrefWidth(buttonWidth);
        editInstrumentButton.setPrefWidth(buttonWidth);
        deleteInstrumentButton.setPrefWidth(buttonWidth);
        HBox instrumentContent = new HBox(TweakingHelper.GENERAL_SPACING);
        instrumentContent.getChildren().addAll(addInstrumentButton, editInstrumentButton,
                deleteInstrumentButton);
        instrumentList = new StyledListview<HBox>();
        content.getChildren().addAll(instrumentContent, instrumentList);
    }
    
    /**
     * Initialize the camera type add.
     * @param content the content to put this in
     */
    private void initCameraTypeAdd(VBox content) {
        addCameraTypeButton = createButton("Add Camera Type", true);
        editCameraTypeButton = createButton("Edit Camera Type", true);
        deleteCameraTypeButton = createButton("Delete Camera Type", true);
        addCameraTypeButton.setPrefWidth(buttonWidth);
        editCameraTypeButton.setPrefWidth(buttonWidth);
        deleteCameraTypeButton.setPrefWidth(buttonWidth);
        HBox cameraTypeContent = new HBox(TweakingHelper.GENERAL_SPACING);
        cameraTypeContent.getChildren().addAll(addCameraTypeButton, editCameraTypeButton,
                deleteCameraTypeButton);
        cameraTypeList = new StyledListview<HBox>();
        content.getChildren().addAll(cameraTypeContent, cameraTypeList);
    }
    
    /**
     * Initialize the camera add.
     * @param content the content to put this in
     */
    private void initCameraAdd(VBox content) {
        addCameraButton = createButton("Add Camera", true);
        editCameraButton = createButton("Edit Camera", true);
        deleteCameraButton = createButton("Delete Camera", true);
        addCameraButton.setPrefWidth(buttonWidth);
        editCameraButton.setPrefWidth(buttonWidth);
        deleteCameraButton.setPrefWidth(buttonWidth);
        HBox cameraContent = new HBox(TweakingHelper.GENERAL_SPACING);
        cameraContent.getChildren().addAll(addCameraButton, editCameraButton, deleteCameraButton);
        cameraList = new StyledListview<>();
        content.getChildren().addAll(cameraContent, cameraList);
    }
    
    /**
     * Initialize the camera type list.
     * @param typeList the list that should be initiated.
     */
    private void initCameraTypeList(ListView<HBox> typeList) {
        typeList.setMinHeight(75);
        for (CameraType type: cameraTypes) {
            HBox box = new HBox();
            box.getChildren().addAll(
                    new Label(type.getName()), new Label(" - "), new Label(type.getDescription()));
            typeList.getItems().add(box);
        }
    }

    /**
     * Initialize the camera list.
     * @param cameraList the camera list that should be initiated
     */
    private void initCameraList(ListView<HBox> cameraList) {
        cameraList.setMinHeight(75);
        ArrayList<Camera> cameras = project.getCameras();
        for (Camera c: cameras) {
            HBox box = new HBox();
            box.getChildren().addAll(
                    new Label(c.getName()), new Label(" - "), new Label(c.getDescription()));
            cameraList.getItems().add(box);
        }
    }
    
    /**
     * Initialize the list of instruments.
     * @param instrumentList the instruments to put in
     */
    private void initInstrumentList(ListView<HBox> instrumentList) {
        System.out.println("Adding instruments");
        System.out.println(project.getInstruments().size());
        instrumentList.setMinHeight(75);
        ArrayList<Instrument> instruments = project.getInstruments();
        for (Instrument i : instruments) {
            HBox box = new HBox();
            if (i.getDescription().isEmpty()) {
                box.getChildren().add(new Label(i.getName()));
            } else {
                box.getChildren().addAll(new Label(i.getName()), 
                        new Label(" - "), new Label(i.getDescription()));
            }
            instrumentList.getItems().add(box);
        }
    }
    


    
    /**
     * Initialize the save/cancel buttons.
     */
    private void initButtons() {
        // setup button pane
        this.buttonPane = ModalUtilities.constructButtonPane();
        this.viewPane.getChildren().add(buttonPane);

        // Add cancel button
        cancelButton = createButton("Cancel", false);
        // Add creation button
        applyButton = createButton("Apply", false);

        this.buttonPane.getChildren().addAll(applyButton, cancelButton);
    }

}
