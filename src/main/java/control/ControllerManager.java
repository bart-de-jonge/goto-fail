package control;

import data.CameraTimeline;
import data.DirectorTimeline;
import data.ScriptingProject;
import gui.centerarea.ShotBlock;
import gui.events.NewProjectCreationEvent;
import gui.modal.NewProjectModalView;
import gui.root.RootCenterArea;
import gui.root.RootPane;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Class wrapper for model management controllers.
 * @author alex
 */
@Log4j2
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
    @Setter
    private ScriptingProject scriptingProject = new ScriptingProject("BOSS Project", 1.0);

    /**
     * Constructor.
     *
     * @param rootPane Root Window
     */
    public ControllerManager(RootPane rootPane) {
        log.debug("Initializing new ControllerManager");

        this.rootPane = rootPane;
        initializeControllers();
    }

    /**
     * Overloaded constructor to directly pass controllers.
     *
     * @param rootPane             - the root window of the application
     * @param timelineController   - the controller that controls the centerarea
     * @param detailViewController - the controller that controls the detailview
     * @param toolViewController   - the controller that controls the toolview
     */
    public ControllerManager(RootPane rootPane, TimelineController timelineController,
                             DetailViewController detailViewController,
                             ToolViewController toolViewController) {
        log.debug("Initializing new ControllerManager");

        this.rootPane = rootPane;
        this.timelineControl = timelineController;
        this.detailViewController = detailViewController;
        this.toolViewController = toolViewController;
    }
    
    public void newProject() {
        System.out.println("Here");
        new NewProjectModalView(rootPane, this::createProject);
    }
    
    private void createProject(NewProjectCreationEvent event) {
        ScriptingProject project = new ScriptingProject(event.getDescription(), event.getSecondsPerCount());
        project.setDirectorTimeline(new DirectorTimeline(event.getDirectorTimelineDescription(), null));
        project.setCameras(event.getCameras());
        project.setCameraTimelines(event.getTimelines());
        project.getDirectorTimeline().setProject(project);
        for (CameraTimeline timeline : project.getCameraTimelines()) {
            timeline.setProject(project);
        }
        
        scriptingProject = project;
        RootCenterArea area = new RootCenterArea(rootPane, event.getTimelines().size(), false);
        rootPane.reInitRootCenterArea(area);
        
    }
    
    public void newProject(MouseEvent event) {
        newProject();
    }

    /**
     * Initialize all necessary controllers.
     */
    private void initializeControllers() {
        timelineControl = new TimelineController(this);
        detailViewController = new DetailViewController(this);
        toolViewController = new ToolViewController(this);
    }
    
    public void loadProject(MouseEvent event) {
        timelineControl.load();
    }

    /**
     * Sets the active ShotBlock and notifies necessary controllers.
     *
     * @param block ShotBlock to set as active
     */
    public void setActiveShotBlock(ShotBlock block) {
        this.activeShotBlock = block;
        detailViewController.activeBlockChanged();
        toolViewController.activeBlockChanged();
    }
}
