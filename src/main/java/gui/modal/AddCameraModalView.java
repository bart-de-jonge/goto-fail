package gui.modal;

import data.CameraType;
import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledListview;
import gui.styling.StyledTextfield;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;

/**
 * Class responsible for displaying a modal view for the addition of a camera to the project.
 */
public class AddCameraModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 450 and 300 work very, very well.
    private static final int width = 600;
    private static final int height = 350;

    // variables for spacing
    private static final int topAreaHeight = 50;
    private static final int bottomAreaHeight = 60;
    
    private static final String BACKGROUND_STYLE_STRING = "-fx-background-color: ";

    // simple background styles of the three main areas.
    private String topStyle = BACKGROUND_STYLE_STRING
            + TweakingHelper.STRING_PRIMARY + ";"
            + "-fx-text-fill: white; -fx-font-size: 22;"
            + "-fx-font-family: helvetica neue; -fx-font-weight: lighter;"
            + "-fx-border-width: 0 0 10 0;"
            + "-fx-border-color: "
            + TweakingHelper.STRING_SECONDARY + ";";
    private String centerLeftStyle = BACKGROUND_STYLE_STRING
            + TweakingHelper.STRING_BACKGROUND_HIGH + ";";
    private String centerRightStyle = BACKGROUND_STYLE_STRING
            + TweakingHelper.STRING_BACKGROUND + ";";
    private String bottomStyle = BACKGROUND_STYLE_STRING
            + TweakingHelper.STRING_PRIMARY + ";";

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
    private static final int TEXT_AREA_MIN_WIDTH = 280;
    private static final int LISTS_AREA_MIN_WIDTH = 250;

    @Getter
    private StyledTextfield nameField;
    @Getter
    private StyledTextfield descriptionField;
    @Getter
    private StyledListview<Label> cameraTypes;
    @Getter
    private StyledButton addCameraButton;
    @Getter
    private StyledButton cancelButton;
    @Getter
    private VBox viewPane;
    private HBox centerPane;
    @Getter
    private Label titleLabel;
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
     * Initialize title label.
     */
    private void initTitleLabel() {
        titleLabel = new Label("Add a camera...");
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
        content.setAlignment(Pos.CENTER);
        content.setMinWidth(TEXT_AREA_MIN_WIDTH);
        content.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        content.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        content.setPadding(new Insets(TweakingHelper.GENERAL_PADDING));
        content.setStyle(centerLeftStyle);

        final Label nameLabel = new Label("Name:  ");
        nameField = new StyledTextfield();
        nameField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        nameField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        nameField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        nameField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        HBox nameBox = new HBox(TweakingHelper.GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);
        
        final Label descriptionLabel = new Label("Description: ");
        descriptionField = new StyledTextfield();
        descriptionField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        descriptionField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        descriptionField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        descriptionField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        HBox descriptionBox = new HBox(TweakingHelper.GENERAL_SPACING);
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);
        descriptionBox.setAlignment(Pos.CENTER_RIGHT);
        
        content.getChildren().addAll(nameBox, descriptionBox);
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
        // setup button pane
        HBox content = new HBox();
        content.setSpacing(buttonSpacing);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinHeight(bottomAreaHeight);
        content.setPrefHeight(bottomAreaHeight);
        content.setMaxHeight(bottomAreaHeight);
        content.setStyle(bottomStyle);
        content.setPadding(new Insets(0, titlelabelOffsetFromLeft,
                0, titlelabelOffsetFromLeft));
        this.viewPane.getChildren().add(content);

        // Add adding button
        addCameraButton = new StyledButton("Add");
        addCameraButton.setPrefWidth(buttonWidth);
        addCameraButton.setPrefHeight(buttonHeight);
        addCameraButton.setAlignment(Pos.CENTER);
        addCameraButton.setBorderColor(Color.WHITE);
        addCameraButton.setFillColor(TweakingHelper.COLOR_PRIMARY);

        // Add cancel button
        cancelButton = new StyledButton("Cancel");
        cancelButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setAlignment(Pos.CENTER);
        cancelButton.setBorderColor(Color.WHITE);
        cancelButton.setFillColor(TweakingHelper.COLOR_PRIMARY);

        content.getChildren().addAll(addCameraButton, cancelButton);
    }

}
