package data;

import lombok.Getter;

import java.util.ArrayList;

/**
 * Created by Bart.
 * Abstract class for timelines.
 */
public abstract class Timeline {

    // Description of this Timeline.
    @Getter
    private String description;

    // Collection of all Shot elements in this Timeline.
    private ArrayList<Shot> shots;

    /**
     * Constructor.
     * @param description - the description of this timeline
     */
    public Timeline(String description) {
        this.description = description;
        this.shots = new ArrayList<>();
    }
}
