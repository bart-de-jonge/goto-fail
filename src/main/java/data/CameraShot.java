package data;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.ToString;

/**
 * This class extends the Shot class, this one is specific for the CameraTimeline.
 * Created by martijn.
 */
@XmlRootElement(name = "cameraShot")
public class CameraShot extends Shot {

    // Counter that ensures no shots with duplicate numbers will be created.
    private static int instanceCounter = 0;
   
    /**
     * Default constructor.
     */
    public CameraShot() {
        super(instanceCounter, "", "", 0, 0);
        instanceCounter++;
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
        instanceCounter++;
    }
}
