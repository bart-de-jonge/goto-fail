package data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

/**
 * Class to store information about cameras.
 */
@XmlRootElement(name = "camera")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@Log4j2
public class Camera implements Cloneable {

    // Name of the camera
    @Getter @Setter
    private String name;
    
    // Description of the camera
    @Getter @Setter
    private String description;

    // Type of this camera
    @Getter @Setter
    private CameraType cameraType;
    
    @Getter @Setter
    private String ip;

    // The movementMargin, the time it takes for the Camera to move to a new position
    // Defined in seconds
    @Getter @Setter
    private double movementMargin;

    // Counter that ensures no timelines with duplicate numbers will be created.
    @Getter
    private static int instanceCounter = 0;

    // The instancenumber of the timeline.
    @Getter @Setter
    private int instance;
    
    /**
     * Default constructor.
     */
    public Camera() {
        this("", "", null);
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
        this.instance = Camera.getInstanceCounter();
        Camera.incrementCounter();
        log.debug("Created new Camera(name={}, description={}, cameraType={}",
                name, description, cameraType);
        this.ip = "";
    }
    
    @Override
    public Camera clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Camera camera = new Camera(name, description, cameraType.clone());
        camera.setInstance(this.getInstance());
        return camera;
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

    /**
     * Static method to increment the instance counter.
     */
    public static void incrementCounter() {
        instanceCounter++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null ||  !(o instanceof Camera)) {
            return false;
        }

        Camera camera = (Camera) o;

        if (Double.compare(camera.getMovementMargin(), getMovementMargin()) != 0) {
            return false;
        }
        if (instance != camera.instance) {
            return false;
        }
        if ((name != null && !name.equals(camera.name))
                || (name == null && camera.name != null)) {
            return false;
        }
        if ((description != null && !description.equals(camera.description))
                || (description == null && camera.description != null)) {
            return false;
        }
        if ((cameraType != null && !cameraType.equals(camera.cameraType))
                || (cameraType == null && camera.cameraType != null)) {
            return false;
        }
        if (ip != null) {
            return ip.equals(camera.ip);
        } else {
            return camera.ip == null;
        }
    }
}
