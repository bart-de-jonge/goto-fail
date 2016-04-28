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
     * @param startCount the count the Shot starts
     * @param endCount the count the Shot ends
     */
    public CameraShot(String name, int startCount, int endCount) {
        super(instanceCounter, name, startCount, endCount);
        instanceCounter++;
    }
}
