package gui.centerarea;

import gui.misc.BlurHelper;
import gui.root.RootCenterArea;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
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

/**
 * Class that resembles a draggable, resizable block inside the timetable,
 * whose sole purpose is to display information.
 * Highly volatile. Do not poke the dragging-dragon too much.
 */
public class TimetableBlock extends Pane {

    public enum DraggingTypes { Move, Resize_Top, Resize_Right, Resize_Bottom, Resize_Left }

    /**
     *  Styling variables.
     *  For tweaking the styling.
     */

    private double verticalBorderSize = 4.0;
    private double margin = 5.0;
    private double blurRadius = 20.0;

    private String colorBlockBackground = "#B1E2FA";
    private String colorBlockBorders = "#DDF2FC";
    private String colorBlockBorderHighlight = "#005482";
    private String colorText = "#047FB7";

    /**
     *  Temporary variables, until they can be moved to CSS files properly.
     */

    private String title = "Camblock title";
    private String shots = "15 - 20";

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

    private TimetableBlock thisBlock;

    private Pane draggedPane; // pane shown when dragging
    private Pane feedbackPane; // pane shown when snapping
    private VBox contentPane; // content of this pane
    private VBox draggedContentPane; // content of pane shown when dragging

    // for feedbackPane
    private WritableImage feedbackImage; // content of feedbackPane (is just an image, sneaky!)
    private GaussianBlur gaussianBlur;
    private ColorAdjust darken;

    // for glass effect
    private BlurHelper behindPanelBlur;

    private double dragXOffset;
    private double dragYOffset;

    private RootCenterArea pane;
    private boolean dragging;
    private DraggingTypes draggingType;

    private double mouseCurrentXPosition;
    private double mouseCurrentYPosition;
    private double mouseCurrentXMovement;
    private double mouseCurrentYMovement;

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
        this.pane = pane;

        // pane helpers
        initNormalPane();
        initDraggedPane();
        initFeedbackPane();

