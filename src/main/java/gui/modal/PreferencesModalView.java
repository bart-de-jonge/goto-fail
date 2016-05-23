package gui.modal;

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
 * Modal view representing the preferences/settings window.
 */
public class PreferencesModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 350 work very, very well.
    private static final int width = 450;
    private static final int height = 200;

    // variables for spacing
    protected static final int topAreaHeight = 50;
    protected static final int bottomAreaHeight = 60;

    // area styling
    protected String topStyle = "-fx-background-color: "
            + TweakingHelper.getPrimaryString() + ";"
            + "-fx-text-fill: white; -fx-font-size: 22;"
            + "-fx-font-family: helvetica neue; -fx-font-weight: lighter;"
            + "-fx-border-width: 0 0 10 0;"
            + "-fx-border-color: "
            + TweakingHelper.getSecondaryString() + ";";
    protected String centerStyle = "-fx-background-color: "
            + TweakingHelper.getBackgroundString() + ";";
    protected String bottomStyle = "-fx-background-color: "
            + TweakingHelper.getPrimaryString() + ";";

    // variables for the buttons
    private int buttonSpacing = 20;

    // variables for the title label
    private static final int titlelabelOffsetFromLeft = 20;

    /*
     * Other variables
     */

    private Label titleLabel;
    private VBox viewPane;
    private VBox centerPane;
    private HBox buttonPane;
    @Getter
    private StyledButton saveButton;
    @Getter
    private StyledButton cancelButton;
    @Getter
    private StyledListview colorList;

    /**
     * Class constructtor.
     * @param rootPane parent rootPane.
     */
    public PreferencesModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }

    /**
     * Class constructor.
     * @param rootPane parent rootPane.
     * @param width width of pane.
     * @param height height of pane.
     */
    public PreferencesModalView(RootPane rootPane, int width, int height) {
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

        // add title area
        initTitleLabel();

        // add center area
        initCenter();

        // add button area
        initButtons();

        super.setModalView(this.viewPane);
        super.displayModal();
    }

    /**
     * Init the title label.
     */
    private void initTitleLabel() {
        titleLabel = new Label("Preferences");
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
     * Init center area and content.
     */
    private void initCenter() {
        // add space for textfields and lists
        this.centerPane = new VBox(TweakingHelper.GENERAL_SPACING);
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(0, 0, 0, 0));
        this.centerPane.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        this.centerPane.setStyle(centerStyle);
        this.viewPane.getChildren().add(centerPane);

        // add list for colors
        this.colorList = new StyledListview();
        colorList.setMinHeight(75);
        for (int i = 0; i < TweakingHelper.getNumberOfColors(); i++) {
            HBox box = new HBox();
            box.getChildren().add(
              new Label(TweakingHelper.getColorNames()[i]));
            colorList.getItems().add(box);
        }
        this.colorList.getFocusModel().focus(TweakingHelper.getColorChoice());
        this.colorList.getSelectionModel().select(TweakingHelper.getColorChoice());
        this.centerPane.getChildren().add(colorList);
    }

    /**
     * Initialize the save/cancel buttons.
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
        cancelButton = createButton("Cancel", false);
        // Add creation button
        saveButton = createButton("Save", false);

        this.buttonPane.getChildren().addAll(saveButton, cancelButton);
    }

}
