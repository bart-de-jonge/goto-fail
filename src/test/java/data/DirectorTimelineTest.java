package data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Bart.
 */
public class DirectorTimelineTest {

    DirectorTimeline timeline;
    @Before
    public void initializeTest() {
        timeline = new DirectorTimeline("A test director timeline");
    }

    @Test
    public void addDirectorShotTest() {
        DirectorShot shot = new DirectorShot("testname", "testdescription");
        timeline.addDirectorShot(shot);
        assertEquals(1, timeline.getShots().size());
        assertEquals(shot, timeline.getShots().get(0));
    }

    @Test
    public void getShotsTest() {
        DirectorShot shot = new DirectorShot("testname", "testdescription");
        ArrayList<DirectorShot> shots = new ArrayList<DirectorShot>();
        shots.add(shot);
        timeline.addDirectorShot(shot);
        assertEquals(shots, timeline.getShots());

    }

    @Test
    public void clearShotsTest() {
        DirectorShot shot = new DirectorShot("testname", "testdescription");
        ArrayList<DirectorShot> shots = new ArrayList<DirectorShot>();
        shots.add(shot);
        timeline.clearDirectorShots();
        assertEquals(0, timeline.getShots().size());
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("A test director timeline", timeline.getDescription());
    }

}