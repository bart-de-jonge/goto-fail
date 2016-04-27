package data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Bart.
 */
@EqualsAndHashCode
public class CameraType {

    // Name of the cameraType
    @Getter @Setter
    private String name;

    // Description of the cameraType, things like serial number.
    @Getter @Setter
    private String description;

    // Number of counts this camera needs at maximum to move to a new position.
    // This defines the minimum margin between two consecutive shots
    // Defined in seconds
    @Getter @Setter
    private double movementMargin;

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
    }
}
