package gui.misc;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import lombok.Getter;


/**
 * Class to assist in easy setup of transitions for Nodes on common events
 * such as mouse-over, buttonclick, etc.
 * Also, if anyone has a better way to do the generic types, tell me,
 * because I haven't exactly used that a lot so far.
 * @author Mark
 */
public class TransitionHelper {

    @Getter
    private Node node;
    @Getter
    private List<EventHandler> addedHandlers;
    private List<EventType> addedTypes;

    /**
     * Constructor of class.
     * @param node the node for which we want transitions.
     */
    public TransitionHelper(Node node) {
        this.node = node;
        this.addedHandlers = new ArrayList<EventHandler>();
        this.addedTypes = new ArrayList<EventType>();
    }

    /**
     * Transition functions below.
     */

    /**
     * Permanently destroys all mouse in/out and press/release transitions stored
     * in this transitionHelper. Use it to clear transitions on an object, if they
     * need to be re-made.
     */
    public void removeTransitions() {
        for (int i = 0; i < addedHandlers.size(); i++) {
            node.removeEventHandler(addedTypes.get(i), addedHandlers.get(i));
        }
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
        // setup timelines
        Timeline t1 = new Timeline();
        Timeline t2 = new Timeline();

        // Create event handlers from x to y, and from y to x.
        EventHandler mouseInHandler = createHandlerTowardsDouble(property, t1, t2, ms,
                v, false, interpolator);
        EventHandler mouseOutHandler = createHandlerTowardsDouble(property, t1, t2, ms,
                v, true, interpolator);

        // Store handlers so we can kill them again
        addedHandlers.add(mouseInHandler);
        addedHandlers.add(mouseOutHandler);
        addedTypes.add(MouseEvent.MOUSE_PRESSED.getSuperType());
        addedTypes.add(MouseEvent.MOUSE_RELEASED.getSuperType());

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
     * @param interpolator type of interpolation used.
     * @param <T> generic type.
     */
    public <T> void addMouseClickTransition(Property<T> property, int ms,
                                            T x, T y, Interpolator interpolator) {
        // Create event handlers from x to y, and from y to x.
        EventHandler mouseInHandler = createHandlerBetweenGenerics(property, ms,
                x, y, interpolator);
        EventHandler mouseOutHandler = createHandlerBetweenGenerics(property, ms,
                y, x, interpolator);

        // Store handlers so we can kill them again
        addedHandlers.add(mouseInHandler);
        addedHandlers.add(mouseOutHandler);
        addedTypes.add(MouseEvent.MOUSE_PRESSED.getSuperType());
        addedTypes.add(MouseEvent.MOUSE_RELEASED.getSuperType());

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
     * @param interpolator the interpolation type being used.
     */
    public void addMouseOverTransition(DoubleProperty property, int ms,
                                           double v, Interpolator interpolator) {
        // setup timelines
        Timeline t1 = new Timeline();
        Timeline t2 = new Timeline();

        // Create event handlers from x to y, and from y to x.
        EventHandler mouseInHandler = createHandlerTowardsDouble(property, t1, t2, ms,
                v, false, interpolator);
        EventHandler mouseOutHandler = createHandlerTowardsDouble(property, t1, t2, ms,
                v, true, interpolator);

        // Store handlers so we can kill them again
        addedHandlers.add(mouseInHandler);
        addedHandlers.add(mouseOutHandler);
        addedTypes.add(MouseEvent.MOUSE_ENTERED.getSuperType());
        addedTypes.add(MouseEvent.MOUSE_EXITED.getSuperType());

        // Bind event handlers on mouse enter and exit for this Node.
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseInHandler);
        node.addEventHandler(MouseEvent.MOUSE_EXITED, mouseOutHandler);
    }

    /**
     * Add a transition event, on mouse enter and exit, between two values,
     * with a specified transition half-time, over a specified property
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
        EventHandler mouseInHandler = createHandlerBetweenGenerics(property, ms,
                x, y, interpolator);
        EventHandler mouseOutHandler = createHandlerBetweenGenerics(property, ms,
                y, x, interpolator);

        // Store handlers so we can kill them again
        addedHandlers.add(mouseInHandler);
        addedHandlers.add(mouseOutHandler);
        addedTypes.add(MouseEvent.MOUSE_ENTERED.getSuperType());
        addedTypes.add(MouseEvent.MOUSE_EXITED.getSuperType());

        // Bind event handlers on mouse enter and exit for this Node.
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseInHandler);
        node.addEventHandler(MouseEvent.MOUSE_EXITED, mouseOutHandler);
    }

    /**
     * Event handler creation functions below.
     */

    /**
     * Returns an unspecified event handler with an embedded transition.
     * The transition operates over a specified property, with a duration in
     * milliseconds, between values x and y, and with specified interpolation.
     * The interpolation can be something like linear, edge-in, etc...
     * Realize that elements are always forced to the x and y position by this transition.
     * Downside is, this means that multiple transitions on the same property do not stack.
     * Upside is, this is very safe to use.
     * @param property the property to operate on.
     * @param ms duration in milliseconds.
     * @param x begin value of transition.
     * @param y end value of transition.
     * @param interpolator type of interpolation used.
     * @param <T> generic type.
     * @return the new eventhandler.
     */
    private <T> EventHandler createHandlerBetweenGenerics(Property<T> property, int ms,
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
     * Run a transition over a certain property of the node. Helpful function,
     * used instead of EventHandlers because it is
     * (A) a one-time transition,
     * (B) run from the current value, which is neat.
     * (C) not bound to a specific event.
     * Example usage: when firing a custom event. See StyledCheckbox for usage.
     * @param property property to transition over.
     * @param ms duration in milliseconds.
     * @param v value to transition towards.
     * @param interpolator type of Interpolation used.
     * @param <T> generic type.
     */
    public <T> void runTransitionToValue(Property<T> property, int ms, T v,
                                           Interpolator interpolator) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(property,
                                property.getValue(),
                                interpolator)),
                new KeyFrame(Duration.millis(ms),
                        new KeyValue(property,
                                v,
                                interpolator))
        );
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.play();
    }

    /**
     * Returns an unspecified event handler with an embedded transition.
     * The transition operates over a specified property, with a duration in
     * milliseconds, over an offset value v, and with specified interpolation.
     * The interpolation can be something like linear, edge-in, etc...
     * This transition moves from wherever the current point is.
     * This means elements can get lost if handled improperly.
     * @param property the property to operate on.
     * @param t1 timeline for forward-transition.
     * @param t2 timeline for backward-transition.
     * @param ms duration in milliseconds.
     * @param v end value of transition.
     * @param done whether double used is done.
     * @param interpolator type of interpolation used.
     * @return the new eventhandler.
     */
    private EventHandler createHandlerTowardsDouble(DoubleProperty property,
                                                                 Timeline t1, Timeline t2,
                                                                int ms, double v, boolean done,
                                                                Interpolator interpolator) {
        return  e -> {
            if (!done) { // setup animation t1 towards v.
                t1.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(0),
                                new KeyValue(property,
                                        property.getValue(),
                                        interpolator)) );
                t1.getKeyFrames().add(
                        new KeyFrame(Duration.millis(ms),
                                new KeyValue(property,
                                        property.getValue() + v,
                                        interpolator)) );
                t1.setCycleCount(1);
                t1.setAutoReverse(false);
                t1.play();
            } else { // setup animation t2 to move back what has been progressed by t1.
                double progressBack = v;
                if (t1.getStatus() == Animation.Status.RUNNING) {
                    progressBack *= (t1.getCurrentTime().toMillis()
                            / t1.getTotalDuration().toMillis());
                    t1.stop();
                }
                t2.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(0),
                                new KeyValue(property,
                                        property.getValue(),
                                        interpolator)) );
                t2.getKeyFrames().add(
                        new KeyFrame(Duration.millis(ms),
                                new KeyValue(property,
                                        property.getValue() - progressBack,
                                        interpolator)) );
                t2.setCycleCount(1);
                t2.setAutoReverse(false);
                t2.play();
            }
        };
    }

}
