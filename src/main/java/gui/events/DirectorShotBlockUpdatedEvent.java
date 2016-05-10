package gui.events;

import gui.centerarea.DirectorShotBlock;
import gui.centerarea.ShotBlock;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created by Bart.
 */
public class DirectorShotBlockUpdatedEvent extends ShotblockUpdatedEvent {

    /**
     * The eventtype.
     */
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
