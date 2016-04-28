package data;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Bart.
 * Class to store information about a camera timeline.
 */
public class CameraTimeline extends Timeline {

    // The camera that is associated with this timeline.
    @Getter @Setter
    private Camera camera;
    
    @Getter
    private ArrayList<CameraShot> shots;

    /**
     * Constructor.
     * @param camera        - the camera belonging to this timeline
     * @param description   - the description of this timeline
     */
    public CameraTimeline(Camera camera, String description) {
        super(description);
        this.camera = camera;
        this.shots = new ArrayList<CameraShot>();
    }
}
