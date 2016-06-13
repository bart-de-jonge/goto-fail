package control;

import data.CameraShot;
import data.DirectorShot;
import data.Instrument;
import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.centerarea.DirectorShotBlock;
import gui.centerarea.TimetableBlock;
import gui.headerarea.DirectorDetailView;
import gui.headerarea.DoubleTextField;
import gui.root.RootHeaderArea;
import gui.root.RootPane;
import gui.styling.StyledMenuButton;
import gui.styling.StyledTextfield;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Bart on 12/05/2016.
 */
public class DetailViewControllerTest extends ApplicationTest {

    private ControllerManager manager;
    private DirectorDetailView detailView;
    private DetailViewController detailViewController;
    private DoubleTextField beginCountField, endCountField, paddingBeforeField, paddingAfterField;

    private Pane pane;

    @Before
    public void initialize() {
        manager = Mockito.mock(ControllerManager.class);
        RootPane rootPane = Mockito.mock(RootPane.class);
        RootHeaderArea rootHeaderArea = Mockito.mock(RootHeaderArea.class);

        detailView = Mockito.mock(DirectorDetailView.class);
        when(manager.getRootPane()).thenReturn(rootPane);
        when(rootPane.getRootHeaderArea()).thenReturn(rootHeaderArea);
        when(rootHeaderArea.getDetailView()).thenReturn(detailView);

        // Add necessary fields
        beginCountField = new DoubleTextField("0");
        endCountField = new DoubleTextField("0");
        paddingBeforeField = new DoubleTextField("0");
        paddingAfterField = new DoubleTextField("0");

        when(detailView.getDescriptionField()).thenReturn(new StyledTextfield());
        when(detailView.getNameField()).thenReturn(new StyledTextfield());
        when(detailView.getBeginCountField()).thenReturn(beginCountField);
        when(detailView.getEndCountField()).thenReturn(endCountField);
        when(detailView.getPaddingBeforeField()).thenReturn(paddingBeforeField);
        when(detailView.getPaddingAfterField()).thenReturn(paddingAfterField);
        when(detailView.getEndCountField()).thenReturn(endCountField);
        when(detailView.getEndCountField()).thenReturn(endCountField);
        when(detailView.getInstrumentsDropdown()).thenReturn(new CheckComboBox<>());
        when(detailView.getSelectCamerasButton()).thenReturn(new StyledMenuButton());

        detailViewController = spy(new DetailViewController(manager));
    }

    @Test
    public void initInstrumentsDropdown() {
        CameraShotBlock cameraShotBlock = Mockito.mock(CameraShotBlock.class);
        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        ArrayList<Instrument> instruments = new ArrayList<>();
        instruments.add(new Instrument("name", "description"));
        when(cameraShotBlock.getInstruments()).thenReturn(instruments);
        when(manager.getScriptingProject()).thenReturn(project);

        detailViewController.initInstrumentsDropdown(cameraShotBlock);
        Mockito.verify(detailView, times(2)).getInstrumentsDropdown();
        Mockito.verify(detailView, times(1)).setInstruments(anyObject());
    }

    @Test
    public void instrumentsDropdownChangeListenerRemoved() {
        // Setup mocks
        ListChangeListener.Change change = Mockito.mock(ListChangeListener.Change.class);
        DirectorShot shot = Mockito.mock(DirectorShot.class);
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        ArrayList<Integer> changeList = new ArrayList<>(Arrays.asList(0));
        ArrayList<Instrument> instrumentList1 = new ArrayList<>(Arrays.asList(new Instrument("name", "description")));
        ArrayList<Instrument> instrumentList2 = new ArrayList<>(Arrays.asList(new Instrument("name", "description")));
        ArrayList<Instrument> instrumentList3 = new ArrayList<>(Arrays.asList(new Instrument("name", "description")));
        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        TimetableBlock timetableBlock = Mockito.mock(TimetableBlock.class);

        // Mock all the methods
        when(manager.getActiveShotBlock()).thenReturn(shotBlock);
        when(project.getInstruments()).thenReturn(instrumentList1);
        when(manager.getScriptingProject()).thenReturn(project);
        when(shotBlock.getInstruments()).thenReturn(instrumentList2);
        when(shotBlock.getShot()).thenReturn(shot);
        when(shot.getInstruments()).thenReturn(instrumentList3);
        when(shotBlock.getTimetableBlock()).thenReturn(timetableBlock);
        when(change.getRemoved()).thenReturn(changeList);

        // Call method under testing
        detailViewController.instrumentsDropdownChangeListener(change);

        // verify
        assertFalse(instrumentList1.isEmpty());
        assertTrue(instrumentList2.isEmpty());
        assertTrue(instrumentList3.isEmpty());
        Mockito.verify(shotBlock, times(1)).recompute();
        Mockito.verify(timetableBlock, times(1)).removeInstrument(anyObject());
    }

