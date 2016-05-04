package control;

import gui.root.RootPane;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;

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

        controllerManager = new ControllerManager(rootMock, timelineController,
                                                  detailViewController, toolViewController);
    }

    @Test
    public void getTimelineControllerTest() {
        assertNotNull(controllerManager.getTimelineControl());
    }
}
