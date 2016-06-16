package gui.modal;

import java.util.ArrayList;

import data.Camera;
import data.CameraType;
import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledListview;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * Class responsible for displaying a modal view for the addition of a camera to the project.
 */
public class AddCameraModalView extends CameraModificationModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 450 and 300 work very, very well.
    private static final int width = 680;
    private static final int height = 350;
    
    private static final String BACKGROUND_STYLE_STRING = "-fx-background-color: ";

    // simple background styles of the three main areas.
    private String centerRightStyle = BACKGROUND_STYLE_STRING
            + TweakingHelper.getBackgroundString() + ";";

    /*
     * Other variables.
     */

    // No touching these constants. They work well for all general cases,
    // and there is no reason to change them ever again.
    private static final int TEXT_AREA_MIN_WIDTH = 280;
    private static final int LISTS_AREA_MIN_WIDTH = 250;
    
    @Getter
    private StyledListview<Label> cameraTypes;
    @Getter
    private StyledButton addCameraButton;

    private HBox centerPane;
    
    @Getter
    private ArrayList<CameraType> cameraTypeList;

    public AddCameraModalView(RootPane rootPane,
                              ArrayList<CameraType> types) {
        this(rootPane, types, width, height);
    }
    
    /**
     * Construct a new AddCameraModalView.
     * @param rootPane the rootPane that uses this modal
     * @param types the camera types that can be used
     * @param width the width of the modal screen
     * @param height the height of the modal screen
     */
    public AddCameraModalView(RootPane rootPane,
                              ArrayList<CameraType> types, 
                              int width, 
                              int height) {
        super(rootPane, width, height);
        this.cameraTypeList = types;
        initializeView();
    }
    
    /**
     * Constructor that fills in data (used for edit).
     * @param rootPane the rootPane that uses the modal
     * @param types the camera types that are available
     * @param camera the camera displayed
     * @param typeIndex the index of the camera type that the camera uses
     */
    public AddCameraModalView(RootPane rootPane, ArrayList<CameraType> types,
            Camera camera,int typeIndex) {
        this(rootPane, types, width, height);
        this.nameField.setText(camera.getName());
        this.descriptionField.setText(camera.getDescription());
        this.cameraTypes.getSelectionModel().select(typeIndex);
        this.addCameraButton.setText("Save");
        this.titleLabel.setText("Edit a camera");
        
    }
    
    /**
     * Initialize the view of this modal.
     */
    private void initializeView() {
        // force minimum size
        forceBounds(height, width);

        // Create a new VBox for vertical layout
        this.viewPane = new VBox();

        // Add label at top
        initTitleLabel();

        // add space for textfields and listviews
        this.centerPane = new HBox();
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(0, TweakingHelper.GENERAL_PADDING, 0, 0));
        this.centerPane.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        this.centerPane.setSpacing(40.0);
        this.viewPane.getChildren().add(centerPane);

        // Actually add textfields and listviews
        initFields();
        initTypeList();

        initButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }

    
    
    /**
     * Initialize the fields.
     */
    private void initFields() {
        VBox content = initNameDescriptionFields();
        this.centerPane.getChildren().add(content);
    }
    
    /**
     * Initialize the list of camera types.
     */
    private void initTypeList() {
        // vertical pane to hold content
        VBox content = new VBox(TweakingHelper.GENERAL_SPACING);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinWidth(LISTS_AREA_MIN_WIDTH);
        content.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        content.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        content.setPadding(new Insets(TweakingHelper.GENERAL_PADDING));
        content.setStyle(centerRightStyle);

        final Label cameraTypeLabel = new Label("Camera Type: ");
        cameraTypes = new StyledListview<>();
        cameraTypes.setMaxHeight(100);
        for (CameraType type: cameraTypeList) {
            cameraTypes.getItems().add(new Label(type.getName()));
        }
        
        content.getChildren().addAll(cameraTypeLabel, cameraTypes);
        this.centerPane.getChildren().add(content);
    }

    /**
     * Initialize the buttons.
     */
    private void initButtons() {
        // Add adding button
        addCameraButton = createButton("Add", false);
        initCancelButton();
        //HBox content = initHBoxForButtons();
        initHBoxForButtons().getChildren().addAll(addCameraButton, cancelButton);
    }

}
