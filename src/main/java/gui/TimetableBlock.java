package gui;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * Class that resembles a draggable, resiable block inside the timetable.
 * Highly volatile. Do not poke too much.
 */
class TimetableBlock extends Region {

    public enum DraggingTypes { Move, Resize_Top, Resize_Right, Resize_Bottom, Resize_Left }

    private TimetableBlock thisBlock;
    private Pane dummyPane;

    private double dragXOffset;
    private double dragYOffset;

    private String normalStyle;
    private String dragStyle;


    private RootCenterArea pane;
    private boolean dragging;
    private DraggingTypes draggingType;
    private double margin;

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
        this.dragging = false;
        this.thisBlock = this;

        dummyPane = new Pane();
        dummyPane.setPrefHeight(100);
        dummyPane.setPrefWidth(200);
        dummyPane.setStyle("-fx-background-color: green");
        pane.getParentPane().getChildren().add(dummyPane);
        dummyPane.setVisible(false);

        this.normalStyle = "-fx-border-style: solid inside;"
                + "-fx-border-width: 3;"
                + "-fx-border-color: yellow;"
                + "-fx-background-color: orange;";
        this.dragStyle = "-fx-border-style: solid inside;"
                + "-fx-border-width: 3;"
                + "-fx-border-color: red;"
                + "-fx-background-color: orange;";

        this.pane = pane;
        setStyle(normalStyle);
        this.margin = 15;

