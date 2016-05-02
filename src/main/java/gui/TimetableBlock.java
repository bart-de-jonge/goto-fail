package gui;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

/**
 * Class that resembles a draggable, resiable block inside the timetable,
 * whose sole purpose is to display information.
 * Highly volatile. Do not poke the dragging-dragon too much.
 */
public class TimetableBlock extends Pane {

    public enum DraggingTypes { Move, Resize_Top, Resize_Right, Resize_Bottom, Resize_Left }

    /*
        Styling variables.
        For styling, tweaking and displayable content.
     */

    private double verticalBorderSize = 3.0; // tweaks size of visible blocks above and below content.

    private double margin = 3.5;

    private String colorBlockBackground = "#B1E2FA";
    private String colorBlockBorders = "#DDF2FC";
    private String colorBlockBorderHighlight = "#005482";
    private String colorText = "#047FB7";

    private String normalStyleBackground =
            "-fx-background-color:" + colorBlockBorders + ";";
    private String normalStyleForeground =
            "-fx-padding: " + verticalBorderSize + " 0 " + verticalBorderSize + " 0;"
            + "-fx-border-style: solid inside;"
            + "-fx-border-insets: " + verticalBorderSize + " 0 " + verticalBorderSize + " 0;"
            + "-fx-border-width: 2 0 0 0;"
            + "-fx-border-color: " + colorBlockBorderHighlight + ";"
            + "-fx-background-color: " + colorBlockBackground + ";"
            + "-fx-background-insets: " + verticalBorderSize + " 0 " + verticalBorderSize + " 0;";

    private String textTitleStyle =
            "-fx-text-fill: " + colorBlockBorderHighlight + ";";
    private String textNormalStyle =
            "-fx-text-fill: " + colorText + ";";

    //private String

    private String title = "Camblock title";
    private String shots = "15 - 20";

    /*
        Misc variables.
        For dragging, interaction, etc.
     */

    private TimetableBlock thisBlock;
    private Pane dummyPane;
    private Pane feedbackPane;

    private VBox contentPane;
    private VBox dummyContentPane;

    private double dragXOffset;
    private double dragYOffset;

    private RootCenterArea pane;
    private boolean dragging;
    private DraggingTypes draggingType;

    private double mouseCurrentXPosition;
    private double mouseCurrentYPosition;
    private double mouseCurrentXMovement;
    private double mouseCurrentYMovement;

    private Point2D scrollMouse;

    @Getter
    private ShotBlock parentBlock;

    private double startingY;

    /**
     * Constructor for TimetableBlock class.
     * @param pane - the parent pane.
     * @param parent - the parent node
     */
    TimetableBlock(RootCenterArea pane, ShotBlock parent) {
        this.dragging = false;
        this.thisBlock = this;
        this.parentBlock = parent;

        feedbackPane = new Pane();
        feedbackPane.setVisible(false);
        dummyPane = new Pane();
        dummyPane.setVisible(false);

        pane.getParentPane().getChildren().add(dummyPane);
        pane.getGrid().add(feedbackPane, 0, 0);

        this.pane = pane;
        setStyle(normalStyleBackground);

        // content pane for our pane, which holds content (text and stuff)
        contentPane = new VBox();
        addClipRegion(contentPane, this);
        contentPane.minWidthProperty().bind(widthProperty());
        contentPane.maxWidthProperty().bind(widthProperty());
        contentPane.minHeightProperty().bind(heightProperty());
        contentPane.maxHeightProperty().bind(heightProperty());
        contentPane.setStyle(normalStyleForeground);

        // dummy content pane which mirrors our content pane, shown when dragging.
        dummyContentPane = new VBox();
        addClipRegion(dummyContentPane, dummyPane);
        dummyContentPane.minWidthProperty().bind(widthProperty());
        dummyContentPane.maxWidthProperty().bind(widthProperty());
        //dummyContentPane.minHeightProperty().bind(heightProperty());
        //dummyContentPane.maxHeightProperty().bind(heightProperty());
        dummyContentPane.setStyle(normalStyleForeground);

        // test labels, please ignore.
        addTestTitleLabel(contentPane);
        addTestLabels(contentPane);
        addTestTitleLabel(dummyContentPane);
        addTestLabels((dummyContentPane));

        // mouse event handlers
        setOnMousePressed(getOnPressedHandler());
        setOnMouseDragged(getOnDraggedHandler());
        setOnMouseReleased(getOnreleaseHandler());

        setOnMouseMoved(getOnMouseMovedHandler());
    }

