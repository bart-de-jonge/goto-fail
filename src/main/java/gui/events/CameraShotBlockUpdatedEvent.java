package gui.events;

import gui.centerarea.CameraShotBlock;
import gui.centerarea.ShotBlock;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

/**
 * Event for when a camera shot block is updated.
 */
public class CameraShotBlockUpdatedEvent extends ShotblockUpdatedEvent {


    @Getter
    private int oldTimelineNumber;

    /**
     * The eventtype.
     */
    public static final EventType<CameraShotBlockUpdatedEvent> CAMERASHOTBLOCK_UPDATED =
            new EventType<>(Event.ANY, "CAMERASHOTBLOCK_UPDATED");

    /**
     * Constructor.
     *
     * @param shotBlock - The shotblock belonging to this event
     * @param oldTimelineNumber - the old timelinenumber (for controller logic)
     */
    public CameraShotBlockUpdatedEvent(ShotBlock shotBlock, int oldTimelineNumber) {
        super(shotBlock);
        this.oldTimelineNumber = oldTimelineNumber;
    }

    public CameraShotBlock getCameraShotBlock() {
        return (CameraShotBlock) this.getShotBlock() ;
    }
}
