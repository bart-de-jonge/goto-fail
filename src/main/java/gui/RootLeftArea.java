package gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lombok.Getter;

/**
 * Created by markv on 4/26/2016.
 * Mwahahaha I created this class, losers.
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
