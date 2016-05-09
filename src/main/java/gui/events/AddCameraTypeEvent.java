package gui.events;

import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class AddCameraTypeEvent extends Event {
    
    @Getter
    private String name;
    
    @Getter
    private String description;
    
    @Getter
    private double movementMargin;
    
    public static final EventType<AddCameraTypeEvent> CAMERA_TYPE_ADDED =
            new EventType<>(Event.ANY, "CAMERA_TYPE_ADDED");
    
    public AddCameraTypeEvent(String name, String description, double movementMargin) {
        super(CAMERA_TYPE_ADDED);
        this.name = name;
        this.description = description;
        this.movementMargin = movementMargin;
    }

}
