package gui.modal;

import gui.headerarea.DoubleTextField;
import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledCheckbox;
import gui.styling.StyledTextfield;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

public class ShotCreationModalView extends ModalView {
    
 // variables for spacing
    protected static final int topAreaHeight = 70;
    protected static final int bottomAreaHeight = 60;
    
    protected static final String BACKGROUND_STYLE_STRING = "-fx-background-color: ";

    // simple background styles of the three main areas.
    protected String topStyle = TweakingHelper.constructDefaultModalTopStyle(26);
    protected String centerLeftStyle = BACKGROUND_STYLE_STRING
            + TweakingHelper.getBackgroundHighString() + ";";
    protected String centerRightStyle = BACKGROUND_STYLE_STRING
            + TweakingHelper.getBackgroundString() + ";";
    protected String bottomStyle = BACKGROUND_STYLE_STRING
            + TweakingHelper.getColorString(0) + ";";

    // variables for the Create and Cancel buttons
    protected static final int buttonWidth = 90;
    protected static final int buttonHeight = 25;
    protected static final int buttonSpacing = 20;
    
    private static final int CAMERA_AREA_MIN_WIDTH = 250;
    private static final int TEXT_AREA_MIN_WIDTH = 350;

    // variables for the title label
    protected static final int titlelabelOffsetFromLeft = 20;
    
    @Setter
    protected String defaultStartCount = "0";
    @Setter
    protected String defaultEndCount = "1";
    
    protected VBox rootPane;
    protected HBox centerPane;
    protected FlowPane checkboxPane;
    protected HBox buttonPane;
    @Getter
    protected Label titleLabel;

    // Text fields
    @Getter
    protected StyledTextfield descriptionField;
    @Getter
    protected StyledTextfield nameField;
    @Getter
    protected DoubleTextField startField;
    @Getter
    protected DoubleTextField endField;
    
 // Buttons
    @Getter
    protected StyledButton creationButton;
    @Getter
    protected StyledButton cancelButton;
    @Getter
    protected List<StyledCheckbox> cameraCheckboxes;
    
    public ShotCreationModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);
    }
    
    /**
     * Initialize title label.
     * @param text the text to put in the title label
     */
    protected void initTitleLabel(String text) {
        titleLabel = new Label(text);
        titleLabel.setStyle(topStyle);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        titleLabel.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        titleLabel.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        titleLabel.setMinHeight(topAreaHeight);
        titleLabel.setPrefHeight(topAreaHeight);
        titleLabel.setMaxHeight(topAreaHeight);
        this.rootPane.getChildren().add(titleLabel);
    }
    
    /**
     * Initializes name and description textfields.
     * @param content pane in which to intiialize.
     */
    protected void initNameDescriptionFields(VBox content) {
        // init name field
        final Label nameLabel = new Label("Shot Name: ");
        nameField = new StyledTextfield();
        HBox nameBox = new HBox(TweakingHelper.GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);

        // init description field
        final Label descripLabel = new Label("Shot Description: ");
        descriptionField = new StyledTextfield();
        HBox descriptionBox = new HBox(TweakingHelper.GENERAL_SPACING);
        descriptionBox.getChildren().addAll(descripLabel, descriptionField);
        descriptionBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(nameBox, descriptionBox);
    }
    
    /**
     * Style the checkboxes for cameras.
     */
    protected void styleCamCheckBoxes() {
        this.checkboxPane = new FlowPane();
        this.checkboxPane.setHgap(TweakingHelper.GENERAL_PADDING);
        this.checkboxPane.setVgap(TweakingHelper.GENERAL_PADDING);
        this.checkboxPane.setMinWidth(CAMERA_AREA_MIN_WIDTH);
        this.checkboxPane.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        this.checkboxPane.setAlignment(Pos.CENTER);
        this.checkboxPane.setStyle(centerRightStyle);
    }
    
    /**
     * Initializes pane with buttons at bottom.
     */
    protected void initButtons() {
        // setup button pane
        this.buttonPane = new HBox();
        this.buttonPane.setSpacing(buttonSpacing);
        this.buttonPane.setAlignment(Pos.CENTER_LEFT);
        this.buttonPane.setMinHeight(bottomAreaHeight);
        this.buttonPane.setPrefHeight(bottomAreaHeight);
        this.buttonPane.setMaxHeight(bottomAreaHeight);
        this.buttonPane.setStyle(bottomStyle);
        this.buttonPane.setPadding(new Insets(0, titlelabelOffsetFromLeft,
                0, titlelabelOffsetFromLeft));
        this.rootPane.getChildren().add(buttonPane);

        // Add cancel button
        cancelButton = createButton("Cancel", false);

        // Add creation button
        creationButton = createButton("Create", false);

        this.buttonPane.getChildren().addAll(creationButton, cancelButton);
    }
    
    /**
     * Initialize the start and end count text fields.
     * @param content the pane in which they are located.
     */
    protected void initCountTextfields(VBox content) {
        // init start field
        final Label startLabel = new Label("Start:");
        startField = new DoubleTextField();
        startField.setText(this.defaultStartCount);
        HBox startBox = new HBox(TweakingHelper.GENERAL_SPACING);
        startBox.getChildren().addAll(startLabel, startField);
        startBox.setAlignment(Pos.CENTER_RIGHT);

        // init end field
        final Label endLabel = new Label("End:");
        endField = new DoubleTextField();
        endField.setText(this.defaultEndCount);
        HBox endBox = new HBox(TweakingHelper.GENERAL_SPACING);
        endBox.getChildren().addAll(endLabel, endField);
        endBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(startBox, endBox);
    }
    
    /**
     * Get the box containing the text fields.
     * @return the box containing the text fields
     */
    protected VBox getTextfieldBox() {
        VBox content = new VBox(TweakingHelper.GENERAL_SPACING);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinWidth(TEXT_AREA_MIN_WIDTH);
        content.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        content.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        content.setPadding(new Insets(TweakingHelper.GENERAL_PADDING));
        content.setStyle(centerLeftStyle);
        return content;
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
