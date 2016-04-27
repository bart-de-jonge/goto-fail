package data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bart.
 */
public class CameraTimelineTest {

    CameraTimeline timeline;
    @Before
    public void initialize() {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        timeline = new CameraTimeline(camera, "A test cameratimeline");
    }

    @Test
    public void getCamera() throws Exception {
        CameraType type = new CameraType("cameratype-1", "A test cameratype", 2.00);
        Camera camera = new Camera("camera-1", "A test camera", type);
        assertEquals(camera, timeline.getCamera());
    }

    @Test
    public void setCamera() throws Exception {
        CameraType type = new CameraType("cameratype-2", "A test cameratype 2", 3.00);
        Camera camera = new Camera("camera-2", "A test camera 2", type);
        timeline.setCamera(camera);
        assertEquals(camera, timeline.getCamera());

    }

}