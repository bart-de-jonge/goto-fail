package control;

import data.ScriptingProject;
import gui.root.RootPane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author alex
 */
public class ControllerManagerTest {
    ControllerManager controllerManager;

    @Before
    public void initialize() {
        // GUI element necessary for instantiation
        RootPane rootMock = Mockito.mock(RootPane.class);
        TimelineController timelineController = Mockito.mock(TimelineController.class);
        DetailViewController detailViewController = Mockito.mock(DetailViewController.class);
        ToolViewController toolViewController = Mockito.mock(ToolViewController.class);
        DirectorTimelineController directorTimelineController = Mockito.mock(DirectorTimelineController.class);
        ProjectController projectController = Mockito.mock(ProjectController.class);

        controllerManager = new ControllerManager(rootMock, timelineController,
                                                  detailViewController, toolViewController,
                                                  directorTimelineController, projectController);
    }

    @Test
    public void getTimelineControllerTest() {
        assertNotNull(controllerManager.getTimelineControl());
    }

    @Test
    public void getDirectorTimelineControllerTest() {
        assertNotNull(controllerManager.getDirectorTimelineControl());
    }

    @Test
    public void getToolViewControllerTest() {
        assertNotNull(controllerManager.getToolViewController());
    }

    @Test
    public void getDetailViewControllerTest() {
        assertNotNull(controllerManager.getDetailViewController());
    }

    @Test
    public void getProjectControllerTest() {
        assertNotNull(controllerManager.getProjectController());
    }
}
