package control;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import data.*;
import gui.modal.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.testfx.framework.junit.ApplicationTest;

import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

@PrepareForTest(ProjectController.class)
public class ProjectControllerTest extends ApplicationTest {

    private ProjectController projectController;
    private ControllerManager controllerManager;
    private ScriptingProject project;
    private RootPane rootPane;
    private UploadSuccessModalView successModal;
    private ErrorWhileUploadingModalView errorModal;

    @Before
    public void setUp() throws Exception {
        rootPane = mock(RootPane.class);
        StartupModalView startModal = mock(StartupModalView.class);
        when(rootPane.getStartupModalView()).thenReturn(startModal);
        StyledButton buttonMock = mock(StyledButton.class);
        when(startModal.getNewButton()).thenReturn(buttonMock);
        when(startModal.getLoadButton()).thenReturn(buttonMock);
        when(startModal.getExitButton()).thenReturn(buttonMock);
        controllerManager = mock(ControllerManager.class);
        when(controllerManager.getRootPane()).thenReturn(rootPane);
        projectController = spy(new ProjectController(controllerManager));
        project = mock(ScriptingProject.class);
        when(controllerManager.getScriptingProject()).thenReturn(project);
        when(controllerManager.getRootPane()).thenReturn(rootPane);
        when(rootPane.getControllerManager()).thenReturn(controllerManager);
    }

    @Test
    public void constructorTest() {
        assertEquals(projectController.getControllerManager(), controllerManager);
    }
    
    @Test
    public void saveTest() {
        when(project.getFilePath()).thenReturn(null);
        Mockito.doNothing().when(projectController).saveAs();
        projectController.save();
        Mockito.verify(projectController).saveAs();
    }

//    @Test
//    public void saveAsTest() {
//        when(project.getFilePath()).thenReturn(null);
//        final CountDownLatch[] latch = {new CountDownLatch(1)};
//        Platform.runLater(() -> {
//            projectController.saveAs();
//            latch[0].countDown();
//        });
//        Mockito.verify(controllerManager, times(0)).getScriptingProject();
//    }
    
    @Test
    public void saveTestWithExistingFilePath() {
        when(project.getFilePath()).thenReturn("Kek");
        projectController.save();
        Mockito.verify(project).write();
    }
    
    @Test
    public void loadTest() {
        String oldConfigIni = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("config.ini"));
            oldConfigIni = reader.readLine();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        ArrayList<CameraTimeline> listMock = mock(ArrayList.class);
        LinkedList<DirectorShot> directorListMock = mock(LinkedList.class);
        LinkedList<CameraShot> cameraListMock = mock(LinkedList.class);
        LinkedList<CameraShot> cameraList = new LinkedList<>();
        cameraList.add(mock(CameraShot.class));
        cameraList.add(mock(CameraShot.class));
        cameraList.add(mock(CameraShot.class));
        DirectorTimeline timeline = new DirectorTimeline("tet", project);
        GeneralShotData shotData = new GeneralShotData("a", "b", 0.0, 10.0);
        timeline.addShot(new DirectorShot(shotData, 0, 0, new ArrayList<Integer>()));
        timeline.addShot(new DirectorShot(shotData, 0, 0, new ArrayList<Integer>()));

        timeline.getShots().add(new DirectorShot(shotData, 0, 0, new ArrayList<Integer>()));
        CameraShot shot = mock(CameraShot.class);

        timeline.getShots().get(0).addCameraShot(shot);
        timeline.getShots().get(1).addCameraShot(shot);

        when(listMock.size()).thenReturn(3);
        when(project.getCameraTimelines()).thenReturn(listMock);
        when(project.getDirectorTimeline()).thenReturn(timeline);
        File file = new File("src/test/files/general_test3.scp");
        TimelineController timelineController = mock(TimelineController.class);
        DirectorTimelineController directorTimelineController = mock(DirectorTimelineController.class);
        when(controllerManager.getTimelineControl()).thenReturn(timelineController);
        when(controllerManager.getDirectorTimelineControl()).thenReturn(directorTimelineController);
        Mockito.doNothing().when(rootPane).reInitRootCenterArea(Mockito.any());
        CameraTimeline timelineMock = mock(CameraTimeline.class);
        when(listMock.get(Mockito.anyInt())).thenReturn(timelineMock);
        when(timelineMock.getShots()).thenReturn(cameraList);
        ArrayList<Camera> cameras = new ArrayList<Camera>();
        cameras.add(new Camera());
        cameras.add(new Camera());
        cameras.add(new Camera());
        when(project.getCameras()).thenReturn(cameras);

