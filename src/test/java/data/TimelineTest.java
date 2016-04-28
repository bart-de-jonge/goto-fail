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
        timeline = new CameraTimeline(new Camera("", "", new CameraType("", "", 0.0)), "Timeline");
        timeline.addShot("", "", 1, 3);
        timeline.addShot("", "", 2, 4);
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("Timeline", timeline.getDescription());
    }

    @Test
    public void checkOverlapTest() {
        timeline.checkOverlap(timeline.getShots().get(0), timeline.getShots().get(1));
        assertTrue(timeline.getShots().get(0).isOverlapping());
        assertTrue(timeline.getShots().get(1).isOverlapping());
    }

    @Test
    public void checkNoOverlapTest() {
        timeline.getShots().get(0).setEndCount(2);
        timeline.checkOverlap(timeline.getShots().get(0), timeline.getShots().get(1));
        assertFalse(timeline.getShots().get(0).isOverlapping());
        assertFalse(timeline.getShots().get(1).isOverlapping());
    }

    @Test
    public void clearTimelineTest() {
        assert (!timeline.getShots().isEmpty());
        timeline.clearShots();
        assertEquals(0, timeline.getShots().size());
    }
}
