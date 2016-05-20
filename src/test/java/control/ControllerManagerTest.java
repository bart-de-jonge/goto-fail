package control;

import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.root.RootPane;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author alex
 */
@PrepareForTest(ControllerManager.class)
public class ControllerManagerTest extends ApplicationTest {
    ControllerManager controllerManager;
    RootPane rootPaneMock;
    DetailViewController detailViewControllerMock;
    ToolViewController toolViewControllerMock;

    @Before
    public void initialize() {
        // GUI element necessary for instantiation
        rootPaneMock = Mockito.mock(RootPane.class);
        TimelineController timelineController = Mockito.mock(TimelineController.class);
        detailViewControllerMock = Mockito.mock(DetailViewController.class);
        toolViewControllerMock = Mockito.mock(ToolViewController.class);
        DirectorTimelineController directorTimelineController = Mockito.mock(DirectorTimelineController.class);
        ProjectController projectController = Mockito.mock(ProjectController.class);

        controllerManager = new ControllerManager(rootPaneMock, timelineController,
                                                  detailViewControllerMock, toolViewControllerMock,
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

    @Test
    public void getActiveShotBlockTest() {
        assertNull(controllerManager.getActiveShotBlock());
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

//    TODO: Fix the test conflicts w/ javafx issues
//    @Test
//    public void updateWindowTitleTest() throws InterruptedException {
//
//        // Call method under test
//        final CountDownLatch[] latch = {new CountDownLatch(1)};
//        Platform.runLater(() -> {
//            Stage primaryStage = Mockito.mock(Stage.class);
//            when(rootPaneMock.getPrimaryStage()).thenReturn(primaryStage);
//            ScriptingProject scriptingProjectMock = Mockito.mock(ScriptingProject.class);
//            when(scriptingProjectMock.getName()).thenReturn("I'M A TITLE!");
//            controllerManager.setScriptingProject(scriptingProjectMock);
//
//            controllerManager.updateWindowTitle();
//            latch[0].countDown();
//            Mockito.verify(primaryStage, times(1)).setTitle("I'M A TITLE");
//        });
//        latch[0].await();
//
//    }


    @Test
    public void setActiveShotBlockTest() {
        CameraShotBlock shotBlockMock = mock(CameraShotBlock.class);

        controllerManager.setActiveShotBlock(shotBlockMock);

        assertEquals(shotBlockMock, controllerManager.getActiveShotBlock());

        verify(detailViewControllerMock).activeBlockChanged();
        verify(toolViewControllerMock).activeBlockChanged();
    }

//    TODO: Fix the test conflicts w/ javafx issues
//    @Test
//    public void initOnCloseOperationTest() throws InterruptedException {
//        Stage primaryStage = Mockito.mock(Stage.class);
//        when(rootPaneMock.getPrimaryStage()).thenReturn(primaryStage);
//
//        // Call method under test
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
