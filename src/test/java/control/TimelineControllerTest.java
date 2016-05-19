package control;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

import data.DirectorShot;
import gui.centerarea.TimelinesGridPane;
import gui.root.RootCenterArea;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.events.CameraShotBlockUpdatedEvent;
import gui.root.RootPane;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.CountDownLatch;

/**
 * @author alex
 */
public class TimelineControllerTest extends ApplicationTest {
    private TimelineController timelineController;
    private ControllerManager manager;
    private ScriptingProject project;
    private CameraShot shot;
    private CameraShotBlock shotBlock;

    @Before
    public void initialize() {
        RootPane rootPane = Mockito.mock(RootPane.class);
        TimelineController timelineControllerMock = Mockito.mock(TimelineController.class);
        DetailViewController detailViewController = Mockito.mock(DetailViewController.class);
        ToolViewController toolViewController = Mockito.mock(ToolViewController.class);
        manager = spy(new ControllerManager(rootPane, timelineControllerMock, detailViewController, toolViewController));

        project = spy(new ScriptingProject());
        project.addCameraTimeline(new CameraTimeline(new Camera("a", "b", new CameraType()), "kek", null));
        project.addCameraTimeline(new CameraTimeline(new Camera("a", "b", new CameraType()), "kek", null));

        timelineController = spy(new TimelineController(manager));
        timelineController.getControllerManager().setScriptingProject(project);

        shot = new CameraShot("Shot test", "", 1, 2);
        project.getCameraTimelines().get(0).addShot(shot);

        shotBlock = Mockito.mock(CameraShotBlock.class);
        when(shotBlock.getShotId()).thenReturn(shot.getInstance());
        when(shotBlock.getShot()).thenReturn(new CameraShot("test", "test", 1, 2));
    }

    @Test
    public void shotChangedLengthTest() {
        timelineController.shotChangedHandler(
                new CameraShotBlockUpdatedEvent(shotBlock, 0)
        );

        assertEquals(2.0, shot.getEndCount(), 0);
    }

    @Test
    public void shotChangedStartPositionTest() {
        timelineController.shotChangedHandler(
                new CameraShotBlockUpdatedEvent(shotBlock, 0)
        );

        assertEquals(1.0, shot.getBeginCount(), 0);
    }

    @Test
    public void shotChangedTimelinesTest() {
        timelineController.shotChangedHandler(
                new CameraShotBlockUpdatedEvent(shotBlock, 0)
        );

        // Verify movement between centerarea
        assertEquals(1, project.getCameraTimelines().get(0).getShots().size());
        assertEquals(0, project.getCameraTimelines().get(1).getShots().size());
    }

    @Test
    public void decoupleTest() {
        DirectorShot directorShotSpy = spy(new DirectorShot("dir shot", "description", 1, 2, 0, 0));
        CameraShot shotSpy = spy(shot);
        directorShotSpy.addCameraTimelineIndex(0);
        directorShotSpy.addCameraShot(shotSpy);
        shotSpy.setDirectorShot(directorShotSpy);

        timelineController.decoupleShot(0, shotSpy);
        verify(directorShotSpy).removeCameraShot(shotSpy, 0);
        verify(shotSpy).setDirectorShot(null);
    }

    @Test
    public void addCameraShot1() {
        Mockito.doNothing().when(timelineController).addCameraShot(anyInt(), anyObject());
        timelineController.addCameraShot(1, "name", "description", 2.0, 3.0);
        Mockito.verify(timelineController, times(1)).addCameraShot(anyInt(), anyObject());
    }

    @Test
    public void addCameraShot2() throws InterruptedException {
        CameraTimeline timeline = manager.getScriptingProject().getCameraTimelines().get(0);
        int curTimelineLength = timeline.getShots().size();
        CameraShot shot = new CameraShot("name", "description", 2.0, 3.0);

        Mockito.doNothing().when(manager).setActiveShotBlock(anyObject());
        int curLength = timelineController.getCameraShotBlocks().size();
        Mockito.doNothing().when(project).changed();
        Mockito.doNothing().when(timelineController).checkCollisions(anyInt(), anyObject());

        // Set rootpane of timelinecontroller to a mock
        RootPane rootPane = Mockito.mock(RootPane.class);
        timelineController.setRootPane(rootPane);

        // Add needed mocks to the rootpane
        RootCenterArea rootCenterArea = Mockito.mock(RootCenterArea.class);
        ObservableList<Node> nodeList = Mockito.mock(ObservableList.class);
        AnchorPane mainTimelineAchorPane = Mockito.mock(AnchorPane.class);
        when(rootCenterArea.getMainTimeLineAnchorPane()).thenReturn(mainTimelineAchorPane);
        when(mainTimelineAchorPane.getChildren()).thenReturn(nodeList);
        when(rootPane.getRootCenterArea()).thenReturn(rootCenterArea);
        TimelinesGridPane timelinesGridPane = Mockito.mock(TimelinesGridPane.class);
        when(timelinesGridPane.getChildren()).thenReturn(nodeList);
        when(rootCenterArea.getMainTimeLineGridPane()).thenReturn(timelinesGridPane);

        // Call method under test
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            timelineController.addCameraShot(0, shot);
            latch[0].countDown();
        });
        latch[0].await();

        // Do verifications
        Mockito.verify(timelineController, times(1)).checkCollisions(anyInt(), anyObject());
        Mockito.verify(manager, times(1)).setActiveShotBlock(anyObject());
        assertEquals(curLength + 1, timelineController.getCameraShotBlocks().size());
        Mockito.verify(project, times(1)).changed();
        assertEquals(curTimelineLength + 1, timeline.getShots().size());
        assertEquals(shot, timeline.getShots().get(timeline.getShots().size() - 1));
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
