package gui.events;

import java.util.ArrayList;

import data.Camera;
import data.CameraTimeline;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class NewProjectCreationEvent extends Event {
    
    @Getter
    private String description;
    
    @Getter
    private double secondsPerCount;
    
    @Getter
    private String directorTimelineDescription;
    
    @Getter
    private ArrayList<CameraTimeline> timelines;
    
    @Getter
    private ArrayList<Camera> cameras;
    
    public static final EventType<NewProjectCreationEvent> PROJECT_CREATED = 
            new EventType<>(Event.ANY, "PROJECT_CREATED");
    
    /**
     * Construct a new NewProjectCreationEvent.
     * @param description the description of the project
     * @param secondsPerCount the seconds per count in this project
     * @param directorTimelineDescription the description of the director timeline
     * @param timelines the timelines in this project
     * @param cameras the cameras used in this project
     */
    public NewProjectCreationEvent(String description,
                                   double secondsPerCount,
                                   String directorTimelineDescription,
                                   ArrayList<CameraTimeline> timelines,
                                   ArrayList<Camera> cameras) {
        super(PROJECT_CREATED);
        this.description = description;
        this.secondsPerCount = secondsPerCount;
        this.directorTimelineDescription = directorTimelineDescription;
        this.timelines = timelines;
        this.cameras = cameras;
    }

}
