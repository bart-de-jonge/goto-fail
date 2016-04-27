package gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import org.w3c.dom.css.Rect;

/**
 * Class representing the center (main section) of the gui.
 * In other words, the time line view goes here.
 */
class RootCenterArea extends ScrollPane {

    //private double width, height;

    RootCenterArea(RootPane rootPane) {
        setStyle("-fx-border-style: solid inside;"
                + "-fx-border-width: 1;");

        AnchorPane contentPane = new AnchorPane();
        contentPane.setMaxWidth(1024.0);
        contentPane.setMinWidth(1024.0);
        contentPane.setMaxHeight(2048.0);
        contentPane.setMinHeight(2048.0);
        setContent(contentPane);

        System.out.println("Width = " + getWidth());
        System.out.println("Height = " + getHeight());

        TimetableBlock_PaneAttempt rect1 = new TimetableBlock_PaneAttempt(this);
        TimetableBlock_PaneAttempt rect2 = new TimetableBlock_PaneAttempt(this);
        TimetableBlock_PaneAttempt rect3 = new TimetableBlock_PaneAttempt(this);
        TimetableBlock_PaneAttempt rect4 = new TimetableBlock_PaneAttempt(this);


        contentPane.getChildren().add(rect1);
        contentPane.getChildren().add(rect2);
        contentPane.getChildren().add(rect3);
        contentPane.getChildren().add(rect4);
    }

}
