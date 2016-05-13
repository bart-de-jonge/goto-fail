package data;

import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;

/**
 * This class extends the Shot class, this one is specific for the CameraTimeline.
 * Created by martijn.
 */
@XmlRootElement(name = "cameraShot")
@ToString
@Log4j2
public class CameraShot extends Shot {

    // Counter that ensures no shots with duplicate numbers will be created.
    private static int instanceCounter = 0;

    @Getter
    @Setter
    private DirectorShot directorShot;
   
    /**
     * Default constructor.
     */
    public CameraShot() {
        super();
        CameraShot.incrementCounter();
    }

    /**
     * The constructor for the Shot.
     * @param name the name of the Shot
     * @param description the description of the Shot
     * @param startCount the count the Shot starts
     * @param endCount the count the Shot ends
     * @param directorShot the parent DirectorShot
     */
    public CameraShot(String name, String description,
                      double startCount, double endCount, DirectorShot directorShot) {
        super(instanceCounter, name, description, startCount, endCount);
        log.debug("Created new CameraShot");
        this.directorShot = directorShot;
        CameraShot.incrementCounter();
    }

    /**
     * Constructor.
     * @see #CameraShot(String, String, double, double, DirectorShot)
     */
    public CameraShot(String name, String description, double startCount, double endCount) {
        this(name, description, startCount, endCount, null);
    }

    /**
     * Static method to increment the instance counter.
     */
    public static void incrementCounter() {
        instanceCounter++;
    }
    
    @Override
    public CameraShot clone() {
        CameraShot result = new CameraShot(
                getName(), getDescription(), getBeginCount(), getEndCount(), getDirectorShot());
        result.setColliding(isColliding());
        return result;
    }
}
