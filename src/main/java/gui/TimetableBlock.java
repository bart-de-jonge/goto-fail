package gui;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
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

    private double verticalBorderSize = 4.0; // tweaks size of visible blocks above and below content.

    private double margin = 5.0;

    private String colorBlockBackground = "#B1E2FA";
    private String colorBlockBorders = "#DDF2FC";
    private String colorBlockBorderHighlight = "#005482";
    private String colorText = "#047FB7";

    private String normalStyleBackground =
            "-fx-background-color:" + colorBlockBorders + ";"
            + "-fx-background-insets: 1 1 1 1";

    private String normalStyleForeground =
            "-fx-padding: " + verticalBorderSize + " 0 " + verticalBorderSize + " 0;"
            + "-fx-border-style: solid inside;"
            + "-fx-border-insets: " + verticalBorderSize + " 0 " + verticalBorderSize + " 0;"
            + "-fx-border-width: 2 0 0 0;"
            + "-fx-border-color: " + colorBlockBorderHighlight + ";"
            + "-fx-background-color: " + colorBlockBackground + ";"
            + "-fx-background-insets: " + verticalBorderSize + " 2 " + verticalBorderSize + " 2;";

    private String textTitleStyle =
            "-fx-text-fill: " + colorBlockBorderHighlight + ";"
            + "-fx-font-size: 13";
    private String textNormalStyle =
            "-fx-text-fill: " + colorText + ";"
            + "-fx-font-size: 12";

    private String title = "Camblock title";
    private String shots = "15 - 20";

    /*
        Misc variables.
        For dragging, interaction, etc.
     */

    private TimetableBlock thisBlock;

    private Pane draggedPane; // pane shown when dragging
    private Pane feedbackPane; // pane shown when snapping
    private VBox contentPane; // content of this pane
    private VBox draggedContentPane; // content of pane shown when dragging

    // for feedbackPane
    private WritableImage feedbackImage; // content of pane shown when snapping (is just an image, sneaky!)
    private GaussianBlur gaussianBlur;
    private ColorAdjust darken;

    // for glass effect
    private WritableImage behindPaneImage; // to which we render content shown behind the pane
    private ImageView behindPaneView; // which we use to show content shown behind the pane

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
        draggedPane = new Pane();
        draggedPane.setVisible(false);

        pane.getParentPane().getChildren().add(draggedPane);
        pane.getGrid().add(feedbackPane, 0, 0);

        this.pane = pane;
        setStyle(normalStyleBackground);
        draggedPane.setStyle(normalStyleBackground);

        gaussianBlur = new GaussianBlur(15.0);
        DropShadow ds = new DropShadow(15.0, 5.0, 5.0, Color.GRAY);
        darken = new ColorAdjust(0, 0, -0.1, 0);
       // draggedPane.setEffect(ds);

        // content pane for our pane, which holds content (text and stuff)
        contentPane = new VBox();
        addClipRegion(contentPane, this);
        contentPane.minWidthProperty().bind(widthProperty());
        contentPane.maxWidthProperty().bind(widthProperty());
        contentPane.minHeightProperty().bind(heightProperty());
        contentPane.maxHeightProperty().bind(heightProperty());
        contentPane.setStyle(normalStyleForeground);

        // dragged content pane which mirrors our content pane, shown when dragging.
        draggedContentPane = new VBox();
        addClipRegion(draggedContentPane, draggedPane);
        draggedContentPane.minWidthProperty().bind(widthProperty());
        draggedContentPane.maxWidthProperty().bind(widthProperty());
        draggedContentPane.minHeightProperty().bind(draggedPane.heightProperty());
        draggedContentPane.maxHeightProperty().bind(draggedPane.heightProperty());
        draggedContentPane.setStyle(normalStyleForeground);

        // blurring stuff
        behindPaneView = new ImageView();
        behindPaneView.setEffect(gaussianBlur);

        //draggedPane.getChildren().add(behindPaneView);
        addClipRegion(behindPaneView, draggedPane);

        // test labels, please ignore.
        addTestTitleLabel(contentPane);
        addCountLabel(contentPane);
        addTestTitleLabel(draggedContentPane);
        addCountLabel((draggedContentPane));

        // mouse event handlers
        setOnMousePressed(getOnPressedHandler());
        setOnMouseDragged(getOnDraggedHandler());
        setOnMouseReleased(getOnreleaseHandler());
        setOnMouseMoved(getOnMouseMovedHandler());
    }

    /**
     * Add clip region to pane (pane0 and its content (vbox).
     * @param content the Pane in which content is located.
     * @param pane the Pane in which the vbox is located.
     */
    private void addClipRegion(Node content, Pane pane) {
        Rectangle clipRegion = new Rectangle(); // clip region to restrict content
        clipRegion.widthProperty().bind(pane.widthProperty());
        clipRegion.heightProperty().bind(pane.heightProperty().subtract(verticalBorderSize));
        content.setClip(clipRegion);
        pane.getChildren().add(content);
    }

    /**
     * Temporary helper function to add test title labels to panes.
     * @param vbox pane to add to
     */
    private void addTestTitleLabel(VBox vbox) {
        Label label = new Label(title);
        label.maxWidthProperty().bind(this.widthProperty());
        label.setPadding(new Insets(0,0,0,5));
        label.setStyle(textTitleStyle);
        vbox.getChildren().add(label);
    }

    /**
     * Temporary helper function to add test count labels to panes.
     * @param vbox pane to add to
     */
    private void addCountLabel(VBox vbox) {
        Label label = new Label();
        label.setText(getParentBlock().getBeginCount() + " - " + parentBlock.getEndCount());
        label.maxWidthProperty().bind(this.widthProperty());
        label.setPadding(new Insets(5,5,5,5));
        label.setStyle(textNormalStyle);
        vbox.getChildren().add(label);
    }

    /**
     * Temporary helper function to add test labels to panes.
     * @param vbox pane to add to
     */
    private void addTestLabels(VBox vbox) {
        for (int i = 0; i < 6; i++) {
            Label label = new Label(shots);
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
            // init correct object ordering
            feedbackPane.toFront();
            draggedPane.toFront();
            this.toFront();

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setViewport(new Rectangle2D(e.getSceneX() - e.getX(), e.getSceneY() - e.getY(), getWidth(), getHeight()));
            setOpacity(0.0); // hide me while taking background snapshot
            behindPaneImage = getScene().getRoot().snapshot(parameters, null);
            behindPaneView.setImage(behindPaneImage);
            //draggedContentPane.getChildren().add(new Label("derp"));
            behindPaneView.toFront();
            setOpacity(1.0); // unhide me

            // init draggingpane
            draggingType = findEdgeZone(e);
            draggedPane.setLayoutX(getLayoutX());
            draggedPane.setLayoutY(getLayoutY());

            // Init feedbackpane
            /*feedbackImage = this.snapshot(new SnapshotParameters(), null);
            ImageView image = new ImageView(feedbackImage);
            image.fitHeightProperty().bind(feedbackPane.heightProperty());
            darken.setInput(gaussianBlur);
            image.setEffect(darken);
            feedbackPane.getChildren().add(image);*/
            feedbackPane.setStyle("-fx-background-color: rgb(255,0,0)");
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
                draggedPane.setVisible(true);
                draggedPane.setPrefHeight(getHeight());
                draggedPane.setPrefWidth(getWidth());
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
            //draggedPane.getChildren().remove(0);
            draggedPane.setVisible(false);
            thisBlock.setVisible(true);
            feedbackPane.setVisible(false);
            //TODO: enable this again
            //feedbackPane.getChildren().remove(0);
            dragging = false;
            snapPane(thisBlock, draggedPane, e.getSceneX(), e.getSceneY(), draggingType);

            this.fireEvent(parentBlock.getShotBlockUpdatedEvent());
        };
    }



    /**
     * Helper function for MouseDragged event. Normal (actual dragging) part.
     * @param event the mousedrag event in question.
     */
    private void onMouseDraggedHelper(MouseEvent event) {
        //TODO: remove this
        // create blurred image
        SnapshotParameters parameters = new SnapshotParameters();
        Rectangle2D rekt = new Rectangle2D(event.getSceneX() - dragXOffset, event.getSceneY() - dragYOffset, draggedPane.getWidth(), draggedPane.getHeight());
        parameters.setViewport(rekt);
        draggedPane.setOpacity(0.0);
        behindPaneImage = new WritableImage((int) draggedPane.getWidth(), (int) draggedPane.getHeight());
        getScene().getRoot().snapshot(parameters, behindPaneImage);
        behindPaneView.setImage(behindPaneImage);
        draggedPane.setOpacity(1.0);

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
        if (snapPane(feedbackPane, draggedPane, event.getSceneX(), event.getSceneY(), draggingType)) {
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

