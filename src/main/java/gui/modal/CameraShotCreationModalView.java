package gui.modal;

import java.util.ArrayList;
import java.util.List;

import data.CameraTimeline;
import data.Instrument;
import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledCheckbox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Class responsible for displaying a modal view for the creation of shots.
 */
public class CameraShotCreationModalView extends ShotCreationModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 290 work very, very well.
    private static final int width = 680;
    private static final int height = 460;

    /*
     * Other variables.
     */

    private List<CameraTimeline> cameraTimelines;
    private ArrayList<Instrument> instruments;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param cameraTimelines CameraTimelines to work with
     */
    public CameraShotCreationModalView(RootPane rootPane, List<CameraTimeline> cameraTimelines,
                                        ArrayList<Instrument> instruments) {
        this(rootPane, cameraTimelines, instruments, width, height);
    }

    /**
     * Constructor.
     * @param rootPane Pane to display modal on top of
     * @param cameraTimelines CameraTimelines to work with
     * @param modalWidth Modal display width
     * @param modalHeight Modal display height
     * @param instruments the instruments that can be used
     */
    public CameraShotCreationModalView(RootPane rootPane, List<CameraTimeline> cameraTimelines,
                                       ArrayList<Instrument> instruments,
                                       int modalWidth, int modalHeight) {
        super(rootPane, modalWidth, modalHeight);
        this.cameraTimelines = cameraTimelines;
        this.instruments = instruments;
        initializeCreationView();
    }

    /**
     * Initialize and display the modal view.
     */
    private void initializeCreationView() {
        // force minimum size
        forceBounds(height, width);
        // Create a new VBox for vertical layout
        this.rootPane = new VBox();

        // Add label at top
        initTitleLabel("Add a camera shot...");

        // add space for textfields and checkboxes
        this.centerPane = new HBox();
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(0, TweakingHelper.GENERAL_PADDING, 0, 0));
        this.centerPane.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        this.centerPane.setSpacing(40.0);
        this.rootPane.getChildren().add(centerPane);
        
        // add buttons at bottom.
        initButtons();

        // actually add textfields and checkboxes
        initTextFields();
        initCamCheckBoxes();

        super.setModalView(this.rootPane);
        super.displayModal();
    }

    /**
     * Initialize the checkboxes with labels for each camera, in a flowpane.
     */
    private void initCamCheckBoxes() {
        styleCamCheckBoxes();

        // add checkboxes
        cameraCheckboxes = new ArrayList<>();
        for (int i = 0; i < this.cameraTimelines.size(); i++) {
            String checkBoxString = this.cameraTimelines.get(i).getCamera().getName();
            StyledCheckbox checkBox = new StyledCheckbox(checkBoxString);
            cameraCheckboxes.add(checkBox);
        }

        // add all to scene
        this.checkboxPane.getChildren().addAll(cameraCheckboxes);
        this.centerPane.getChildren().add(this.checkboxPane);
    }
    
    /**
     * Initialize all textfields, add them to a left-central VBox.
     */
    private void initTextFields() {
        VBox content = getTextfieldBox();

        initNameDescriptionFields(content);
        initCountTextfields(content);
        initInstrumentsDropdown(content, instruments);

        this.centerPane.getChildren().add(content);
    }

   
}
