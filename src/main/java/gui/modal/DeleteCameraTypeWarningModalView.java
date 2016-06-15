package gui.modal;

import java.util.List;

import data.Camera;
import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class DeleteCameraTypeWarningModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of the screen.
    private static final int width = 550;
    private static final int height = 350;

    // variables for spacing
    private static final int topAreaHeight = 80;
    private static final int bottomAreaHeight = 60;

    // simple background styles of the three main areas.
    private String topStyle = ModalUtilities.constructDefaultModalTopStyle(20);
    private String bottomStyle = "-fx-background-color: "
            + TweakingHelper.getColorString(0) + ";";

    // variables for the buttons
    private int buttonSpacing = 20;

    // variables for the title label
    private static final int titlelabelOffsetFromLeft = 20;

    /*
     * Other variables
     */

    @Getter
    private StyledButton confirmButton;
    @Getter
    private StyledButton cancelButton;
    @Getter
    private VBox viewPane;
    @Getter
    private Label titleLabel;
    @Getter
    private List<Camera> camerasToShow;

    /**
     * Constructor that uses default width and height.
     * @param rootPane the rootPane for this modal.
     * @param camerasToShow the cameras to show in this warning modal
     */
    public DeleteCameraTypeWarningModalView(RootPane rootPane, List<Camera> camerasToShow) {
        this(rootPane, camerasToShow, width, height);
    }
    
    /**
     * Constructor with manual width and height.
     * @param rootPane the rootPane for this modal.
     * @param camerasToShow the cameras to show in this warning modal
     * @param width the width of this modal
     * @param height the height of this modal
     */
    public DeleteCameraTypeWarningModalView(RootPane rootPane, List<Camera> camerasToShow,
            int width, int height) {
        super(rootPane, width, height);
        this.camerasToShow = camerasToShow;
        initView();
    }
    
    /**
     * Initialize the view of this modal.
     */
    private void initView() {
        forceBounds(height, width);
        
        this.viewPane = new VBox();
        
        initTitleLabel();
        initMessage();
        initButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }
    
    /**
     * Initialize the title label.
     */
    private void initTitleLabel() {
        titleLabel = ModalUtilities.constructTitleLabel(topStyle, topAreaHeight);
        titleLabel.setText("Are you sure you want to delete this camera type?\n"
                + "The following cameras will be deleted!");
        this.viewPane.getChildren().add(titleLabel);
    }
    
    /**
     * Initialize the message.
     */
    private void initMessage() {
        // Scrollpane to handle too-large number of cameras
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.viewPane.getChildren().add(scrollPane);

        // VBox to make list of cameras.
        VBox messageArea = new VBox();
        messageArea.maxWidthProperty().bind(scrollPane.widthProperty());
        scrollPane.setContent(messageArea);

        // Run through cameras, adding them to VBox, differing styling depending on
        // odd or even camera-number. Gives list-like effect.
        boolean even = false;
        for (int i = 0; i < camerasToShow.size(); i++) {
            HBox cameraBox = new HBox();
            cameraBox.getChildren().addAll(
                    new Label(camerasToShow.get(i).getName()),
                    new Label(" - "),
                    new Label(camerasToShow.get(i).getDescription())
            );

            if (even) {
                cameraBox.setStyle("-fx-background-color: rgb(245,245,245);");
            }

            cameraBox.setPrefWidth(TweakingHelper.GENERAL_SIZE);
            cameraBox.setPadding(new Insets(3, 0, 3, 6));
            messageArea.getChildren().add(cameraBox);

            even = !even;
        }
    }
    
    /**
     * Initialize the buttons.
     */
    private void initButtons() {
        HBox content = ModalUtilities.constructButtonPane();
        this.viewPane.getChildren().add(content);

        confirmButton = createButton("Confirm", false);
        cancelButton = createButton("Cancel", false);

        content.getChildren().addAll(confirmButton, cancelButton);
    }

}
