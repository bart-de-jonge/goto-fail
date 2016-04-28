package control;

import gui.CameraShotBlock;
import gui.TimelinesGridPane;
import gui.RootPane;

/**
 * @author alex
 */
public class TimelineController {

    private TimelinesGridPane timelinePane;
    private RootPane rootPane;

    public TimelineController(TimelinesGridPane timelinePane, RootPane rootPane) {
        this.timelinePane = timelinePane;
        this.rootPane = rootPane;

        // Add a few test shots
        timelinePane.addCamerShotBlock(new CameraShotBlock(1, rootPane.getRootCenterArea(), 2, 3));
        timelinePane.addCamerShotBlock(new CameraShotBlock(0, rootPane.getRootCenterArea(), 1, 5));
    }
}
