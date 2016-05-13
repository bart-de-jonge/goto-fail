package gui.modal;

import gui.headerarea.DoubleTextField;
import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledTextfield;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;

/**
 * Class responsible for displaying a modal view for the addition of a camera type.
 */
public class AddCameraTypeModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 450 and 300 work very, very well.
    private static final int width = 400;
    private static final int height = 300;

    // three main colors used throughout window. Experiment a little!
    private static final Color mainColor = Color.rgb(255, 172, 70); // main bright color
    private static final Color secondaryColor = Color.rgb(255, 140, 0); // darker color
    private static final Color tertiaryColor = Color.rgb(255, 235, 190); // lighter color

    // variables for spacing
    private static final int topAreaHeight = 50;
    private static final int bottomAreaHeight = 60;

    // simple background styles of the three main areas.
    private String topStyle = "-fx-background-color: " + getStringFromColor(mainColor) + ";"
            + "-fx-text-fill: white; -fx-font-size: 22;"
            + "-fx-font-family: helvetica neue; -fx-font-weight: lighter;"
            + "-fx-border-width: 0 0 10 0;"
            + "-fx-border-color: " + getStringFromColor(secondaryColor) + ";";
    private String centerStyle = "-fx-background-color: rgb(255, 255, 255);";
    private String bottomStyle = "-fx-background-color: " + getStringFromColor(mainColor) + ";";

    // variables for the Create and Cancel buttons
    private static final int buttonWidth = 90;
    private static final int buttonHeight = 25;
    private static final int buttonSpacing = 20;

    // variables for the title label
    private int titlelabelOffsetFromLeft = 20;

    /*
     * Other variables.
     */

    @Getter
    private StyledTextfield nameField;
    @Getter
    private StyledTextfield descriptionField;
    @Getter
    private DoubleTextField movementMarginField;
    @Getter
    private StyledButton addCameraTypeButton;
    @Getter
    private StyledButton cancelButton;
    @Getter
    private VBox viewPane;
    @Getter
    private Label titleLabel;
    
    public AddCameraTypeModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }
    
    /**
     * Construct a new AddCameraTypeModalView
     * @param rootPane the rootPane that uses this modal.
     * @param width the width of the modal screen
     * @param height the height of the modal screen
     */
    public AddCameraTypeModalView(RootPane rootPane,
                                  int width,
                                  int height) {
        super(rootPane, width, height);
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

        // Add textfields in the middle.
        initFields();

        // Add buttons at the bottom.
        initButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }

    /**
     * Initialize title label.
     */
    private void initTitleLabel() {
        titleLabel = new Label("Add a camera type...");
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
        content.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        content.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        content.setPadding(new Insets(TweakingHelper.GENERAL_PADDING));
        content.setStyle(centerStyle);

        final Label nameLabel = new Label("Name:  ");
        nameField = new StyledTextfield();
        nameField.setBorderColor(mainColor);
        nameField.setTextColor(mainColor);
        nameField.setTextActiveColor(secondaryColor);
        nameField.setFillActiveColor(tertiaryColor);
        HBox nameBox = new HBox(TweakingHelper.GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);
        
        final Label descriptionLabel = new Label("Description: ");
        descriptionField = new StyledTextfield();
        descriptionField.setBorderColor(mainColor);
        descriptionField.setTextColor(mainColor);
        descriptionField.setTextActiveColor(secondaryColor);
        descriptionField.setFillActiveColor(tertiaryColor);
        HBox descriptionBox = new HBox(TweakingHelper.GENERAL_SPACING);
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);
        descriptionBox.setAlignment(Pos.CENTER_RIGHT);
        
        final Label marginLabel = new Label("Movement margin (in seconds): ");
        movementMarginField = new DoubleTextField();
        movementMarginField.setBorderColor(mainColor);
        movementMarginField.setTextColor(mainColor);
        movementMarginField.setTextActiveColor(secondaryColor);
        movementMarginField.setFillActiveColor(tertiaryColor);
        HBox movementMarginBox = new HBox(TweakingHelper.GENERAL_SPACING);
        movementMarginBox.getChildren().addAll(marginLabel, movementMarginField);
        movementMarginBox.setAlignment(Pos.CENTER_RIGHT);
        content.getChildren().addAll(nameBox, descriptionBox, movementMarginBox);
        this.viewPane.getChildren().add(content);
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
        addCameraTypeButton = new StyledButton("Add");
        addCameraTypeButton.setPrefWidth(buttonWidth);
        addCameraTypeButton.setPrefHeight(buttonHeight);
        addCameraTypeButton.setAlignment(Pos.CENTER);
        addCameraTypeButton.setBorderColor(Color.WHITE);
        addCameraTypeButton.setFillColor(mainColor);

        // Add cancel button
        cancelButton = new StyledButton("Cancel");
        cancelButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setAlignment(Pos.CENTER);
        cancelButton.setBorderColor(Color.WHITE);
        cancelButton.setFillColor(mainColor);

        content.getChildren().addAll(addCameraTypeButton, cancelButton);
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
