package data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The DirectorShot class has more elaborate information for the Director, like the description.
 * Created by martijn.
 */
@XmlRootElement(name = "directorShot")
@ToString
@Log4j2
public class DirectorShot extends Shot {

    // Counter that ensures no shots with duplicate numbers will be created.
    private static int instanceCounter = 0;

    @Getter
    private Set<Integer> timelineIndices;

    @Getter
    private Set<CameraShot> cameraShots;

    // Additional time to film before the real shot starts
    @Getter
    @Setter
    private double frontShotPadding = 0.0;

    // Additional time to film before the real shot starts
    @Getter
    @Setter
    private double endShotPadding = 0.0;

    /**
     * Default Constructor.
     */
    public DirectorShot() {
        this("", "", 0, 0, 0, 0, new ArrayList<>());
    }

    /**
     * The constructor for the Shot.
     * @param name the name of the Shot
     * @param description the description of the Shot
     * @param startCount the start count of the Shot
     * @param endCount the end count of the Shot
     * @param frontShotPadding the additional time to film before the shot starts
     * @param endShotPadding the additional time to film after the shot starts
     * @param cameras the cameras that the shot will or can use
     */
    public DirectorShot(String name, String description, double startCount, double endCount, double frontShotPadding, double endShotPadding, List<Integer> cameras) {
        super(instanceCounter, name, description, startCount, endCount);
        this.frontShotPadding = frontShotPadding;
        this.endShotPadding = endShotPadding;
        this.timelineIndices = new HashSet<>(cameras);
        this.cameraShots = new HashSet<>();
        log.debug("Created new DirectorShot");
        DirectorShot.incrementCounter();
    }

    public static void incrementCounter() {
        instanceCounter++;
    }

    /**
     * Add a camera timeline index (but not an actual shot).
     * @param index timeline index to add
     */
    public void addCameraTimelineIndex(int index) {
        this.timelineIndices.add(index);
    }

    /**
     * Add a CameraShot that belongs to this DirectorShot.
     * @param shot CameraShot to be added
     */
    public void addCameraShot(CameraShot shot) {
        this.cameraShots.add(shot);
    }

    /**
     * Remove/Free an associated CameraShot from this DirectorShot.
     * @param shot Shot to be removed
     * @param index Timeline index of the removed shot
     */
    public void removeCameraShot(CameraShot shot, int index) {
        this.timelineIndices.remove(index);
        this.cameraShots.remove(shot);
    }
}
