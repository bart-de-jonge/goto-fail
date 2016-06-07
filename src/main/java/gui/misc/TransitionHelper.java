package gui.misc;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Getter;


/**
 * Class to assist in easy setup of transitions for Nodes on common events
 * such as mouse-over, buttonclick, etc.
 * Also, if anyone has a better way to do the generic types, tell me,
 * because I haven't exactly used that a lot so far.
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
     * @param v the offset value.
     * @param data the transition data
     */
    public void addMouseClickTransition(TransitionData data, double v) {
        setupMouseTransition(data, v, MouseEvent.MOUSE_PRESSED, MouseEvent.MOUSE_RELEASED);
    }

    /**
     * Add a transition event, on mouse press and release, between two values,
     * with a specified transition half-time, over a specified property.
     * @param x value before transition.
     * @param y value after transition.
     * @param data the transition data
     * @param <T> generic type.
     */
    public <T> void addMouseClickTransition(TransitionData<T> data, T x, T y) {
        // Create event handlers from x to y, and from y to x.
        EventHandler mouseInHandler = createHandlerBetweenGenerics(data, x, y);
        EventHandler mouseOutHandler = createHandlerBetweenGenerics(data, y, x);

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
     * Setup a mouse transition.
     * @param data the transition data
     * @param v the offset value
     * @param in the event type for ingoing transition
     * @param out the event type for outgoing transition
     */
    public void setupMouseTransition(TransitionData data, 
            double v, EventType<MouseEvent> in, EventType<MouseEvent> out) {
        // setup timelines
        Timeline t1 = new Timeline();
        Timeline t2 = new Timeline();
        
        // Create event handlers from x to y, and from y to x.
        EventHandler mouseInHandler = createHandlerTowardsDouble(data, t1, t2, v, false);
        EventHandler mouseOutHandler = createHandlerTowardsDouble(data, t1, t2, v, true);
        
        // Store handlers so we can kill them again
        addedHandlers.add(mouseInHandler);
        addedHandlers.add(mouseOutHandler);
        addedTypes.add(in.getSuperType());
        addedTypes.add(out.getSuperType());
        
        // Bind event handlers on mouse enter and exit for this Node.
        node.addEventHandler(in, mouseInHandler);
        node.addEventHandler(out, mouseOutHandler);
    }
    
    /**
     * Add a transition event, on mouse enter and exit, between a specified double
     * value and a specified double offset.
     * @param v the offset value.
     * @param data the transition data
     */
    public void addMouseOverTransition(TransitionData data, double v) {
        setupMouseTransition(data, v, MouseEvent.MOUSE_ENTERED, MouseEvent.MOUSE_EXITED);
    }

    /**
     * Add a transition event, on mouse enter and exit, between two values,
     * with a specified transition half-time, over a specified property
     * @param x value before transition.
     * @param y value after transition.
     * @param data the transition data
     * @param <T> generic type.
     */
    public <T> void addMouseOverTransition(TransitionData<T> data, T x, T y) {
        // Create event handlers from x to y, and from y to x.
        EventHandler mouseInHandler = createHandlerBetweenGenerics(data, x, y);
        EventHandler mouseOutHandler = createHandlerBetweenGenerics(data, y, x);

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
     * @param x begin value of transition.
     * @param y end value of transition.
     * @param data the transition data
     * @param <T> generic type.
     * @return the new eventhandler.
     */
    private <T> EventHandler createHandlerBetweenGenerics(TransitionData<T> data, T x, T y) {
        return  e -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(data.getProperty(),
                                    x,
                                    data.getInterpolator())),
                    new KeyFrame(Duration.millis(data.getMs()),
                            new KeyValue(data.getProperty(),
                                    y,
                                    data.getInterpolator()))
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
     * @param v value to transition towards.
     * @param data the transition data
     * @param <T> generic type.
     */
    public <T> void runTransitionToValue(TransitionData<T> data, T v) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(data.getProperty(),
                                data.getProperty().getValue(),
                                data.getInterpolator())),
                new KeyFrame(Duration.millis(data.getMs()),
                        new KeyValue(data.getProperty(),
                                v,
                                data.getInterpolator()))
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
     * @param t1 timeline for forward-transition.
     * @param t2 timeline for backward-transition.
     * @param v end value of transition.
     * @param done whether double used is done.
     * @param data the transition data.
     * @return the new eventhandler.
     */
    private EventHandler createHandlerTowardsDouble(TransitionData data, Timeline t1, Timeline t2,
                                                                double v, boolean done) {
        return  e -> {
            if (!done) { // setup animation t1 towards v.
                t1.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(0),
                                new KeyValue(data.getProperty(),
                                        data.getProperty().getValue(),
                                        data.getInterpolator())) );
                t1.getKeyFrames().add(
                        new KeyFrame(Duration.millis(data.getMs()),
                                new KeyValue(data.getProperty(),
                                        (double) data.getProperty().getValue() + v,
                                        data.getInterpolator())) );
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
                                new KeyValue(data.getProperty(),
                                        data.getProperty().getValue(),
                                        data.getInterpolator())) );
                t2.getKeyFrames().add(
                        new KeyFrame(Duration.millis(data.getMs()),
                                new KeyValue(data.getProperty(),
                                        (double) data.getProperty().getValue() - progressBack,
                                        data.getInterpolator())) );
                t2.setCycleCount(1);
                t2.setAutoReverse(false);
                t2.play();
            }
        };
    }
    
    /**
     * Add a default mouse over transition.
     * @param innerShadow the inner shadow to use
     * @param shadowTotalRadius the radius of the shadow
     * @param transitionMouseoverTime the time for the transition
     * @param shadowOpacity the opacity to use
     */
    public void addDefaultMouseOverTransition(InnerShadow innerShadow, 
            double shadowTotalRadius, int transitionMouseoverTime,
            double shadowOpacity) {
        addMouseOverTransition(new TransitionData<>(innerShadow.radiusProperty(),
                100, Interpolator.LINEAR), shadowTotalRadius);
        addMouseOverTransition(new TransitionData<>(innerShadow.colorProperty(),
                transitionMouseoverTime, Interpolator.LINEAR), Color.rgb(0, 0, 0, 0),
                Color.rgb(0, 0, 0, shadowOpacity));
    }

}
