package gui.events;

import data.Camera;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class AddTimelineEvent extends Event {
    
    @Getter
    private String description;
    
    @Getter
    private Camera camera;
    
    public static final EventType<AddTimelineEvent> TIMELINE_ADDED = 
            new EventType<>(Event.ANY, "TIMELINE_ADDED");

    /**
     * Construct a new AddTimelineEvent.
     * @param description the description of the timeline
     * @param camera the camera to be used in this timeline
     */
    public AddTimelineEvent(String description, Camera camera) {
        super(TIMELINE_ADDED);
        this.description = description;
        this.camera = camera;
    }
}