        projectController.load(file);
        
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter("config.ini"));
            writer.println(oldConfigIni);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (writer!=null) {
                writer.close();
            }
        }

        verify(project, atLeastOnce()).getCameraTimelines();
        verify(project, atLeastOnce()).getDirectorTimeline();
    }
    
    @Test
    public void loadTestNullFile() {
        File file = new File("ThisDoesNotEvenExist");
        Stage stage = mock(Stage.class);
        when(rootPane.getPrimaryStage()).thenReturn(stage);
        projectController.load(file);
        Mockito.verify(stage, times(1)).close();
    }
    
    @Test
    public void newProjectTest() throws InterruptedException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
           projectController.newProject();
           latch[0].countDown();
        });
        latch[0].await();
    }
    
    @Test
    public void confirmTypeDeleteTest() throws Exception {
       MouseEvent event = mock(MouseEvent.class);
       List<Camera> camera = mock(List.class);
       Platform.runLater(() -> {
           projectController.setEditProjectModal(new EditProjectModalView(rootPane, false));
           projectController.setTypeWarningModal(new DeleteCameraTypeWarningModalView(rootPane, camera));
         try {
            WhiteboxImpl.invokeMethod(projectController, "confirmTypeDelete", event, camera, 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       });
    }
    
    @Override
    public void start(Stage arg0) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    public void uploadToWebserverTest() {
        when(project.getFilePath()).thenReturn(null);
        Mockito.doNothing().when(projectController).saveAs();
        when(project.getFilePath()).thenReturn("src/test/files/upload_test4.scp");
        Mockito.doNothing().when(projectController).showErrorModal();

        projectController.uploadToWebserver();

        Mockito.verify(projectController).save();
        Mockito.verify(projectController).showErrorModal();
    }

    @Test
    public void testShowSuccessModal() throws InterruptedException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            projectController.showSuccessModal();
            latch[0].countDown();
        });
        latch[0].await();
    }

    @Test
    public void testSuccessModalClose() throws InterruptedException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);
        Platform.runLater(() -> {
            projectController.showSuccessModal();
            try {
                WhiteboxImpl.invokeMethod(projectController, "successModalClose", mouseEvent);
            } catch (Exception e) {
            }
            latch[0].countDown();
        });
        latch[0].await();
    }

    @Test
    public void testGoToWebsite() throws InterruptedException, IOException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            projectController.showSuccessModal();
            latch[0].countDown();
        });
        latch[0].await();
        projectController.getSuccessModal().getGoToWebsiteButton().fire();
    }

    @Test
    public void testShowErrorModal() throws InterruptedException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        Platform.runLater(() -> {
            projectController.showErrorModal();
            latch[0].countDown();
        });
        latch[0].await();
    }

    @Test
    public void testErrorModalOk() throws InterruptedException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);
        Platform.runLater(() -> {
            projectController.showErrorModal();
            try {
                WhiteboxImpl.invokeMethod(projectController, "errorModalOk", mouseEvent);
            } catch (Exception e) {
            }
            latch[0].countDown();
        });
    }

    @Test
    public void applyEditTest() throws InterruptedException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);

        Platform.runLater(() -> {
            projectController.getControllerManager().getScriptingProject().addCamera(
                    new Camera("a", "b", new CameraType("a", "a", 1.0))
            );

            projectController.newProject();

            Camera camera = new Camera("a", "b", new CameraType("a", "b", 1.0));

            projectController.getEditProjectModal().getProject().getCameras().add(camera);

            ScriptingProject project = new ScriptingProject("a", "b", 1.0);
            project.getCameras().add(camera);
            project.getCameraTimelines().add(new CameraTimeline(project.getCameras().get(0), project));


            when(controllerManager.getScriptingProject()).thenReturn(project);

            projectController.getEditProjectModal().setProject(project);
            projectController.getEditProjectModal().getNameField().setText("a");
            projectController.getEditProjectModal().getDescriptionField().setText("a");
            projectController.getEditProjectModal().getSecondsPerCountField().setText("1.0");
            projectController.getEditProjectModal().getDirectorTimelineDescriptionField().setText("a");
            projectController.getEditProjectModal().getCameras().add(camera);
            projectController.getEditProjectModal().getTimelines().add(
                    new CameraTimeline(projectController.getEditProjectModal().getCameras().get(0), project)
            );

            try {
                WhiteboxImpl.invokeMethod(projectController, "applyEdit", mouseEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch[0].countDown();
        });
        latch[0].await();

        Mockito.verify(projectController).newProject();
    }

    @Test
    public void applyNewTest() throws InterruptedException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);

        Platform.runLater(() -> {
            projectController.getControllerManager().getScriptingProject().addCamera(
                    new Camera("a", "b", new CameraType("a", "a", 1.0))
            );

            projectController.newProject();

            Camera camera = new Camera("a", "b", new CameraType("a", "b", 1.0));

            projectController.getEditProjectModal().getProject().getCameras().add(camera);

            ScriptingProject project = new ScriptingProject("a", "b", 1.0);
            project.getCameras().add(camera);
            project.getCameraTimelines().add(new CameraTimeline(project.getCameras().get(0), project));


            when(controllerManager.getScriptingProject()).thenReturn(project);

            projectController.getEditProjectModal().setProject(project);
            projectController.getEditProjectModal().getNameField().setText("a");
            projectController.getEditProjectModal().getDescriptionField().setText("a");
            projectController.getEditProjectModal().getSecondsPerCountField().setText("1.0");
            projectController.getEditProjectModal().getDirectorTimelineDescriptionField().setText("a");
            projectController.getEditProjectModal().getCameras().add(camera);
            projectController.getEditProjectModal().getTimelines().add(
                    new CameraTimeline(projectController.getEditProjectModal().getCameras().get(0), project)
            );

            try {
                WhiteboxImpl.invokeMethod(projectController, "applyNew", mouseEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch[0].countDown();
        });
        latch[0].await();

        Mockito.verify(projectController, times(1)).newProject();
    }

    @Test
    public void addCameraTypeCorrectTest() throws InterruptedException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);
        MouseEvent mouseEvent2 = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);
        Platform.runLater(() -> {
            try {
                projectController.getControllerManager().getScriptingProject().addCamera(
                        new Camera("a", "b", new CameraType("a", "a", 1.0))
                );
                projectController.newProject();
                WhiteboxImpl.invokeMethod(projectController, "addCameraType", mouseEvent);
                projectController.getCameraTypeModal().getNameField().setText("a");
                projectController.getCameraTypeModal().getDescriptionField().setText("b");
                projectController.getCameraTypeModal().getMovementMarginField().setText("1.0");
                WhiteboxImpl.invokeMethod(projectController, "typeAdded", mouseEvent2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch[0].countDown();
        });
        latch[0].await();

        verify(projectController, atLeastOnce()).getCameraTypeModal();
    }

    @Test
    public void addCameraTypeIncorrectTest() throws InterruptedException {
        final CountDownLatch[] latch = {new CountDownLatch(1)};
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);
        MouseEvent mouseEvent2 = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);
        MouseEvent mouseEvent3 = new MouseEvent(MouseEvent.ANY, 2, 3, 4, 5, MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null);
        Platform.runLater(() -> {
            try {
                projectController.getControllerManager().getScriptingProject().addCamera(
                        new Camera("a", "b", new CameraType("a", "a", 1.0))
                );
                projectController.newProject();
                WhiteboxImpl.invokeMethod(projectController, "addCameraType", mouseEvent);
                WhiteboxImpl.invokeMethod(projectController, "typeAdded", mouseEvent2);
                WhiteboxImpl.invokeMethod(projectController, "cancelAddCameraType", mouseEvent3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch[0].countDown();
        });
        latch[0].await();

        verify(projectController, never()).getCameraTypeModal();
    }

    @Test
    public void addInstrumentCorrectTest() throws InterruptedException {

    }

}
