package gui;

import javafx.event.EventHandler;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class representing the center (main section) of the gui.
 * In other words, the time line view goes here.
 */
class RootCenterArea extends AnchorPane {

    private double width, height;

    RootCenterArea(RootPane rootPane) {
        setStyle("-fx-border-style: solid inside;"
                + "-fx-border-width: 1;");

        this.width = rootPane.getPrimaryStage().getWidth() - 15;
        this.height = rootPane.getPrimaryStage().getHeight() - 130;
        System.out.println("Width = " + width);
        System.out.println("Height = " + height);
        System.out.println(rootPane.getRootHeaderArea().getHeight());

        final Rectangle rect = new Rectangle(50, 50, Color.RED);


        rect.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double newX = event.getX() - rect.getWidth() / 2;
                double newY = event.getY() - rect.getHeight() / 2;
                if (newX >= 0 && (newX + rect.getWidth()) < width) rect.setX(newX);
                if (newY >= 0 && (newY + rect.getHeight()) < height) {
                    rect.setY(newY);
                } else {
                    System.out.println(newY);
                }
            }
        });

        getChildren().add(rect);
    }

}
