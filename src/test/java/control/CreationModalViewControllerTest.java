package control;

import data.Instrument;
import data.ScriptingProject;
import gui.headerarea.DoubleTextField;
import gui.modal.CameraShotCreationModalView;
import gui.modal.DirectorShotCreationModalView;
import gui.root.RootPane;
import gui.styling.StyledCheckbox;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Bart on 12/05/2016.
 */
@PowerMockIgnore("javax.management.*")
public class CreationModalViewControllerTest extends ApplicationTest {

    private ControllerManager manager;
    private CreationModalViewController creationModalViewController;
    private DirectorTimelineController directorTimelineController;

    @Before
    public void initialize() {
        manager = Mockito.mock(ControllerManager.class);
        RootPane rootPane = Mockito.mock(RootPane.class);
        ScriptingProject scriptingProject = Mockito.mock(ScriptingProject.class);

        when(manager.getRootPane()).thenReturn(rootPane);
        when(manager.getScriptingProject()).thenReturn(scriptingProject);
        when(scriptingProject.getCameraTimelines()).thenReturn(new ArrayList<>());

        directorTimelineController = Mockito.mock(DirectorTimelineController.class);

        creationModalViewController = spy(new CreationModalViewController(manager));
    }

    @Test
    public void constructor() {
        assertNotNull(creationModalViewController);
    }

    @Test
    public void showCameraCreationWindow() throws InterruptedException {
        setupCameraCreationModalView();

        CameraShotCreationModalView modalView = creationModalViewController.getCameraShotCreationModalView();
        assertNotNull(modalView.getCreationButton().getOnMouseReleased());
        assertNotNull(modalView.getCancelButton().getOnMouseReleased());
        assertNotNull(modalView.getStartField().getOnKeyPressed());
        assertNotNull(modalView.getEndField().getOnKeyPressed());

        tearDownCameraCreationModalView();
    }

    @Test
    public void cameraShotStartCountEnterHandler() throws Exception {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.ENTER,
                false, false, false, false);

        setupCameraCreationModalView();

