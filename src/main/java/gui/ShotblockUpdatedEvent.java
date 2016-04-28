package gui;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * Created by Bart.
 */
public class ShotblockUpdatedEvent extends Event {

    /**
     * The only valid EventType for the CustomEvent.
     */
    public static final EventType<ShotblockUpdatedEvent> SHOTBLOCK_UPDATED =
            new EventType<>(Event.ANY, "SHOTBLOCK_UPDATED");

    /**
     * Constructor.
     */
    public ShotblockUpdatedEvent() {
        super(SHOTBLOCK_UPDATED);
    }

    /**
     * Constructor.
     * @param source - the source
     * @param target - the target
     */
    public ShotblockUpdatedEvent(Object source, EventTarget target) {
        super(source, target, SHOTBLOCK_UPDATED);
    }
}