        // mouse event handlers
        setOnMousePressed(getOnPressedHandler());
        setOnMouseDragged(getOnDraggedHandler());
        setOnMouseReleased(getOnreleaseHandler());
    }

    /**
     * Event handler for on mouse pressed.
     * @return - the eventhandler
     */
    private EventHandler<MouseEvent> getOnPressedHandler() {
        return e -> {

            setStyle(dragStyle);
            draggingType = findEdgeZone(e);
            dummyPane.setLayoutX(getLayoutX());
            dummyPane.setLayoutY(getLayoutY());
        };
    }

    /**
     * Event handler for on mouse dragged.
     * @return - the eventhandler
     */
    private EventHandler<MouseEvent> getOnDraggedHandler() {
        return e -> {
            if (!dragging) {
                dragXOffset = e.getX();
                dragYOffset = e.getY();

                dragging = true;
                dummyPane.setVisible(true);
                dummyPane.setPrefHeight(getHeight());
                dummyPane.setPrefWidth(getWidth());
                thisBlock.setVisible(false);
            }
            onMouseDraggedHelper(e);
            e.consume();
        };
    }

    /**
     * Event handler for on mouse release.
     * @return - the event handler
     */
    private EventHandler<MouseEvent> getOnreleaseHandler() {
        return e -> {
            if (dragging) {
                dummyPane.setVisible(false);
                thisBlock.setVisible(true);
                dragging = false;

                if (draggingType == DraggingTypes.Move) {

                    double yCoordinate = e.getSceneY() - thisBlock.getHeight() / 2;
                    SnappingPane myPane = pane.getMyPane(e.getSceneX(), yCoordinate);
                    if (myPane != null) {
                        pane.getGrid().getChildren().remove(thisBlock);
                        pane.getGrid().add(thisBlock, myPane.getColumn(), myPane.getRow());
                    }
                } else if (draggingType == DraggingTypes.Resize_Top) {
                    int numCounts = (int) Math.round(dummyPane.getHeight() / pane.getCountHeight());
                    SnappingPane myPane = pane.getMyPane(e.getSceneX(), e.getSceneY());
                    if (myPane != null) {
                        if (myPane.isBottomHalf()) {
                            GridPane.setRowIndex(thisBlock, myPane.getRow() + 1);
                        } else {
                            GridPane.setRowIndex(thisBlock, myPane.getRow());
                        }

                        GridPane.setRowSpan(thisBlock, numCounts);
                    }
                } else if (draggingType == DraggingTypes.Resize_Bottom) {
                    int numCounts = (int) Math.round(dummyPane.getHeight() / pane.getCountHeight());
                    GridPane.setRowSpan(thisBlock, numCounts);
                }
            }

        };
    }



    /**
     * Helper function for MouseDragged event. Normal (actual dragging) part.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelper(MouseEvent event) {
        // Mouse movement in pixels during this event.
        mouseCurrentXMovement = event.getSceneX() - mouseCurrentXPosition;
        mouseCurrentYMovement = event.getSceneY() - mouseCurrentYPosition;

        // useful items to convert local coordinates into parent (scrollable) coordinates.
        Parent localToScrollPane = pane.getParent();
        scrollMouse = localToScrollPane.getLocalToParentTransform().transform(
                event.getSceneX(), event.getSceneY());

        // determine what kind of dragging we're going to do.
        if (draggingType == DraggingTypes.Resize_Bottom
                || draggingType == DraggingTypes.Resize_Top) {
            // handle vertical drags in helper.
            onMouseDraggedHelperVertical(event);
        } else { // handle just general dragging
            onMouseDraggedHelperNormal(event);
        }

        // store current mouse position for next mouse movement calculation
        mouseCurrentXPosition = event.getSceneX();
        mouseCurrentYPosition = event.getSceneY();
    }

    /**
     * Helper function for MouseDragged event. Normal (actual dragging) part.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelperNormal(MouseEvent event) {

        AnchorPane parentPane = pane.getParentPane();
        Bounds parentBounds = parentPane.localToScene(parentPane.getBoundsInLocal());

        dummyPane.setLayoutX(event.getSceneX() - parentBounds.getMinX() - dragXOffset);
        dummyPane.setLayoutY(event.getSceneY() - parentBounds.getMinY() - dragYOffset);
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
        if (draggingType == DraggingTypes.Resize_Top) { // drag from top edge
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
        } else { // drag from bottom edge
            double newHeight = scrollMouse.getY() - getLayoutY() + viewTop - pane.getLayoutY();
            dummyPane.setPrefHeight(newHeight);
            if (getLayoutY() + getPrefHeight() > totalHeight) { // cap off beyond border
                dummyPane.setPrefHeight(totalHeight - getLayoutY());
            }
        }
    }

    // Commented because we dont support horizontal resizing yet
//    /**
//     * Helper function for MouseDragged event. Horizontal part.
//     * @param event the mousedrag event in question.
//     */
//    private void onMouseDraggedHelperHorizontal(MouseEvent event) {
//        // current  width of total scrollable area
//        double totalWidth = pane.getContent().getBoundsInLocal().getWidth();
//        // current width of visible part of scrollable area.
//        double viewWidth = pane.getWidth();
//        // current number of pixels from left that are invisible due to scrolling.
//        double viewLeft = (totalWidth - viewWidth) * pane.getHvalue();
//        if (dragging == 2) { // drag from right edge
//            setPrefWidth(scrollMouse.getX() - getLayoutX() + viewLeft - pane.getLayoutX());
//            if (getLayoutX() + getPrefWidth() > totalWidth) { // cap off beyond border
//                setPrefWidth(totalWidth - getLayoutX());
//            }
//        } else { // drag from left edge
//            double oldLayoutX = getLayoutX() - (scrollMouse.getX() - pane.getLayoutX());
//            if (getPrefWidth() > getMinWidth()
//                    || (mouseCurrentXMovement < 0.0 && mouseCurrentXPosition < getLayoutX())) {
//                double newLayout = event.getSceneX() + viewLeft - pane.getLayoutX();
//                double newPrefWidth = oldLayoutX - viewLeft + getPrefWidth();
//                if (newLayout < 0.0) {
//                    newLayout = 0.0;
//                    newPrefWidth = getPrefWidth();
//                    if (getLayoutX() != 0.0) {
//                        newPrefWidth += getLayoutX();
//                    }
//                }
//                setLayoutX(newLayout);
//                setPrefWidth(newPrefWidth);
//            }
//        }
//    }

    /**
     Find out in what area of a block (0,1,2,3,4) the mouse is pressed.
     0 is the center, 1 is top, 2 is right, 3 is bottom, 4 is left.
     Changing margin size (see top of file) makes the side areas thicker.
     @param event The MouseEvent to read for this.
     @return int to what area of a block mouse is pressed in.
     */
    private DraggingTypes findEdgeZone(MouseEvent event) {
        if (event.getY() < margin) {
            return DraggingTypes.Resize_Top;
        } else if (event.getX() > getWidth() - margin) {
            return DraggingTypes.Resize_Right;
        } else if (event.getY() > getHeight() - margin) {
            return DraggingTypes.Resize_Bottom;
        } else if (event.getX() < margin) {
            return DraggingTypes.Resize_Left;
        } else {
            return DraggingTypes.Move;
        }
    }

}

