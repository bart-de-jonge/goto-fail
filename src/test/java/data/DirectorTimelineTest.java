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
        timeline = new DirectorTimeline("A test director timeline", new ScriptingProject("test", 1));
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("A test director timeline", timeline.getDescription());
    }

    @Test
    public void addShotTest() {
        timeline.addShot("testname", "testdescription", 1, 2);
        assertEquals(1, timeline.getShots().size());
        assertEquals("testname", timeline.getShots().getFirst().getName());
    }

    @Test
    public void addDirectorShotTest() {
        DirectorShot shot = new DirectorShot("testname", "testdescription", 1, 2);
        timeline.addShot(shot);
        assertEquals(1, timeline.getShots().size());
        assertEquals(shot, timeline.getShots().get(0));
    }

    @Test
    public void addBeforeDirectorShotTest() {
        timeline.addShot("last", "", 2, 4);
        timeline.addShot("first", "", 1, 3);
        assertEquals("first", timeline.getShots().getFirst().getName());
    }

    @Test
    public void addAfterDirectorShotTest() {
        timeline.addShot("first", "", 1, 3);
        timeline.addShot("last", "", 2, 4);
        assertEquals("last", timeline.getShots().getLast().getName());
    }

    @Test
    public void addFullOverlapDirectorShotTest() {
        timeline.addShot("first", "", 2, 3);
        timeline.addShot("last", "", 1, 4);
        assertEquals("last", timeline.getShots().getFirst().getName());
    }

    @Test
    public void addSameTimeDirectorShotTest() {
        timeline.addShot("first", "", 1, 3);
        timeline.addShot("last", "", 1, 3);
        assertEquals("last", timeline.getShots().getFirst().getName());
    }

    @Test
    public void getShotsTest() {
        DirectorShot shot = new DirectorShot("testname", "testdescription", 1, 2);
        ArrayList<DirectorShot> shots = new ArrayList<DirectorShot>();
        shots.add(shot);
        timeline.addShot(shot);
        assertEquals(shots, timeline.getShots());
    }

    @Test
    public void clearShotsTest() {
        DirectorShot shot = new DirectorShot("testname", "testdescription", 1, 2);
        timeline.addShot(shot);
        timeline.clearShots();
        assertEquals(0, timeline.getShots().size());
    }

    @Test
    public void removeDirectorShotTest() {
        DirectorShot shot = new DirectorShot("", "", 0, 2);
        timeline.addShot(shot);
        timeline.addShot("", "", 2, 3);
        assertTrue(timeline.getShots().contains(shot));
        timeline.removeShot(shot);
        assertFalse(timeline.getShots().contains(shot));
    }
}