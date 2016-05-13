package gui.root;

import javafx.scene.control.ScrollPane;
import lombok.Getter;

/**
 * Unused for now. This class is supposed to represent an area to the left of the timeline.
 */
public class RootLeftArea extends ScrollPane {

    @Getter
    private RootPane rootPane;

    /**
     * Constructor class.
     * @param rootPane parent pane passed through.
     */
    RootLeftArea(RootPane rootPane) {
        this.rootPane = rootPane;
    }

}
