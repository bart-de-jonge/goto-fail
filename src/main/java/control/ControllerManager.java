package control;

import gui.RootPane;

/**
 * Class wrapper for model management controllers.
 * @author alex
 */
public class ControllerManager {

    private RootPane rootPane;

    private TimelineController timelineControl;

    /**
     * Constructor.
     * @param rootPane Root Window
     */
    public ControllerManager(RootPane rootPane) {
        this.rootPane = rootPane;
        initializeControllers();
    }

    private void initializeControllers() {
        timelineControl = new TimelineController(rootPane.getRootCenterArea().getGrid(), rootPane);
    }
}
