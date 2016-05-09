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

    public AddTimelineEvent(String description, Camera camera) {
        super(TIMELINE_ADDED);
        this.description = description;
        this.camera = camera;
    }
}
