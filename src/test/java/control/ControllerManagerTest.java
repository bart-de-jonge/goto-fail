package control;

import gui.RootPane;
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
        RootPane rootMock = Mockito.spy(new RootPane());
        controllerManager = new ControllerManager(rootMock);
    }

    @Test
    public void getTimelineControllerTest() {
        assertNotNull(controllerManager.getTimelineControl());
    }
}
