package gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;

/**
 * Created by markv on 4/27/2016.
 */
public class TimetableBlock extends Rectangle {

    private RootCenterArea pane; // area blocks are located in

    public TimetableBlock(RootCenterArea _pane) {
        super(50, 50, Color.GREEN);
        pane = _pane;
        super.setStyle("-fx-border-style: solid outside;"
                     + "-fx-border-width: 1;");

        EventHandler eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(pane.getWidth());
                double newX = event.getX() - getWidth() / 2;
                double newY = event.getY() - getHeight() / 2;
                double max = 2000.0;
//                setX(Math.min(Math.max(newX, 0.0), pane.getWidth() - getWidth()));
//                setY(Math.min(Math.max(newY, 0.0), pane.getHeight() - getHeight()));
                setX(Math.min(Math.max(newX, 0.0),max));
                setY(Math.min(Math.max(newY, 0.0), max));
            }
        };

        setOnMouseDragged(eventHandler);

    }

}
