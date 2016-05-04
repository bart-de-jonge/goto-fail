package data;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte1.other;

/**
 * Created by martijn.
 * This class contains information about a Shot.
 */
@XmlRootElement(name = "shot")
@Log4j2
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

    // True if the shot is colliding with another Shot.
    @Getter @Setter
    private boolean colliding;

    @Getter
    private ArrayList<Shot> collidesWith;
    
    /**
     * Default Constructor.
     */
    public Shot() {
        name = "";
        description = "";
        instance = 0;
        beginCount = 0;
        endCount = 0;
        collidesWith = new ArrayList<>();
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
        log.debug("Adding Shot(instance={}, name={}, description={}, beginCount={}, endCount={})",
                instance, name, description, beginCount, endCount);

        assert (beginCount <= endCount);

        this.name = name;
        this.description = description;
        this.instance = instance;
        this.beginCount = beginCount;
        this.endCount = endCount;
        this.collidesWith = new ArrayList<>();
    }

    /**
     * Compares the two shots.
     * @param other the other shot to compare with
     * @return if the Shot starts earlier than the other Shot -1 is returned. If the shots have
       equal start times, the end times are compared.
     */
    public int compareTo(Shot other) {
        log.debug("Comparing this(beginCount={}, endCount={}) to other(beginCount={}, endCount={})",
                beginCount, endCount, other.getBeginCount(), other.getEndCount());

        int result = Double.compare(getBeginCount(), other.getBeginCount());

        if (result == 0) {
            result = Double.compare(getEndCount(), other.getEndCount());
        }

        log.debug("Compare returns {}", result);

        return result;
    }

    /**
     * Checks whether the two Shots are colliding.
     *
     * @param other the other Shot the check the overlap with
     * @param movementOffset the offset the camera needs to move to the shot.
     * @return true when shots are colliding, false when there are not colliding
     */
    public boolean areOverlapping(Shot other, double movementOffset) {
        log.debug("Checking overlap of this(beginCount={}, endCount={}) to other(beginCount={}, "
                + "endCount={})", beginCount, endCount, other.getBeginCount(), other.getEndCount());

            boolean result = false;

            // Other shot starts during this shot
        if (other.getBeginCount() > getBeginCount() - movementOffset
            && other.getBeginCount() - movementOffset < getEndCount()) {
            log.debug("Other shot starts during this shot");
            result = true;
        }

        // This shot starts during other shot
        if (other.getEndCount() > getBeginCount() - movementOffset
                && other.getEndCount() < getEndCount()) {
            log.debug("Other shot ends during this shot");
            result = true;
        }

        // This shot entirely in other shot or the other way around
        if (other.getBeginCount() <= getBeginCount() && other.getEndCount() >= getEndCount()
            || getBeginCount() < other.getBeginCount() && getEndCount() > other.getEndCount()) {
            log.debug("One of the two shots completely overlaps the other");

            result = true;
        }

        editCollidesWith(result, other);

        // Update collides fields
        this.colliding = !this.collidesWith.isEmpty();
        other.setColliding(!other.getCollidesWith().isEmpty());

        log.debug("No overlap found");

        return result;
    }

    /**
     * Edit the collidesWith list using the provided other shot.
     * @param addCollisioin - identifies if we want to add or remove collisions
     * @param other - the shot to edit the collisions with
     */
    private void editCollidesWith(boolean addCollisioin, Shot other) {
        if (addCollisioin) {
            // Add to collideswith if it is not in there
            if (!this.collidesWith.contains(other)) {
                this.collidesWith.add(other);
            }
            if (!other.getCollidesWith().contains(this)) {
                other.getCollidesWith().add(this);
            }
        } else {
            // Remove from collideswith if it is in there, doesn't collide anymore
            if (this.collidesWith.contains(other)) {
                this.collidesWith.remove(other);
            }
            if (other.getCollidesWith().contains(this)) {
                other.getCollidesWith().remove(this);
            }
        }
    }
}
