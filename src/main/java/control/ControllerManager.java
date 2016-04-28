package control;

import gui.RootPane;
import lombok.Getter;

/**
 * Class wrapper for model management controllers.
 * @author alex
 */
public class ControllerManager {

    private RootPane rootPane;

    @Getter
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
