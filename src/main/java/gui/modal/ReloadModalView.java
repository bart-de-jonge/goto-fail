package gui.modal;

import gui.root.RootPane;

/**
 * Modal shown to confirm that the application will be reloaded, and whether
 * data should be saved.
 */
public class ReloadModalView extends SaveModalView {

    /**
     * Class constructor.
     * @param rootPane the original rootPane.
     */
    public ReloadModalView(RootPane rootPane) {
        super(rootPane);

        this.titleLabel.setText("This will cause the application to restart!\n"
                + "Do you wish to save project changes?");
    }

}
