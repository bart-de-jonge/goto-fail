package data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class to store information about a directors timeline.
 * @author Bart.
 */
public class DirectorTimeline extends Timeline {

    // Collection of all Shot elements in this Timeline.
    @Getter
    private LinkedList<DirectorShot> shots;

    /**
     * Constructor.
     *
     * @param description the description of this timeline
     */
    public DirectorTimeline(String description) {
        super(description);
        shots = new LinkedList<>();
    }

    /**
     * {@code addShot} defaults to {@link DirectorShot}.
     *
     * @param name the name of the Shot to add
     * @param description the description of the Shot
     * @param startCount the start count of the Shot
     * @param endCount the end count of the Shot
     * @return If no overlap is found, only the newly added shot will be returned. If any
       overlapping shots are found, all overlapping shots will be returned. If any overlapping
       shots are found, the shot that was added will be the last one in the list.
     * @see DirectorTimeline#addShot(DirectorShot)
     */
    public ArrayList<DirectorShot> addShot(String name, String description,
                                           int startCount, int endCount) {
        return addShot(new DirectorShot(name, description, startCount, endCount));
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
    public ArrayList<DirectorShot> addShot(DirectorShot shot) {
        ArrayList<DirectorShot> result = new ArrayList<>();
        boolean added = false;

        // Add the new Shot the the shots
        for (int i = 0; i < shots.size(); i++) {
            DirectorShot other = shots.get(i);
            if (!added) {
                if (shot.compareTo(other) <= 0) {
                    shots.add(i, shot);
                    added = true;
                }
            }
            if (shot.areOverlapping(other)) {
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
}
