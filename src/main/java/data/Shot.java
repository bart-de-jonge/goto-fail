package data;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by martijn.
 * This class contains information about a Shot.
 */
@XmlRootElement(name = "shot")
@ToString
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
    private double beginCount;

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
        beginCount = 0;
        endCount = 0;
    }

    /**
     * The constructor for the Shot.
     *
     * @param instance the instance number of the Shot
     * @param name the name of the Shot
     * @param description the description of the Shot
     * @param beginCount the start count of the Shot
     * @param endCount the end count of the Shot
     */
    public Shot(int instance, String name, String description, int beginCount, int endCount) {
        assert (beginCount <= endCount);

        this.name = name;
        this.description = description;
        this.instance = instance;
        this.beginCount = beginCount;
        this.endCount = endCount;
    }

    /**
     * Compares the two shots.
     * @param other the other shot to compare with
     * @return if the Shot starts earlier than the other Shot -1 is returned. If the shots have
       equal start times, the end times are compared.
     */
    public int compareTo(Shot other) {
        int result = Double.compare(getBeginCount(), other.getBeginCount());

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
        if (other.getBeginCount() > getBeginCount() - movementOffset
                && other.getBeginCount() < getEndCount()) {
            return true;
        }
        if (other.getEndCount() > getBeginCount() - movementOffset
                && other.getEndCount() < getEndCount()) {
            return true;
        }
        if (other.getBeginCount() <= getBeginCount() && other.getEndCount() >= getEndCount()
            || getBeginCount() < other.getBeginCount() && getEndCount() > other.getEndCount()) {
            return true;
        }
        return false;
    }
}
