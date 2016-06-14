package control;

import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.root.RootPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author goto fail;
 */
@PrepareForTest(ControllerManager.class)
public class ControllerManagerTest extends ApplicationTest {
    ControllerManager controllerManager;
    private TimelineController timelineController;
    private RootPane rootpane;
    private DetailViewController detailViewController;
    private ToolViewController toolViewController;
    private DirectorTimelineController directorTimelineController;
    private ProjectController projectController;

    @Override
    public void start(Stage stage) throws Exception {
        rootpane = Mockito.mock(RootPane.class);

        timelineController = Mockito.mock(TimelineController.class);
        detailViewController = Mockito.mock(DetailViewController.class);
        toolViewController = Mockito.mock(ToolViewController.class);
        directorTimelineController = Mockito.mock(DirectorTimelineController.class);
        projectController = Mockito.mock(ProjectController.class);

        controllerManager = Mockito.spy(new ControllerManager(rootpane, timelineController, detailViewController,
                toolViewController, directorTimelineController, projectController));
    }

    @Test
    public void getRootPaneTest() {
        assertNotNull(controllerManager.getRootPane());
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
    public void getPreferencesViewControllerTest() {
        assertNull(controllerManager.getPreferencesViewController());
    }

    @Test
    public void getProjectControllerTest() {
        assertNotNull(controllerManager.getProjectController());
    }

    @Test
    public void scriptingProjectTest() {
        ScriptingProject scriptingProject = Mockito.mock(ScriptingProject.class);
        controllerManager.setScriptingProject(scriptingProject);

        assertEquals(scriptingProject, controllerManager.getScriptingProject());
    }

    @Test
    public void getActiveShotBlockTest() {
        assertNull(controllerManager.getActiveShotBlock());
    }

//    @Test
//    public void updateWindowTitleTest() throws InterruptedException {
    // Call method under test
//        final CountDownLatch[] latch = {new CountDownLatch(1)};
//        Platform.runLater(() -> {
//            Stage primaryStage = Mockito.mock(Stage.class);
//            when(rootpane.getPrimaryStage()).thenReturn(primaryStage);
//            ScriptingProject scriptingProjectMock = Mockito.mock(ScriptingProject.class);
//            when(scriptingProjectMock.getName()).thenReturn("I'M A TITLE!");
//            controllerManager.setScriptingProject(scriptingProjectMock);
//
//            controllerManager.updateWindowTitle();
//            latch[0].countDown();
//            Mockito.verify(primaryStage).setTitle("I'M A TITLE");
//        });
//        latch[0].await();
//    }

    @Test
    public void setActiveShotBlockTest() {
        CameraShotBlock shotBlockMock = mock(CameraShotBlock.class);

        controllerManager.setActiveShotBlock(shotBlockMock);

        assertEquals(shotBlockMock, controllerManager.getActiveShotBlock());

        verify(detailViewController).activeBlockChanged();
        verify(toolViewController).activeBlockChanged();
    }

//    TODO: Fix the test conflicts w/ javafx issues
//    @Test
//    public void initOnCloseOperationTest() throws InterruptedException {
//        Stage primaryStage = Mockito.mock(Stage.class);
//        when(rootpane.getPrimaryStage()).thenReturn(primaryStage);

        // Call method under test
//        final CountDownLatch[] latch = {new CountDownLatch(1)};
//        Platform.runLater(() -> {
//
//            try {
//                WhiteboxImpl.invokeMethod(controllerManager, "initOnCloseOperation");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            latch[0].countDown();
//        });
//        latch[0].await();
//
//        Mockito.verify(primaryStage, times(1)).setOnCloseRequest(Matchers.anyObject());
//    }
}
