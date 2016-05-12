package control;

import data.CameraTimeline;
import data.ScriptingProject;
import gui.modal.CameraShotCreationModalView;
import gui.modal.ModalView;
import gui.modal.SaveModalView;
import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Bart on 12/05/2016.
 */
@RunWith(PowerMockRunner.class)
public class CreationModalViewControllerTest {

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
    public void showCameraCreationWindow() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            RootPane rootPane = Mockito.mock(RootPane.class);
            when(rootPane.getPrimaryStage()).thenReturn(new Stage());

            creationModalViewController.showCameraCreationWindow();

            latch.countDown();
        });

        latch.await();
        CameraShotCreationModalView modalView = creationModalViewController.getCameraShotCreationModalView();
        assertNotNull(modalView.getCreationButton().getOnMouseReleased());
        assertNotNull(modalView.getCancelButton().getOnMouseReleased());
        assertNotNull(modalView.getStartField().getOnKeyPressed());
        assertNotNull(modalView.getEndField().getOnKeyPressed());
    }

    @Test






    public static class AsNonApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            // noop
        }
    }

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        // Initialise Java FX

        System.out.printf("About to launch FX App\n");
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                try {
                    ApplicationTest.launch(AsNonApp.class, new String[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.setDaemon(true);
        t.start();
        System.out.printf("FX App thread started\n");
    }
}