package gui.events;

import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

/**
 * Class that describes a modal action.
 * @author alex
 */
public abstract class ModalEvent extends Event {

    /**
     * Enum that represents the modal event type
     * and manages the javafx event type.
     */
    public enum ModalEventType {
        CONFIRM ("MODAL_CONFIRMED"),
        ABORT ("MODAL_ABORTED");

        private final EventType<ModalEvent> event;

        ModalEventType(String s) {
            event = new EventType<>(Event.ANY, s);
        }

        public EventType<ModalEvent> getEvent() {
            return this.event;
        }
    }

    @Getter
    private ModalEventType type;

    /**
     * Constructor.
     * @param type Modal event type
     */
    public ModalEvent(ModalEventType type) {
        super(type.getEvent());
        this.type = type;
    }
}
