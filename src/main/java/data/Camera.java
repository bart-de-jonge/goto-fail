package data;

        import lombok.Getter;
        import lombok.Setter;

/**
 * Created by Bart.
 * Class to store information about cameras.
 */
public class Camera {

    // Description of the camera, things like serial number.
    @Getter
    private String description;

    // Number of counts this camera needs at maximum to move to a new position.
    // This defines the minimum margin between two consecutive shots
    @Getter @Setter
    private int movementMargin;

    public Camera(String description, int movementMargin) {
        this.description = description;
        this.movementMargin = movementMargin;
    }
}
