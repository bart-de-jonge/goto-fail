package control;

import data.*;
import gui.centerarea.CameraShotBlock;
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

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@PrepareForTest({DirectorTimelineController.class,ShotBlock.class,DirectorShotBlock.class})
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
        DirectorShot shot1 = Mockito.mock(DirectorShot.class);
        DirectorShot shot2 = Mockito.mock(DirectorShot.class);
        ArrayList<DirectorShot> collisionList = new ArrayList<>(Arrays.asList(shot1, shot2));
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        when(timeline.getOverlappingShots(anyObject())).thenReturn(collisionList);

        HashMap<DirectorShot, DirectorShotBlock> hashMap = new HashMap<>();
        hashMap.put(shot1, shotBlock);
        directorTimelineController.setDirectorShotBlockMap(hashMap);

        directorTimelineController.setOverlappingShotBlocks(new ArrayList<>(Arrays.asList(shotBlock)));
        when(shotBlock.getShot()).thenReturn(shot1);
        when(shot1.getCollidesWith()).thenReturn(new ArrayList<>());

        List<Integer> cameraList = new ArrayList<>();
        cameraList.add(1);
        DirectorShot shotToAdd = spy(new DirectorShot(new GeneralShotData("Violas", "Left of conductor", 1, 2),
                0.5, 0.5, cameraList));
        when(shotToAdd.getCollidesWith()).thenReturn(new ArrayList<Shot>(Arrays.asList(shot1)));

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            RootCenterArea rootCenterArea = new RootCenterArea(rootPane);
            when(rootPane.getRootCenterArea()).thenReturn(rootCenterArea);

            directorTimelineController.addDirectorShot(shotToAdd);
            latch[0].countDown();
        });
        latch[0].await();

        verify(controllerManager).setActiveShotBlock(Mockito.any(ShotBlock.class));
        verify(timeline).addShot(Mockito.any(DirectorShot.class));
    }

    @Test
    public void addDirectorShotTestFalse() throws InterruptedException {
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
            directorTimelineController.addDirectorShot(new DirectorShot(new GeneralShotData("Violas2", "Left of conductor2", 1, 2),
                    0.5, 0.5, cameraList));
            latch[0].countDown();
        });
        latch[0].await();

        verify(controllerManager).setActiveShotBlock(Mockito.any(ShotBlock.class));
        verify(timeline).addShot(Mockito.any(DirectorShot.class));
    }

    @Test
    public void shotChangedHandlerTest() {
        // Generate all necessary mocks
        ScriptingProject scriptMock = Mockito.mock(ScriptingProject.class);
        DirectorShotBlock shotBlockMock = Mockito.mock(DirectorShotBlock.class);
        DirectorShotBlock shotBlockMock2 = Mockito.mock(DirectorShotBlock.class);
        DirectorShot shotMock = Mockito.mock(DirectorShot.class);
        DirectorShot shotMock2 = Mockito.mock(DirectorShot.class);
        DirectorShotBlockUpdatedEvent event = Mockito.mock(DirectorShotBlockUpdatedEvent.class);
        DirectorTimeline timeline = Mockito.mock(DirectorTimeline.class);
        TimelineController timelineMock = Mockito.mock(TimelineController.class);
        CameraShotBlock cameraShotBlock = Mockito.mock(CameraShotBlock.class);
        ArrayList<CameraShotBlock> shotBlockList = new ArrayList<>(Arrays.asList(cameraShotBlock));

        // Mock all the methods
        when(shotMock.getBeginCount()).thenReturn(0.0);
        when(shotMock.getEndCount()).thenReturn(1.0);
        when(shotBlockMock.getBeginCount()).thenReturn(0.0);
        when(shotBlockMock.getPaddingBefore()).thenReturn(1.0);
        when(event.getDirectorShotBlock()).thenReturn(shotBlockMock);
        when(controllerManager.getScriptingProject()).thenReturn(scriptMock);
        when(shotBlockMock.getShot()).thenReturn(shotMock);
        when(scriptMock.getDirectorTimeline()).thenReturn(timeline);
        when(controllerManager.getTimelineControl()).thenReturn(timelineMock);
        when(timelineMock.getCameraShotBlocks()).thenReturn(shotBlockList);
        when(shotMock.getCollidesWith()).thenReturn(new ArrayList<>(Arrays.asList(shotMock2)));
        when(shotBlockMock2.getShot()).thenReturn(shotMock2);

        // Hashmap setup
        HashMap<DirectorShot, DirectorShotBlock> hashMap = new HashMap<>();
        hashMap.put(shotMock, shotBlockMock);
        hashMap.put(shotMock2, shotBlockMock2);
        directorTimelineController.setDirectorShotBlockMap(hashMap);

        // Call method under testing
        directorTimelineController.shotChangedHandler(event);

        // Verify all the things
        verify(controllerManager).setActiveShotBlock(Mockito.any(ShotBlock.class));
        verify(scriptMock).changed();
    }

    @Test
    public void removeShotTest() throws Exception {
        ScriptingProject scriptSpy = spy(new ScriptingProject("Project", "", 2));
        when(controllerManager.getScriptingProject()).thenReturn(scriptSpy);

        DirectorShotBlock shotBlockMock = Mockito.mock(DirectorShotBlock.class);
        when(controllerManager.getActiveShotBlock()).thenReturn(shotBlockMock);

        DirectorShot shotMock = Mockito.mock(DirectorShot.class);
        when(shotBlockMock.getShot()).thenReturn(shotMock);

        CameraShot cameraShotMock = Mockito.mock(CameraShot.class);
        Set<CameraShot> cameraShots = new HashSet<>();
        cameraShots.add(cameraShotMock);
        when(shotMock.getCameraShots()).thenReturn(cameraShots);

        TimelineController timelineControllerMock = Mockito.mock(TimelineController.class);
        when(controllerManager.getTimelineControl()).thenReturn(timelineControllerMock);

        HashMap<DirectorShot, DirectorShotBlock> hashMap = new HashMap<>();
        hashMap.put(shotMock, shotBlockMock);
        directorTimelineController.setDirectorShotBlockMap(hashMap);
        directorTimelineController.removeShot(shotBlockMock);

        verify(shotMock).getCameraShots();
        verify(timelineControllerMock).removeCameraShot(cameraShotMock);
    }

    @Test
    public void getControllerManagerTest() {
        assertEquals(controllerManager, directorTimelineController.getControllerManager());
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
            directorTimelineController.addDirectorShot(new DirectorShot(new GeneralShotData("Violas", "Left of conductor", 1, 2),
                                                       0.5, 0.5, cameraList));

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
