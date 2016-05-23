package gui.modal;

import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;

public class StartupModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 550 and 200 work very, very well.
    private static final int width = 550;
    private static final int height = 200;

    // variables for spacing
    private static final int topAreaHeight = 80;
    private static final int bottomAreaHeight = 60;

    // simple background styles of the three main areas.
    private String topStyle = "-fx-background-color: " + TweakingHelper.getPrimaryString() + ";"
            + "-fx-text-fill: white; -fx-font-size: 20;"
            + "-fx-font-family: helvetica neue; -fx-font-weight: lighter;"
            + "-fx-border-width: 0 0 10 0;"
            + "-fx-border-color: " + TweakingHelper.getSecondaryString() + ";";

    // variables for the buttons
    private int buttonWidth = 120;
    private int buttonHeight = 25;
    private int buttonSpacing = 20;

    // variables for the title label
    private static final int titlelabelOffsetFromLeft = 20;

    /*
     * Other variables
     */

    // No touching these constants. They work well for all general cases,
    // and there is no reason to change them ever again.
    private static final int GENERAL_SIZE = 10000;
    private static final int GENERAL_SPACING = 10;
    private static final int GENERAL_PADDING = 20;

    private static final String loadFailedTitle =
            "An error occurred when loading the last project!\n"
            + "Please create a new project, or load an existing one.";
    private static final String noLoadTitle =
            "Welcome!\n"
            + "Please create a new project, or load an existing one.";

    private Label informationLabel;
    @Getter
    private StyledButton newButton;
    @Getter
    private StyledButton loadButton;
    @Getter
    private StyledButton exitButton;
    private VBox viewPane;

    /**
     * Constructor of class.
     * @param rootPane the rootpane from which this is defined.
     */
    public StartupModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }

    /**
     * Constructor of class.
     * @param rootPane the rootpane from which this is defined.
     * @param width min. width of screen.
     * @param height min. height of screen.
     */
    public StartupModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);
        initializeView();
    }
    
    /**
     * Initialize the view of this modal.
     */
    private void initializeView() {
       

        // Create a new VBox for vertical layout
        this.viewPane = new VBox();
        
        // force minimum size
        getModalStage().setHeight(height);
        getModalStage().setWidth(width);
        getModalStage().setMinWidth(width);
        getModalStage().setMinHeight(height);

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
        informationLabel = new Label(noLoadTitle);
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

        newButton = createButton("New", true);
        loadButton = createButton("Load", true);
        exitButton = createButton("Exit", true);

        content.getChildren().addAll(newButton, loadButton, exitButton);
    }

    public void setLoadFailed() {
        informationLabel.setText(loadFailedTitle);
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
