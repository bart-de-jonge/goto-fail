package control;

import data.ScriptingProject;
import gui.headerarea.DoubleTextField;
import gui.modal.CameraShotCreationModalView;
import gui.root.RootPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Bart on 12/05/2016.
 */
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(CreationModalViewController.class)
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

        creationModalViewController = new CreationModalViewController(manager);
    }

    @Test
    public void constructor() {
        assertNotNull(creationModalViewController);
    }

    @Test
    public void showCameraCreationWindow() throws InterruptedException {
        setupCreationModalView();

        CameraShotCreationModalView modalView = creationModalViewController.getCameraShotCreationModalView();
        assertNotNull(modalView.getCreationButton().getOnMouseReleased());
        assertNotNull(modalView.getCancelButton().getOnMouseReleased());
        assertNotNull(modalView.getStartField().getOnKeyPressed());
        assertNotNull(modalView.getEndField().getOnKeyPressed());

        tearDownCreationModalView();
    }

    @Test
    public void cameraShotStartCountEnterHandler() throws Exception {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.ENTER,
                false, false, false, false);

        setupCreationModalView();

        DoubleTextField startField = creationModalViewController.getCameraShotCreationModalView().getStartField();
        startField.setText("5.70");
        WhiteboxImpl.invokeMethod(creationModalViewController, "cameraShotStartCountEnterHandler", keyEvent);
        assertEquals("5.75", startField.getText());

        tearDownCreationModalView();
    }

    @Test
    public void cameraShotEndCountEnterHandler() throws Exception {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.ENTER,
                false, false, false, false);

        setupCreationModalView();

        DoubleTextField endField = creationModalViewController.getCameraShotCreationModalView().getEndField();
        endField.setText("4.2");
        WhiteboxImpl.invokeMethod(creationModalViewController, "cameraShotEndCountEnterHandler", keyEvent);
        assertEquals("4.25", endField.getText());

        tearDownCreationModalView();
    }

    @Test
    public void cameraShotStartCountFocusHandler() throws Exception {
        setupCreationModalView();

        DoubleTextField startField = creationModalViewController.getCameraShotCreationModalView().getStartField();
        startField.setText("5.70");
        WhiteboxImpl.invokeMethod(creationModalViewController, "cameraShotStartCountFocusHandler", new ObservableValue<Boolean>() {
            @Override
            public void addListener(InvalidationListener listener) {
            }
            @Override
            public void removeListener(InvalidationListener listener) {
            }
            @Override
            public void addListener(ChangeListener<? super Boolean> listener) {
            }
            @Override
            public void removeListener(ChangeListener<? super Boolean> listener) {
            }
            @Override
            public Boolean getValue() {
                return null;
            }
        }, true, false);
        assertEquals("5.75", startField.getText());

        tearDownCreationModalView();
    }

    @Test
    public void cameraShotEndCountFocusHandler() throws Exception {
        setupCreationModalView();

        DoubleTextField endField = creationModalViewController.getCameraShotCreationModalView().getEndField();
        endField.setText("5.70");
        WhiteboxImpl.invokeMethod(creationModalViewController, "cameraShotEndCountFocusHandler", new ObservableValue<Boolean>() {
            @Override
            public void addListener(InvalidationListener listener) {
            }
            @Override
            public void removeListener(InvalidationListener listener) {
            }
            @Override
            public void addListener(ChangeListener<? super Boolean> listener) {
            }
            @Override
            public void removeListener(ChangeListener<? super Boolean> listener) {
            }
            @Override
            public Boolean getValue() {
                return null;
            }
        }, true, false);
        assertEquals("5.75", endField.getText());

        tearDownCreationModalView();
    }

    private void setupCreationModalView() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            creationModalViewController.showCameraCreationWindow();
            latch.countDown();
        });

        latch.await();
    }

    private void tearDownCreationModalView() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            creationModalViewController.getCameraShotCreationModalView().getModalStage().close();
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