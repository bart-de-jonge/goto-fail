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
    private RootCenterArea pane; // parent pane this block is located in.

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
        setLayoutX(100.0);
        setLayoutY(100.0);
        setMinHeight(50.0);
        setMinWidth(50.0);
        setPrefHeight(100.0);
        setPrefWidth(100.0);
        init(pane);
    }

    /**
     * Constructor for TimetableBlock class.
     * @param pane the parent pane.
     * @param x start coordinate.
     * @param y start coordinate.
     */
    TimetableBlock(RootCenterArea pane, double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
        setMinHeight(50.0);
        setMinWidth(50.0);
        setPrefWidth(100.0);
        setPrefHeight(100.0);
        init(pane);
    }

    /**
     * Constructor for TimetableBlock class.
     * @param pane the parent pane.
     * @param x start coordinate.
     * @param y start coordinate.
     * @param width start width.
     * @param height start height.
     */
    TimetableBlock(RootCenterArea pane, double x, double y, double width, double height) {
        setLayoutX(x);
        setLayoutY(y);
        setMinWidth(50.0);
        setMinHeight(50.0);
        setPrefWidth(width);
        setPrefHeight(height);
        init(pane);
    }

    /**
     * Helper function for initialization. Called by constructors.
     * @param pane pane passed by constructors.
     */
    private void init(RootCenterArea pane) {

        this.pane = pane;
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
                onMouseDraggedHelper(event);
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
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelper( MouseEvent event) {
        // Mouse movement in pixels during this event.
        mouseCurrentXMovement = event.getSceneX() - mouseCurrentXPosition;
        mouseCurrentYMovement = event.getSceneY() - mouseCurrentYPosition;

        // useful items to convert local coordinates into parent (scrollable) coordinates.
        Parent localToScrollPane = pane.getParent();
        scrollMouse = localToScrollPane.getLocalToParentTransform().transform(
                event.getSceneX(), event.getSceneY());

        // determine what kind of dragging we're going to do.
        if (dragging == 1 || dragging == 3) { // handle vertical drags in helper.
            onMouseDraggedHelperVertical(event);
        } else if (dragging == 2 || dragging == 4) { // handle horizontal drags in helper.
            onMouseDraggedHelperHorizontal(event);
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
        // current height and width of TOTAL scrollable area.
        double totalHeight = pane.getContent().getBoundsInLocal().getHeight();
        double totalWidth = pane.getContent().getBoundsInLocal().getWidth();
        // set block position depending on movement made. Cap off at borders with min and max.
        setLayoutX(Math.min(Math.max(getLayoutX() + mouseCurrentXMovement,
                0.0), totalWidth - getWidth()));
        setLayoutY(Math.min(Math.max(getLayoutY() + mouseCurrentYMovement,
                0.0), totalHeight - getHeight()));
    }

    /**
     * Helper function for MouseDragged event. Vertical part.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelperVertical(MouseEvent event) {
        System.out.println(getLayoutY() + " " + (getLayoutY() + getPrefHeight()));
        // current height of total scrollable area
        double totalHeight = pane.getContent().getBoundsInLocal().getHeight();
        // current height of visible part of scrollable area.
        double viewHeight = pane.getHeight();
        // current number of pixels from top that are invisible due to scrolling.
        double viewTop = (totalHeight - viewHeight) * pane.getVvalue();
        if (dragging == 1) { // drag from top edge
            double oldLayoutY = getLayoutY() - (scrollMouse.getY() - pane.getLayoutY());
            if (getPrefHeight() > getMinHeight()
                    || (mouseCurrentYMovement < 0.0 && mouseCurrentYPosition < getLayoutY())) {
                double newLayout = event.getSceneY() + viewTop - pane.getLayoutY();
                double newPrefHeight = oldLayoutY - viewTop + getPrefHeight();
                if (newLayout < 0.0) {
                    newLayout = 0.0;
                    newPrefHeight = getPrefHeight();
                    if (getLayoutY() != 0.0) {
                        newPrefHeight += getLayoutY();
                    }
                }
                setLayoutY(newLayout);
                setPrefHeight(newPrefHeight);
            }
        } else { // drag from bottom edge
            setPrefHeight(scrollMouse.getY() - getLayoutY() + viewTop - pane.getLayoutY());
            if (getLayoutY() + getPrefHeight() > totalHeight) { // cap off beyond border
                setPrefHeight(totalHeight - getLayoutY());
            }
        }
    }

    /**
     * Helper function for MouseDragged event. Horizontal part.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelperHorizontal(MouseEvent event) {
        // current  width of total scrollable area
        double totalWidth = pane.getContent().getBoundsInLocal().getWidth();
        // current width of visible part of scrollable area.
        double viewWidth = pane.getWidth();
        // current number of pixels from left that are invisible due to scrolling.
        double viewLeft = (totalWidth - viewWidth) * pane.getHvalue();
        if (dragging == 2) { // drag from right edge
            setPrefWidth(scrollMouse.getX() - getLayoutX() + viewLeft - pane.getLayoutX());
            if (getLayoutX() + getPrefWidth() > totalWidth) { // cap off beyond border
                setPrefWidth(totalWidth - getLayoutX());
            }
        } else { // drag from left edge
            double oldLayoutX = getLayoutX() - (scrollMouse.getX() - pane.getLayoutX());
            if (getPrefWidth() > getMinWidth()
                    || (mouseCurrentXMovement < 0.0 && mouseCurrentXPosition < getLayoutX())) {
                double newLayout = event.getSceneX() + viewLeft - pane.getLayoutX();
                double newPrefWidth = oldLayoutX - viewLeft + getPrefWidth();
                if (newLayout < 0.0) {
                    newLayout = 0.0;
                    newPrefWidth = getPrefWidth();
                    if (getLayoutX() != 0.0) {
                        newPrefWidth += getLayoutX();
                    }
                }
                setLayoutX(newLayout);
                setPrefWidth(newPrefWidth);
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
