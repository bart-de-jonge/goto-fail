package gui.events;

import gui.centerarea.ShotBlock;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

/**
 * Event for when a shot block is updated.
 */
public abstract class ShotblockUpdatedEvent extends Event {

    @Getter
    private ShotBlock shotBlock;

    /**
     * The EventType.
     */
    public static final EventType<ShotblockUpdatedEvent> SHOTBLOCK_UPDATED =
            new EventType<>(Event.ANY, "SHOTBLOCK_UPDATED");

    /**
     * Constructor.
     */
    public ShotblockUpdatedEvent(ShotBlock shotBlock) {
        super(SHOTBLOCK_UPDATED);
        this.shotBlock = shotBlock;
    }

}
