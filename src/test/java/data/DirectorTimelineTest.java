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
        timeline = new DirectorTimeline("A test director timeline", new ScriptingProject("test", "", 1));
    }

    @Test
    public void getOverlappingShots() {
        DirectorShot shot = new DirectorShot();
        timeline.addShot(shot);
        assertEquals(1, timeline.getOverlappingShots(shot).size());
        assertTrue(timeline.getOverlappingShots(shot).contains(shot));
    }

    @Test
    public void removeShot() {
        DirectorShot shot = new DirectorShot();
        timeline.addShot(shot);
        assertTrue(timeline.getShots().contains(shot));
        timeline.removeShot(shot);
        assertFalse(timeline.getShots().contains(shot));
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("A test director timeline", timeline.getDescription());
    }
    
    @Test
    public void constructorWithNoArgumentsTest() {
        DirectorTimeline timeline = new DirectorTimeline();
        assertEquals("", timeline.getDescription());
    }
    
    @Test
    public void toStringTest() {
        assertEquals("DirectorTimeline(shots=[], description=A test director timeline)", timeline.toString());
    }

    @Test
    public void addDirectorShotTest() {
        DirectorShot shot = new DirectorShot(new GeneralShotData("testname", "testdescription", 1, 2), 0, 0, new ArrayList<>());
        timeline.addShot(shot);
        assertEquals(1, timeline.getShots().size());
        assertEquals(shot, timeline.getShots().get(0));
    }

    @Test
    public void addBeforeDirectorShotTest() {
        timeline.addShot(new DirectorShot(new GeneralShotData("last", "", 2, 4), 0, 0, new ArrayList<>()));
        timeline.addShot(new DirectorShot(new GeneralShotData("first", "", 1, 3), 0, 0, new ArrayList<>()));
        assertEquals("first", timeline.getShots().getFirst().getName());
    }

    @Test
    public void addAfterDirectorShotTest() {
        timeline.addShot(new DirectorShot(new GeneralShotData("first", "", 1, 3), 0, 0, new ArrayList<>()));
        timeline.addShot(new DirectorShot(new GeneralShotData("last", "", 2, 4), 0, 0, new ArrayList<>()));
        assertEquals("last", timeline.getShots().getLast().getName());
    }

    @Test
    public void addFullOverlapDirectorShotTest() {
        timeline.addShot(new DirectorShot(new GeneralShotData("first", "", 2, 3), 0, 0, new ArrayList<>()));
        timeline.addShot(new DirectorShot(new GeneralShotData("last", "", 1, 4), 0, 0, new ArrayList<>()));
        assertEquals("last", timeline.getShots().getFirst().getName());
    }

    @Test
    public void addSameTimeDirectorShotTest() {
        timeline.addShot(new DirectorShot(new GeneralShotData("first", "", 1, 3), 0, 0, new ArrayList<>()));
        timeline.addShot(new DirectorShot(new GeneralShotData("last", "", 1, 3), 0, 0, new ArrayList<>()));
        assertEquals("last", timeline.getShots().getFirst().getName());
    }

    @Test
    public void getShotsTest() {
        DirectorShot shot = new DirectorShot(new GeneralShotData("testname", "testdescription", 1, 2), 0, 0, new ArrayList<>());
        ArrayList<DirectorShot> shots = new ArrayList<DirectorShot>();
        shots.add(shot);
        timeline.addShot(shot);
        assertEquals(shots, timeline.getShots());

    }

    @Test
    public void clearShotsTest() {
        DirectorShot shot = new DirectorShot(new GeneralShotData("testname", "testdescription", 1, 2), 0, 0, new ArrayList<>());
        ArrayList<DirectorShot> shots = new ArrayList<DirectorShot>();
        shots.add(shot);
        timeline.clearShots();
        assertEquals(0, timeline.getShots().size());
    }
}