    /**
     * Add clip region to pane (pane0 and its content (vbox).
     * @param vbox the VBox in which content is located.
     * @param pane the Pane in which the vbox is located.
     */
    private void addClipRegion(VBox vbox, Pane pane) {
        Rectangle clipRegion = new Rectangle(); // clip region to restrict content
        clipRegion.widthProperty().bind(pane.widthProperty());
        clipRegion.heightProperty().bind(pane.heightProperty().subtract(verticalBorderSize));
        vbox.setClip(clipRegion);
        pane.getChildren().add(vbox);
    }

    /**
     * Temporary helper function to add test title labels to panes.
     * @param vbox pane to add to
     */
    private void addTestTitleLabel(VBox vbox) {
        Label label = new Label(title);
        label.maxWidthProperty().bind(this.widthProperty());
        label.setPadding(new Insets(5,5,5,5));
        label.setStyle(textTitleStyle);
        vbox.getChildren().add(label);
    }

    /**
     * Temporary helper function to add test labels to panes.
     * @param vbox pane to add to
     */
    private void addTestLabels(VBox vbox) {
        for (int i = 0; i < 6; i++) {
            Label label = new Label(title);
            label.maxWidthProperty().bind(this.widthProperty());
            label.setPadding(new Insets(5,5,5,5));
            label.setStyle(textNormalStyle);
            vbox.getChildren().add(label);
        }
    }

    /**
     * Get handler for on mouse moved (handling cursors).
     * @return - the handler
     */
    private EventHandler<MouseEvent> getOnMouseMovedHandler() {
        return e -> {
            DraggingTypes dragType = findEdgeZone(e);
            Cursor newCursor = null;
            switch (dragType) {
                case Move:
                    newCursor = Cursor.CLOSED_HAND;
                    break;
                case Resize_Bottom:
                case Resize_Top:
                    newCursor = Cursor.N_RESIZE;
                    break;
                case Resize_Left:
                case Resize_Right:
                    newCursor = Cursor.E_RESIZE;
                    break;
                default:
                    newCursor = Cursor.DEFAULT;
            }

            if (getCursor() != newCursor) {
                setCursor(newCursor);
            }
        };
    }

    /**
     * Event handler for on mouse pressed.
     * @return - the eventhandler
     */
    private EventHandler<MouseEvent> getOnPressedHandler() {
        return e -> {

            // init dummypane
            //setStyle(normalStyleForeground);
            draggingType = findEdgeZone(e);
            dummyPane.setLayoutX(getLayoutX());
            dummyPane.setLayoutY(getLayoutY());

            // Init feedbackpane
            feedbackPane.setVisible(true);
            TimelinesGridPane.setColumnIndex(feedbackPane,
                    TimelinesGridPane.getColumnIndex(thisBlock));
            TimelinesGridPane.setRowIndex(feedbackPane, TimelinesGridPane.getRowIndex(thisBlock));
            TimelinesGridPane.setRowSpan(feedbackPane, TimelinesGridPane.getRowSpan(thisBlock));

            // Set startingY if dragging
            double blockY = pane.getGrid().localToScene(thisBlock.getLayoutX(),
                    thisBlock.getLayoutY()).getY();
            if (draggingType == DraggingTypes.Resize_Top) {
                startingY = blockY + thisBlock.getHeight();
            } else if (draggingType == DraggingTypes.Resize_Bottom) {
                startingY = blockY;
            }
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
            dummyPane.setVisible(false);
            thisBlock.setVisible(true);
            feedbackPane.setVisible(false);
            dragging = false;
            snapPane(thisBlock, dummyPane, e.getSceneX(), e.getSceneY(), draggingType);

            this.fireEvent(parentBlock.getShotBlockUpdatedEvent());
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
        } else if (draggingType == DraggingTypes.Move) { // handle just general dragging
            onMouseDraggedHelperNormal(event);
        }

        // set feedbackpane
        if (snapPane(feedbackPane, dummyPane, event.getSceneX(), event.getSceneY(), draggingType)) {
            feedbackPane.setVisible(true);
        } else {
            feedbackPane.setVisible(false);
        }

        // store current mouse position for next mouse movement calculation
        mouseCurrentXPosition = event.getSceneX();
        mouseCurrentYPosition = event.getSceneY();
    }

