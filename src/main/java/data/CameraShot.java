package data;

import lombok.Getter;

/**
 * This class extends the Shot class, this one is specific for the CameraTimeline.
 * Created by martijn.
 */
public class CameraShot extends Shot {

    // Counter that ensures no shots with duplicate numbers will be created.
    private static int instanceCounter = 0;

    /**
     * The constructor for the Shot.
     * @param name the name of the Shot
     */
    public CameraShot(String name) {
        super(instanceCounter, name);
        instanceCounter++;
    }
}
