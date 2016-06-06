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
    private static final int height = 300;

    // variables for spacing
    protected static final int topAreaHeight = 50;
    protected static final int bottomAreaHeight = 60;

    // area styling
    protected String topStyle = TweakingHelper.constructDefaultModalTopStyle(22);
    protected String centerStyle = "-fx-background-color: "
            + TweakingHelper.getBackgroundString() + ";";
    protected String bottomStyle = "-fx-background-color: "
            + TweakingHelper.getColorString(0) + ";";

    // variables for the buttons
    private static final int buttonSpacing = 20;

    // variables for the title label
    private static final int titlelabelOffsetFromLeft = 20;
    private static final int colorListHeight = 100;

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
        forceBounds(height, width);
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
        titleLabel = ModalUtilities.constructTitleLabel(topStyle, topAreaHeight);
        titleLabel.setText("Preferences");
        this.viewPane.getChildren().add(titleLabel);
    }

    /**
     * Init center area and content.
     */
    private void initCenter() {
        // add space for content
        this.centerPane = new VBox(TweakingHelper.GENERAL_SPACING);
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(0, TweakingHelper.GENERAL_PADDING,
                0, TweakingHelper.GENERAL_PADDING));
        this.centerPane.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        this.centerPane.setStyle(centerStyle);
        this.centerPane.setAlignment(Pos.CENTER_LEFT);
        this.viewPane.getChildren().add(centerPane);

        // add list for colors
        this.colorList = new StyledListview();
        colorList.setPrefHeight(colorListHeight);
        for (int i = 0; i < TweakingHelper.getNumberOfColors(); i++) {
            HBox box = new HBox();
            box.getChildren().add(
                new Label(TweakingHelper.getColorNames()[i]));
            colorList.getItems().add(box);
        }
        this.colorList.getFocusModel().focus(TweakingHelper.getColorChoice());
        this.colorList.getSelectionModel().select(TweakingHelper.getColorChoice());

        // textlabel
        Label colorLabel = new Label("Application color");

        // add everything together
        this.centerPane.getChildren().addAll(colorLabel, colorList);
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
        // Add apply button
        saveButton = createButton("Apply", false);

        this.buttonPane.getChildren().addAll(saveButton, cancelButton);
    }

}
