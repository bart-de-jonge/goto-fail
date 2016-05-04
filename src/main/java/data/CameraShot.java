package data;

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
   
    /**
     * Default constructor.
     */
    public CameraShot() {
        new CameraShot("", "", 0, 0);
    }

    /**
     * The constructor for the Shot.
     * @param name the name of the Shot
     * @param description the description of the Shot
     * @param startCount the count the Shot starts
     * @param endCount the count the Shot ends
     */
    public CameraShot(String name, String description, int startCount, int endCount) {
        super(instanceCounter, name, description, startCount, endCount);
        log.debug("Created new CameraShot");
        instanceCounter++;
    }
}
