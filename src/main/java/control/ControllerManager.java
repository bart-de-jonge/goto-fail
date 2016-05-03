package control;

import data.ScriptingProject;
import gui.RootPane;
import gui.ShotBlock;
import lombok.Getter;
import lombok.Setter;

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

    @Getter
    private ShotBlock activeBlock;

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

    private void initializeControllers() {
        timelineControl = new TimelineController(this);
        detailViewController = new DetailViewController(this);
    }

    public void setActiveBlock(ShotBlock block) {
        this.activeBlock = block;
        detailViewController.activeBlockChanged();
    }
}
