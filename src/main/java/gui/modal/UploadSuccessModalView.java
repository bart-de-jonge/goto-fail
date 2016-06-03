package gui.modal;

import gui.root.RootPane;
import gui.styling.StyledButton;
import lombok.Getter;

public class UploadSuccessModalView extends ButtonsOnlyModalView {
    
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    
    @Getter
    private StyledButton closeButton;
    @Getter
    private StyledButton goToWebsiteButton;
    
    /**
     * Construct a new success modal with default size.
     * @param rootPane the RootPane for this modal.
     */
    public UploadSuccessModalView(RootPane rootPane) {
        this(rootPane, WIDTH, HEIGHT);
    }
    
    /**
     * Construct a new success modal with specific size.
     * @param rootPane the RootPane for this modal.
     * @param width the width of the modal.
     * @param height the height of the modal.
     */
    public UploadSuccessModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);

        initialize();
    }

    /**
     * Adds content to modal view.
     */
    private void initialize() {
        // Set title text
        titleLabel.setText("Upload successful!");

        // Add buttons
        closeButton = createButton("Close", true);
        goToWebsiteButton = createButton("Go to website", true);
        buttonPane.getChildren().addAll(closeButton, goToWebsiteButton);
    }


}
