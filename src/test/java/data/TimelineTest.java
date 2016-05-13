package data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author martijn
 */
public class TimelineTest {
    CameraTimeline timeline;

    @Before
    public void initialize() {
        timeline = new CameraTimeline(new Camera("", "", new CameraType("", "", 0.0)), "Timeline",
                new ScriptingProject("test", "", 1));
        timeline.addShot("", "", 1, 3);
        timeline.addShot("", "", 2, 4);
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("Timeline", timeline.getDescription());
    }

    @Test
    public void setProjectTest() {
        ScriptingProject project = new ScriptingProject("", "test", 9.0);
        timeline.setProject(project);
        assertEquals(project, timeline.getProject());
    }

    @Test
    public void checkOverlapTest() {
        timeline.checkOverlap(timeline.getShots().get(0), timeline.getShots().get(1), 0);
        assertTrue(timeline.getShots().get(0).isColliding());
        assertTrue(timeline.getShots().get(1).isColliding());
    }

    @Test
    public void checkNoOverlapTest() {
        timeline.clearShots();
        timeline.addShot("", "", 1, 2);
        timeline.addShot("", "", 2, 3);
        assertFalse(timeline.checkOverlap(timeline.getShots().get(0), timeline.getShots().get(1), 0));
        assertFalse(timeline.getShots().get(0).isColliding());
        assertFalse(timeline.getShots().get(1).isColliding());
    }

    @Test
    public void clearTimelineTest() {
        assert (!timeline.getShots().isEmpty());
        timeline.clearShots();
        assertEquals(0, timeline.getShots().size());
    }
    
    @Test
    public void toStringTest() {
        Timeline timeline = new CameraTimeline(new Camera("", "", new CameraType("", "", 0.0)), "Timeline",
                new ScriptingProject("", "test", 1));
        assertEquals("CameraTimeline(description=Timeline, name=null, camera=Camera(name=, description=, cameraType=CameraType(name=, description=, movementMargin=0.0), movementMargin=0.0), shots=[])", timeline.toString());
    }
    
    @Test
    public void constructorWithoutArgumentsTest() {
        Timeline timeline = new DirectorTimeline();
        assertEquals(null, timeline.getProject());
    }
}
