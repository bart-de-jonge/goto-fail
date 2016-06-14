package control;

import data.ScriptingProject;
import gui.modal.PreferencesModalView;
import gui.modal.ReloadModalView;
import gui.root.RootPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.Spy;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.CountDownLatch;

@PowerMockIgnore("javax.management.*")
public class PreferencesViewControllerTest extends ApplicationTest {

    private PreferencesViewController preferencesViewController;
    private ControllerManager controllerManager;
    private ScriptingProject scriptingProject;
    private RootPane rootPane;
    private ProjectController projectController;
    private Stage stage;

    @Before
    public void setUp() throws Exception {
        controllerManager = Mockito.mock(ControllerManager.class);
        rootPane = Mockito.mock(RootPane.class);
        scriptingProject = Mockito.mock(ScriptingProject.class);
        projectController = Mockito.mock(ProjectController.class);
        stage = Mockito.mock(Stage.class);

        when(controllerManager.getProjectController()).thenReturn(projectController);
        when(controllerManager.getRootPane()).thenReturn(rootPane);
        when(controllerManager.getScriptingProject()).thenReturn(scriptingProject);

        preferencesViewController = spy(new PreferencesViewController(controllerManager));
    }

    @Test
    public void constructorTest() {
        assertNotNull(preferencesViewController);
    }

    @Test
    public void showPreferencesWindowTest() throws InterruptedException {
        setupPreferencesModalView();

        PreferencesModalView modalView = preferencesViewController.getPreferencesModalView();
        assertNotNull(modalView.getSaveButton());
        assertNotNull(modalView.getCancelButton());
        assertNotNull(modalView.getColorList());

        tearDownPreferencesModalView();
    }

    @Test
    public void showReloadWindowTest() throws InterruptedException {
        setupReloadModalView();

        ReloadModalView modalView = preferencesViewController.getReloadModalView();
        assertNotNull(modalView.getSaveButton());
        assertNotNull(modalView.getCancelButton());
        assertNotNull(modalView.getDontSaveButton());

        tearDownReloadModalView();
    }

    @Test
    public void handleSavesaveButtonTest() throws InterruptedException {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);

        setupPreferencesModalView();
        setupReloadModalView();

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            when(rootPane.getPrimaryStage()).thenReturn(stage);

            try {
                WhiteboxImpl.invokeMethod(preferencesViewController, "handleSavesaveButton", mouseEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch[0].countDown();
        });
        latch[0].await();

        verify(projectController, times(1)).save();

        tearDownReloadModalView();
        tearDownPreferencesModalView();
    }

    @Test
    public void handleNoSavesaveButton() throws InterruptedException {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);

        setupPreferencesModalView();
        setupReloadModalView();

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            when(rootPane.getPrimaryStage()).thenReturn(stage);

            try {
                WhiteboxImpl.invokeMethod(preferencesViewController, "handleNoSavesaveButton", mouseEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch[0].countDown();
        });
        latch[0].await();

        verify(projectController, times(0)).save();

        tearDownReloadModalView();
        tearDownPreferencesModalView();
    }

    @Test
    public void handleApplyButton() {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, null);
        Mockito.doNothing().when(preferencesViewController).showReloadWindow();
        preferencesViewController.handleApplyButton(mouseEvent);
        Mockito.verify(preferencesViewController, times(1)).showReloadWindow();
    }

    @Test
    public void handleCancelButton() {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, null);
        PreferencesModalView modalView = Mockito.mock(PreferencesModalView.class);
        preferencesViewController.setPreferencesModalView(modalView);
        preferencesViewController.handleCancelButton(mouseEvent);
        Mockito.verify(modalView, times(1)).hideModal();
    }

    @Test
    public void handleCancelsaveButton() {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, null);
        ReloadModalView modalView = Mockito.mock(ReloadModalView.class);
        preferencesViewController.setReloadModalView(modalView);
        preferencesViewController.handleCancelsaveButton(mouseEvent);
        Mockito.verify(modalView, times(1)).hideModal();
    }

    private void setupPreferencesModalView() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            preferencesViewController.showPreferencesWindow();
            latch.countDown();
        });

        latch.await();
    }

    private void tearDownPreferencesModalView() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            preferencesViewController.getPreferencesModalView().getModalStage().close();
            latch.countDown();
        });

        latch.await();
    }

    private void setupReloadModalView() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            preferencesViewController.showReloadWindow();
            latch.countDown();
        });

        latch.await();
    }

    private void tearDownReloadModalView() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            preferencesViewController.getReloadModalView().getModalStage().close();
            latch.countDown();
        });

        latch.await();
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    public static class AsNonApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            // noop
        }
    }
}
