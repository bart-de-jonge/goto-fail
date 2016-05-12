package gui.modal;

import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;

public class SaveModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 350 work very, very well.
    private static final int width = 450;
    private static final int height = 200;

    // three main colors used throughout window. Experiment a little!
    private Color mainColor = Color.rgb(255, 172, 70); // main bright color
    private Color secondaryColor = Color.rgb(255, 140, 0); // darker color

    // variables for spacing
    private int topAreaHeight = 80;
    private int bottomAreaHeight = 60;

    // simple background styles of the three main areas.
    private String topStyle = "-fx-background-color: " + getStringFromColor(mainColor) + ";"
            + "-fx-text-fill: white; -fx-font-size: 20;"
            + "-fx-font-family: helvetica neue; -fx-font-weight: lighter;"
            + "-fx-border-width: 0 0 10 0;"
            + "-fx-border-color: " + getStringFromColor(secondaryColor) + ";";

    // variables for the buttons
    private int buttonWidth = 120;
    private int buttonHeight = 25;
    private int buttonSpacing = 20;

    // variables for the title label
    private int titlelabelOffsetFromLeft = 20;

    /*
     * Other variables
     */

    // No touching these constants. They work well for all general cases,
    // and there is no reason to change them ever again.
    private static final int GENERAL_SIZE = 10000;
    private static final int GENERAL_SPACING = 10;
    private static final int GENERAL_PADDING = 20;

    private Label informationLabel;
    @Getter
    private StyledButton saveButton;
    @Getter
    private StyledButton dontSaveButton;
    @Getter
    private StyledButton cancelButton;
    private VBox viewPane;

    /**
     * Constructor of class.
     * @param rootPane the rootpane from which this is defined.
     */
    public SaveModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }

    /**
     * Constructor of class.
     * @param rootPane the rootpane from which this is defined.
     * @param width min. width of screen.
     * @param height min. height of screen.
     */
    public SaveModalView(RootPane rootPane, int width, int height) {
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
        initInformationLabel();

        // add buttons at bottom.
        initButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }

    /**
     * Initialize title label.
     */
    private void initInformationLabel() {
        informationLabel = new Label("There are unsaved changes\nDo you want to save them?");
        informationLabel.setStyle(topStyle);
        informationLabel.setAlignment(Pos.CENTER);
        informationLabel.setPadding(new Insets(0, titlelabelOffsetFromLeft,
                0, titlelabelOffsetFromLeft));
        informationLabel.setPrefWidth(GENERAL_SIZE);
        informationLabel.setMinHeight(topAreaHeight);
        informationLabel.setPrefHeight(topAreaHeight);
        informationLabel.setMaxHeight(topAreaHeight);
        this.viewPane.getChildren().add(informationLabel);
    }

    /**
     * Initialize the buttons.
     */
    private void initButtons() {
        HBox content = new HBox(GENERAL_SPACING);
        content.setSpacing(buttonSpacing);
        content.setAlignment(Pos.CENTER);
        content.setPrefHeight(GENERAL_SIZE);
        content.setPadding(new Insets(0, titlelabelOffsetFromLeft,
                0, titlelabelOffsetFromLeft));
        this.viewPane.getChildren().add(content);

        saveButton = new StyledButton("Save");
        saveButton.setPrefWidth(buttonWidth);
        saveButton.setPrefHeight(buttonHeight);
        saveButton.setFillColor(Color.WHITE);
        saveButton.setBorderColor(mainColor);

        dontSaveButton = new StyledButton("Don't save");
        dontSaveButton.setPrefWidth(buttonWidth);
        dontSaveButton.setPrefHeight(buttonHeight);
        dontSaveButton.setFillColor(Color.WHITE);
        dontSaveButton.setBorderColor(mainColor);

        cancelButton = new StyledButton("Cancel");
        cancelButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setFillColor(Color.WHITE);
        cancelButton.setBorderColor(mainColor);

        content.getChildren().addAll(saveButton, dontSaveButton, cancelButton);
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
