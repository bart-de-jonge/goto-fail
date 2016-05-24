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

public class SaveModalView extends ButtonsOnlyModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 350 work very, very well.
    private static final int width = 450;
    private static final int height = 200;

    // variables for spacing
    private static final int topAreaHeight = 80;

    /*
     * Other variables
     */

    @Getter
    private StyledButton saveButton;
    @Getter
    private StyledButton dontSaveButton;
    @Getter
    private StyledButton cancelButton;

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
        initialize();
    }

    /**
     * Initializes all content for this modal.
     */
    private void initialize() {
        // Set title text.
        titleLabel.setText("There are unsaved changes\nDo you want to save them?");

        // Add buttons.
        saveButton = createButton("Save", true);
        dontSaveButton = createButton("Don't save", true);
        cancelButton = createButton("Cancel", true);
        buttonPane.getChildren().addAll(saveButton, dontSaveButton, cancelButton);
    }

}