    @Test
    public void instrumentsDropdownChangeListenerAdded() {
        // Setup mocks
        ListChangeListener.Change change = Mockito.mock(ListChangeListener.Change.class);
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        ArrayList<Integer> changeList = new ArrayList<>(Arrays.asList(0));
        ArrayList<Instrument> instrumentList1 = new ArrayList<>();
        ArrayList<Instrument> instrumentList2 = new ArrayList<>(Arrays.asList(new Instrument("name", "description")));
        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        TimetableBlock timetableBlock = Mockito.mock(TimetableBlock.class);

        // Mock all the methods
        when(manager.getActiveShotBlock()).thenReturn(shotBlock);
        when(project.getInstruments()).thenReturn(instrumentList2);
        when(manager.getScriptingProject()).thenReturn(project);
        when(shotBlock.getInstruments()).thenReturn(instrumentList1);
        when(shotBlock.getTimetableBlock()).thenReturn(timetableBlock);
        when(change.getAddedSubList()).thenReturn(changeList);
        when(change.wasAdded()).thenReturn(true);

        // Call method under testing
        detailViewController.instrumentsDropdownChangeListener(change);

        // verify
        assertEquals(1, instrumentList1.size());
        assertEquals(1, instrumentList1.size());
        Mockito.verify(shotBlock, times(1)).recompute();
        Mockito.verify(timetableBlock, times(1)).addInstrument(anyObject());
    }

    private void setupPaddingUpdateHelperTests(DirectorShot shot, DirectorShotBlock shotBlock, TimelineController timelineController) {
        // Make mocks
        CameraShot cameraShot = Mockito.mock(CameraShot.class);
        CameraShotBlock cameraShotBlock = Mockito.mock(CameraShotBlock.class);

        // Mock all the methods
        when(shotBlock.getShot()).thenReturn(shot);
        when(manager.getActiveShotBlock()).thenReturn(shotBlock);
        when(manager.getTimelineControl()).thenReturn(timelineController);
        when(shot.getCameraShots()).thenReturn(new HashSet<CameraShot>(Arrays.asList(cameraShot)));
        when(shot.getBeginCount()).thenReturn(0.0);
        when(timelineController.getShotBlockForShot(anyObject())).thenReturn(cameraShotBlock);
    }

    @Test
    public void beforePaddingUpdateHelper() {
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        DirectorShot shot = Mockito.mock(DirectorShot.class);
        TimelineController timelineController = Mockito.mock(TimelineController.class);
        setupPaddingUpdateHelperTests(shot, shotBlock, timelineController);

        // Call method under testing
        detailViewController.beforePaddingUpdateHelper();

        // Verify
        assertEquals(0.0, shot.getFrontShotPadding(), 0);
        assertEquals(0.0, shotBlock.getPaddingBefore(), 0);
        Mockito.verify(timelineController, times(1)).recomputeAllCollisions();
    }

    @Test
    public void afterPaddingUpdateHelper() {
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        DirectorShot shot = Mockito.mock(DirectorShot.class);
        TimelineController timelineController = Mockito.mock(TimelineController.class);
        setupPaddingUpdateHelperTests(shot, shotBlock, timelineController);

        // Call method under testing
        detailViewController.afterPaddingUpdateHelper();

        // Verify
        assertEquals(0.0, shot.getEndShotPadding(), 0);
        assertEquals(0.0, shotBlock.getPaddingAfter(), 0);
        Mockito.verify(timelineController, times(1)).recomputeAllCollisions();
    }

    @Test
    public void reInitForCameraBlock() {
        detailViewController.reInitForCameraBlock();

        Mockito.verify(detailView, times(2)).getNameField();
        Mockito.verify(detailView, times(2)).getDescriptionField();
        Mockito.verify(detailView, times(4)).getBeginCountField();
        Mockito.verify(detailView, times(4)).getEndCountField();
    }

