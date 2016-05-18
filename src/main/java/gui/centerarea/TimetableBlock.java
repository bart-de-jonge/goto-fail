package gui.centerarea;

import control.CountUtilities;
import gui.misc.BlurHelper;
import gui.root.RootCenterArea;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

import static gui.centerarea.TimetableBlock.DraggingTypes.Move;

/**
 * Class that resembles a draggable, resizable block inside the timetable,
 * whose sole purpose is to display information.
 * Highly volatile. Do not poke the dragging-dragon too much.
 */
public abstract class TimetableBlock extends Pane {

    public enum DraggingTypes { Move, Resize_Top, Resize_Right, Resize_Bottom, Resize_Left }

    /**
     *  Styling variables.
     *  For tweaking the styling.
     */

    private double verticalBorderSize = 4.0;
    private double margin = 5.0;
    private double blurRadius = 20.0;

    /**
     *  Content variables.
     *  Directly displaying block content, such as the name.
     */
    @Getter
    private Label titleNormalLabel;
    @Getter
    private Label titleDraggedLabel;
    @Getter
    private Label countNormalLabel;
    @Getter
    private Label countDraggedLabel;
    @Getter
    private Label descriptionNormalLabel;
    @Getter
    private Label descriptionDraggedLabel;

    /**
     * Misc variables.
     * For dragging, panes etc
     */
    @Getter
    private TimetableBlock thisBlock;
    @Getter
    private Pane draggedPane; // rootCenterArea shown when dragging
    @Getter
    private Pane feedbackPane; // rootCenterArea shown when snapping
    @Getter
    private VBox contentPane; // content of this rootCenterArea
    @Getter
    private VBox draggedContentPane; // content of rootCenterArea shown when dragging
    // for feedbackPane
    @Getter
    private WritableImage feedbackImage; // content of feedbackPane (is just an image, sneaky!)
    @Getter
    private GaussianBlur gaussianBlur;
    @Getter
    private ColorAdjust darken;

    // for glass effect
    @Getter
    private BlurHelper behindPanelBlur;
    @Getter
    private double dragXOffset;
    @Getter
    private double dragYOffset;
    @Getter
    private RootCenterArea rootCenterArea;
    @Getter
    private boolean dragging;
    @Getter
    private DraggingTypes draggingType;

    @Getter
    private double mouseCurrentXPosition;
    @Getter
    private double mouseCurrentYPosition;
    @Getter
    private double mouseCurrentXMovement;
    @Getter
    private double mouseCurrentYMovement;

    @Getter
    private ShotBlock parentBlock;

    @Getter
    private double startingY;

    /**
     * Constructor for TimetableBlock class.
     * @param pane - the parent rootCenterArea.
     * @param parent - the parent node
     */
    TimetableBlock(RootCenterArea pane, ShotBlock parent) {
        this.dragging = false;
        this.thisBlock = this;
        this.parentBlock = parent;
        this.rootCenterArea = pane;
    }

    /**
     * Inits the necessary eventhandlers for this block.
     * @param horizontalAllowed - specifies if horizontal dragging (between timelines)
     *      is allowed
     */
    void addMouseEventHandlers(boolean horizontalAllowed) {
        // mouse event handlers
        setOnMousePressed(getOnPressedHandler(horizontalAllowed));
        setOnMouseDragged(getOnDraggedHandler(horizontalAllowed));
        setOnMouseReleased(getOnreleaseHandler(horizontalAllowed));
        setOnMouseMoved(getOnMouseMovedHandler());
    }

    /**
     * Helper function to initialize normal (visible) blocks.
     */
    void initNormalPane() {
        setBlendMode(BlendMode.MULTIPLY);
        getStyleClass().add("block_Background_Normal");

        // content rootCenterArea for our
        // actual rootCenterArea, which holds content (text and stuff)
        contentPane = new VBox();
        contentPane.minWidthProperty().bind(widthProperty());
        contentPane.maxWidthProperty().bind(widthProperty());
        contentPane.minHeightProperty().bind(heightProperty());
        contentPane.maxHeightProperty().bind(heightProperty());

        // add some labels etc
        titleNormalLabel = initTitleLabel(contentPane);
        countNormalLabel = initCountLabel(contentPane);
        descriptionNormalLabel = initDescriptionLabel(contentPane);
        descriptionNormalLabel.setWrapText(true);

        addWithClipRegion(contentPane, this);
    }