    /**
     * Snap the targetregion to a grid using the model provided by the mappingPane.
     * @param targetRegion - the target region to snap
     * @param mappingPane - the model mappingPane to follow while snapping
     * @param x - the X coordinate of the mouse during this snap
     * @param y - the Y coordinate of the mouse during this snap
     * @param dragType - The type of drag used while snapping (move, resize)
     * @return - boolean that indicates if the snap was possible and completed
     */
    private boolean snapPane(Region targetRegion, Region mappingPane,
                             double x, double y, DraggingTypes dragType) {
        // set feedback pane
        double yCoordinate;
        double xCoordinate;

        if (dragType == DraggingTypes.Move) {
            yCoordinate = y - dragYOffset;
            xCoordinate = mappingPane.localToScene(mappingPane.getBoundsInLocal()).getMinX()
                    + mappingPane.getWidth() / 2;
        } else {
            Bounds bounds = mappingPane.localToScene(mappingPane.getBoundsInLocal());
            yCoordinate = bounds.getMinY();
            xCoordinate = mappingPane.getLayoutX() + mappingPane.getWidth() / 2;
            yCoordinate++;
        }

        SnappingPane myPane = pane.getGrid().getMyPane(xCoordinate, yCoordinate);
        if (myPane != null) {
            int numCounts = (int) Math.round(mappingPane.getHeight() / pane.getCountHeight());
            if (myPane.isBottomHalf() && dragType == DraggingTypes.Resize_Top) {
                numCounts = (int) Math.round((mappingPane.getHeight() - 5) / pane.getCountHeight());
            }

            if ((dragType == DraggingTypes.Resize_Top
                    || dragType == DraggingTypes.Move)
                    && myPane.isBottomHalf()) {
                GridPane.setRowIndex(targetRegion, myPane.getRow() + 1);
            } else {
                GridPane.setRowIndex(targetRegion, myPane.getRow());
            }
            GridPane.setColumnIndex(targetRegion, myPane.getColumn());
            GridPane.setRowSpan(targetRegion, Math.max(numCounts, 1));
            return true;
        } else {
            return false;
        }
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
        double newLayoutY = 0;
        double newPrefHeight = 0;
        Point2D bounds = pane.getParentPane().sceneToLocal(event.getSceneX(), event.getSceneY());

        if (thisBlock.draggingType == DraggingTypes.Resize_Top) {
            newPrefHeight = startingY - event.getSceneY();
            newLayoutY = bounds.getY();
        } else if (thisBlock.draggingType == DraggingTypes.Resize_Bottom) {
            newLayoutY = pane.getParentPane().sceneToLocal(0, startingY).getY();
            newPrefHeight = bounds.getY() - newLayoutY;
        }

        if (newPrefHeight < pane.getGrid().getCountHeight()) {
            newPrefHeight = pane.getGrid().getCountHeight();
            if (draggingType == DraggingTypes.Resize_Top) {
                newLayoutY = pane.getParentPane().sceneToLocal(0, startingY).getY() - newPrefHeight;
            }
        }

        dummyPane.setLayoutY(newLayoutY);
        dummyPane.setPrefHeight(newPrefHeight);

    }

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
            // Horizontal resizing disabled for now.
//            return DraggingTypes.Resize_Right;
            return DraggingTypes.Move;
        } else if (event.getY() > getHeight() - margin) {
            return DraggingTypes.Resize_Bottom;
        } else if (event.getX() < margin) {
            // Horizontal resizing disabled for now.
//            return DraggingTypes.Resize_Left;
            return DraggingTypes.Move;
        } else {
            return DraggingTypes.Move;
        }
    }

}

