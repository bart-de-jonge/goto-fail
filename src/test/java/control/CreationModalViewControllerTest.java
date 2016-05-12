package control;

import data.CameraTimeline;
import data.ScriptingProject;
import gui.modal.CameraShotCreationModalView;
import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        CameraShotCreationModalView modalView = Mockito.mock(CameraShotCreationModalView.class);
        StyledButton creationButton = Mockito.mock(StyledButton.class);
        StyledButton cancelButton = Mockito.mock(StyledButton.class);

        when(creationModalViewController.generateCameraShotCreationModalView())
                .thenReturn(modalView);
        when(modalView.getCreationButton()).thenReturn(creationButton);
        when(modalView.getCancelButton()).thenReturn(cancelButton);

        verify(creationButton, times(1)).setOnMouseReleased(anyObject());
        verify(cancelButton, times(1)).setOnMouseReleased(anyObject());
    }

    @Override
    public void start(Stage stage) throws Exception {
    }
}