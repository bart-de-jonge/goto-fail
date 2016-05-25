package data;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
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

    

    // The start count of the Shot.
    private DoubleProperty beginCount;

    // The end count of the Shot.
    private DoubleProperty endCount;

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
        beginCount = new SimpleDoubleProperty(0);
        endCount = new SimpleDoubleProperty(0);
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
    public Shot(String name, String description, double beginCount, double endCount) {
        log.debug("Adding Shot(name={}, description={}, beginCount={}, endCount={})",
                 name, description, beginCount, endCount);

        assert (beginCount <= endCount);

        this.name = name;
        this.description = description;
        this.beginCount = new SimpleDoubleProperty(beginCount);
        this.endCount = new SimpleDoubleProperty(endCount);
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

    /**
     * Get the begin count of this shot.
     * @return The begin count
     */
    public double getBeginCount() {
        return this.beginCount.get();
    }

    /**
     * Set the begin count of this shot.
     * @param beginCount The new begin count
     */
    public void setBeginCount(double beginCount) {
        this.beginCount.set(beginCount);
    }

    /**
     * Get the begin count's property (useful for bindings).
     * @return The Begin Count Property
     */
    public DoubleProperty getBeginCountProperty() {
        return this.beginCount;
    }

    /**
     * Get the end count of this shot.
     * @return The end count
     */
    public double getEndCount() {
        return this.endCount.get();
    }

    /**
     * Set the end count of this shot.
     * @param endCount The new end count
     */
    public void setEndCount(double endCount) {
        this.endCount.set(endCount);
    }

    /**
     * Get the end count's property (useful for bindings).
     * @return The End Count Property
     */
    public DoubleProperty getEndCountProperty() {
        return this.endCount;
    }
}
