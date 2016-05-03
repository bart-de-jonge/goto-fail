package gui;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Class responsible for displaying a modal view for the creation of shots.
 * @author alex
 */
public class CreationModalView extends ModalView {

    private static final int width = 200;
    private static final int height = 200;

    /**
     * Constructor.
     * @param rootPane Pane to display modal on top of
     */
    public CreationModalView(RootPane rootPane) {
        super(rootPane, width, height);
        initializeCreationView();
    }

    /**
     * Initialize and display the modal view.
     */
    private void initializeCreationView() {
        VBox nameBox = new VBox(20);
        nameBox.getChildren().add(new Text("ZOMG CreationModalView!"));
        super.setModalView(nameBox);
        super.displayModal();
    }
}
