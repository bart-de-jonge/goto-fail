package gui;

import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;

/**
 * Class that resembles a draggable, resiable block inside the timetable.
 * Highly volatile. Do not poke too much.
 */
class TimetableBlock extends Region {

    private TimetableBlock thisBlock;
    private Rectangle dummyRectangle;
    private Pane dummyPane;
    private Rectangle rect;

    private double dragXOffset, dragYOffset;

    private String normalStyle = "-fx-border-style: solid inside;"
            + "-fx-border-width: 3;"
            + "-fx-border-color: yellow;"
            + "-fx-background-color: orange;";
    private String dragStyle = "-fx-border-style: solid inside;"
            + "-fx-border-width: 3;"
            + "-fx-border-color: red;"
            + "-fx-background-color: orange;";


    private RootCenterArea pane;
    private boolean dragging;

    /**
     * Constructor for TimetableBlock class.
     * @param pane the parent pane.
     */
    TimetableBlock(RootCenterArea pane) {
//        setLayoutX(100.0);
//        setLayoutY(100.0);
//        setMinHeight(50.0);
//        setMinWidth(50.0);
//        setPrefHeight(100.0);
//        setPrefWidth(100.0);

        this.dragging = false;

        this.thisBlock = this;

        this.dummyRectangle = new Rectangle(getWidth(), getHeight());
        dummyRectangle.setStyle("-fx-background-color: green");
        dummyRectangle.setX(100);
        dummyRectangle.setY(100);
        dummyRectangle.setVisible(false);
        pane.getParentPane().getChildren().add(dummyRectangle);
//        System.out.println(pane.getParentPane().getChildren().toString());

        dummyPane = new Pane();
        dummyPane.setPrefHeight(100);
        dummyPane.setPrefWidth(200);
        dummyPane.setStyle("-fx-background-color: green");
        pane.getParentPane().getChildren().add(dummyPane);
        dummyPane.setVisible(false);



        this.pane = pane;
        setStyle(normalStyle);

        setOnMouseDragged(event -> {
            if(!dragging) {
                dragXOffset = event.getX();
                dragYOffset = event.getY();

                dragging = true;
//                dummyRectangle.setVisible(true);
//                dummyRectangle.setWidth(getWidth());
//                dummyRectangle.setHeight(getHeight());
                dummyPane.setVisible(true);
                dummyPane.setPrefHeight(getHeight());
                dummyPane.setPrefWidth(getWidth());
                thisBlock.setVisible(false);
            }
            onMouseDraggedHelper(event, dummyPane);
            event.consume();
        });

        System.out.println("HERE?!");

        setOnMouseReleased(e -> {
            if(dragging) {
                dragging = false;
                dummyRectangle.setVisible(false);
                dummyPane.setVisible(false);

                MyPane myPane = pane.getMyPane(e.getSceneX(), e.getSceneY() - thisBlock.getHeight() / 2);
                if (myPane != null) {
                    System.out.println("Row = " + myPane.row);
                    System.out.println("column = " + myPane.column);
                    pane.getGrid().getChildren().remove(thisBlock);
                    pane.getGrid().add(thisBlock, myPane.column, myPane.row);
                } else {
                    System.out.println("Not in a grid thingy");
                }
                thisBlock.setVisible(true);
            }

        });
    }

    /**
     * Helper function for MouseDragged event. Normal (actual dragging) part.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelper(MouseEvent event, Rectangle dummy) {

        AnchorPane parentPane = pane.getParentPane();
        Bounds parentBounds = parentPane.localToScene(parentPane.getBoundsInLocal());
        System.out.println(parentBounds.getMinY());

        dummy.setLayoutX(event.getSceneX() - parentBounds.getMinX() - 100 - thisBlock.getWidth() / 2);
        dummy.setLayoutY(event.getSceneY() - parentBounds.getMinY() - 100 - thisBlock.getHeight() / 2);

    }

    /**
     * Helper function for MouseDragged event. Normal (actual dragging) part.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelper(MouseEvent event, Pane dummy) {

        AnchorPane parentPane = pane.getParentPane();
        Bounds parentBounds = parentPane.localToScene(parentPane.getBoundsInLocal());
        System.out.println(parentBounds.getMinY());

        dummy.setLayoutX(event.getSceneX() - parentBounds.getMinX() - dragXOffset);
        dummy.setLayoutY(event.getSceneY() - parentBounds.getMinY() - dragYOffset);

    }

}
