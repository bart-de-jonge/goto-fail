package control;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.testfx.framework.junit.ApplicationTest;

import data.Camera;
import data.CameraTimeline;
import data.DirectorTimeline;
import data.ScriptingProject;
import gui.modal.DeleteCameraTypeWarningModalView;
import gui.modal.EditProjectModalView;
import gui.modal.StartupModalView;
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
    
    @Test
    public void saveTestWithExistingFilePath() {
        when(project.getFilePath()).thenReturn("Kek");
       // Mockito.doNothing().when(project).write();
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
        DirectorTimeline directorTimelineMock = mock(DirectorTimeline.class);
        when(listMock.size()).thenReturn(3);
        when(project.getCameraTimelines()).thenReturn(listMock);
        when(project.getDirectorTimeline()).thenReturn(directorTimelineMock);
        File file = new File("src/test/files/general_test3.scp");
        TimelineController timelineController = mock(TimelineController.class);
        when(controllerManager.getTimelineControl()).thenReturn(timelineController);
        Mockito.doNothing().when(rootPane).reInitRootCenterArea(Mockito.any());
        CameraTimeline timelineMock = mock(CameraTimeline.class);
        when(listMock.get(Mockito.anyInt())).thenReturn(timelineMock);
        
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

}
