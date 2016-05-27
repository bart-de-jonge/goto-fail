package gui.modal;

import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class ErrorWhileUploadingModalView extends ButtonsOnlyModalView {
    
    private static final int WIDTH = 400;
    private static final int HEIGHT = 150;
    
    @Getter
    private StyledButton okButton;
    
    /**
     * Construct a new modal with default size.
     * @param rootPane the RootPane for this modal
     */
    public ErrorWhileUploadingModalView(RootPane rootPane) {
        this(rootPane, WIDTH, HEIGHT);
    }
    
    /**
     * Construct a new modal with specific size.
     * @param rootPane the RootPane for this modal
     * @param width the width of this modal
     * @param height the height of this modal
     */
    public ErrorWhileUploadingModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);

        initialize();
    }

    /**
     * Initializes content for this modal.
     */
    private void initialize() {
        // set title
        titleLabel.setText("Something went wrong while uploading project files.");

        // add buttons
        okButton = this.createButton("Ok", true);
        buttonPane.getChildren().add(okButton);
    }

}
