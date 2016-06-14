package control;

import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.headerarea.DetailView;
import gui.headerarea.ToolView;
import gui.modal.SaveModalView;
import gui.root.RootHeaderArea;
import gui.root.RootPane;
import gui.styling.StyledTextfield;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.testfx.framework.junit.ApplicationTest;

import java.util.Observable;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

/**
 * @author goto fail;
 */
@PrepareForTest({ControllerManager.class, ToolViewController.class})
public class ControllerManagerTest extends ApplicationTest {
    ControllerManager controllerManager;
    private TimelineController timelineController;
    private RootPane rootpane;
    private DetailViewController detailViewController;
    private ToolViewController toolViewController;
    private DirectorTimelineController directorTimelineController;
    private ProjectController projectController;

    @Override
    public void start(Stage stage) throws Exception {
        rootpane = Mockito.mock(RootPane.class);

        timelineController = Mockito.mock(TimelineController.class);
        detailViewController = Mockito.mock(DetailViewController.class);
        toolViewController = Mockito.mock(ToolViewController.class);
        directorTimelineController = Mockito.mock(DirectorTimelineController.class);
        projectController = Mockito.mock(ProjectController.class);

        controllerManager = Mockito.spy(new ControllerManager(rootpane, timelineController, detailViewController,
                toolViewController, directorTimelineController, projectController));
    }

    @Test
    public void getRootPaneTest() {
        assertNotNull(controllerManager.getRootPane());
    }

    @Test
    public void getTimelineControllerTest() {
        assertNotNull(controllerManager.getTimelineControl());
    }

    @Test
    public void getDirectorTimelineControllerTest() {
        assertNotNull(controllerManager.getDirectorTimelineControl());
    }

    @Test
    public void getToolViewControllerTest() {
        assertNotNull(controllerManager.getToolViewController());
    }

    @Test
    public void getDetailViewControllerTest() {
        assertNotNull(controllerManager.getDetailViewController());
    }

    @Test
    public void getPreferencesViewControllerTest() {
        assertNull(controllerManager.getPreferencesViewController());
    }

    @Test
    public void getProjectControllerTest() {
        assertNotNull(controllerManager.getProjectController());
    }

    @Test
    public void scriptingProjectTest() {
        ScriptingProject scriptingProject = Mockito.mock(ScriptingProject.class);
        controllerManager.setScriptingProject(scriptingProject);

        assertEquals(scriptingProject, controllerManager.getScriptingProject());
    }

    @Test
    public void getActiveShotBlockTest() {
        assertNull(controllerManager.getActiveShotBlock());
    }

    @Test
    public void setActiveShotBlockTest() {
        CameraShotBlock shotBlockMock = mock(CameraShotBlock.class);

        controllerManager.setActiveShotBlock(shotBlockMock);

        assertEquals(shotBlockMock, controllerManager.getActiveShotBlock());

        verify(detailViewController).activeBlockChanged();
        verify(toolViewController).activeBlockChanged();
    }

    @Test
    public void focusChangeListener() {
        ObservableValue<Node> observableValue = Mockito.mock(ObservableValue.class);
        Node oldNode = Mockito.mock(Node.class);
        Node newNode = Mockito.mock(Node.class);

        controllerManager.focusChangeListener(observableValue, oldNode, newNode);
        Mockito.verify(controllerManager, times(0)).initOnCloseOperation();
    }

    @Test
    public void focusChangeListenerNull() {
        ObservableValue<Node> observableValue = Mockito.mock(ObservableValue.class);
        controllerManager.focusChangeListener(observableValue, null, null);
        Mockito.verify(controllerManager, times(0)).initOnCloseOperation();
    }

    @Test
    public void initOnCloseOperation() {
        Stage stage = Mockito.mock(Stage.class);
        when(rootpane.getPrimaryStage()).thenReturn(stage);
        controllerManager.initOnCloseOperation();
        Mockito.verify(controllerManager, times(1)).initOnCloseOperation();
    }

    @Test
    public void handleOnClose() {
        WindowEvent event = Mockito.mock(WindowEvent.class);
        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        controllerManager.setScriptingProject(project);
        when(project.isChanged()).thenReturn(true);
        Mockito.doNothing().when(controllerManager).initSaveModal();

        controllerManager.handleOnClose(event);
        Mockito.verify(controllerManager, times(1)).initSaveModal();
    }

    @Test
    public void handleOnCloseProjectNull() {
        WindowEvent event = Mockito.mock(WindowEvent.class);

        controllerManager.handleOnClose(event);
        Mockito.verify(controllerManager, times(0)).initSaveModal();
    }

    @Test
    public void handleOnCloseProjectNotChanged() {
        WindowEvent event = Mockito.mock(WindowEvent.class);
        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        controllerManager.setScriptingProject(project);

        controllerManager.handleOnClose(event);
        Mockito.verify(controllerManager, times(0)).initSaveModal();
    }

    @Test
    public void initSaveModal() throws InterruptedException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {

            controllerManager.initSaveModal();

            latch[0].countDown();
        });
        latch[0].await();
        Mockito.verify(controllerManager, times(1)).initSaveModal();
        assertNotNull(controllerManager.getSaveModal());
    }

    @Test
    public void handleSave() {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, null);
        SaveModalView saveModalView = Mockito.mock(SaveModalView.class);
        controllerManager.setSaveModal(saveModalView);
        Stage stage = Mockito.mock(Stage.class);
        when(rootpane.getPrimaryStage()).thenReturn(stage);

        controllerManager.handleSave(mouseEvent);

        Mockito.verify(projectController, times(1)).save();
        Mockito.verify(saveModalView, times(1)).hideModal();
        Mockito.verify(stage, times(1)).close();
    }

    @Test
    public void handleDontSave() {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, null);
        SaveModalView saveModalView = Mockito.mock(SaveModalView.class);
        controllerManager.setSaveModal(saveModalView);
        Stage stage = Mockito.mock(Stage.class);
        when(rootpane.getPrimaryStage()).thenReturn(stage);

        controllerManager.handleDontSave(mouseEvent);

        Mockito.verify(projectController, times(0)).save();
        Mockito.verify(saveModalView, times(1)).hideModal();
        Mockito.verify(stage, times(1)).close();
    }

    @Test
    public void handleClose() {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, null);
        SaveModalView saveModalView = Mockito.mock(SaveModalView.class);
        controllerManager.setSaveModal(saveModalView);

        controllerManager.handleCancel(mouseEvent);
        Mockito.verify(projectController, times(0)).save();
        Mockito.verify(saveModalView, times(1)).hideModal();
    }

    @Test
    public void updateWindowTitle() {
        Stage stage = Mockito.mock(Stage.class);
        when(rootpane.getPrimaryStage()).thenReturn(stage);
        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        doReturn(project).when(controllerManager).getScriptingProject();

        controllerManager.updateWindowTitle();

        Mockito.verify(project, times(1)).getName();
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }
}
