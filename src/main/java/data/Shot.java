package data;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by martijn.
 * This class contains information about a Shot.
 */
@XmlRootElement(name = "shot")
public abstract class Shot {

    // The name of the Shot.
    @Getter @Setter
    private String name;

    // The description provides additional information for the director.
    @Getter @Setter
    private String description;

    // The instancenumber of the Shot.
    @Getter
    private int instance;

    // The start count of the Shot.
    @Getter @Setter
    private double startCount;

    // The end count of the Shot.
    @Getter @Setter
    private double endCount;

    // True if the shot is overlapping with another Shot.
    @Getter @Setter
    private boolean overlapping;
    
    /**
     * Default Constructor.
     */
    public Shot() {
        name = "";
        description = "";
        instance = 0;
        startCount = 0;
        endCount = 0;
    }

    /**
     * The constructor for the Shot.
     *
     * @param instance the instance number of the Shot
     * @param name the name of the Shot
     * @param description the description of the Shot
     * @param startCount the start count of the Shot
     * @param endCount the end count of the Shot
     */
    public Shot(int instance, String name, String description, int startCount, int endCount) {
        assert (startCount <= endCount);

        this.name = name;
        this.description = description;
        this.instance = instance;
        this.startCount = startCount;
        this.endCount = endCount;
    }

    /**
     * Compares the two shots.
     * @param other the other shot to compare with
     * @return if the Shot starts earlier than the other Shot -1 is returned. If the shots have
       equal start times, the end times are compared.
     */
    public int compareTo(Shot other) {
        int result = Double.compare(getStartCount(), other.getStartCount());

        if (result == 0) {
            result = Double.compare(getEndCount(), other.getEndCount());
        }

        return result;
    }

    /**
     * Checks whether the two Shots are overlapping.
     *
     * @param other the other Shot the check the overlap with
     * @param movementOffset the offset the camera needs to move to the shot.
     * @return true when shots are overlapping, false when there are not overlapping
     */
    public boolean areOverlapping(Shot other, double movementOffset) {
        if (other.getStartCount() > getStartCount() - movementOffset
                && other.getStartCount() < getEndCount()) {
            return true;
        }
        if (other.getEndCount() > getStartCount() - movementOffset
                && other.getEndCount() < getEndCount()) {
            return true;
        }
        if (other.getStartCount() <= getStartCount() && other.getEndCount() >= getEndCount()
            || getStartCount() < other.getStartCount() && getEndCount() > other.getEndCount()) {
            return true;
        }
        return false;
    }
}
