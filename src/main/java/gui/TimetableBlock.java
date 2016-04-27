package gui;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * Class that resembles a draggable, resiable block inside the timetable.
 * Highly volatile. Do not poke too much.
 */
class TimetableBlock extends Region {

    private String normalStyle = "-fx-border-style: solid inside;"
            + "-fx-border-width: 3;"
            + "-fx-border-color: yellow;"
            + "-fx-background-color: orange;";
    private String dragStyle = "-fx-border-style: solid inside;"
            + "-fx-border-width: 3;"
            + "-fx-border-color: red;"
            + "-fx-background-color: orange;";

    private double margin = 6.0; // margin of edge size for dragging.
    private int dragging = 0; // 0 is center, 1 top, 2 right, 3 bottom, 4 left.

    private double mouseCurrentXPosition;
    private double mouseCurrentYPosition;
    private double mouseCurrentXMovement;
    private double mouseCurrentYMovement;

    private Point2D scrollMouse;

    /**
     * Constructor class for timetable block.
     * @param pane parent area this block is located in.
     */
    TimetableBlock(RootCenterArea pane) {
        // some basic temporary styling for the block. That's size, position, and style.
        setLayoutX(100.0);
        setLayoutY(100.0);
        setMinHeight(100.0);
        setMinWidth(100.0);
        setStyle(normalStyle);

        // mouse press handler. Called when mouse is pressed on this block.
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStyle(dragStyle);
                dragging = findEdgeZone(event);
                mouseCurrentXPosition = event.getSceneX();
                mouseCurrentYPosition = event.getSceneY();
            }
        });

        // mouse drag handler (uses helper function because it's too large)
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseDraggedHelper(pane, event);
            }
        });

        // mouse release handler: called when mouse is released on this block.
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStyle(normalStyle);
            }
        });
    }

    /**
     * Helper function for MouseDragged event.
     * @param pane pane this block is located in.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelper(RootCenterArea pane, MouseEvent event) {
        // Mouse movement in pixels during this event.
        mouseCurrentXMovement = event.getSceneX() - mouseCurrentXPosition;
        mouseCurrentYMovement = event.getSceneY() - mouseCurrentYPosition;

        // useful items to convert local coordinates into parent (scrollable) coordinates.
        Parent localToScrollPane = pane.getParent();
        scrollMouse = localToScrollPane.getLocalToParentTransform().transform(
                event.getSceneX(), event.getSceneY());

        // determine what kind of dragging we're going to do.
        if (dragging == 1 || dragging == 3) { // handle vertical drags in helper.
            onMouseDraggedHelperVertical(pane, event);
        } else if (dragging == 2 || dragging == 4) { // handle horizontal drags in helper.
            onMouseDraggedHelperHorizontal(pane, event);
        } else { // handle just general dragging
            onMouseDraggedHelperNormal(pane, event);
        }

        // store current mouse position for next mouse movement calculation
        mouseCurrentXPosition = event.getSceneX();
        mouseCurrentYPosition = event.getSceneY();
    }

    /**
     * Helper function for MouseDragged event. Normal (actual dragging) part.
     * @param pane pane this block is located in.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelperNormal(RootCenterArea pane, MouseEvent event) {
        // current height and width of TOTAL scrollable area.
        double totalHeight = pane.getContent().getBoundsInLocal().getHeight();
        double totalWidth = pane.getContent().getBoundsInLocal().getWidth();
        // set block position depending on movement made. Cap off at borders with min and max.
        setLayoutX(Math.min(Math.max(getLayoutX() + mouseCurrentXMovement,
                0.0), totalWidth - getMinWidth()));
        setLayoutY(Math.min(Math.max(getLayoutY() + mouseCurrentYMovement,
                0.0), totalHeight - getMinHeight()));
    }

    /**
     * Helper function for MouseDragged event. Vertical part.
     * @param pane pane this block is located in.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelperVertical(RootCenterArea pane, MouseEvent event) {
        // current height of total scrollable area
        double totalHeight = pane.getContent().getBoundsInLocal().getHeight();
        // current height of visible part of scrollable area.
        double viewHeight = pane.getHeight();
        // current number of pixels from top that are invisible due to scrolling.
        double viewTop = (totalHeight - viewHeight) * pane.getVvalue();
        if (dragging == 1) { // drag from top edge
            double oldLayoutY = getLayoutY()
                    - (scrollMouse.getY() - pane.getLayoutY());
            setLayoutY(event.getSceneY() + viewTop - pane.getLayoutY());
            if (getLayoutY() < 0.0) {
                setLayoutY(0.0); // cap off beyond border
            } else {
                setMinHeight(oldLayoutY  - viewTop + getMinHeight());
            }
        } else { // drag from bottom edge
            setMinHeight(scrollMouse.getY() - getLayoutY() + viewTop - pane.getLayoutY());
            if (getLayoutY() + getMinHeight() > totalHeight) { // cap off beyond border
                setMinHeight(totalHeight - getLayoutY());
            }
        }
    }

    /**
     * Helper function for MouseDragged event. Horizontal part.
     * @param pane pane this block is located in.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelperHorizontal(RootCenterArea pane, MouseEvent event) {
        // current  width of total scrollable area
        double totalWidth = pane.getContent().getBoundsInLocal().getWidth();
        // current width of visible part of scrollable area.
        double viewWidth = pane.getWidth();
        // current number of pixels from left that are invisible due to scrolling.
        double viewLeft = (totalWidth - viewWidth) * pane.getHvalue();
        if (dragging == 2) { // drag from right edge
            setMinWidth(scrollMouse.getX() - getLayoutX() + viewLeft - pane.getLayoutX());
            if (getLayoutX() + getMinWidth() > totalWidth) { // cap off beyond border
                setMinWidth(totalWidth - getLayoutX());
            }
        } else { // drag from left edge
            double oldLayoutX = getLayoutX()
                    - (scrollMouse.getX() - pane.getLayoutX());
            setLayoutX(event.getSceneX() + viewLeft - pane.getLayoutX());
            if (getLayoutX() < 0.0) {
                setLayoutX(0.0); // cap off beyond border
            } else {
                setMinWidth(oldLayoutX  - viewLeft + getMinWidth());
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
