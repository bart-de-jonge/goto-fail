package data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class that represents a camera type.
 */
@ToString
@XmlRootElement(name = "cameraType")
@Log4j2
public class CameraType implements Cloneable {

    // Name of the cameraType
    @Getter @Setter
    private String name;

    // Description of the cameraType, things like serial number.
    @Getter @Setter
    private String description;

    /* Number of counts this camera needs at maximum to move to a new position.
     * This defines the minimum margin between two consecutive shots
     * Defined in seconds */
    @Getter @Setter
    private double movementMargin;
    
    /**
     * Default constructor.
     */
    public CameraType() {
        this("", "", -1);
    }
    
    /**
     * Constructor.
     * @param name - the name of this camera
     * @param description - a description of this camera
     * @param movementMargin - the minimum margin of this camera
     */
    public CameraType(String name, String description, double movementMargin) {
        this.name = name;
        this.description = description;
        this.movementMargin = movementMargin;
        log.debug("Created new CameraType(name={}, description={}, movementMargin={})",
            name, description, movementMargin);
    }
    
    /**
     * Clone this camera type.
     * @return A clone of this camera type
     */
    public CameraType clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        CameraType type = new CameraType(name, description, movementMargin);
        return type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        long temp;
        temp = Double.doubleToLongBits(movementMargin);
        result = prime * result + (int) (temp ^ (temp >>> Integer.SIZE));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof CameraType)) {
            return false;
        }

        CameraType that = (CameraType) o;

        if (Double.compare(that.getMovementMargin(), getMovementMargin()) != 0) {
            return false;
        }
        if ((getName() != null && !getName().equals(that.getName()))
                || (getName() == null && that.getName() != null)) {
            return false;
        }
        if (getDescription() != null) {
            return getDescription().equals(that.getDescription());
        } else {
            return that.getDescription() == null;
        }
    }
}
