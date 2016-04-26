package data;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Bart.
 * Abstract class for timelines.
 */
public abstract class Timeline {

    // Description of this timeline
    @Getter
    private String description;

    // Lowest count of this timeline
    @Getter @Setter
    private int beginCount;

    // Highest count of this timeline
    @Getter @Setter
    private int endCount;

    /**
     * Constructor.
     * @param description - the description of this timeline
     */
    public Timeline(String description) {
        this.description = description;
        this.beginCount = 0;
        this.endCount = 0;
    }
}
