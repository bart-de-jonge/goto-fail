package gui;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Class that describes a modal action.
 * @author alex
 */
public abstract class ModalEvent extends Event {
    public enum ModalEventType {
        CONFIRM ("MODAL_CONFIRMED"),
        ABORT ("MODAL_ABORTED");

        private final String typeName;

        ModalEventType(String s) {
            typeName = s;
        }

        public String toString() {
            return this.typeName;
        }
    }

    private ModalEventType type;

    /**
     * Constructor.
     * @param type Modal event type
     */
    public ModalEvent(ModalEventType type) {
        super(new EventType<>(Event.ANY, type.toString()));
        this.type = type;
    }
}
