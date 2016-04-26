package data;

import lombok.Getter;

/**
 * Created by Bart.
 * Abstract class for timelines.
 */
public abstract class Timeline {

    // Description of this timeline
    @Getter
    private String description;

    /**
     * Constructor.
     * @param description - the description of this timeline
     */
    public Timeline(String description) {
        this.description = description;
    }
}
