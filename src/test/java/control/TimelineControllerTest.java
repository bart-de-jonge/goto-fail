package control;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import data.DirectorShot;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.events.CameraShotBlockUpdatedEvent;
import gui.root.RootPane;

/**
 * @author alex
 */
public class TimelineControllerTest {
    private TimelineController timelineController;
    private ScriptingProject project;
    private CameraShot shot;
    private CameraShotBlock shotBlock;

    @Before
    public void initialize() {
        RootPane rootPane = Mockito.mock(RootPane.class);
        TimelineController timelineControllerMock = Mockito.mock(TimelineController.class);
        DetailViewController detailViewController = Mockito.mock(DetailViewController.class);
        ToolViewController toolViewController = Mockito.mock(ToolViewController.class);
        ControllerManager controllerManager = new ControllerManager(rootPane, timelineControllerMock,
                                                                    detailViewController, toolViewController);

        ScriptingProject defaultProject = new ScriptingProject();
        defaultProject.addCameraTimeline(new CameraTimeline(new Camera("a", "b", new CameraType()), "kek", null));
        defaultProject.addCameraTimeline(new CameraTimeline(new Camera("a", "b", new CameraType()), "kek", null));

        timelineController = new TimelineController(controllerManager);
        timelineController.getControllerManager().setScriptingProject(defaultProject);

        project = timelineController.getControllerManager().getScriptingProject();

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
        DirectorShot directorShotSpy = Mockito.spy(new DirectorShot("dir shot", "description", 1, 2, 0, 0));
        CameraShot shotSpy = Mockito.spy(shot);
        directorShotSpy.addCameraTimelineIndex(0);
        directorShotSpy.addCameraShot(shotSpy);
        shotSpy.setDirectorShot(directorShotSpy);

        timelineController.decoupleShot(0, shotSpy);
        verify(directorShotSpy).removeCameraShot(shotSpy, 0);
        verify(shotSpy).setDirectorShot(null);
    }
}
