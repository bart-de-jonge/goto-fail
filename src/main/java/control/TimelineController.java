package control;

import gui.CameraShotBlock;
import gui.TimelinesGridPane;
import gui.RootPane;

/**
 * Class that controls the timeline.
 * @author alex
 */
public class TimelineController {

    private TimelinesGridPane timelinePane;
    private RootPane rootPane;

    /**
     * Constructor.
     * @param timelinePane Timeline view to be controlled.
     * @param rootPane Root Pane.
     */
    public TimelineController(TimelinesGridPane timelinePane, RootPane rootPane) {
        this.timelinePane = timelinePane;
        this.rootPane = rootPane;
    }
}
