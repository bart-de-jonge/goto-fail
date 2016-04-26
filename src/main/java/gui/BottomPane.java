package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Class that represents the whole of bottom-level elements in the gui.
 * In other words, the bar at the bottom, at the least, goes here.
 */
class BottomPane extends HBox {

    BottomPane() {
        setPadding(new Insets(5, 10, 5, 10));
        setStyle("-fx-border-style: solid inside;" +
                "-fx-border-width: 1;");

        Label lbl = new Label("Text output goes here.");
        getChildren().add(lbl);
    }

}
