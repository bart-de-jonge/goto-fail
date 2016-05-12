package control;

import data.DirectorShot;
import gui.centerarea.CameraShotBlock;
import gui.centerarea.DirectorShotBlock;
import gui.headerarea.ToolButton;
import gui.headerarea.ToolView;
import gui.root.RootHeaderArea;
import gui.root.RootPane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

/**
 * Created by alexandergeenen on 12/05/16.
 */
public class ToolViewControllerTest extends ApplicationTest {
    ToolViewController toolViewController;
    private ToolView toolView;
    private ControllerManager controllerManager;

    @Override
    public void start(Stage stage) throws Exception {
    }

    @Before
    public void initialize() {
        controllerManager = Mockito.mock(ControllerManager.class);
        RootPane rootPane = Mockito.mock(RootPane.class);
        RootHeaderArea rootHeaderArea = Mockito.mock(RootHeaderArea.class);
        toolView = Mockito.spy(new ToolView());

        when(controllerManager.getRootPane()).thenReturn(rootPane);
        when(rootPane.getRootHeaderArea()).thenReturn(rootHeaderArea);
        when(rootHeaderArea.getToolView()).thenReturn(toolView);

        toolViewController = new ToolViewController(controllerManager);
    }


    @Test
    public void noActiveBlockDeletionTest() {
        ToolButton deletionSpy = Mockito.spy(toolView.getBlockDeletionTool());
        when(toolView.getBlockDeletionTool()).thenReturn(deletionSpy);
        toolViewController.activeBlockChanged();
        Mockito.verify(deletionSpy).disableButton();
    }

    @Test
    public void noActiveBlockGenerationTest() {
        ToolButton genSpy = Mockito.spy(toolView.getShotGenerationTool());
        when(toolView.getShotGenerationTool()).thenReturn(genSpy);
        toolViewController.activeBlockChanged();
        Mockito.verify(genSpy).disableButton();
    }

    @Test
    public void activeBlockDeletionTest() {
        CameraShotBlock mockBlock = Mockito.mock(CameraShotBlock.class);
        when(controllerManager.getActiveShotBlock()).thenReturn(mockBlock);
        ToolButton deletionSpy = Mockito.spy(toolView.getBlockDeletionTool());
        when(toolView.getBlockDeletionTool()).thenReturn(deletionSpy);
        toolViewController.activeBlockChanged();
        Mockito.verify(deletionSpy).enableButton();
    }

    @Test
    public void noEnableGenActiveBlockTest() {
        CameraShotBlock mockBlock = Mockito.mock(CameraShotBlock.class);
        when(controllerManager.getActiveShotBlock()).thenReturn(mockBlock);
        ToolButton genSpy = Mockito.spy(toolView.getShotGenerationTool());
        when(toolView.getShotGenerationTool()).thenReturn(genSpy);
        toolViewController.activeBlockChanged();
        Mockito.verify(genSpy).disableButton();
    }

    @Test
    public void enableGenActiveBlockTest() {
        DirectorShotBlock mockBlock = Mockito.mock(DirectorShotBlock.class);
        DirectorShot directorShot = new DirectorShot("Dir shot", "description", 0, 1, 0, 0, new ArrayList<>());
        when(mockBlock.getShot()).thenReturn(directorShot);
        when(controllerManager.getActiveShotBlock()).thenReturn(mockBlock);
        ToolButton genSpy = Mockito.spy(toolView.getShotGenerationTool());
        when(toolView.getShotGenerationTool()).thenReturn(genSpy);
        toolViewController.activeBlockChanged();
        Mockito.verify(genSpy).enableButton();
    }
}