    /**
     * Helper function to initialize dragged (visible when dragging) blocks.
     * @param anchorPane the AnchorPane drag on
     */
    void initDraggedPane(AnchorPane anchorPane) {
        // draggedPane itself
        draggedPane = new Pane();
        draggedPane.setVisible(false);

        // dragged content rootCenterArea which mirrors
        // our content rootCenterArea, shown when dragging.
        draggedContentPane = new VBox() ;
        draggedContentPane.minWidthProperty().bind(draggedPane.widthProperty());
        draggedContentPane.maxWidthProperty().bind(draggedPane.widthProperty());
        draggedContentPane.minHeightProperty().bind(draggedPane.heightProperty());
        draggedContentPane.maxHeightProperty().bind(draggedPane.heightProperty());

        // blurring shown behind dragged rootCenterArea
        behindPanelBlur = new BlurHelper(draggedPane);
        behindPanelBlur.getImageView().setBlendMode(BlendMode.MULTIPLY);
        behindPanelBlur.getImageView().setOpacity(0.9);
        behindPanelBlur.setRadius(blurRadius);
        behindPanelBlur.setOffset(new Point2D(8, 8));
        addWithClipRegion(behindPanelBlur.getImageView(), draggedPane);

        // add some labels etc
        titleDraggedLabel = initTitleLabel(draggedContentPane);
        countDraggedLabel = initCountLabel(draggedContentPane);
        descriptionDraggedLabel = initCountLabel(draggedContentPane);
        descriptionDraggedLabel.setWrapText(true);

        // dropshadow shown underneath dragged rootCenterArea
        DropShadow ds = new DropShadow(15.0, 5.0, 5.0, Color.GRAY);
        this.getDraggedPane().setEffect(ds);

        addWithClipRegion(draggedContentPane, draggedPane);
        anchorPane.getChildren().add(draggedPane);
    }

    /**
     * Helper function to initialize feedback (the snapping) blocks.
     * @param gridPane the gridpane to have the feedback on
     */
    public void initFeedbackPane(GridPane gridPane) {
        feedbackPane = new Pane();
        feedbackPane.setVisible(false);
        gaussianBlur = new GaussianBlur(15.0);
        darken = new ColorAdjust(0, -0.4, -0.2, 0.2);
        gridPane.add(feedbackPane, 0, 0);
    }

    /**
     * Add content to rootCenterArea, but with a clipping region bound to the rootCenterArea's size.
     * @param content the Pane in which content is located.
     * @param pane the Pane in which the vbox is located.
     */
    private void addWithClipRegion(Node content, Pane pane) {
        Rectangle clipRegion = new Rectangle(); // clip region to restrict content
        clipRegion.widthProperty().bind(pane.widthProperty());
        clipRegion.heightProperty().bind(pane.heightProperty().subtract(verticalBorderSize));
        content.setClip(clipRegion);
        pane.getChildren().add(content);
    }

    /**
     * Helper function to add title labels to panes.
     * @param vbox rootCenterArea to add this label to
     * @return the label in question.
     */
    private Label initTitleLabel(VBox vbox) {
        Label res = new Label(parentBlock.getName());
        res.maxWidthProperty().bind(this.widthProperty());
        res.getStyleClass().add("block_Text_Title");
        vbox.getChildren().add(res);
        return res;
    }

    /**
     * Helper function to add the description label to panes.
     * @param vbox - rootCenterArea to add this label to
     * @return - the label in question
     */
    private Label initDescriptionLabel(VBox vbox) {
        Label res = new Label(parentBlock.getDescription());
        res.maxWidthProperty().bind(this.widthProperty());
        res.getStyleClass().add("block_Text_Normal");
        vbox.getChildren().add(res);
        return res;
    }

