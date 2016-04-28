package control;

import gui.RootPane;

/**
 * Created by alexandergeenen on 28/04/16.
 */
public class ControllerManager {

    private RootPane rootPane;

    private TimelineController timelineController;

    public ControllerManager(RootPane rootPane) {
        this.rootPane = rootPane;
        initializeControllers();
    }

    private void initializeControllers() {
       timelineController = new TimelineController(rootPane.getRootCenterArea().getGrid(), rootPane);
    }
}
