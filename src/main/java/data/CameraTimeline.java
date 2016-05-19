package data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Class to store information about a camera timeline.
 */
@XmlRootElement(name = "cameraTimeline")
@ToString
public class CameraTimeline extends Timeline {
    
    @Getter @Setter
    private String description;
    
    // Name of this timeline
    @Getter @Setter
    private String name;

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
        super(null);
        description = null;
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
        super(project);
        this.description = description;
        this.camera = camera;
        shots = new LinkedList<>();
    }
    
    /**
     * Clone constructor.
     * @param timeline timeline to clone
     */
    public CameraTimeline(CameraTimeline timeline) {
        super(timeline.getProject());
        this.description = timeline.getDescription();
        this.camera = new Camera(timeline.getCamera());
        this.shots = timeline.getShots();
        this.name = timeline.getName();
    }
    
    /**
     * Constructor with name variable.
     * @param name the name of the timeline
     * @param camera the camera of this timeline
     * @param description the description of the timeline
     * @param project the project this timeline is a part of
     */
    public CameraTimeline(String name, Camera camera,
            String description, ScriptingProject project) {
        this(camera, description, project);
        this.name = name;
    }

    /**
     * {@code addShot} defaults to {@link CameraShot}.
     *
     * @param name the name of the Shot to add
     * @param description the description of the Shot
     * @param startCount the start count of the Shot
     * @param endCount the end count of the Shot
     * @return If no overlap is found, only the newly added shot will be returned. If any
       colliding shots are found, all colliding shots will be returned. If any colliding
       shots are found, the shot that was added will be the last one in the list. 
     * @see CameraTimeline#addShot(CameraShot)
     */
    public ArrayList<CameraShot> addShot(String name, String description,
                                         double startCount, double endCount) {
        return addShot(new CameraShot(name, description, startCount, endCount));
    }

    /**
     * Adds a Shot to the Timeline. The Shot is inserted in a sorted manner. When a Shot is
     * inserted, the shots before the Shot have a lower start count of a lower end count. The
     * Shot after the inserted shot have a higher start count or end count. This method also checks
     * for colliding shots. The colliding shots will have their colliding variable set to
     * true.
     *
     * @param shot the Shot to add to the timeline
     * @return If no overlap is found, only the newly added shot will be returned. If any
       colliding shots are found, all colliding shots will be returned. If any colliding
       shots are found, the shot that was added will be the last one in the list.
     */
    public ArrayList<CameraShot> addShot(CameraShot shot) {
        boolean added = false;

        // Add the new Shot the the shots
        for (int i = 0; i < shots.size(); i++) {
            CameraShot other = shots.get(i);
            if (!added && shot.compareTo(other) <= 0) {
                shots.add(i, shot);
                added = true; 
            }
        }
        if (!added) {
            shots.add(shot);
        }
        return getOverlappingShots(shot);
    }

    /**
     * Get the list of shots colliding with the given shots.
     * @param shot - the shot to check with
     * @return - only the shot when no overlap, list of colliding shots otherwise
     */
    public ArrayList<CameraShot> getOverlappingShots(CameraShot shot) {
        ArrayList<CameraShot> result = new ArrayList<>();

        // check for colliding shots
        result.addAll(shots.stream()
                .filter(other -> shot != other)
                .filter(other -> checkOverlap(shot, other, camera.getMovementMargin()))
                .collect(Collectors.toList()));
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
