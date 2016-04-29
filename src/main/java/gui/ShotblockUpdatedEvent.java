package gui;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import lombok.Getter;

/**
 * Created by Bart.
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
