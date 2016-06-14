package control;

import data.CameraShot;
import data.DirectorShot;
import data.GeneralShotData;
import gui.centerarea.CameraShotBlock;
import gui.centerarea.DirectorShotBlock;
import gui.headerarea.ToolButton;
import gui.headerarea.ToolView;
import gui.root.RootHeaderArea;
import gui.root.RootPane;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * Created by alexandergeenen on 12/05/16.
 */
@PrepareForTest(ToolViewController.class)
public class ToolViewControllerTest extends ApplicationTest {
    ToolViewController toolViewController;
    private ToolView toolView;
    private ControllerManager controllerManager;

    @Override
    public void start(Stage stage) throws Exception {
        controllerManager = Mockito.mock(ControllerManager.class);
        RootPane rootPane = Mockito.mock(RootPane.class);
        RootHeaderArea rootHeaderArea = Mockito.mock(RootHeaderArea.class);
        toolView = spy(new ToolView());

        when(controllerManager.getRootPane()).thenReturn(rootPane);
        when(rootPane.getRootHeaderArea()).thenReturn(rootHeaderArea);
        when(rootHeaderArea.getToolView()).thenReturn(toolView);

        when(rootPane.getPrimaryStage()).thenReturn(stage);

        Scene sceneMock = Mockito.mock(Scene.class);
        ObservableMap<KeyCombination, Runnable> mapMock = Mockito.mock(ObservableMap.class);
        when(sceneMock.getAccelerators()).thenReturn(mapMock);
        stage.setScene(sceneMock);

        DirectorTimelineController directorTimelineControllerMock = Mockito.mock(DirectorTimelineController.class);
        when(controllerManager.getDirectorTimelineControl()).thenReturn(directorTimelineControllerMock);

        toolViewController = spy(new ToolViewController(controllerManager));
    }

    @Test
    public void noActiveBlockDeletionTest() {
        ToolButton deletionSpy = spy(toolView.getBlockDeletionTool());
        when(toolView.getBlockDeletionTool()).thenReturn(deletionSpy);
        toolViewController.activeBlockChanged();
        Mockito.verify(deletionSpy).disableButton();
    }

    @Test
    public void noActiveBlockGenerationTest() {
        ToolButton genSpy = spy(toolView.getShotGenerationTool());
        when(toolView.getShotGenerationTool()).thenReturn(genSpy);
        toolViewController.activeBlockChanged();
        Mockito.verify(genSpy).disableButton();
    }

    @Test
    public void activeBlockDeletionTest() {
        CameraShotBlock mockBlock = Mockito.mock(CameraShotBlock.class);
        when(controllerManager.getActiveShotBlock()).thenReturn(mockBlock);
        ToolButton deletionSpy = spy(toolView.getBlockDeletionTool());
        when(toolView.getBlockDeletionTool()).thenReturn(deletionSpy);
        toolViewController.activeBlockChanged();
        Mockito.verify(deletionSpy).enableButton();
    }

    @Test
    public void noEnableGenActiveBlockTest() {
        CameraShotBlock mockBlock = Mockito.mock(CameraShotBlock.class);
        when(controllerManager.getActiveShotBlock()).thenReturn(mockBlock);
        ToolButton genSpy = spy(toolView.getShotGenerationTool());
        when(toolView.getShotGenerationTool()).thenReturn(genSpy);
        toolViewController.activeBlockChanged();
        Mockito.verify(genSpy).disableButton();
    }

    @Test
    public void enableGenActiveBlockTest() {
        DirectorShotBlock mockBlock = Mockito.mock(DirectorShotBlock.class);
        DirectorShot directorShot = new DirectorShot(new GeneralShotData("Dir shot", "descr iption", 0, 1), 0, 0, new ArrayList<>());
        when(mockBlock.getShot()).thenReturn(directorShot);
        when(controllerManager.getActiveShotBlock()).thenReturn(mockBlock);
        ToolButton genSpy = spy(toolView.getShotGenerationTool());
        when(toolView.getShotGenerationTool()).thenReturn(genSpy);
        toolViewController.activeBlockChanged();
        Mockito.verify(genSpy).enableButton();
    }

