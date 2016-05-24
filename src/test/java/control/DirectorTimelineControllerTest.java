package control;

import data.CameraShot;
import data.DirectorShot;
import data.DirectorTimeline;
import data.ScriptingProject;
import gui.centerarea.DirectorShotBlock;
import gui.centerarea.ShotBlock;
import gui.events.DirectorShotBlockUpdatedEvent;
import gui.root.RootCenterArea;
import gui.root.RootPane;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@PrepareForTest(DirectorTimelineController.class)
public class DirectorTimelineControllerTest extends ApplicationTest {
    ControllerManager controllerManager;
    DirectorTimelineController directorTimelineController;
    private RootPane rootPane;

    @Before
    public void initialize() {
        controllerManager = Mockito.mock(ControllerManager.class);
        rootPane = Mockito.mock(RootPane.class);
        when(controllerManager.getRootPane()).thenReturn(rootPane);

        directorTimelineController = new DirectorTimelineController(controllerManager);
    }

    @Test
    public void addDirectorShotTest() throws InterruptedException {
        ScriptingProject scriptMock = Mockito.mock(ScriptingProject.class);
        DirectorTimeline timeline = Mockito.mock(DirectorTimeline.class);
        when(scriptMock.getDirectorTimeline()).thenReturn(timeline);
        when(controllerManager.getScriptingProject()).thenReturn(scriptMock);
        TimelineController timelineControllerMock = Mockito.mock(TimelineController.class);
        when(controllerManager.getTimelineControl()).thenReturn(timelineControllerMock);

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            RootCenterArea rootCenterArea = new RootCenterArea(rootPane);
            when(rootPane.getRootCenterArea()).thenReturn(rootCenterArea);

            List<Integer> cameraList = new ArrayList<>();
            cameraList.add(1);
            directorTimelineController.addDirectorShot("Violas", "Left of conductor", 1, 2,
                                                       0.5, 0.5, cameraList);
            latch[0].countDown();
        });
        latch[0].await();

        verify(controllerManager).setActiveShotBlock(Mockito.any(ShotBlock.class));
        verify(timeline).addShot(Mockito.any(DirectorShot.class));
    }

    @Test
    public void shotChangedHandlerTest() {
        ScriptingProject scriptMock = Mockito.mock(ScriptingProject.class);
        when(controllerManager.getScriptingProject()).thenReturn(scriptMock);
        DirectorShotBlock shotBlockMock = Mockito.mock(DirectorShotBlock.class);
        DirectorShot shotMock = Mockito.mock(DirectorShot.class);
        when(shotMock.getBeginCount()).thenReturn(0.0);
        when(shotMock.getEndCount()).thenReturn(1.0);
        when(shotBlockMock.getShot()).thenReturn(shotMock);

        DirectorShotBlockUpdatedEvent event = Mockito.mock(DirectorShotBlockUpdatedEvent.class);
        when(event.getDirectorShotBlock()).thenReturn(shotBlockMock);

        DirectorTimeline timeline = Mockito.mock(DirectorTimeline.class);
        when(scriptMock.getDirectorTimeline()).thenReturn(timeline);

        directorTimelineController.shotChangedHandler(event);

        verify(controllerManager).setActiveShotBlock(Mockito.any(ShotBlock.class));
        verify(scriptMock).changed();
    }

    @Test
    public void removeShotTest() {
        ScriptingProject scriptSpy = Mockito.spy(new ScriptingProject("Project", "", 2));
        when(controllerManager.getScriptingProject()).thenReturn(scriptSpy);

        DirectorShotBlock shotBlockMock = Mockito.mock(DirectorShotBlock.class);
        when(controllerManager.getActiveShotBlock()).thenReturn(shotBlockMock);

        DirectorShot shotMock = Mockito.mock(DirectorShot.class);
        when(shotBlockMock.getShot()).thenReturn(shotMock);

        TimelineController timelineControllerMock = Mockito.mock(TimelineController.class);
        when(controllerManager.getTimelineControl()).thenReturn(timelineControllerMock);

        directorTimelineController.removeShot(shotBlockMock);

        verify(shotBlockMock).removeFromView();
        verify(controllerManager).setActiveShotBlock(null);
    }

    @Test
    public void getControllerManagerTest() {
        assertEquals(controllerManager, directorTimelineController.getControllerManager());
    }

    @Test
    public void getShotBlocksTest() {
        assertEquals(Collections.emptyList(), directorTimelineController.getShotBlocks());
    }

    @Test
    public void generateAllShots() throws InterruptedException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            RootCenterArea rootCenterArea = new RootCenterArea(rootPane);
            when(rootPane.getRootCenterArea()).thenReturn(rootCenterArea);

            ScriptingProject scriptMock = Mockito.mock(ScriptingProject.class);
            DirectorTimeline timeline = Mockito.mock(DirectorTimeline.class);
            when(scriptMock.getDirectorTimeline()).thenReturn(timeline);
            when(controllerManager.getScriptingProject()).thenReturn(scriptMock);
            TimelineController timelineControllerMock = Mockito.mock(TimelineController.class);
            when(controllerManager.getTimelineControl()).thenReturn(timelineControllerMock);

            List<Integer> cameraList = new ArrayList<>();
            cameraList.add(1);
            directorTimelineController.addDirectorShot("Violas", "Left of conductor", 1, 2,
                                                       0.5, 0.5, cameraList);

            directorTimelineController.generateAllShots();

            verify(timelineControllerMock).addCameraShot(Mockito.anyInt(), Mockito.any(CameraShot.class));
            latch[0].countDown();
        });
        latch[0].await();
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
