package control;

import data.CameraShot;
import data.ScriptingProject;
import gui.CameraShotBlock;
import gui.CameraShotBlockUpdatedEvent;
import gui.RootPane;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author alex
 */
public class TimelineControllerTest {
    TimelineController timelineController;
    private ScriptingProject project;
    private CameraShot shot;
    private CameraShotBlock shotBlock;

    @Before
    public void initialize() {
        RootPane rootPane = Mockito.mock(RootPane.class);
        timelineController = new TimelineController(rootPane);
        project = timelineController.getScriptingProject();

        shot = new CameraShot("Shot test", "", 1, 2);
        project.getCameraTimelines().get(0).addShot(shot);

        shotBlock = Mockito.mock(CameraShotBlock.class);
        when(shotBlock.getShotId()).thenReturn(shot.getInstance());
    }

    @Test
    public void shotChangedLengthTest() {
        when(shotBlock.getBeginCount()).thenReturn(1.0);
        when(shotBlock.getEndCount()).thenReturn(5.0);
        when(shotBlock.getTimetableNumber()).thenReturn(0);

        timelineController.shotChangedHandler(
                new CameraShotBlockUpdatedEvent(shotBlock, 0)
        );

        assertEquals(shot.getEndCount(), 5.0, 0);
    }

    @Test
    public void shotChangedStartPositionTest() {
        when(shotBlock.getBeginCount()).thenReturn(0.0);
        when(shotBlock.getEndCount()).thenReturn(2.0);
        when(shotBlock.getTimetableNumber()).thenReturn(0);

        timelineController.shotChangedHandler(
                new CameraShotBlockUpdatedEvent(shotBlock, 0)
        );

        assertEquals(shot.getStartCount(), 0.0, 0);
    }

    @Test
    public void shotChangedTimelinesTest() {
        when(shotBlock.getBeginCount()).thenReturn(1.0);
        when(shotBlock.getEndCount()).thenReturn(2.0);
        when(shotBlock.getTimetableNumber()).thenReturn(1);

        timelineController.shotChangedHandler(
                new CameraShotBlockUpdatedEvent(shotBlock, 0)
        );

        // Verify movement between timelines
        assertEquals(project.getCameraTimelines().get(0).getShots().size(), 0);
        assertEquals(project.getCameraTimelines().get(1).getShots().size(), 1);
    }
}
