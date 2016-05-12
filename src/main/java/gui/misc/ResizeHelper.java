package gui.misc;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;


/**
 * Allows for the resizing of undecorated JavaFX stages (windows without OS title bar).
 * @author Mark
 */
public class ResizeHelper {

    @Getter @Setter
    private Stage stage;
    private Scene scene;
    @Getter
    private Cursor cursor = Cursor.DEFAULT;
    @Getter @Setter
    private int border = 8;
    private double startX = 0;
    private double startY = 0;

    private double mouseEventX;
    private double mouseEventY;
    private double sceneWidth;
    private double sceneHeight;

    /**
     * Constructor of class.
     * @param stage the stage we want to give resizing abilities.
     */
    public ResizeHelper(Stage stage) {
        this.stage = stage;
        this.scene = stage.getScene();
        stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, getOnMouseMovedHandler());
        stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, getOnMouseDraggedHandler());
        stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, getOnMousePressedHandler());
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, getOnMouseExitHandler());
    }

    /**
     * Updates necessary variables used by all event handlers.
     * @param e the event whose data we use.
     */
    private void updateVariables(MouseEvent e) {

        mouseEventX = e.getSceneX();
        mouseEventY = e.getSceneY();
        sceneWidth = scene.getWidth();
        sceneHeight = scene.getHeight();
    }


    /**
     * Event handler for when the mouse exits screen.
     * This event handler makes sure the cursor is reset to default properly.
     * @return the event handler.
     */
    private EventHandler<MouseEvent> getOnMouseExitHandler() {
        return e -> {
            scene.setCursor(Cursor.DEFAULT);
        };
    }

    /**
     * Event handler for when the mouse is moved.
     * This event handler detects the kind of "dragging" that can be done
     * from a position.
     * @return the event handler.
     */
    private EventHandler<MouseEvent> getOnMouseMovedHandler() {
        return e -> {
            updateVariables(e);

            // find out what type of resize we're talking about.
            // and yes, this is horrible code. Help.
            // TODO: Find help.

            if (mouseEventX < border && mouseEventY < border) {
                cursor = Cursor.NW_RESIZE;
            } else if (mouseEventX < border && mouseEventY > sceneHeight - border) {
                cursor = Cursor.SW_RESIZE;
            } else if (mouseEventX > sceneWidth - border && mouseEventY < border) {
                cursor = Cursor.NE_RESIZE;
            } else if (mouseEventX > sceneWidth - border && mouseEventY > sceneHeight - border) {
                cursor = Cursor.SE_RESIZE;
            } else if (mouseEventX < border) {
                cursor = Cursor.W_RESIZE;
            } else if (mouseEventX > sceneWidth - border) {
                cursor = Cursor.E_RESIZE;
            } else if (mouseEventY < border) {
                cursor = Cursor.N_RESIZE;
            } else if (mouseEventY > sceneHeight - border) {
                cursor = Cursor.S_RESIZE;
            } else {
                cursor = Cursor.DEFAULT;
            }
            System.out.println(mouseEventX);
            // store the type of mouse movement
            scene.setCursor(cursor);
        };
    }

    /**
     * Event handler for when the mouse is dragged.
     * This event handler deals with the actual dragging and resizing.
     * @return the event handler.
     */
    private EventHandler<MouseEvent> getOnMouseDraggedHandler() {
        return e -> {
            updateVariables(e);
            if (!cursor.equals(Cursor.DEFAULT)) {
                if (!cursor.equals(Cursor.W_RESIZE) && !cursor.equals(Cursor.E_RESIZE)) {
                    handleVerticalDrag(e);
                }

                if (!cursor.equals(Cursor.N_RESIZE) && !cursor.equals(Cursor.S_RESIZE)) {
                    handleHorizontalDrag(e);
                }
            }
        };
    }

    /**
     * Submethod to handle vertical resizing.
     * @param e MouseEvent caught by handler.
     */
    private void handleVerticalDrag(MouseEvent e) {
        // calculate minimal height we can drag.
        double minHeight = stage.getMinHeight() > (border * 2)
                ? stage.getMinHeight() : (border * 2);
        // find type of dragging (in this area) and respond accordingly
        if (cursor.equals(Cursor.NW_RESIZE)
                || cursor.equals(Cursor.N_RESIZE)
                || cursor.equals( Cursor.NE_RESIZE)) {
            // resize to top, means we set a new Y and increase height.
            if (stage.getHeight() > minHeight || mouseEventY < 0) {
                stage.setHeight(stage.getY() - e.getScreenY() + stage.getHeight());
                stage.setY(e.getScreenY());

            }
        } else if (stage.getHeight() > minHeight
                || mouseEventY + startY - stage.getHeight() > 0) {
            // resize to right, means we simply change width.
            stage.setHeight(mouseEventY + startY);
        }
    }

    /**
     * Submethod to handle horizontal resizing.
     * @param e MouseEvent caught by handler.
     */
    private void handleHorizontalDrag(MouseEvent e) {
        // calculate minimal width we can drag
        double minWidth = stage.getMinWidth() > (border * 2)
                ? stage.getMinWidth() : (border * 2);
        // find type of dragging (in this area) and respond accordingly
        if (cursor.equals(Cursor.NW_RESIZE)
                || cursor.equals(Cursor.W_RESIZE)
                || cursor.equals(Cursor.SW_RESIZE)) {
            // resize to left, means we set a new X and increase width.
            if (stage.getWidth() > minWidth || mouseEventX < 0) {
                stage.setWidth(stage.getX() - e.getScreenX() + stage.getWidth());
                stage.setX(e.getScreenX());
            }
        } else if (stage.getWidth() > minWidth
                || mouseEventX + startX - stage.getWidth() > 0) {
            // resize to right, means we simply change width.
            stage.setWidth(mouseEventX + startX);
        }
    }

    /**
     * Event handler for when the mouse is first pressed.
     * @return the event handler.
     */
    private EventHandler<MouseEvent> getOnMousePressedHandler() {
        return e -> {
            System.out.println(cursor);
            updateVariables(e);
            startX = stage.getWidth() - mouseEventX;
            startY = stage.getHeight() - mouseEventY;
        };
    }


}
