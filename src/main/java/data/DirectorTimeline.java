package data;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Bart.
 * Class to store information about a directors timeline.
 */
public class DirectorTimeline extends Timeline {

    @Getter @Setter
    private ArrayList<DirectorShot> shots;

    /**
     * Constructor.
     *
     * @param description - the description of this timeline
     */
    public DirectorTimeline(String description) {
        super(description);
        this.shots = new ArrayList<DirectorShot>();
    }

    public void addDirectorShot(DirectorShot shot) {
        shots.add(shot);
    }
}
