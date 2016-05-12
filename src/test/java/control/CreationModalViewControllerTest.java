package control;

import data.CameraTimeline;
import data.ScriptingProject;
import gui.root.RootPane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by Bart on 12/05/2016.
 */
public class CreationModalViewControllerTest extends ApplicationTest {

    private ControllerManager manager;
    private CreationModalViewController creationModalViewController;

    @Before
    public void initialize() {
        manager = Mockito.mock(ControllerManager.class);
        RootPane rootPane = Mockito.mock(RootPane.class);
        ScriptingProject scriptingProject = Mockito.mock(ScriptingProject.class);

        when(manager.getRootPane()).thenReturn(rootPane);
        when(manager.getScriptingProject()).thenReturn(scriptingProject);
        when(scriptingProject.getCameraTimelines()).thenReturn(new ArrayList<>());

        creationModalViewController = spy(new CreationModalViewController(manager));
    }

    @Test
    public void constructor() {
        assertNotNull(creationModalViewController);
    }

    @Test
    public void showCameraCreationWindow() {
        creationModalViewController.showCameraCreationWindow();
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.launch(RootPane.class);
    }
}