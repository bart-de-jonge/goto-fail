package gui.modal;

import data.CameraType;
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

    // three main colors used throughout window. Experiment a little!
    private Color mainColor = Color.rgb(255, 172, 70); // main bright color
    private Color secondaryColor = Color.rgb(255, 140, 0); // darker color
    private Color tertiaryColor = Color.rgb(255, 235, 190); // lighter color

    // variables for spacing
    private int topAreaHeight = 50;
    private int bottomAreaHeight = 60;

    // simple background styles of the three main areas.
    private String topStyle = "-fx-background-color: " + getStringFromColor(mainColor) + ";"
            + "-fx-text-fill: white; -fx-font-size: 22;"
            + "-fx-font-family: helvetica neue; -fx-font-weight: lighter;"
            + "-fx-border-width: 0 0 10 0;"
            + "-fx-border-color: " + getStringFromColor(secondaryColor) + ";";
    private String centerLeftStyle = "-fx-background-color: rgb(245, 245, 245);";
    private String centerRightStyle = "-fx-background-color: rgb(255, 255, 255);";
    private String bottomStyle = "-fx-background-color: " + getStringFromColor(mainColor) + ";";

    // variables for the Create and Cancel buttons
    private int buttonWidth = 90;
    private int buttonHeight = 25;
    private int buttonSpacing = 20;

    // variables for the title label
    private int titlelabelOffsetFromLeft = 20;

    /*
     * Other variables.
     */

    // No touching these constants. They work well for all general cases,
    // and there is no reason to change them ever again.
    private static final int GENERAL_SIZE = 10000;
    private static final int GENERAL_SPACING = 10;
    private static final int GENERAL_PADDING = 20;
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
        this.centerPane.setPadding(new Insets(0, GENERAL_PADDING, 0, 0));
        this.centerPane.setPrefHeight(GENERAL_SIZE);
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
        titleLabel.setPrefWidth(GENERAL_SIZE);
        titleLabel.setMinHeight(topAreaHeight);
        titleLabel.setPrefHeight(topAreaHeight);
        titleLabel.setMaxHeight(topAreaHeight);
        this.viewPane.getChildren().add(titleLabel);
    }
    
    /**
     * Initialize the fields.
     */
    private void initFields() {
        VBox content = new VBox(GENERAL_SPACING);
        content.setAlignment(Pos.CENTER);
        content.setMinWidth(TEXT_AREA_MIN_WIDTH);
        content.setPrefWidth(GENERAL_SIZE);
        content.setPrefHeight(GENERAL_SIZE);
        content.setPadding(new Insets(GENERAL_PADDING));
        content.setStyle(centerLeftStyle);

        final Label nameLabel = new Label("Name:  ");
        nameField = new StyledTextfield();
        nameField.setBorderColor(mainColor);
        nameField.setTextColor(mainColor);
        nameField.setTextActiveColor(secondaryColor);
        nameField.setFillActiveColor(tertiaryColor);
        HBox nameBox = new HBox(GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);
        
        final Label descriptionLabel = new Label("Description: ");
        descriptionField = new StyledTextfield();
        descriptionField.setBorderColor(mainColor);
        descriptionField.setTextColor(mainColor);
        descriptionField.setTextActiveColor(secondaryColor);
        descriptionField.setFillActiveColor(tertiaryColor);
        HBox descriptionBox = new HBox(GENERAL_SPACING);
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
        VBox content = new VBox(GENERAL_SPACING);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinWidth(LISTS_AREA_MIN_WIDTH);
        content.setPrefWidth(GENERAL_SIZE);
        content.setPrefHeight(GENERAL_SIZE);
        content.setPadding(new Insets(GENERAL_PADDING));
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
        addCameraButton.setFillColor(mainColor);

        // Add cancel button
        cancelButton = new StyledButton("Cancel");
        cancelButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setAlignment(Pos.CENTER);
        cancelButton.setBorderColor(Color.WHITE);
        cancelButton.setFillColor(mainColor);

        content.getChildren().addAll(addCameraButton, cancelButton);
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
