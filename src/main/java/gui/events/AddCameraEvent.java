package gui.events;

import data.CameraType;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class AddCameraEvent extends Event {
    
    @Getter
    private String name;
    
    @Getter
    private String description;
    
    @Getter
    private CameraType type;
    
    public static final EventType<AddCameraEvent> CAMERA_ADDED = 
            new EventType<>(Event.ANY, "CAMERA_ADDED");
    
    /**
     * Construct a new AddCameraEvent.
     * @param name the name of the camera
     * @param description the description of the camera
     * @param type the type of the camera
     */
    public AddCameraEvent(String name, String description, CameraType type) {
        super(CAMERA_ADDED);
        this.name = name;
        this.description = description;
        this.type = type;
    }

}