    @Test
    public void generateCameraShotsTest() {
        DirectorShotBlock mockBlock = Mockito.mock(DirectorShotBlock.class);
        DirectorShot directorShot = new DirectorShot(new GeneralShotData("Dir shot", "description", 0, 1), 0, 0, new ArrayList<>());
        directorShot.addCameraTimelineIndex(0);
        when(mockBlock.getShot()).thenReturn(directorShot);
        when(controllerManager.getActiveShotBlock()).thenReturn(mockBlock);

        // Mock timeline controller
        TimelineController timelineController = spy(new TimelineController(controllerManager));
        when(controllerManager.getTimelineControl()).thenReturn(timelineController);

        Mockito.doNothing().when(timelineController).addCameraShot(Mockito.anyInt(),
                                                                   Mockito.any(CameraShot.class));

        try {
            WhiteboxImpl.invokeMethod(toolViewController, "generateCameraShots");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Mockito.verify(timelineController).addCameraShot(Mockito.anyInt(),
                                                         Mockito.any(CameraShot.class));
    }

    @Test
    public void deleteActiveShotTestCamera() {
        CameraShotBlock mockBlock = Mockito.mock(CameraShotBlock.class);
        CameraShot cameraShot = new CameraShot("Cam shot", "descrip", 0, 1);
        when(mockBlock.getShot()).thenReturn(cameraShot);
        when(controllerManager.getActiveShotBlock()).thenReturn(mockBlock);

        // Mock timeline controller
        TimelineController timelineController = spy(new TimelineController(controllerManager));
        when(controllerManager.getTimelineControl()).thenReturn(timelineController);

        Mockito.doNothing().when(timelineController).removeCameraShot(Mockito.any(CameraShotBlock.class));

        try {
            WhiteboxImpl.invokeMethod(toolViewController, "deleteActiveCameraShot");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Mockito.verify(timelineController).removeCameraShot(Mockito.any(CameraShotBlock.class));
    }

    @Test
    public void deleteActiveShotTestDirector() {
        DirectorShotBlock mockBlock = Mockito.mock(DirectorShotBlock.class);
        DirectorShot directorShot = Mockito.mock(DirectorShot.class);
        when(mockBlock.getShot()).thenReturn(directorShot);
        when(controllerManager.getActiveShotBlock()).thenReturn(mockBlock);

        // Mock timeline controller
        DirectorTimelineController timelineController = spy(new DirectorTimelineController(controllerManager));
        when(controllerManager.getDirectorTimelineControl()).thenReturn(timelineController);

        Mockito.doNothing().when(timelineController).removeShot(anyObject());

        try {
            WhiteboxImpl.invokeMethod(toolViewController, "deleteActiveCameraShot");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Mockito.verify(timelineController).removeShot(anyObject());
    }

    @Test
    public void deleteEventFilterDelete() {
        Mockito.doReturn("").when(toolViewController).getCurrentFocusClass();
        KeyEvent event = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.DELETE,
                false, false, false, false);
        toolViewController.deleteEventFilter(event);
        Mockito.verify(toolViewController, times(1)).deleteActiveCameraShot();
    }

    @Test
    public void deleteEventFilterNone() {
        Mockito.doReturn("").when(toolViewController).getCurrentFocusClass();
        KeyEvent event = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.SPACE,
                false, false, false, false);
        toolViewController.deleteEventFilter(event);
        Mockito.verify(toolViewController, times(0)).deleteActiveCameraShot();
    }

    @Test
    public void deleteEventFilterTextField() {
        Mockito.doReturn("gui.styling.StyledTextfield").when(toolViewController).getCurrentFocusClass();
        KeyEvent event = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.DELETE,
                false, false, false, false);
        toolViewController.deleteEventFilter(event);
        Mockito.verify(toolViewController, times(0)).deleteActiveCameraShot();
    }

    @Test
    public void deleteEventFilterBackspace() {
        Mockito.doReturn("").when(toolViewController).getCurrentFocusClass();
        KeyEvent event = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.BACK_SPACE,
                true, true, true, true);
        toolViewController.deleteEventFilter(event);
        Mockito.verify(toolViewController, times(1)).deleteActiveCameraShot();
    }
}
