package gui;

import javafx.event.EventHandler;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

import java.awt.*;
import javafx.scene.input.MouseEvent;

/**
 * Created by markv on 4/27/2016.
 */
public class TimetableBlock_PaneAttempt extends Pane {

    private double deltaX;
    private double deltaY;
    private boolean dragging = false;

    public TimetableBlock_PaneAttempt(RootCenterArea _pane) {

        setMinHeight(50);
        setMinWidth(50);
        setWidth(50);
        setHeight(50);
        setMaxHeight(50);
        setMaxWidth(50);
        setLayoutX(100);
        setLayoutY(200);
        setStyle("-fx-border-style: solid inside;"
                + "-fx-border-width: 3;"
                + "-fx-border-color: red;"
                + "-fx-background-color: orange;");

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dragging = isOnEdgeZone(event);
                deltaX = getLayoutX() - event.getSceneX();
                deltaY = getLayoutY() - event.getSceneY();
                setStyle("-fx-border-style: solid inside;"
                        + "-fx-border-width: 3;"
                        + "-fx-border-color: green;"
                        + "-fx-background-color: orange;");
            }
        });

        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(dragging);
                if (dragging) {
                    setLayoutX(Math.min(Math.max(event.getSceneX() + deltaX, 0.0), 1024.0 - 50.0));
                    setLayoutY(Math.min(Math.max(event.getSceneY() + deltaY, 0.0), 2048.0 - 50.0));
                }
                //System.out.println(getLayoutX());
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dragging = false;
//                deltaX = getLayoutX() - event.getSceneX();
//                deltaY = getLayoutY() - event.getSceneY();
                setStyle("-fx-border-style: solid inside;"
                        + "-fx-border-width: 3;"
                        + "-fx-border-color: red;"
                        + "-fx-background-color: orange;");
            }
        });

    }

    private boolean isOnEdgeZone(MouseEvent event) {
        double mouseX = event.getSceneX();
        double mouseY = event.getSceneY();
        if (mouseX > getLayoutY() + 15 && mouseY > getLayoutY() + 15
                && mouseX < getLayoutX() + 35 && mouseY < getLayoutY() + 35 ) return false;
        return true;
    }

}