        DoubleTextField startField = creationModalViewController.getCameraShotCreationModalView().getStartField();
        startField.setText("5.70");
        WhiteboxImpl.invokeMethod(creationModalViewController, "cameraShotStartCountEnterHandler", keyEvent);
        assertEquals("5.75", startField.getText());
        tearDownCameraCreationModalView();
    }

    @Test
    public void cameraShotStartCountEnterHandlerOtherKeycode() throws Exception {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.SPACE,
                false, false, false, false);

        setupCameraCreationModalView();

        DoubleTextField startField = creationModalViewController.getCameraShotCreationModalView().getStartField();
        startField.setText("5.70");
        WhiteboxImpl.invokeMethod(creationModalViewController, "cameraShotStartCountEnterHandler", keyEvent);
        assertEquals("5.70", startField.getText());
        tearDownCameraCreationModalView();
    }

    @Test
    public void cameraShotEndCountEnterHandler() throws Exception {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.ENTER,
                false, false, false, false);

        setupCameraCreationModalView();

        DoubleTextField endField = creationModalViewController.getCameraShotCreationModalView().getEndField();
        endField.setText("4.2");
        WhiteboxImpl.invokeMethod(creationModalViewController, "cameraShotEndCountEnterHandler", keyEvent);
        assertEquals("4.25", endField.getText());

        tearDownCameraCreationModalView();
    }

    @Test
    public void cameraShotEndCountEnterHandlerOtherKeycode() throws Exception {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.SPACE,
                false, false, false, false);

        setupCameraCreationModalView();

        DoubleTextField endField = creationModalViewController.getCameraShotCreationModalView().getEndField();
        endField.setText("4.2");
        WhiteboxImpl.invokeMethod(creationModalViewController, "cameraShotEndCountEnterHandler", keyEvent);
        assertEquals("4.2", endField.getText());

        tearDownCameraCreationModalView();
    }

    @Test
    public void cameraShotStartCountFocusHandler() throws Exception {
        setupCameraCreationModalView();

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

        tearDownCameraCreationModalView();
    }

    @Test
    public void cameraShotStartCountFocusHandlerFalse() throws Exception {
        setupCameraCreationModalView();

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
        }, false, true);
        assertEquals("5.70", startField.getText());

        tearDownCameraCreationModalView();
    }

    @Test
    public void cameraShotEndCountFocusHandler() throws Exception {
        setupCameraCreationModalView();

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

        tearDownCameraCreationModalView();
    }

    @Test
    public void cameraShotEndCountFocusHandlerFalse() throws Exception {
        setupCameraCreationModalView();

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
        }, false, true);
        assertEquals("5.70", endField.getText());

        tearDownCameraCreationModalView();
    }

    @Test
    public void createCameraShot() throws Exception {
        // Setup necessary variables
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);
        setupCameraCreationModalView();
        final CameraShotCreationModalView[] modalView = new CameraShotCreationModalView[1];
        final TimelineController[] timelineController = new TimelineController[1];

        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        when(manager.getScriptingProject()).thenReturn(project);
        when(project.getInstruments()).thenReturn(new ArrayList<>(Arrays.asList(new Instrument("", ""))));

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            // More setup
            CreationModalViewController controller = creationModalViewController;
            when(controller.validateCameraShot()).thenReturn(true);
            modalView[0] = spy(controller.getCameraShotCreationModalView());
            when(modalView[0].getInstrumentsInShot()).thenReturn(new ArrayList<>(Arrays.asList(0)));
            controller.setCameraShotCreationModalView(modalView[0]);
            when(modalView[0].getCamerasInShot()).thenReturn(new ArrayList(Arrays.asList(1)));

            timelineController[0] = Mockito.mock(TimelineController.class);
            Mockito.doNothing().when(timelineController[0]).addCameraShot(anyInt(), anyObject());
            when(manager.getTimelineControl()).thenReturn(timelineController[0]);

            try {
                WhiteboxImpl.invokeMethod(controller, "createCameraShot", mouseEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch[0].countDown();
        });
        latch[0].await();

        verify(timelineController[0], times(1)).addCameraShot(anyInt(), anyObject());
        verify(modalView[0], times(1)).getModalStage();

        tearDownCameraCreationModalView();
    }

    @Test
    public void createCameraShotNoValidate() throws Exception {
        // Setup necessary variables
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);

        doReturn(false).when(creationModalViewController).validateCameraShot();

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {

            try {
                WhiteboxImpl.invokeMethod(creationModalViewController, "createCameraShot", mouseEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch[0].countDown();
        });
        latch[0].await();
    }

    @Test
    public void cameraCreationCancelButtonHandler() throws Exception {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);
        setupCameraCreationModalView();
        final CameraShotCreationModalView[] modalView = new CameraShotCreationModalView[1];

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {

            modalView[0] = spy(creationModalViewController.getCameraShotCreationModalView());
            creationModalViewController.setCameraShotCreationModalView(modalView[0]);

            try {
                WhiteboxImpl.invokeMethod(creationModalViewController, "cameraCreationCancelButtonHandler", mouseEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch[0].countDown();
        });
        latch[0].await();

       verify(modalView[0], times(1)).getModalStage();

        tearDownCameraCreationModalView();
    }

    @Test
    public void validateCameraShotFalse() throws InterruptedException {
        setupCameraCreationModalView();

        CameraShotCreationModalView modalView = creationModalViewController.getCameraShotCreationModalView();
        modalView.getDescriptionField().setText("description");
        modalView.getNameField().setText("name");
        modalView.getStartField().setText("15");
        modalView.getEndField().setText("5");

        final boolean[] result = new boolean[1];

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            result[0] = creationModalViewController.validateCameraShot();
            latch[0].countDown();
        });
        latch[0].await();

        assertFalse(result[0]);

        tearDownCameraCreationModalView();
    }

    @Test
    public void validateCameraShotTrue() throws InterruptedException {
        setupCameraCreationModalView();

        CameraShotCreationModalView modalView = spy(creationModalViewController.getCameraShotCreationModalView());
        modalView.getDescriptionField().setText("description");
        modalView.getNameField().setText("name");
        modalView.getStartField().setText("2");
        modalView.getEndField().setText("5");
        creationModalViewController.setCameraShotCreationModalView(modalView);

        StyledCheckbox checkbox1 = Mockito.mock(StyledCheckbox.class);
        StyledCheckbox checkbox2 = Mockito.mock(StyledCheckbox.class);
        when(modalView.getCameraCheckboxes()).thenReturn(new ArrayList<>(Arrays.asList(checkbox1, checkbox2)));
        doReturn(true).when(checkbox1).getSelected();

        final boolean[] result = new boolean[1];

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            result[0] = creationModalViewController.validateCameraShot();
            latch[0].countDown();
        });
        latch[0].await();

        assertTrue(result[0]);

        tearDownCameraCreationModalView();
    }

    @Test
    public void showDirectorCreationWindow() throws InterruptedException {
        setupDirectorCreationModalView();

        DirectorShotCreationModalView modalView = creationModalViewController.getDirectorShotCreationModalView();
        assertNotNull(modalView.getCancelButton().getOnMouseReleased());
        assertNotNull(modalView.getCreationButton().getOnMouseReleased());

        tearDownDirectorCreationModalView();
    }

    @Test
    public void createDirectorShot() throws InterruptedException {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);
        setupDirectorCreationModalView();
        final DirectorShotCreationModalView[] modalView = new DirectorShotCreationModalView[1];

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {

            CreationModalViewController controller = creationModalViewController;
            when(controller.validateDirectorShot()).thenReturn(true);
            when(manager.getDirectorTimelineControl()).thenReturn(directorTimelineController);

            modalView[0] = spy(controller.getDirectorShotCreationModalView());
            controller.setDirectorShotCreationModalView(modalView[0]);
            when(modalView[0].getCamerasInShot()).thenReturn(new ArrayList(Arrays.asList(1)));

            try {
                WhiteboxImpl.invokeMethod(controller, "createDirectorShot", mouseEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch[0].countDown();
        });
        latch[0].await();

        verify(modalView[0], times(1)).getModalStage();
        tearDownDirectorCreationModalView();
    }

    @Test
    public void createDirectorShotNoValidate() throws InterruptedException {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);
        setupDirectorCreationModalView();
        final DirectorShotCreationModalView[] modalView = new DirectorShotCreationModalView[1];

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {

            CreationModalViewController controller = creationModalViewController;
            when(controller.validateDirectorShot()).thenReturn(false);

            modalView[0] = spy(controller.getDirectorShotCreationModalView());
            controller.setDirectorShotCreationModalView(modalView[0]);
            when(modalView[0].getCamerasInShot()).thenReturn(new ArrayList(Arrays.asList(1)));

            try {
                WhiteboxImpl.invokeMethod(controller, "createDirectorShot", mouseEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch[0].countDown();
        });
        latch[0].await();

        verify(modalView[0], times(0)).getModalStage();
        tearDownDirectorCreationModalView();
    }

    @Test
    public void validateDirecorShotFalse() throws InterruptedException {
        setupDirectorCreationModalView();

        DirectorShotCreationModalView modalView = creationModalViewController.getDirectorShotCreationModalView();
        modalView.getDescriptionField().setText("description");
        modalView.getNameField().setText("name");
        modalView.getStartField().setText("15");
        modalView.getEndField().setText("5");
        modalView.getFrontPaddingField().setText("-2");
        modalView.getFrontPaddingField().setText("-2");

        final boolean[] result = new boolean[1];

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            result[0] = creationModalViewController.validateDirectorShot();
            latch[0].countDown();
        });
        latch[0].await();

        assertFalse(result[0]);

        tearDownDirectorCreationModalView();
    }

    @Test
    public void validateDirecorShotFalseOtherBranch() throws InterruptedException {
        setupDirectorCreationModalView();

        DirectorShotCreationModalView modalView = creationModalViewController.getDirectorShotCreationModalView();
        modalView.getDescriptionField().setText("description");
        modalView.getNameField().setText("name");
        modalView.getStartField().setText("15");
        modalView.getEndField().setText("5");
        modalView.getFrontPaddingField().setText("2");
        modalView.getEndPaddingField().setText("-2");

        final boolean[] result = new boolean[1];

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            result[0] = creationModalViewController.validateDirectorShot();
            latch[0].countDown();
        });
        latch[0].await();

        assertFalse(result[0]);

        tearDownDirectorCreationModalView();
    }

    @Test
    public void validateDirecorShotTrue() throws InterruptedException {
        setupDirectorCreationModalView();

        DirectorShotCreationModalView modalView = creationModalViewController.getDirectorShotCreationModalView();
        modalView.getDescriptionField().setText("description");
        modalView.getNameField().setText("name");
        modalView.getStartField().setText("2");
        modalView.getEndField().setText("5");
        modalView.getFrontPaddingField().setText("2");
        modalView.getFrontPaddingField().setText("2");

        final boolean[] result = new boolean[1];

        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            result[0] = creationModalViewController.validateDirectorShot();
            latch[0].countDown();
        });
        latch[0].await();

        assertTrue(result[0]);

        tearDownDirectorCreationModalView();
    }

    private void setupCameraCreationModalView() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            creationModalViewController.showCameraCreationWindow();
            latch.countDown();
        });

        latch.await();
    }

    private void tearDownCameraCreationModalView() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            creationModalViewController.getCameraShotCreationModalView().getModalStage().close();
            latch.countDown();
        });

        latch.await();
    }

    private void setupDirectorCreationModalView() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            creationModalViewController.showDirectorCreationWindow();
            latch.countDown();
        });

        latch.await();
    }

    private void tearDownDirectorCreationModalView() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            creationModalViewController.getDirectorShotCreationModalView().getModalStage().close();
            latch.countDown();
        });

        latch.await();
    }

    @Test
    public void directorShotStartCountEnterHandler() throws InterruptedException {
        setupDirectorCreationModalView();
        KeyEvent event = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.ENTER,
                false, false, false, false);
        creationModalViewController.directorShotStartCountEnterHandler(event);
        tearDownDirectorCreationModalView();
    }

    @Test
    public void directorShotStartCountEnterHandlerOtherKeyCode() throws InterruptedException {
        setupDirectorCreationModalView();
        KeyEvent event = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.SPACE,
                false, false, false, false);
        creationModalViewController.directorShotStartCountEnterHandler(event);
        tearDownDirectorCreationModalView();
    }

    @Test
    public void directorShotEndCountEnterHandler() throws InterruptedException {
        setupDirectorCreationModalView();
        KeyEvent event = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.ENTER,
                false, false, false, false);
        creationModalViewController.directorShotEndCountEnterHandler(event);
        tearDownDirectorCreationModalView();
    }

    @Test
    public void directorShotEndCountEnterHandlerOtherKeyCode() throws InterruptedException {
        setupDirectorCreationModalView();
        KeyEvent event = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.SPACE,
                false, false, false, false);
        creationModalViewController.directorShotEndCountEnterHandler(event);
        tearDownDirectorCreationModalView();
    }

    @Test
    public void directorShotEndCountFocusHandler() throws InterruptedException {
        setupDirectorCreationModalView();
        ObservableValue value = Mockito.mock(ObservableValue.class);
        creationModalViewController.directorShotEndCountFocusHandler(value, true, false);
        tearDownDirectorCreationModalView();
    }

    @Test
    public void directorShotEndCountFocusHandlerFalse() throws InterruptedException {
        setupDirectorCreationModalView();
        ObservableValue value = Mockito.mock(ObservableValue.class);
        creationModalViewController.directorShotEndCountFocusHandler(value, false, true);
        tearDownDirectorCreationModalView();
    }

    @Test
    public void directorShotStartCountFocusHandler() throws InterruptedException {
        setupDirectorCreationModalView();
        ObservableValue value = Mockito.mock(ObservableValue.class);
        creationModalViewController.directorShotStartCountFocusHandler(value, true, false);
        tearDownDirectorCreationModalView();
    }

    @Test
    public void directorShotStartCountFocusHandlerFalse() throws InterruptedException {
        setupDirectorCreationModalView();
        ObservableValue value = Mockito.mock(ObservableValue.class);
        creationModalViewController.directorShotStartCountFocusHandler(value, false, true);
        tearDownDirectorCreationModalView();
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