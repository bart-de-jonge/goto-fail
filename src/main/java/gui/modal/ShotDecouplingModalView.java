package gui.modal;

import data.CameraShot;
import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * Class responsible for displaying a popup to confirm that a CameraShot should
 * separate from it's DirectorShot.
 */
public class ShotDecouplingModalView extends ModalView {

    private CameraShot cameraShot;

    @Getter
    private StyledButton cancelButton;
    @Getter
    private StyledButton confirmButton;

    // variables for the Confirm and Cancel buttons
    private int buttonWidth = 90;
    private int buttonHeight = 25;
    private int buttonSpacing = 20;

    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 300;
    private static final int GENERAL_SIZE = 10000;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param shot shot to be separated
     */
    public ShotDecouplingModalView(RootPane rootPane, CameraShot shot) {
        this(rootPane, DEFAULT_WIDTH, DEFAULT_HEIGHT, shot);
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
        VBox rootPane = new VBox();

        Label sureLabel = new Label("Are you sure you want to separate "
                                        + this.cameraShot.getName() + " from it's director shot?");
        rootPane.getChildren().add(sureLabel);

        HBox buttonPane = new HBox();
        buttonPane.setSpacing(buttonSpacing);
        buttonPane.setAlignment(Pos.CENTER_LEFT);
        buttonPane.setPrefHeight(GENERAL_SIZE);
        rootPane.getChildren().add(buttonPane);

        cancelButton = new StyledButton("Cancel");
        cancelButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setAlignment(Pos.CENTER);

        confirmButton = new StyledButton("Confirm");
        confirmButton.setPrefWidth(buttonWidth);
        confirmButton.setPrefHeight(buttonHeight);
        confirmButton.setAlignment(Pos.CENTER);

        buttonPane.getChildren().addAll(cancelButton, confirmButton);

        super.setModalView(rootPane);
        super.displayModal();
    }
}
