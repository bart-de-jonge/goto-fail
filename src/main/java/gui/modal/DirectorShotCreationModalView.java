package gui.modal;

import java.util.ArrayList;
import java.util.List;

import gui.headerarea.DoubleTextField;
import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledCheckbox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;


/**
 * Class responsible for displaying a modal view for the creation of director shots.
 */
public class DirectorShotCreationModalView extends ShotCreationModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 430 work very, very well.
    private static final int width = 680;
    private static final int height = 460;

    /*
     * Other variables
     */

    private int numberOfCameras;

    // General panes used
    @Getter
    private DoubleTextField frontPaddingField;
    @Getter
    private DoubleTextField endPaddingField;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     */
    public DirectorShotCreationModalView(RootPane rootPane, int numberOfCamerasInTimeline) {
        this(rootPane, numberOfCamerasInTimeline, width, height);
    }

    /**
     * Constructor.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     * @param modalWidth Modal display width
     * @param modalHeight Modal display height
     */
    public DirectorShotCreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                                 int modalWidth, int modalHeight) {
        super(rootPane, modalWidth, modalHeight);
        this.numberOfCameras = numberOfCamerasInTimeline;
        initializeCreationView();
    }

    /**
     * Initialize and display the modal view.
     */
    private void initializeCreationView() {
        
        // force minimum size
        getModalStage().setHeight(height);
        getModalStage().setWidth(width);
        getModalStage().setMinWidth(width);
        getModalStage().setMinHeight(height);

        // Create a new VBox for vertical layout
        this.rootPane = new VBox();

        // Add label at top
        initTitleLabel("Add a director shot...");

        // add space for textfields and checkboxes
        this.centerPane = new HBox(40.0);
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(0, TweakingHelper.GENERAL_PADDING, 0, 0));
        this.centerPane.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        this.rootPane.getChildren().add(centerPane);

        // actually add textfields and checkboxes
        initTextfields();
        initCamCheckBoxes();

        // add buttons at bottom.
        initButtons();

        super.setModalView(this.rootPane);
        super.displayModal();
    }

    

    /**
     * Initialize all six of the text fields.
     */
    private void initTextfields() {
        VBox content = getTextfieldBox();

        initNameDescriptionFields(content);
        initCountTextfields(content);
        initPaddingTextfields(content);

        this.centerPane.getChildren().add(content);
    }

    
    

    /**
     * Initialize the before and after padding text fields.
     * @param content the pane in which they are located.
     */
    private void initPaddingTextfields(VBox content) {
        // init padding before field
        final Label frontPaddingLabel = new Label("Padding before shot:");
        frontPaddingField = new DoubleTextField();
        frontPaddingField.setText("0.0");
        HBox frontPaddingBox = new HBox(TweakingHelper.GENERAL_SPACING);
        frontPaddingBox.getChildren().addAll(frontPaddingLabel, frontPaddingField);
        frontPaddingBox.setAlignment(Pos.CENTER_RIGHT);

        // init padding after field
        final Label endPaddingLabel = new Label("Padding after shot:");
        endPaddingField = new DoubleTextField();
        endPaddingField.setText("0.0");
        HBox endPaddingBox = new HBox(TweakingHelper.GENERAL_SPACING);
        endPaddingBox.getChildren().addAll(endPaddingLabel, endPaddingField);
        endPaddingBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(frontPaddingBox, endPaddingBox);
    }

    /**
     * Initialize the checkboxes with labels for each camera.
     */
    private void initCamCheckBoxes() {
        styleCamCheckBoxes();

        cameraCheckboxes = new ArrayList<>();
        for (int i = 0; i < numberOfCameras; i++) {
            String checkBoxString = "Camera " + (i + 1);
            StyledCheckbox checkBox = new StyledCheckbox(checkBoxString);
            cameraCheckboxes.add(checkBox);
        }

        this.checkboxPane.getChildren().addAll(cameraCheckboxes);
        this.centerPane.getChildren().add(this.checkboxPane);
    }

    

    /**
     * Displays an error message in the view.
     * @param errorString Error to be displayed.
     */
    public void displayError(String errorString) {
        Text errText = new Text(errorString);
        errText.setFill(Color.RED);
        this.rootPane.getChildren().add(this.rootPane.getChildren().size() - 1, errText);
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
