package control;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

import data.*;
import gui.centerarea.TimelinesGridPane;
import gui.modal.ShotDecouplingModalView;
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

import gui.centerarea.CameraShotBlock;
import gui.events.CameraShotBlockUpdatedEvent;
import gui.root.RootPane;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private DirectorTimelineController directorTimelineController;

    @Before
    public void initialize() {
        RootPane rootPane = Mockito.mock(RootPane.class);
        TimelineController timelineControllerMock = Mockito.mock(TimelineController.class);
        DetailViewController detailViewController = Mockito.mock(DetailViewController.class);
        ToolViewController toolViewController = Mockito.mock(ToolViewController.class);
        directorTimelineController = Mockito.mock(DirectorTimelineController.class);
        ProjectController projectController = Mockito.mock(ProjectController.class);
        manager = spy(new ControllerManager(rootPane, timelineControllerMock, detailViewController, toolViewController,
                                            directorTimelineController, projectController));

        project = spy(new ScriptingProject());
        project.addCameraTimeline(new CameraTimeline(new Camera("a", "b", new CameraType()), null));
        project.addCameraTimeline(new CameraTimeline(new Camera("a", "b", new CameraType()), null));

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
    public void shotChangedWithBlocks() {
        timelineController.setCameraShotBlocks(new ArrayList<>(Arrays.asList(shotBlock)));
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
        DirectorShot directorShotSpy = Mockito.spy(new DirectorShot(new GeneralShotData("dir shot", "description",
                1, 2), 0, 0, new ArrayList<>()));
        CameraShot shotSpy = Mockito.spy(shot);
        directorShotSpy.addCameraTimelineIndex(0);
        directorShotSpy.addCameraShot(shotSpy);
        shotSpy.setDirectorShot(directorShotSpy);

        timelineController.decoupleShot(0, shotSpy);
        verify(directorShotSpy).removeCameraShot(shotSpy, 0);
    }

    private void initRootPaneForCameraShotAdding() {
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
    }

    @Test
    public void addCameraShot1() {
        Mockito.doNothing().when(timelineController).addCameraShot(anyInt(), anyObject());
        timelineController.addCameraShot(1, new CameraShot("name", "description", 2.0, 3.0));
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

        initRootPaneForCameraShotAdding();

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

    @Test
    public void removeCameraShotBlockTest() throws InterruptedException {
        CameraShot shot = new CameraShot("name", "description", 2.0, 3.0);
        initRootPaneForCameraShotAdding();

        // Call method under test
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            timelineController.addCameraShot(0, shot);
            CameraShotBlock shotBlock = timelineController.getCameraShotBlocks().get(timelineController.getCameraShotBlocks().size() - 1);
            timelineController.removeCameraShot(shotBlock);
            latch[0].countDown();
        });
        latch[0].await();

        // Do verifications
        assertNull(manager.getActiveShotBlock());
        Mockito.verify(project, times(2)).changed();
        assertFalse(manager.getScriptingProject().getCameraTimelines().get(0).getShots().contains(shot));
    }

    @Test
    public void removeCameraShotTest() throws InterruptedException {
        CameraShot shot = new CameraShot("name", "description", 2.0, 3.0);
        initRootPaneForCameraShotAdding();

        // Call method under test
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            timelineController.addCameraShot(0, shot);
            timelineController.removeCameraShot(shot);
            latch[0].countDown();
        });
        latch[0].await();

        // Do verifications
        assertNull(manager.getActiveShotBlock());
        Mockito.verify(project, times(2)).changed();
        assertFalse(manager.getScriptingProject().getCameraTimelines().get(0).getShots().contains(shot));
    }

    @Test
    public void modifyCameraShot() {
        CameraShotBlockUpdatedEvent event = Mockito.mock(CameraShotBlockUpdatedEvent.class);
        when(event.getOldTimelineNumber()).thenReturn(0);
        CameraShot shot = spy(new CameraShot("name", "description", 2.0, 3.0));
        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        when(shotBlock.getShot()).thenReturn(shot);
        when(shotBlock.getBeginCount()).thenReturn(2.0);
        when(shotBlock.getEndCount()).thenReturn(3.0);
        when(shotBlock.getTimetableNumber()).thenReturn(1);

        timelineController.modifyCameraShot(event, shotBlock);

        Mockito.verify(shot, times(1)).setBeginCount(2.0);
        Mockito.verify(shot, times(1)).setEndCount(3.0);
        assertTrue(project.getCameraTimelines().get(1).getShots().contains(shot));
        assertFalse(project.getCameraTimelines().get(0).getShots().contains(shot));
        assertEquals(shotBlock, manager.getActiveShotBlock());
    }

    @Test
    public void checkCollisionsAdding() throws InterruptedException {
        CameraShot shot = new CameraShot("name", "description", 2.0, 4.0);
        CameraShot shot2 = new CameraShot("name2", "description2", 2.0, 5.0);

        ArrayList<CameraTimeline> timelines = Mockito.mock(ArrayList.class);
        CameraTimeline timeline = Mockito.mock(CameraTimeline.class);
        when(timelines.get(0)).thenReturn(timeline);
        when(project.getCameraTimelines()).thenReturn(timelines);
        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        Mockito.doNothing().when(timelineController).removeCollisionFromCameraShotBlock(anyObject());
        when(timeline.getOverlappingShots(anyObject())).thenReturn(new ArrayList<>(Arrays.asList(shot, shot2)));
        timelineController.getCameraShotBlocks().add(shotBlock);
        when(shotBlock.getShotId()).thenReturn(shot.getInstance());

        // Call method under test
        timelineController.checkCollisions(0, 1, shotBlock);

        // Do verifications
        assertTrue(timelineController.getOverlappingCameraShotBlocks().contains(shotBlock));
        Mockito.verify(shotBlock, times(1)).setColliding(true);
    }

    @Test
    public void checkCollisionsRemoving() throws InterruptedException {
        CameraShot shot = new CameraShot("name", "description", 2.0, 4.0);

        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        Mockito.doNothing().when(timelineController).removeCollisionFromCameraShotBlock(anyObject());
        timelineController.getOverlappingCameraShotBlocks().add(shotBlock);
        timelineController.getCameraShotBlocks().add(shotBlock);
        when(shotBlock.getShot()).thenReturn(shot);

        // Call method under test
        timelineController.checkCollisions(0, 1, shotBlock);

        // Do verifications
        assertTrue(timelineController.getOverlappingCameraShotBlocks().isEmpty());
        Mockito.verify(shotBlock, times(2)).setColliding(false);
    }

    @Test
    public void checkCollisionsRemovingCollidesWith() throws InterruptedException {
        CameraShot shot = Mockito.mock(CameraShot.class);
        CameraShot shot2 = Mockito.mock(CameraShot.class);

        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        CameraShotBlock shotBlock2 = Mockito.mock(CameraShotBlock.class);
        when(shotBlock.getShot()).thenReturn(shot);
        ArrayList shotList = new ArrayList<>(Collections.singletonList(shot2));
        when(shot.getCollidesWith()).thenReturn(shotList);
        doReturn(shotBlock2).when(timelineController).getShotBlockForShot(shot2);
        when(shotBlock2.getShot()).thenReturn(shot2);
        // Call method under test
        timelineController.resetColliding(shotBlock);

        // Do verifications
        assertTrue(shotList.isEmpty());
        Mockito.verify(shotBlock, times(1)).setColliding(false);
    }

    @Test
    public void removeCollisionFromCameraShotBlock() {
        CameraShot shot = new CameraShot("name", "description", 2.0, 4.0);
        CameraShot shot2 = new CameraShot("name2", "description2", 2.0, 5.0);

        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        when(shotBlock.getShot()).thenReturn(shot);
        shot.getCollidesWith().add(shot2);
        shot2.getCollidesWith().add(shot);

        // Call method under test
        timelineController.removeCollisionFromCameraShotBlock(shotBlock);

        // Do verifications
        assertTrue(shot.getCollidesWith().isEmpty());
        assertTrue(shot2.getCollidesWith().isEmpty());
    }

    @Test
    public void decoupleAndModify() throws InterruptedException {
        CameraShot shot = spy(new CameraShot("name", "description", 2.0, 4.0));
        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        when(shotBlock.getShot()).thenReturn(shot);
        DirectorShot directorShot = Mockito.mock(DirectorShot.class);
        shot.setDirectorShot(directorShot);
        CameraShotBlockUpdatedEvent event = Mockito.mock(CameraShotBlockUpdatedEvent.class);
        when(event.getCameraShotBlock()).thenReturn(shotBlock);

        // Call method under test
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            timelineController.decoupleAndModify(event, shotBlock);
            latch[0].countDown();
        });
        latch[0].await();

        // Do verifications
        Mockito.verify(shotBlock, times(3)).getShot();
    }

    @Test
    public void decoupleAndModifyFalse() throws InterruptedException {
        CameraShot shot = Mockito.mock(CameraShot.class);
        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        when(shotBlock.getShot()).thenReturn(shot);
        DirectorShot directorShot = Mockito.mock(DirectorShot.class);
        when(shot.getDirectorShot()).thenReturn(directorShot);
        CameraShotBlockUpdatedEvent event = Mockito.mock(CameraShotBlockUpdatedEvent.class);
        when(event.getCameraShotBlock()).thenReturn(shotBlock);
        when(shotBlock.getBeginCount()).thenReturn(5.0);
        when(shot.getBeginCount()).thenReturn(5.0);


        // Call method under test
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            timelineController.decoupleAndModify(event, shotBlock);
            latch[0].countDown();
        });
        latch[0].await();

        // Do verifications
        Mockito.verify(shotBlock, times(10)).getShot();
    }

    @Test
    public void setNumTimelines() {
        timelineController.setNumTimelines(5);
        assertEquals(5, timelineController.getNumTimelines());
    }

    @Test
    public void getShotBlockForShotNull() {
        CameraShot shot = Mockito.mock(CameraShot.class);
        assertNull(timelineController.getShotBlockForShot(shot));
    }

    @Test
    public void getShotBlockForShot() {
        CameraShot shot = Mockito.mock(CameraShot.class);
        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        when(shot.getInstance()).thenReturn(5);
        when(shotBlock.getShot()).thenReturn(shot);
        timelineController.setCameraShotBlocks(Arrays.asList(shotBlock));
        assertEquals(shotBlock, timelineController.getShotBlockForShot(shot));
    }

    @Test
    public void recomputeAllCollisions() {
        Mockito.doNothing().when(timelineController).checkCollisions(anyInt(), anyObject());
        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        timelineController.setCameraShotBlocks(Arrays.asList(shotBlock));
        when(shotBlock.getTimetableNumber()).thenReturn(0);

        timelineController.recomputeAllCollisions();
        Mockito.verify(timelineController, times(1)).checkCollisions(0, shotBlock);
    }

    @Test
    public void cancelButtonListener() {
        ShotDecouplingModalView view = Mockito.mock(ShotDecouplingModalView.class);
        timelineController.cancelButtonListener(view, shotBlock);
        Mockito.verify(shotBlock, times(1)).restorePreviousPosition();
        Mockito.verify(view, times(1)).hideModal();
    }

    @Test
    public void confirmButtonListener() {
        ShotDecouplingModalView view = Mockito.mock(ShotDecouplingModalView.class);
        CameraShotBlockUpdatedEvent event = Mockito.mock(CameraShotBlockUpdatedEvent.class);
        when(event.getOldTimelineNumber()).thenReturn(5);
        Mockito.doNothing().when(timelineController).decoupleShot(anyInt(), anyObject());
        Mockito.doNothing().when(timelineController).modifyCameraShot(anyObject(), anyObject());
        CameraShot shot = Mockito.mock(CameraShot.class);
        when(shotBlock.getShot()).thenReturn(shot);

        timelineController.confirmButtonListener(view, shotBlock, event);
        Mockito.verify(timelineController, times(1)).decoupleShot(5, shot);
        Mockito.verify(timelineController, times(1)).modifyCameraShot(event, shotBlock);
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
