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
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;

/**
 * Class that resembles a draggable, resiable block inside the timetable.
 * Highly volatile. Do not poke too much.
 */
class TimetableBlock extends Region {

    private TimetableBlock thisBlock;
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
    private int  draggingNumber;
    private double margin = 15;

    private double mouseCurrentXPosition;
    private double mouseCurrentYPosition;
    private double mouseCurrentXMovement;
    private double mouseCurrentYMovement;

    private Point2D scrollMouse;

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

        dummyPane = new Pane();
        dummyPane.setPrefHeight(100);
        dummyPane.setPrefWidth(200);
        dummyPane.setStyle("-fx-background-color: green");
        pane.getParentPane().getChildren().add(dummyPane);
        dummyPane.setVisible(false);



        this.pane = pane;
        setStyle(normalStyle);

        // mouse press handler. Called when mouse is pressed on this block.
        setOnMousePressed(event -> {
//            int numberOfTimelines = pane.getGrid().getColumnConstraints().size();
//            pane.getGrid().getColumnConstraints().clear();
//            for (int i = 0; i < numberOfTimelines; i++) {
//                pane.getGrid().getColumnConstraints().add(new ColumnConstraints(50));
//            }


            setStyle(dragStyle);
            draggingNumber = findEdgeZone(event);
            dummyPane.setLayoutX(getLayoutX());
            dummyPane.setLayoutY(getLayoutY());
//            mouseCurrentXPosition = event.getSceneX();
//            mouseCurrentYPosition = event.getSceneY();
        });


        setOnMouseDragged(event -> {
            if(!dragging) {
                dragXOffset = event.getX();
                dragYOffset = event.getY();

                dragging = true;
                dummyPane.setVisible(true);
                dummyPane.setPrefHeight(getHeight());
                dummyPane.setPrefWidth(getWidth());
                thisBlock.setVisible(false);
            }
            onMouseDraggedHelper(event, dummyPane);
            event.consume();
        });

        setOnMouseReleased(e -> {
            if(dragging) {
                dummyPane.setVisible(false);
                thisBlock.setVisible(true);
                dragging = false;

                if(draggingNumber == 0) {

                    MyPane myPane = pane.getMyPane(e.getSceneX(), e.getSceneY() - thisBlock.getHeight() / 2);
                    if (myPane != null) {
                        System.out.println("Row = " + myPane.row);
                        System.out.println("column = " + myPane.column);
                        pane.getGrid().getChildren().remove(thisBlock);
                        pane.getGrid().add(thisBlock, myPane.column, myPane.row);
                    } else {
                        System.out.println("Not in a grid thingy");
                    }
                } else if (draggingNumber == 1 || draggingNumber == 3) {
                    System.out.println("Vertical drag releaseds");
                    int numCounts = (int) Math.round(dummyPane.getHeight() / pane.countHeight);
                    System.out.println(numCounts);

                    if(draggingNumber == 3) {
                        GridPane.setRowSpan(thisBlock, numCounts);
                    } else {
                        MyPane myPane = pane.getMyPane(e.getSceneX(), e.getSceneY());
                        if (myPane != null) {
//                            System.out.println("Row = " + myPane.row);
//                            System.out.println("column = " + myPane.column);

                            if (myPane.bottomHalf) {
                                pane.getGrid().setRowIndex(thisBlock, myPane.row + 1);
                                System.out.println("bottom half");
                            } else {
                                pane.getGrid().setRowIndex(thisBlock, myPane.row);
                                System.out.println("No bottom half");
                            }

                            pane.getGrid().setRowSpan(thisBlock, numCounts);
                        } else {
                            System.out.println("Not in a grid thingy");
                        }
                    }

                }
            }

        });
    }



    /**
     * Helper function for MouseDragged event. Normal (actual dragging) part.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelper(MouseEvent event, Pane dummy) {
        // Mouse movement in pixels during this event.
        mouseCurrentXMovement = event.getSceneX() - mouseCurrentXPosition;
        mouseCurrentYMovement = event.getSceneY() - mouseCurrentYPosition;

        // useful items to convert local coordinates into parent (scrollable) coordinates.
        Parent localToScrollPane = pane.getParent();
        scrollMouse = localToScrollPane.getLocalToParentTransform().transform(
                event.getSceneX(), event.getSceneY());

        // determine what kind of dragging we're going to do.
        if (draggingNumber == 1 || draggingNumber == 3) { // handle vertical drags in helper.
            onMouseDraggedHelperVertical(event);
//        } else if (draggingNumer == 2 || draggingNumer == 4) { // handle horizontal drags in helper.
//            onMouseDraggedHelperHorizontal(event);
        } else { // handle just general dragging
            onMouseDraggedHelperNormal(event, dummyPane);
        }

        // store current mouse position for next mouse movement calculation
        mouseCurrentXPosition = event.getSceneX();
        mouseCurrentYPosition = event.getSceneY();
    }

    /**
     * Helper function for MouseDragged event. Normal (actual dragging) part.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelperNormal(MouseEvent event, Pane dummy) {

        AnchorPane parentPane = pane.getParentPane();
        Bounds parentBounds = parentPane.localToScene(parentPane.getBoundsInLocal());

        dummy.setLayoutX(event.getSceneX() - parentBounds.getMinX() - dragXOffset);
        dummy.setLayoutY(event.getSceneY() - parentBounds.getMinY() - dragYOffset);
    }

    /**
     * Helper function for MouseDragged event. Vertical part.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelperVertical(MouseEvent event) {
        // current height of total scrollable area
        double totalHeight = pane.getContent().getBoundsInLocal().getHeight();
        // current height of visible part of scrollable area.
        double viewHeight = pane.getHeight();
        // current number of pixels from top that are invisible due to scrolling.
        double viewTop = (totalHeight - viewHeight) * pane.getVvalue();
        if (draggingNumber == 1) { // drag from top edge
            double oldLayoutY = getLayoutY() - (scrollMouse.getY() - pane.getLayoutY());
                double newLayout = event.getSceneY() + viewTop - pane.getLayoutY();
                double newPrefHeight = oldLayoutY - viewTop + getPrefHeight();
                if (newLayout < 0.0) {
                    newLayout = 0.0;
                    newPrefHeight = getPrefHeight();
                    if (getLayoutY() != 0.0) {
                        newPrefHeight += getLayoutY();
                    }
                }
                newPrefHeight += getHeight();

                dummyPane.setLayoutY(newLayout);
                dummyPane.setPrefHeight(newPrefHeight);
//            }
        } else { // drag from bottom edge
            dummyPane.setPrefHeight(scrollMouse.getY() - getLayoutY() + viewTop - pane.getLayoutY());
            if (getLayoutY() + getPrefHeight() > totalHeight) { // cap off beyond border
                dummyPane.setPrefHeight(totalHeight - getLayoutY());
            }
        }
    }

    /**
     Find out in what area of a block (0,1,2,3,4) the mouse is pressed.
     0 is the center, 1 is top, 2 is right, 3 is bottom, 4 is left.
     Changing margin size (see top of file) makes the side areas thicker.
     @param event The MouseEvent to read for this.
     @return int to what area of a block mouse is pressed in.
     */
    private int findEdgeZone(MouseEvent event) {
        if (event.getY() < margin) {
            return 1;
        } else if (event.getX() > getWidth() - margin) {
            return 2;
        } else if (event.getY() > getHeight() - margin) {
            return 3;
        } else if (event.getX() < margin) {
            return 4;
        } else {
            return 0;
        }
    }

}