    /**
     * Helper function to add count labels to panes.
     * @param vbox rootCenterArea to add this label to
     * @return the label in question.
     */
    private Label initCountLabel(VBox vbox) {
        String labelText = parentBlock.getBeginCount() + " - " + parentBlock.getEndCount();
        Label res = new Label(labelText);
        res.maxWidthProperty().bind(this.widthProperty());
        res.getStyleClass().add("block_Text_Normal");
        vbox.getChildren().add(res);
        return res;
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
     * @param isCameraTimeline true when action is on the CameraTimeline
     * @return - the eventhandler
     */
    private EventHandler<MouseEvent> getOnPressedHandler(boolean isCameraTimeline) {
        return e -> {
            // init correct object ordering
            feedbackPane.toFront();
            draggedPane.toFront();
            this.toFront();

            // init draggingpane
            draggingType = findEdgeZone(e);
            draggedPane.setLayoutX(getLayoutX());
            draggedPane.setLayoutY(getLayoutY());

            // Init feedbackpane
            feedbackImage = this.snapshot(new SnapshotParameters(), null);
            ImageView image = new ImageView(feedbackImage);
            image.fitHeightProperty().bind(feedbackPane.heightProperty());
            darken.setInput(gaussianBlur);
            image.setEffect(darken);
            feedbackPane.getChildren().add(image);
            feedbackPane.toBack();
            feedbackPane.setVisible(true);

            GridPane gridPane;
            if (isCameraTimeline) {
                gridPane = getRootCenterArea().getMainTimeLineGridPane();
                TimelinesGridPane.setColumnIndex(
                        feedbackPane, TimelinesGridPane.getColumnIndex(thisBlock));
                TimelinesGridPane.setRowIndex(
                        feedbackPane, TimelinesGridPane.getRowIndex(thisBlock));
                TimelinesGridPane.setRowSpan(
                        feedbackPane, TimelinesGridPane.getRowSpan(thisBlock));
            } else {
                gridPane = getRootCenterArea().getDirectorGridPane();
                DirectorGridPane.setColumnIndex(
                        feedbackPane, DirectorGridPane.getColumnIndex(thisBlock));
                DirectorGridPane.setRowIndex(
                        feedbackPane, DirectorGridPane.getRowIndex(thisBlock));
                DirectorGridPane.setRowSpan(
                        feedbackPane, DirectorGridPane.getRowSpan(thisBlock));
            }

            // Set startingY if dragging
            double blockY = gridPane.localToScene(thisBlock.getLayoutX(),
                    thisBlock.getLayoutY()).getY();
            if (draggingType == DraggingTypes.Resize_Top) {
                startingY = blockY + thisBlock.getHeight();
            } else if (draggingType == DraggingTypes.Resize_Bottom) {
                startingY = blockY;
            }

            behindPanelBlur.processBlurUsingBounds();
        };
    }

    /**
     * Event handler for on mouse dragged.
     * @param isCameraTimeline - specifies if horizontal dragging
     *                         (between timelines) is allowed
     * @return - the eventhandler
     */
    private EventHandler<MouseEvent> getOnDraggedHandler(boolean isCameraTimeline) {
        return e -> {
            if (!dragging) {
                dragXOffset = e.getX();
                dragYOffset = e.getY();

                dragging = true;
                draggedPane.setVisible(true);
                draggedPane.setPrefHeight(getHeight());
                draggedPane.setMinHeight(getHeight());
                draggedPane.setMaxHeight(getHeight());
                draggedPane.setPrefWidth(getWidth());
                thisBlock.setVisible(false);
            }
            onMouseDraggedHelper(e, isCameraTimeline);
            behindPanelBlur.processBlurUsingBounds();
            e.consume();
        };
    }

    /**
     * Event handler for on mouse release.
     * @param isCameraTimeline true if the action is on a CameraTimeline
     * @return - the event handler
     */
    private EventHandler<MouseEvent> getOnreleaseHandler(boolean isCameraTimeline) {
        return e -> {
            draggedPane.setVisible(false);
            thisBlock.setVisible(true);
            snapPane(thisBlock, feedbackPane, e.getSceneY(), draggingType, isCameraTimeline);

            feedbackPane.setVisible(false);
            feedbackPane.getChildren().remove(0);
            dragging = false;

            // Update ShotBlock
            if (isCameraTimeline) {
                double newBeginCount = TimelinesGridPane.getRowIndex(thisBlock)
                        / (double) CountUtilities.NUMBER_OF_CELLS_PER_COUNT;
                parentBlock.setBeginCount(newBeginCount, false);
                parentBlock.setEndCount(newBeginCount + TimelinesGridPane.getRowSpan(thisBlock)
                        / (double) CountUtilities.NUMBER_OF_CELLS_PER_COUNT, false);
            } else {
                double newBeginCount = DirectorGridPane.getRowIndex(thisBlock)
                        / (double) CountUtilities.NUMBER_OF_CELLS_PER_COUNT;
                parentBlock.setBeginCount(newBeginCount, false);
                parentBlock.setEndCount(newBeginCount + DirectorGridPane.getRowSpan(thisBlock)
                        / (double) CountUtilities.NUMBER_OF_CELLS_PER_COUNT, false);
            }

            this.fireEvent(parentBlock.getShotBlockUpdatedEvent());
        };
    }

    /**
     * Helper function for MouseDragged event. Normal (actual dragging) part.
     * @param event the mousedrag event in question.
     * @param isCameraTimeline - specifies if horizontal dragging
     *                          (between timelines) is allowed
     */
    private void onMouseDraggedHelper(MouseEvent event, boolean isCameraTimeline) {
        double x = event.getSceneX();
        double y = event.getSceneY();

        // Fix dragging out of grid
        if (draggingType == DraggingTypes.Resize_Bottom
                || draggingType == DraggingTypes.Resize_Top) {
            GridPane gridPane;
            if (isCameraTimeline) {
                gridPane = getRootCenterArea().getMainTimeLineGridPane();
            } else {
                gridPane = getRootCenterArea().getDirectorGridPane();
            }
            Bounds sceneBounds = gridPane.localToScene(gridPane.getLayoutBounds());
            if (y < sceneBounds.getMinY()) {
                y = sceneBounds.getMinY();
            }
        }
        // Mouse movement in pixels during this event.
        mouseCurrentXMovement = x - mouseCurrentXPosition;
        mouseCurrentYMovement = y - mouseCurrentYPosition;

        // determine what kind of dragging we're going to do.
        if (draggingType == DraggingTypes.Resize_Bottom
                || draggingType == DraggingTypes.Resize_Top
                || (draggingType == DraggingTypes.Move && !isCameraTimeline)) {
            // handle vertical drags in helper.
            onMouseDraggedHelperVertical(x, y, isCameraTimeline);
        } else if (draggingType == Move) { // handle just general dragging
            onMouseDraggedHelperNormal(x, y, isCameraTimeline);
        }

        // set feedbackpane
        if (snapPane(feedbackPane, draggedPane, y,
                draggingType, isCameraTimeline)) {
            feedbackPane.setVisible(true);
        } else {
            feedbackPane.setVisible(false);
        }

        // store current mouse position for next mouse movement calculation
        mouseCurrentXPosition = x;
        mouseCurrentYPosition = y;
    }

    /**
     * Snap the targetregion to a grid using the model provided by the mappingPane.
     * @param targetRegion - the target region to snap
     * @param mappingPane - the model mappingPane to follow while snapping
     * @param y - the Y coordinate of the mouse during this snap
     * @param dragType - The type of drag used while snapping (move, resize)
     * @param isCameraTimeline true if the action is on the CameraTimeline
     * @return - boolean that indicates if the snap was possible and completed
     */
    private boolean snapPane(Region targetRegion, Region mappingPane,
                              double y, DraggingTypes dragType, boolean isCameraTimeline) {
        // set feedback rootCenterArea
        double yCoordinate;
        double xCoordinate;

        if (dragType == Move) {
            yCoordinate = y - dragYOffset;
            xCoordinate = mappingPane.localToScene(mappingPane.getBoundsInLocal()).getMinX()
                    + mappingPane.getWidth() / 2;
        } else {
            yCoordinate = y;
            xCoordinate = mappingPane.getLayoutX() + mappingPane.getWidth() / 2;
        }

        ScrollableGridPane gridPane;

        if (isCameraTimeline) {
            gridPane = rootCenterArea.getMainTimeLineGridPane();
        } else {
            gridPane = rootCenterArea.getDirectorGridPane();
        }

        SnappingPane myPane = gridPane.getMyPane(xCoordinate, yCoordinate);
        if (myPane != null) {
            int numCounts = (int) Math.round(mappingPane.getHeight()
                    / gridPane.getVerticalElementSize());
            if (myPane.isBottomHalf() && dragType == DraggingTypes.Resize_Top) {
                numCounts = (int) Math.round((mappingPane.getHeight() - 5)
                        / gridPane.getVerticalElementSize());
            }

            if (myPane.isBottomHalf() && (dragType == DraggingTypes.Resize_Top
                    || dragType == Move)) {
                GridPane.setRowIndex(targetRegion, myPane.getRow() + 1);
            } else if (dragType == Move || dragType == DraggingTypes.Resize_Top) {
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
     * @param x - the x coordinate needed to process the vertical dragging
     * @param y - the y coordinate needed to process the vertical dragging
     * @param isCameraTimeline true if the action is in the CameraTimeline
     */
    private void onMouseDraggedHelperNormal(double x, double y, boolean isCameraTimeline) {
        AnchorPane parentPane;
        if (isCameraTimeline) {
            parentPane = rootCenterArea.getMainTimeLineAnchorPane();
        } else {
            parentPane = rootCenterArea.getDirectorAnchorPane();
        }
        Bounds parentBounds = parentPane.localToScene(parentPane.getBoundsInLocal());

        draggedPane.setLayoutX(x - parentBounds.getMinX() - dragXOffset);
    }

    /**
     * Helper function for MouseDragged event. Vertical part.
     * @param x - the x coordinate needed to process the vertical dragging
     * @param y - the y coordinate needed to process the vertical dragging
     * @param isCameraTimeline true if the action is in the CameraTimeline
     */
    private void onMouseDraggedHelperVertical(double x, double y, boolean isCameraTimeline) {
        double newLayoutY = 0;
        double newPrefHeight = 0;
        AnchorPane anchorPane;
        ScrollableGridPane gridPane;
        if (isCameraTimeline) {
            anchorPane = rootCenterArea.getMainTimeLineAnchorPane();
            gridPane = rootCenterArea.getMainTimeLineGridPane();
        } else {
            anchorPane = rootCenterArea.getDirectorAnchorPane();
            gridPane = rootCenterArea.getDirectorGridPane();
        }
        Point2D bounds = anchorPane.sceneToLocal(x, y);

        if (thisBlock.draggingType == DraggingTypes.Resize_Top) {
            newPrefHeight = startingY - y;
            newLayoutY = bounds.getY();
        } else if (thisBlock.draggingType == DraggingTypes.Resize_Bottom) {
            newLayoutY = anchorPane.sceneToLocal(0, startingY).getY();
            newPrefHeight = bounds.getY() - newLayoutY;
        } else if (thisBlock.draggingType == DraggingTypes.Move && !isCameraTimeline) {
            Bounds parentBounds = anchorPane.localToScene(anchorPane.getBoundsInLocal());
            newLayoutY = y - parentBounds.getMinY() - dragYOffset;
            newPrefHeight = getHeight();
        }

        if (newPrefHeight < gridPane.getVerticalElementSize()) {
            newPrefHeight = gridPane.getVerticalElementSize();
            if (draggingType == DraggingTypes.Resize_Top) {
                newLayoutY = gridPane.sceneToLocal(0,
                        startingY).getY() - newPrefHeight;
            }
        }

        draggedPane.setLayoutY(newLayoutY);
        draggedPane.setPrefHeight(newPrefHeight);
        draggedPane.setMinHeight(newPrefHeight);
        draggedPane.setMaxHeight(newPrefHeight);
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
            return Move;
        } else if (event.getY() > getHeight() - margin) {
            return DraggingTypes.Resize_Bottom;
        } else if (event.getX() < margin) {
            // Horizontal resizing disabled for now.
//            return DraggingTypes.Resize_Left;
            return Move;
        } else {
            return Move;
        }
    }

}

