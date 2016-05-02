package data;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class to store information about a camera timeline.
 * @author Bart.
 */
@XmlRootElement(name = "cameraTimeline")
@ToString
public class CameraTimeline extends Timeline {

    // The camera that is associated with this timeline.
    @Getter @Setter
    private Camera camera;
    
    // Collection of all Shot elements in this Timeline.
    @XmlElementWrapper(name = "shotList")
    @XmlElement(name = "shot")
    @Getter
    private LinkedList<CameraShot> shots;
    
    /**
     * Default constructor.
     */
    public CameraTimeline() {
        super("", null);
        camera = null;
        shots = null;
    }

    /**
     * Constructor.
     *
     * @param camera the camera belonging to this timeline
     * @param description the description of this timeline
     * @param project the project that contains this timeline
     */
    public CameraTimeline(Camera camera, String description, ScriptingProject project) {
        super(description, project);
        this.camera = camera;
        shots = new LinkedList<>();
    }

    /**
     * {@code addShot} defaults to {@link CameraShot}.
     *
     * @param name the name of the Shot to add
     * @param description the description of the Shot
     * @param startCount the start count of the Shot
     * @param endCount the end count of the Shot
     * @return If no overlap is found, only the newly added shot will be returned. If any
       overlapping shots are found, all overlapping shots will be returned. If any overlapping
       shots are found, the shot that was added will be the last one in the list. 
     * @see CameraTimeline#addShot(CameraShot)
     */
    public ArrayList<CameraShot> addShot(String name, String description,
                                         int startCount, int endCount) {
        return addShot(new CameraShot(name, description, startCount, endCount));
    }

    /**
     * Adds a Shot to the Timeline. The Shot is inserted in a sorted manner. When a Shot is
     * inserted, the shots before the Shot have a lower start count of a lower end count. The
     * Shot after the inserted shot have a higher start count or end count. This method also checks
     * for overlapping shots. The overlapping shots will have their overlapping variable set to
     * true.
     *
     * @param shot the Shot to add to the timeline
     * @return If no overlap is found, only the newly added shot will be returned. If any
       overlapping shots are found, all overlapping shots will be returned. If any overlapping
       shots are found, the shot that was added will be the last one in the list.
     */
    public ArrayList<CameraShot> addShot(CameraShot shot) {
        ArrayList<CameraShot> result = new ArrayList<>();
        boolean added = false;

        // Add the new Shot the the shots
        for (int i = 0; i < shots.size(); i++) {
            CameraShot other = shots.get(i);
            if (!added) {
                if (shot.compareTo(other) <= 0) {
                    shots.add(i, shot);
                    added = true;
                }
            }
            if (checkOverlap(shot, other, camera.getMovementMargin())) {
                result.add(other);
            }
        }
        if (!added) {
            shots.add(shot);
        }
        result.add(shot);
        return result;
    }

    /**
     * Removes all shots from the Timeline.
     */
    public void clearShots() {
        shots.clear();
    }

    /**
     * Removes shot from the Timeline if it exists.
     * @param shot Shot to be removed.
     */
    public void removeShot(CameraShot shot) {
        shots.remove(shot);
    }
}
