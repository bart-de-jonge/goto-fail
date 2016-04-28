package data;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by martijn.
 * This class contains information about a Shot.
 */
public abstract class Shot {

    // The name of the Shot.
    @Getter @Setter
    private String name;

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
     * The constructor for the Shot.
     *
     * @param instance the instance number of the Shot
     * @param name the name of the Shot
     * @param startCount the start count of the Shot
     * @param endCount the end count of the Shot
     */
    public Shot(int instance, String name, int startCount, int endCount) {
        assert (startCount <= endCount);

        this.name = name;
        this.instance = instance;
        this.startCount = startCount;
        this.endCount = endCount;
    }

    /**
     * Compares the two shots.
     * @param other the other shot to compare with
     * @return if the Shot starts earlier than the other Shot -1 is returned. If the shots have
     * equal start times, the end times are compared.
     */
    public int compareTo(Shot other) {
        int result = Double.compare(getStartCount(), other.getStartCount());

        if (result == 0) {
            result = Double.compare(getEndCount(), other.getEndCount());
        }

        return result;
    }

    // TODO: 28-4-16 Implement the offset due to setup of cameras
    /**
     * Checks whether the two Shots are overlapping.
     *
     * @param other the other Shot the check the overlap with
     * @return true when shots are overlapping, false when there are not overlapping
     */
    public boolean areOverlapping(Shot other) {
        if (other.getStartCount() > getStartCount() &&  other.getStartCount() < getEndCount()) {
            return true;
        }
        if (other.getEndCount() > getStartCount() && other.getEndCount() < getEndCount()) {
            return true;
        }
        if (other.getStartCount() <= getStartCount() && other.getEndCount() >= getEndCount()
            || getStartCount() < other.getStartCount() && getEndCount() > other.getEndCount()) {
            return true;
        }
        return false;
    }
}
