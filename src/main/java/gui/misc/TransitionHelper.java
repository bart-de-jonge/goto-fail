package gui.misc;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.Property;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


/**
 * Class to assist in easy setup of transitions for Nodes on common events
 * such as mouse-over, buttonclick, etc.
 * @author Mark
 */
public class TransitionHelper {

    /**
     * Constructor of class.
     */
    public TransitionHelper() {
        // nill fer nooow.
    }

    /**
     * Add a transition event, on mouse enter and exit, between two values,
     * with a specified transition half-time, over a specified property
     * Also, if anyone has a better way to do the generic types, tell me,
     * because I haven't exactly used that a lot so far.
     * @param node node to which events are bound.
     * @param property property being modified. (for example, color property)
     * @param ms time in milliseconds for transition.
     * @param x value before transition.
     * @param y value after transition.
     * @param <T> generic type.
     */
    public <T> void addMouseOverTransition(Node node, Property<T> property, int ms,
                                       T x, T y) {
        addMouseOverTransition(node, property, ms, x, y, Interpolator.LINEAR);
    }

    /**
     * Add a transition event, on mouse enter and exit, between two values,
     * with a specified transition half-time, over a specified property
     * Also, if anyone has a better way to do the generic types, tell me,
     * because I haven't exactly used that a lot so far.
     * @param node node to which events are bound.
     * @param property property being modified. (for example, color property)
     * @param ms time in milliseconds for transition.
     * @param x value before transition.
     * @param y value after transition.
     * @param interpolator type of interpolation used.
     * @param <T> generic type.
     */
    public <T> void addMouseOverTransition(Node node, Property<T> property, int ms,
                                           T x, T y, Interpolator interpolator) {
        // Create event handlers from x to y, and from y to x.
        EventHandler<MouseEvent> mouseInHandler = createHandler(property, ms, x, y, interpolator);
        EventHandler<MouseEvent> mouseOutHandler = createHandler(property, ms, y, x, interpolator);

        // Bind event handlers on mouse enter and exit for this Node.
        node.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, mouseInHandler);
        node.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, mouseOutHandler);
    }

    /**
     * Add a transition event, on mouse press and release, between two values,
     * with a specified transition half-time, over a specified property.
     * @param node node to which events are bound.
     * @param property property being modified. (for example, color property)
     * @param ms time in milliseconds for transition.
     * @param x value before transition.
     * @param y value after transition.
     * @param <T> generic type.
     */
    public <T> void addMouseClickTransition(Node node, Property<T> property, int ms,
                                           T x, T y) {
        addMouseClickTransition(node, property, ms, x, y, Interpolator.LINEAR);
    }

    /**
     * Add a transition event, on mouse press and release, between two values,
     * with a specified transition half-time, over a specified property.
     * @param node node to which events are bound.
     * @param property property being modified. (for example, color property)
     * @param ms time in milliseconds for transition.
     * @param x value before transition.
     * @param y value after transition.
     * @param interpolator type of interpolation used.
     * @param <T> generic type.
     */
    public <T> void addMouseClickTransition(Node node, Property<T> property, int ms,
                                            T x, T y, Interpolator interpolator) {
        // Create event handlers from x to y, and from y to x.
        EventHandler<MouseEvent> mouseInHandler = createHandler(property, ms, x, y, interpolator);
        EventHandler<MouseEvent> mouseOutHandler = createHandler(property, ms, y, x, interpolator);

        // Bind event handlers on mouse press and release for this Node.
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseInHandler);
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseOutHandler);
    }


    /**
     * Returns an unspecified event handler with an embedded transition.
     * The transition operates over a specified property, with a duration in
     * milliseconds, between values x and y, and with specified interpolation.
     * The interpolation can be something like linear, edge-in, etc...
     * @param property the property to operate on.
     * @param ms duration in milliseconds.
     * @param x begin value of transition.
     * @param y end value of transition.
     * @param interpolator type of interpolation used.
     * @param <T> generic type.
     * @return the new eventhandler.
     */
    private <T> EventHandler<MouseEvent> createHandler(Property<T> property, int ms,
                                                       T x, T y, Interpolator interpolator) {
        return  e -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(property,
                                    x,
                                    interpolator)),
                    new KeyFrame(Duration.millis(ms),
                            new KeyValue(property,
                                    y,
                                    interpolator))
            );
            timeline.setCycleCount(1);
            timeline.setAutoReverse(false);
            timeline.play();
        };
    }

}
