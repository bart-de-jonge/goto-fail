package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Class that represents the whole of bottom-level elements in the gui.
 * In other words, the bar at the bottom, at the least, goes here.
 */
class RootFooterArea extends HBox {

    /**
     * RootFooterArea Constructor.
     */
    RootFooterArea() {
        // border style to mark it, for debugging for now.
        setStyle("-fx-border-style: solid inside;"
                + "-fx-border-width: 1;");
        setPadding(new Insets(5, 10, 5, 10));

        Label lbl = new Label("Text output goes here.");
        getChildren().add(lbl);
    }

}
