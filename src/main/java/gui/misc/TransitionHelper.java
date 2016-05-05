package gui.misc;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import lombok.Getter;


/**
 * Class to assist in easy setup of transitions for Nodes on common events
 * such as mouse-over, buttonclick, etc.
 * @author Mark
 */
public class TransitionHelper {

    @Getter
    private Node node;

    /**
     * Constructor of class.
     * @param node the node for which we want transitions.
     */
    public TransitionHelper(Node node) {
        this.node = node;
    }

    /**
     * Transition functions below.
     */

    /**
     * Add a transition event, on mouse enter and exit, between a specified double
     * value and a specified double offset.
     * @param property the property being modified.
     * @param ms the time in milliseconds for transition.
     * @param v the offset value.
     */
    public void addMouseClickTransition(DoubleProperty property, int ms,
                                       double v) {
        addMouseClickTransition(property, ms, v, Interpolator.LINEAR);
    }

    /**
     * Add a transition event, on mouse enter and exit, between a specified double
     * value and a specified double offset.
     * @param property the property being modified.
     * @param ms the time in milliseconds for transition.
     * @param v the offset value.
     * @param interpolator the interpolation type being used.
     */
    public void addMouseClickTransition(DoubleProperty property, int ms,
                                       double v, Interpolator interpolator) {
        // Create event handlers from x to y, and from y to x.
        EventHandler<MouseEvent> mouseInHandler = createHandlerTowardsDouble(property, ms,
                v, interpolator);
        EventHandler<MouseEvent> mouseOutHandler = createHandlerTowardsDouble(property, ms,
                -v, interpolator);

        // Bind event handlers on mouse enter and exit for this Node.
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseInHandler);
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseOutHandler);
    }

    /**
     * Add a transition event, on mouse press and release, between two values,
     * with a specified transition half-time, over a specified property.
     * @param property property being modified. (for example, color property)
     * @param ms time in milliseconds for transition.
     * @param x value before transition.
     * @param y value after transition.
     * @param <T> generic type.
     */
    public <T> void addMouseClickTransition(Property<T> property, int ms,
                                            T x, T y) {
        addMouseClickTransition(property, ms, x, y, Interpolator.LINEAR);
    }

    /**
     * Add a transition event, on mouse press and release, between two values,
     * with a specified transition half-time, over a specified property.
     * @param property property being modified. (for example, color property)
     * @param ms time in milliseconds for transition.
     * @param x value before transition.
     * @param y value after transition.
     * @param interpolator type of interpolation used.
     * @param <T> generic type.
     */
    public <T> void addMouseClickTransition(Property<T> property, int ms,
                                            T x, T y, Interpolator interpolator) {
        // Create event handlers from x to y, and from y to x.
        EventHandler<MouseEvent> mouseInHandler = createHandlerBetweenGenerics(property, ms,
                x, y, interpolator);
        EventHandler<MouseEvent> mouseOutHandler = createHandlerBetweenGenerics(property, ms,
                y, x, interpolator);

        // Bind event handlers on mouse press and release for this Node.
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseInHandler);
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseOutHandler);
    }

    /**
     * Add a transition event, on mouse enter and exit, between a specified double
     * value and a specified double offset.
     * @param property the property being modified.
     * @param ms the time in milliseconds for transition.
     * @param v the offset value.
     */
    public void addMouseOverTransition(DoubleProperty property, int ms,
                                       double v) {
        addMouseOverTransition(property, ms, v, Interpolator.LINEAR);
    }

    /**
     * Add a transition event, on mouse enter and exit, between a specified double
     * value and a specified double offset.
     * @param property the property being modified.
     * @param ms the time in milliseconds for transition.
     * @param v the offset value.
     * @param interpolator the interpolation type being used.
     */
    public void addMouseOverTransition(DoubleProperty property, int ms,
                                           double v, Interpolator interpolator) {
        // Create event handlers from x to y, and from y to x.
        EventHandler<MouseEvent> mouseInHandler = createHandlerTowardsDouble(property, ms,
                v, interpolator);
        EventHandler<MouseEvent> mouseOutHandler = createHandlerTowardsDouble(property, ms,
                -v, interpolator);

        // Bind event handlers on mouse enter and exit for this Node.
        node.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, mouseInHandler);
        node.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, mouseOutHandler);
    }

    /**
     * Add a transition event, on mouse enter and exit, between two values,
     * with a specified transition half-time, over a specified property
     * Also, if anyone has a better way to do the generic types, tell me,
     * because I haven't exactly used that a lot so far.
     * @param property property being modified. (for example, color property)
     * @param ms time in milliseconds for transition.
     * @param x value before transition.
     * @param y value after transition.
     * @param <T> generic type.
     */
    public <T> void addMouseOverTransition(Property<T> property, int ms,
                                       T x, T y) {
        addMouseOverTransition(property, ms, x, y, Interpolator.LINEAR);
    }

    /**
     * Add a transition event, on mouse enter and exit, between two values,
     * with a specified transition half-time, over a specified property
     * Also, if anyone has a better way to do the generic types, tell me,
     * because I haven't exactly used that a lot so far.
     * @param property property being modified. (for example, color property)
     * @param ms time in milliseconds for transition.
     * @param x value before transition.
     * @param y value after transition.
     * @param interpolator type of interpolation used.
     * @param <T> generic type.
     */
    public <T> void addMouseOverTransition(Property<T> property, int ms,
                                           T x, T y, Interpolator interpolator) {
        // Create event handlers from x to y, and from y to x.
        EventHandler<MouseEvent> mouseInHandler = createHandlerBetweenGenerics(property, ms,
                x, y, interpolator);
        EventHandler<MouseEvent> mouseOutHandler = createHandlerBetweenGenerics(property, ms,
                y, x, interpolator);

        // Bind event handlers on mouse enter and exit for this Node.
        node.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, mouseInHandler);
        node.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, mouseOutHandler);
    }

    /**
     * Event handler functions below.
     */

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
    private <T> EventHandler<MouseEvent> createHandlerBetweenGenerics(Property<T> property, int ms,
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

    /**
     * Returns an unspecified event handler with an embedded transition.
     * The transition operates over a specified property, with a duration in
     * milliseconds, between doubles x and y, and with specified interpolation.
     * The interpolation can be something like linear, edge-in, etc...
     * @param property the property to operate on.
     * @param ms duration in milliseconds.
     * @param v end value of transition.
     * @param interpolator type of interpolation used.
     * @return the new eventhandler.
     */
    private EventHandler<MouseEvent> createHandlerTowardsDouble(DoubleProperty property,
                                                                 int ms, double v,
                                                                 Interpolator interpolator) {
        return  e -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(property,
                                    property.getValue(),
                                    interpolator)),
                    new KeyFrame(Duration.millis(ms),
                            new KeyValue(property,
                                    property.getValue() + v,
                                    interpolator))
            );
            timeline.setCycleCount(1);
            timeline.setAutoReverse(false);
            timeline.play();
        };
    }

}
