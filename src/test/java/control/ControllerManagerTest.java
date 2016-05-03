package control;

import gui.DetailView;
import gui.RootHeaderArea;
import gui.RootPane;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
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

        controllerManager = new ControllerManager(rootMock, timelineController, detailViewController);
    }

    @Test
    public void getTimelineControllerTest() {
        assertNotNull(controllerManager.getTimelineControl());
    }
}
