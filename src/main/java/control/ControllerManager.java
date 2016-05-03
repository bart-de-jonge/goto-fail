package control;

import gui.RootPane;
import lombok.Getter;

/**
 * Class wrapper for model management controllers.
 * @author alex
 */
public class ControllerManager {

    @Getter
    private RootPane rootPane;

    @Getter
    private TimelineController timelineControl;

    @Getter
    private DetailViewController detailViewController;

    /**
     * Constructor.
     * @param rootPane Root Window
     */
    public ControllerManager(RootPane rootPane) {
        this.rootPane = rootPane;
        initializeControllers();
    }

    private void initializeControllers() {
        timelineControl = new TimelineController(rootPane);
        detailViewController = new DetailViewController(this);
    }
}
