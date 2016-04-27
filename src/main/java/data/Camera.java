package data;

        import lombok.EqualsAndHashCode;
        import lombok.Getter;
        import lombok.Setter;

/**
 * Created by Bart.
 * Class to store information about cameras.
 */
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

    // Number of counts this camera needs at maximum to move to a new position.
    // This defines the minimum margin between two consecutive shots
    // Defined in seconds
    // If set to a negative value the default margin in the cameraType is used
    @Getter @Setter
    private double movementMargin;

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
     * Reset the movementMargin overwrite.
     * This means the default value in the camera type is used
     */
    public void resetMovementMargin() {
        this.setMovementMargin(-1);
    }
}