        // mouse event handlers
        setOnMousePressed(getOnPressedHandler());
        setOnMouseDragged(getOnDraggedHandler());
        setOnMouseReleased(getOnreleaseHandler());
        setOnMouseMoved(getOnMouseMovedHandler());
    }

    /**
     * Helper function to initialize normal (visible) blocks.
     */
    private void initNormalPane() {
        setBlendMode(BlendMode.MULTIPLY);
        getStyleClass().add("block_Background_Normal");

        // content pane for our actual pane, which holds content (text and stuff)
        contentPane = new VBox();
        contentPane.minWidthProperty().bind(widthProperty());
        contentPane.maxWidthProperty().bind(widthProperty());
        contentPane.minHeightProperty().bind(heightProperty());
        contentPane.maxHeightProperty().bind(heightProperty());
        contentPane.getStyleClass().add("block_Foreground_Normal");

        // add some labels etc
        titleNormalLabel = initTitleLabel(contentPane);
        countNormalLabel = initCountLabel(contentPane);
        descriptionNormalLabel = initCountLabel(contentPane);
        descriptionNormalLabel.setWrapText(true);

        addWithClipRegion(contentPane, this);
    }

    /**
     * Helper function to initialize dragged (visible when dragging) blocks.
     */
    private void initDraggedPane() {
        // draggedPane itself
        draggedPane = new Pane();
        draggedPane.setVisible(false);
        draggedPane.getStyleClass().add("block_Background_Dragged");

        // dragged content pane which mirrors our content pane, shown when dragging.
        draggedContentPane = new VBox() ;
        draggedContentPane.minWidthProperty().bind(draggedPane.widthProperty());
        draggedContentPane.maxWidthProperty().bind(draggedPane.widthProperty());
        draggedContentPane.minHeightProperty().bind(draggedPane.heightProperty());
        draggedContentPane.maxHeightProperty().bind(draggedPane.heightProperty());
        draggedContentPane.getStyleClass().add("block_Foreground_Dragged");

        // dropshadow shown underneath dragged pane
        DropShadow ds = new DropShadow(15.0, 5.0, 5.0, Color.GRAY);
        draggedPane.setEffect(ds);

        // blurring shown behind dragged pane
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

        addWithClipRegion(draggedContentPane, draggedPane);
        pane.getMainTimeLineAnchorPane().getChildren().add(draggedPane);
    }

    /**
     * Helper function to initialize feedback (the snapping) blocks.
     */
    private void initFeedbackPane() {
        feedbackPane = new Pane();
        feedbackPane.setVisible(false);
        gaussianBlur = new GaussianBlur(15.0);
        darken = new ColorAdjust(0, -0.4, -0.2, 0.2);
        pane.getMainTimeLineGridPane().add(feedbackPane, 0, 0);
    }

    /**
     * Add content to pane, but with a clipping region bound to the pane's size.
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
     * @param vbox pane to add this label to
     * @return the label in question.
     */
    private Label initTitleLabel(VBox vbox) {
        Label res = new Label();
        res.setText("Block Title");
        res.maxWidthProperty().bind(this.widthProperty());
        res.getStyleClass().add("block_Text_Title");
        vbox.getChildren().add(res);
        return res;
    }

    /**
     * Helper function to add count labels to panes.
     * @param vbox pane to add this label to
     * @return the label in question.
     */
    private Label initCountLabel(VBox vbox) {
        Label res = new Label();
        res.setText("0 - 0");
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
     * @return - the eventhandler
     */
    private EventHandler<MouseEvent> getOnPressedHandler() {
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
            //feedbackPane.setStyle("-fx-background-color: rgb(255,0,0)");
            feedbackPane.toBack();
            feedbackPane.setVisible(true);

            TimelinesGridPane.setColumnIndex(feedbackPane,
                    TimelinesGridPane.getColumnIndex(thisBlock));
            TimelinesGridPane.setRowIndex(feedbackPane, TimelinesGridPane.getRowIndex(thisBlock));
            TimelinesGridPane.setRowSpan(feedbackPane, TimelinesGridPane.getRowSpan(thisBlock));

            // Set startingY if dragging
            double blockY = pane.getMainTimeLineGridPane().localToScene(thisBlock.getLayoutX(),
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
     * @return - the eventhandler
     */
    private EventHandler<MouseEvent> getOnDraggedHandler() {
        return e -> {
            if (!dragging) {
                dragXOffset = e.getX();
                dragYOffset = e.getY();

                dragging = true;
                draggedPane.setVisible(true);
                draggedPane.setPrefHeight(getHeight());
                draggedPane.setPrefWidth(getWidth());
                thisBlock.setVisible(false);
            }
            onMouseDraggedHelper(e);
            behindPanelBlur.processBlurUsingBounds();
            e.consume();
        };
    }

    /**
     * Event handler for on mouse release.
     * @return - the event handler
     */
    private EventHandler<MouseEvent> getOnreleaseHandler() {
        return e -> {
            draggedPane.setVisible(false);
            thisBlock.setVisible(true);
            feedbackPane.setVisible(false);
            feedbackPane.getChildren().remove(0);
            dragging = false;
            snapPane(thisBlock, draggedPane, e.getSceneX(), e.getSceneY(), draggingType);

            // Update ShotBlock
            int newBeginCount = TimelinesGridPane.getRowIndex(thisBlock);
            parentBlock.setBeginCount(newBeginCount, false);
            parentBlock.setEndCount(newBeginCount + TimelinesGridPane.getRowSpan(thisBlock), false);

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

        // determine what kind of dragging we're going to do.
        if (draggingType == DraggingTypes.Resize_Bottom
                || draggingType == DraggingTypes.Resize_Top) {
            // handle vertical drags in helper.
            onMouseDraggedHelperVertical(event);
        } else if (draggingType == DraggingTypes.Move) { // handle just general dragging
            onMouseDraggedHelperNormal(event);
        }

        // set feedbackpane
        if (snapPane(feedbackPane, draggedPane, event.getSceneX(), event.getSceneY(),
                draggingType)) {
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

        SnappingPane myPane = pane.getMainTimeLineGridPane().getMyPane(xCoordinate, yCoordinate);
        if (myPane != null) {
            int numCounts = (int) Math.round(mappingPane.getHeight()
                    / pane.getMainTimeLineGridPane().getVerticalElementSize());
            if (myPane.isBottomHalf() && dragType == DraggingTypes.Resize_Top) {
                numCounts = (int) Math.round((mappingPane.getHeight() - 5)
                        / pane.getMainTimeLineGridPane().getVerticalElementSize());
            }

            if (myPane.isBottomHalf() && (dragType == DraggingTypes.Resize_Top
                    || dragType == DraggingTypes.Move)) {
                GridPane.setRowIndex(targetRegion, myPane.getRow() + 1);
            } else if (dragType == DraggingTypes.Move) {
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
        AnchorPane parentPane = pane.getMainTimeLineAnchorPane();
        Bounds parentBounds = parentPane.localToScene(parentPane.getBoundsInLocal());

        draggedPane.setLayoutX(event.getSceneX() - parentBounds.getMinX() - dragXOffset);
        draggedPane.setLayoutY(event.getSceneY() - parentBounds.getMinY() - dragYOffset);
    }

    /**
     * Helper function for MouseDragged event. Vertical part.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelperVertical(MouseEvent event) {
        double newLayoutY = 0;
        double newPrefHeight = 0;
        Point2D bounds = pane.getMainTimeLineAnchorPane().sceneToLocal(event.getSceneX(),
                event.getSceneY());

        if (thisBlock.draggingType == DraggingTypes.Resize_Top) {
            newPrefHeight = startingY - event.getSceneY();
            newLayoutY = bounds.getY();
        } else if (thisBlock.draggingType == DraggingTypes.Resize_Bottom) {
            newLayoutY = pane.getMainTimeLineAnchorPane().sceneToLocal(0, startingY).getY();
            newPrefHeight = bounds.getY() - newLayoutY;
        }

        if (newPrefHeight < pane.getMainTimeLineGridPane().getVerticalElementSize()) {
            newPrefHeight = pane.getMainTimeLineGridPane().getVerticalElementSize();
            if (draggingType == DraggingTypes.Resize_Top) {
                newLayoutY = pane.getMainTimeLineAnchorPane().sceneToLocal(0,
                        startingY).getY() - newPrefHeight;
            }
        }

        draggedPane.setLayoutY(newLayoutY);
        draggedPane.setPrefHeight(newPrefHeight);

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

