package data;

import lombok.Getter;

/**
 * Created by Bart.
 * Abstract class for timelines.
 */
public abstract class Timeline {

    // Description of this Timeline.
    @Getter
    private String description;

    /**
     * Constructor.
     * @param description - the description of this timeline
     */
    public Timeline(String description) {
        this.description = description;
    }

    /**
     * Checks overlap between two shots. If the two shots are overlapping, the two shots will have
     * their overlapping variables set to true.
     * @param s1 the first Shot to check overlap
     * @param s2 the other Shot to check overlap
     * @return true when the two shots are overlapping, false if not
     */
    public boolean checkOverlap(Shot s1, Shot s2) {
        if (s1.areOverlapping(s2)) {
            s1.setOverlapping(true);
            s2.setOverlapping(true);
            return true;
        }
        return false;
    }
}
