package gui.modal;

import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class SaveModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 350 work very, very well.
    private static final int width = 450;
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
    private int buttonSpacing = 20;

    // variables for the title label
    private static final int titlelabelOffsetFromLeft = 20;

    /*
     * Other variables
     */

    protected Label informationLabel;
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

        saveButton = createButton("Save", true);
        dontSaveButton = createButton("Don't save", true);
        cancelButton = createButton("Cancel", true);

        content.getChildren().addAll(saveButton, dontSaveButton, cancelButton);
    }

}