    @Test
    public void reInitForDirectorBlock() {
        detailViewController.reInitForDirectorBlock();

        Mockito.verify(detailView, times(2)).getPaddingBeforeField();
        Mockito.verify(detailView, times(2)).getPaddingAfterField();
    }

    @Test
    public void constructor() {
        assertNotNull(detailViewController);
    }

    @Test
    public void beginCountUpdateHelper() {
        CameraShotBlock block = Mockito.mock(CameraShotBlock.class);
        CameraShot shot = Mockito.mock(CameraShot.class);
        when(manager.getActiveShotBlock()).thenReturn(block);
        when(block.getShot()).thenReturn(shot);

        KeyEvent keyEvent = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.ENTER,
                false, false, false, false);
        detailView.getBeginCountField().getOnKeyPressed().handle(keyEvent);

        verify(block, times(1)).setBeginCount(0);
        verify(shot, times(1)).setBeginCount(0);
    }

    @Test
    public void endCountUpdateHelper() {
        CameraShotBlock block = Mockito.mock(CameraShotBlock.class);
        CameraShot shot = Mockito.mock(CameraShot.class);
        when(manager.getActiveShotBlock()).thenReturn(block);
        when(block.getShot()).thenReturn(shot);

        KeyEvent keyEvent = new KeyEvent(KeyEvent.ANY, "", "", KeyCode.ENTER,
                false, false, false, false);
        detailView.getEndCountField().getOnKeyPressed().handle(keyEvent);

        verify(block, times(1)).setEndCount(0);
        verify(shot, times(1)).setEndCount(0);
    }


    @Test
    public void activeBlockChangedActiveBlockNull() {
        detailViewController.activeBlockChanged();
        verify(detailView, times(1)).resetDetails();
    }

    @Test
    public void beginCountFocusedProperty() {
        CameraShotBlock block = Mockito.mock(CameraShotBlock.class);
        CameraShot shot = Mockito.mock(CameraShot.class);
        when(manager.getActiveShotBlock()).thenReturn(block);
        when(block.getShot()).thenReturn(shot);

        detailViewController.beginCountFocusListener(new ObservableValue<Boolean>() {
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

            @Override
            public void addListener(InvalidationListener listener) {
            }

            @Override
            public void removeListener(InvalidationListener listener) {
            }
        }, true, false);

        verify(block, times(1)).setBeginCount(0);
        verify(shot, times(1)).setBeginCount(0);
    }

    @Test
    public void endCountFocusedProperty() {
        CameraShotBlock block = Mockito.mock(CameraShotBlock.class);
        CameraShot shot = Mockito.mock(CameraShot.class);
        when(manager.getActiveShotBlock()).thenReturn(block);
        when(block.getShot()).thenReturn(shot);

        detailViewController.endCountFocusListener(new ObservableValue<Boolean>() {
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

            @Override
            public void addListener(InvalidationListener listener) {
            }

            @Override
            public void removeListener(InvalidationListener listener) {
            }
        }, true, false);

        verify(block, times(1)).setEndCount(0);
        verify(shot, times(1)).setEndCount(0);
    }

    @Test
    public void nameFieldTextProperty() {
        CameraShotBlock block = Mockito.mock(CameraShotBlock.class);
        CameraShot shot = Mockito.mock(CameraShot.class);
        when(manager.getActiveShotBlock()).thenReturn(block);
        when(block.getShot()).thenReturn(shot);

        detailViewController.nameTextChangedListener(new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {
            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {
            }

            @Override
            public String getValue() {
                return null;
            }

            @Override
            public void addListener(InvalidationListener listener) {
            }

            @Override
            public void removeListener(InvalidationListener listener) {
            }
        }, "", "test newvalue");

        verify(block, times(1)).setName("test newvalue");
        verify(shot, times(1)).setName("test newvalue");
    }

    @Test
    public void descriptionFieldTextProperty() {
        CameraShotBlock block = Mockito.mock(CameraShotBlock.class);
        CameraShot shot = Mockito.mock(CameraShot.class);
        when(manager.getActiveShotBlock()).thenReturn(block);
        when(block.getShot()).thenReturn(shot);

        detailViewController.descriptionTextChangedListener(new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {
            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {
            }

            @Override
            public String getValue() {
                return null;
            }

            @Override
            public void addListener(InvalidationListener listener) {
            }

            @Override
            public void removeListener(InvalidationListener listener) {
            }
        }, "", "test newvalue");

        verify(block, times(1)).setDescription("test newvalue");
        verify(shot, times(1)).setDescription("test newvalue");
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}