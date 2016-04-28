package control;

import gui.RootPane;

/**
 * Created by alexandergeenen on 28/04/16.
 */
public class ControllerManager {

    private RootPane rootPane;

    private TimelineController timelineControl;

    public ControllerManager(RootPane rootPane) {
        this.rootPane = rootPane;
        initializeControllers();
    }

    private void initializeControllers() {
        timelineControl = new TimelineController(rootPane.getRootCenterArea().getGrid(), rootPane);
    }
}
