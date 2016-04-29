package data;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Bart.
 * Class to store information about cameras.
 */
@XmlRootElement(name = "camera")
@EqualsAndHashCode
public class Camera {

    // Name of the camera
    @Getter @Setter
    private String name;

    // Description of the camera
    @Getter @Setter
    private String description;

    // Type of this camera
    @Getter @Setter
    private CameraType cameraType;

    // The movementMargin, the time it takes for the Camera to move to a new position
    @Setter
    private double movementMargin;
    
    /**
     * Default constructor.
     */
    public Camera() {
        name = "";
        description = "";
        cameraType = null;
        movementMargin = -1;
    }

    /**
     * Constructor.
     * @param name - the name of this camera
     * @param description - a description of this camera
     * @param cameraType - the type of this camera
     */
    public Camera(String name, String description, CameraType cameraType) {
        this.name = name;
        this.description = description;
        this.cameraType = cameraType;
        this.movementMargin = -1;
    }

    /**
     * Number of counts this camera needs at maximum to move to a new position.
     * This defines the minimum margin between the two consecutive shots defined in
     * seconds. If set to a negative value the default margin in the cameraType is used.
     * @return the time it takes for the camera to move to a new position in seconds
     */
    public double getMovementMargin() {
        if (movementMargin < 0) {
            return cameraType.getMovementMargin();
        }
        return movementMargin;
    }

    /**
     * Reset the movementMargin overwrite.
     * This means the default value in the camera type is used
     */
    public void resetMovementMargin() {
        this.setMovementMargin(-1);
    }
}
