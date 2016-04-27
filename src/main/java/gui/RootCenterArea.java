package gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

/**
 * Class representing the center (main section) of the gui.
 * In other words, the time line view goes here.
 */
class RootCenterArea extends ScrollPane {

    //private double width, height;

    /**
     * Constructor class
     * @param rootPane parent pane passed through.
     */
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

        TimetableBlock rect1 = new TimetableBlock(this, 500, 100, 60, 50);
        TimetableBlock rect2 = new TimetableBlock(this);
        TimetableBlock rect3 = new TimetableBlock(this, 400, 400);
        TimetableBlock rect4 = new TimetableBlock(this, 300, 200, 200, 150);

        contentPane.getChildren().add(rect1);
        contentPane.getChildren().add(rect2);
        contentPane.getChildren().add(rect3);
        contentPane.getChildren().add(rect4);
    }

}
