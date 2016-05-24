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

public class StartupModalView extends ButtonsOnlyModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 550 and 200 work very, very well.
    private static final int width = 550;
    private static final int height = 200;

    /*
     * Other variables
     */

    private static final String loadFailedTitle =
            "An error occurred when loading the last project!\n"
            + "Please create a new project, or load an existing one.";
    private static final String noLoadTitle =
            "Welcome!\n"
            + "Please create a new project, or load an existing one.";
    @Getter
    private StyledButton newButton;
    @Getter
    private StyledButton loadButton;
    @Getter
    private StyledButton exitButton;

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
        initialize();
    }

    /**
     * Initialize content of this modal.
     */
    private void initialize() {
        // Set title of modal.
        titleLabel.setText(noLoadTitle);

        // Set buttons of modal.
        newButton = createButton("New", true);
        loadButton = createButton("Load", true);
        exitButton = createButton("Exit", true);
        buttonPane.getChildren().addAll(newButton, loadButton, exitButton);
    }

    public void setLoadFailed() {
        titleLabel.setText(loadFailedTitle);
    }

}
