package gui.events;

import gui.centerarea.DirectorShotBlock;
import gui.centerarea.ShotBlock;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event for when a director shot block is updated.
 */
public class DirectorShotBlockUpdatedEvent extends ShotblockUpdatedEvent {

    public static final EventType<DirectorShotBlockUpdatedEvent> DIRECTORSHOTBLOCK_UPDATED =
            new EventType<>(Event.ANY, "DIRECTORSHOTBLOCK_UPDATED");

    /**
     * Constructor.
     *
     * @param shotBlock - The DirectorShotblock belonging to this event
     */
    public DirectorShotBlockUpdatedEvent(ShotBlock shotBlock) {
        super(shotBlock);
    }

    public DirectorShotBlock getDirectorShotBlock() {
        return (DirectorShotBlock) this.getShotBlock() ;
    }
}
