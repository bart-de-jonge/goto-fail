package control;

import data.ScriptingProject;
import gui.RootPane;
import gui.ShotBlock;
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
    private ToolViewController toolViewController;

    @Getter
    private DetailViewController detailViewController;

    @Getter
    private ShotBlock activeShotBlock;

    // Placeholder project in lieu of XML loading
    @Getter
    private final ScriptingProject scriptingProject = new ScriptingProject("BOSS Project", 1.0);

    /**
     * Constructor.
     * @param rootPane Root Window
     */
    public ControllerManager(RootPane rootPane) {
        this.rootPane = rootPane;
        initializeControllers();
    }

    /**
     * Overloaded constructor to directly pass controllers.
     * @param rootPane - the root window of the application
     * @param timelineController - the controller that controls the timelines
     * @param detailViewController - the controller that controls the detailview
     * @param toolViewController  - the controller that controls the toolview
     */
    public ControllerManager(RootPane rootPane, TimelineController timelineController,
                             DetailViewController detailViewController,
                             ToolViewController toolViewController) {
        this.rootPane = rootPane;
        this.timelineControl = timelineController;
        this.detailViewController = detailViewController;
        this.toolViewController = toolViewController;
    }

    /**
     * Initialize all necessary controllers.
     */
    private void initializeControllers() {
        timelineControl = new TimelineController(this);
        detailViewController = new DetailViewController(this);
        toolViewController = new ToolViewController(this);
    }

    /**
     * Sets the active ShotBlock and notifies necessary controllers.
     * @param block ShotBlock to set as active
     */
    public void setActiveShotBlock(ShotBlock block) {
        this.activeShotBlock = block;
        detailViewController.activeBlockChanged();
        toolViewController.activeBlockChanged();
    }
}
