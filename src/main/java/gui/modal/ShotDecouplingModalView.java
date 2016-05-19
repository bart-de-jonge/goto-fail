package gui.modal;

import data.CameraShot;
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

/**
 * Class responsible for displaying a popup to confirm that a CameraShot should
 * separate from it's DirectorShot.
 */
public class ShotDecouplingModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 350 work very, very well.
    private static final int width = 650;
    private static final int height = 200;

    // variables for spacing
    private static final int topAreaHeight = 80;
    private static final int bottomAreaHeight = 60;

    // simple background styles of the three main areas.
    private String topStyle = "-fx-background-color: " + TweakingHelper.STRING_PRIMARY + ";"
            + "-fx-text-fill: white; -fx-font-size: 20;"
            + "-fx-font-family: helvetica neue; -fx-font-weight: lighter;"
            + "-fx-border-width: 0 0 10 0;"
            + "-fx-border-color: " + TweakingHelper.STRING_TERTIARY + ";";

    // variables for the buttons
    private int buttonSpacing = 20;

    // variables for the title label
    private static final int titlelabelOffsetFromLeft = 20;

    /*
     * Other variables
     */

    private Label informationLabel;
    private CameraShot cameraShot;
    @Getter
    private StyledButton cancelButton;
    @Getter
    private StyledButton confirmButton;
    private VBox viewPane;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param shot shot to be separated
     */
    public ShotDecouplingModalView(RootPane rootPane, CameraShot shot) {
        this(rootPane, width, height, shot);
    }

    /**
     * Constructor.
     *
     * @param rootPane root pane on top of which to display the modal.
     * @param width    width of the modal window.
     * @param height   height of the modal window.
     * @param shot     shot to be separated
     */
    public ShotDecouplingModalView(RootPane rootPane,
                                   int width,
                                   int height,
                                   CameraShot shot) {
        super(rootPane, width, height);
        this.cameraShot = shot;
        initializeView();
    }

    /**
     * Initialize the modal view.
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
        informationLabel = new Label("Are you sure you want to separate "
                + this.cameraShot.getName() + " from it's director shot?");
        informationLabel.setStyle(topStyle);
        informationLabel.setAlignment(Pos.CENTER);
        informationLabel.setPadding(new Insets(0, titlelabelOffsetFromLeft,
                0, titlelabelOffsetFromLeft));
        informationLabel.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        informationLabel.setMinHeight(topAreaHeight);
        informationLabel.setPrefHeight(topAreaHeight);
        informationLabel.setMaxHeight(topAreaHeight);
        this.viewPane.getChildren().add(informationLabel);
    }

    /**
     * Initialize the buttons.
     */
    private void initButtons() {
        HBox content = new HBox(TweakingHelper.GENERAL_SPACING);
        content.setSpacing(buttonSpacing);
        content.setAlignment(Pos.CENTER);
        content.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        content.setPadding(new Insets(0, titlelabelOffsetFromLeft,
                0, titlelabelOffsetFromLeft));
        this.viewPane.getChildren().add(content);

        cancelButton = createButton("Cancel", true);
        confirmButton = createButton("Confirm", true);

        content.getChildren().addAll(confirmButton, cancelButton);
    }

}
