package control;

import data.*;
import gui.centerarea.CameraShotBlock;
import gui.centerarea.DirectorShotBlock;
import gui.centerarea.TimetableBlock;
import gui.headerarea.DirectorDetailView;
import gui.headerarea.DoubleTextField;
import gui.root.RootHeaderArea;
import gui.root.RootPane;
import gui.styling.StyledCheckbox;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

import java.util.*;

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
        when(detailView.getSelectCamerasButton()).thenReturn(new StyledMenuButton());

        detailViewController = spy(new DetailViewController(manager));
    }

    @Test
    public void camerasDropdownChangeListenerRemoved() {
        // Setup mocks
        ListChangeListener.Change change = Mockito.mock(ListChangeListener.Change.class);
        DirectorShot shot = Mockito.mock(DirectorShot.class);
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        ArrayList<Integer> changeList = new ArrayList<>(Arrays.asList(0));
        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        TimetableBlock timetableBlock = Mockito.mock(TimetableBlock.class);
        TimelineController timelineController = Mockito.mock(TimelineController.class);
        CameraShot cameraShot1 = Mockito.mock(CameraShot.class);
        CameraShot cameraShot2 = Mockito.mock(CameraShot.class);
        Set<CameraShot> shotList = new HashSet<>(Arrays.asList(cameraShot1, cameraShot2));
        CameraShotBlock cameraShotBlock1 = Mockito.mock(CameraShotBlock.class);
        CameraShotBlock cameraShotBlock2 = Mockito.mock(CameraShotBlock.class);
        DirectorTimelineController directorTimelineController = Mockito.mock(DirectorTimelineController.class);

        // Mock all the methods
        when(manager.getActiveShotBlock()).thenReturn(shotBlock);
        when(manager.getScriptingProject()).thenReturn(project);
        when(shotBlock.getShot()).thenReturn(shot);
        when(shotBlock.getTimetableBlock()).thenReturn(timetableBlock);
        when(change.getRemoved()).thenReturn(changeList);
        when(manager.getTimelineControl()).thenReturn(timelineController);
        when(manager.getDirectorTimelineControl()).thenReturn(directorTimelineController);
        when(shot.getCameraShots()).thenReturn(shotList);
        when(timelineController.getShotBlockForShot(cameraShot1)).thenReturn(cameraShotBlock1);
        when(timelineController.getShotBlockForShot(cameraShot2)).thenReturn(cameraShotBlock2);
        when(cameraShotBlock1.getTimetableNumber()).thenReturn(100);

        // Call method under testing
        detailViewController.camerasDropdownChangeListener(change);

        // verify
        Mockito.verify(shot, times(1)).getTimelineIndices();
        Mockito.verify(shot, times(2)).getCameraShots();
        assertEquals(1, shotList.size());
        assertTrue(shotList.contains(cameraShot1));
        assertFalse(shotList.contains(cameraShot2));
    }

    @Test
    public void camerasDropdownChangeListenerRemoved2() {
        // Setup mocks
        ListChangeListener.Change change = Mockito.mock(ListChangeListener.Change.class);
        DirectorShot shot = Mockito.mock(DirectorShot.class);
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        ArrayList<Integer> changeList = new ArrayList<>(Arrays.asList(5));
        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        TimetableBlock timetableBlock = Mockito.mock(TimetableBlock.class);
        TimelineController timelineController = Mockito.mock(TimelineController.class);
        CameraShot cameraShot1 = Mockito.mock(CameraShot.class);
        Set<CameraShot> shotList = new HashSet<>(Arrays.asList(cameraShot1));
        CameraShotBlock cameraShotBlock1 = Mockito.mock(CameraShotBlock.class);
        DirectorTimelineController directorTimelineController = Mockito.mock(DirectorTimelineController.class);

        // Mock all the methods
        when(manager.getActiveShotBlock()).thenReturn(shotBlock);
        when(manager.getScriptingProject()).thenReturn(project);
        when(shotBlock.getShot()).thenReturn(shot);
        when(shotBlock.getTimetableBlock()).thenReturn(timetableBlock);
        when(change.getRemoved()).thenReturn(changeList);
        when(manager.getTimelineControl()).thenReturn(timelineController);
        when(manager.getDirectorTimelineControl()).thenReturn(directorTimelineController);
        when(shot.getCameraShots()).thenReturn(shotList);
        when(timelineController.getShotBlockForShot(cameraShot1)).thenReturn(cameraShotBlock1);
        when(cameraShotBlock1.getTimetableNumber()).thenReturn(100);

        // Call method under testing
        detailViewController.camerasDropdownChangeListener(change);

        // verify
        Mockito.verify(shot, times(1)).getTimelineIndices();
        Mockito.verify(shot, times(2)).getCameraShots();
        assertEquals(1, shotList.size());
        assertTrue(shotList.contains(cameraShot1));
    }

    @Test
    public void camerasDropdownChangeListenerAdded() {
        // Setup mocks
        ListChangeListener.Change change = Mockito.mock(ListChangeListener.Change.class);
        DirectorShot shot = Mockito.mock(DirectorShot.class);
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        ArrayList<Integer> changeList = new ArrayList<>(Arrays.asList(0));
        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        TimetableBlock timetableBlock = Mockito.mock(TimetableBlock.class);
        TimelineController timelineController = Mockito.mock(TimelineController.class);
        ArrayList<CameraTimeline> timelines = new ArrayList<>();
        CameraTimeline timeline = Mockito.mock(CameraTimeline.class);
        timelines.add(timeline);
        DirectorTimelineController directorTimelineController = Mockito.mock(DirectorTimelineController.class);

        // Mock all the methods
        when(manager.getActiveShotBlock()).thenReturn(shotBlock);
        when(manager.getScriptingProject()).thenReturn(project);
        when(shotBlock.getShot()).thenReturn(shot);
        when(shotBlock.getTimetableBlock()).thenReturn(timetableBlock);
        when(change.getAddedSubList()).thenReturn(changeList);
        when(manager.getTimelineControl()).thenReturn(timelineController);
        when(manager.getDirectorTimelineControl()).thenReturn(directorTimelineController);
        when(change.wasAdded()).thenReturn(true);
        when(project.getCameraTimelines()).thenReturn(timelines);

        // Call method under testing
        detailViewController.camerasDropdownChangeListener(change);

        // verify
        Mockito.verify(timeline, times(1)).addShot(anyObject());
        Mockito.verify(manager, times(1)).setActiveShotBlock(shotBlock);
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
        detailViewController.beforePaddingFocusListener(null, true, false);

        // Verify
        assertEquals(0.0, shot.getFrontShotPadding(), 0);
        assertEquals(0.0, shotBlock.getPaddingBefore(), 0);
        Mockito.verify(timelineController, times(1)).recomputeAllCollisions();
    }

    @Test
    public void beforePaddingUpdateHelperFalse() {
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        DirectorShot shot = Mockito.mock(DirectorShot.class);
        TimelineController timelineController = Mockito.mock(TimelineController.class);
        setupPaddingUpdateHelperTests(shot, shotBlock, timelineController);

        // Call method under testing
        detailViewController.beforePaddingFocusListener(null, false, true);

        // Verify
        assertEquals(0.0, shot.getFrontShotPadding(), 0);
        assertEquals(0.0, shotBlock.getPaddingBefore(), 0);
        Mockito.verify(timelineController, times(0)).recomputeAllCollisions();
    }

    @Test
    public void beforePaddingUpdateHelperNull() {
        TimelineController timelineController = Mockito.mock(TimelineController.class);

        // Call method under testing
        detailViewController.beforePaddingFocusListener(null, true, false);

        // Verify
        Mockito.verify(timelineController, times(0)).recomputeAllCollisions();
    }

    @Test
    public void afterPaddingFocusListener() {
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        DirectorShot shot = Mockito.mock(DirectorShot.class);
        TimelineController timelineController = Mockito.mock(TimelineController.class);
        setupPaddingUpdateHelperTests(shot, shotBlock, timelineController);

        // Call method under testing
        detailViewController.afterPaddingFocusListener(null, true, false);

        // Verify
        assertEquals(0.0, shot.getEndShotPadding(), 0);
        assertEquals(0.0, shotBlock.getPaddingAfter(), 0);
        Mockito.verify(timelineController, times(1)).recomputeAllCollisions();
    }

    @Test
    public void afterPaddingFocusListenerFalse() {
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        DirectorShot shot = Mockito.mock(DirectorShot.class);
        TimelineController timelineController = Mockito.mock(TimelineController.class);
        setupPaddingUpdateHelperTests(shot, shotBlock, timelineController);

        // Call method under testing
        detailViewController.afterPaddingFocusListener(null, false, true);

        // Verify
        Mockito.verify(timelineController, times(0)).recomputeAllCollisions();
    }

    @Test
    public void afterPaddingFocusListenerNull() {
        TimelineController timelineController = Mockito.mock(TimelineController.class);

        // Call method under testing
        detailViewController.afterPaddingFocusListener(null, true, false);

        // Verify
        Mockito.verify(timelineController, times(0)).recomputeAllCollisions();
    }

    @Test
    public void reInitForCameraBlock() {
        StyledMenuButton menuButton = new StyledMenuButton();
        when(detailView.getSelectInstrumentsButton()).thenReturn(menuButton);

        detailViewController.reInitForCameraBlock();

        Mockito.verify(detailView, times(2)).getNameField();
        Mockito.verify(detailView, times(2)).getDescriptionField();
        Mockito.verify(detailView, times(4)).getBeginCountField();
        Mockito.verify(detailView, times(4)).getEndCountField();
    }

    @Test
    public void reInitForDirectorBlock() {
        StyledMenuButton menuButton = new StyledMenuButton();
        when(detailView.getSelectInstrumentsButton()).thenReturn(menuButton);
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
    public void beginCountFocusedPropertyNull() {
        CameraShotBlock block = Mockito.mock(CameraShotBlock.class);
        CameraShot shot = Mockito.mock(CameraShot.class);
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

        verify(block, times(0)).setBeginCount(0);
        verify(shot, times(0)).setBeginCount(0);
    }

    @Test
    public void beginCountFocusedPropertyFalse() {
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
        }, false, true);

        verify(block, times(0)).setBeginCount(0);
        verify(shot, times(0)).setBeginCount(0);
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
    public void endCountFocusedPropertyNull() {
        CameraShotBlock block = Mockito.mock(CameraShotBlock.class);
        CameraShot shot = Mockito.mock(CameraShot.class);
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

        verify(block, times(0)).setEndCount(0);
        verify(shot, times(0)).setEndCount(0);
    }

    @Test
    public void endCountFocusedPropertyFalse() {
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
        }, false, true);

        verify(block, times(0)).setEndCount(0);
        verify(shot, times(0)).setEndCount(0);
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
    public void nameFieldTextPropertyNull() {
        CameraShotBlock block = Mockito.mock(CameraShotBlock.class);
        CameraShot shot = Mockito.mock(CameraShot.class);
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

        verify(block, times(0)).setName("test newvalue");
        verify(shot, times(0)).setName("test newvalue");
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

    @Test
    public void descriptionFieldTextPropertyNull() {
        CameraShotBlock block = Mockito.mock(CameraShotBlock.class);
        CameraShot shot = Mockito.mock(CameraShot.class);
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

        verify(block, times(0)).setDescription("test newvalue");
        verify(shot, times(0)).setDescription("test newvalue");
    }

    @Test
    public void activeBlockChangedCamera() {
        // Setup mocks
        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        RootPane rootPane = Mockito.mock(RootPane.class);
        RootHeaderArea rootHeaderArea = Mockito.mock(RootHeaderArea.class);

        // Mock all the methods
        when(manager.getActiveShotBlock()).thenReturn(shotBlock);
        when(manager.getRootPane()).thenReturn(rootPane);
        when(rootPane.getRootHeaderArea()).thenReturn(rootHeaderArea);

        // Call method under testing
        detailViewController.activeBlockChanged();

        // Verify all the things
        Mockito.verify(detailViewController, times(1)).reInitForCameraBlock();
        Mockito.verify(rootHeaderArea, times(1)).reInitHeaderBar(anyObject());
    }

    @Test
    public void activeBlockChangedDirector() {
        // Setup mocks
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        RootPane rootPane = Mockito.mock(RootPane.class);
        RootHeaderArea rootHeaderArea = Mockito.mock(RootHeaderArea.class);

        // Mock all the methods
        when(manager.getActiveShotBlock()).thenReturn(shotBlock);
        when(manager.getRootPane()).thenReturn(rootPane);
        when(rootPane.getRootHeaderArea()).thenReturn(rootHeaderArea);

        // Call method under testing
        detailViewController.activeBlockChanged();

        // Verify all the things
        Mockito.verify(detailViewController, times(1)).reInitForDirectorBlock();
        Mockito.verify(rootHeaderArea, times(1)).reInitHeaderBar(anyObject());
    }

    @Test
    public void instrumentsDropdownListener() {
        // Setup mocks
        StyledMenuButton menuButton = new StyledMenuButton();
        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        Instrument instrument = Mockito.mock(Instrument.class);
        ArrayList<Instrument> instrumentList = new ArrayList<>(Arrays.asList(instrument));
        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        List<StyledCheckbox> list = Mockito.mock(List.class);
        StyledCheckbox checkbox = Mockito.mock(StyledCheckbox.class);

        // Mock all the methods
        when(shotBlock.getInstruments()).thenReturn(instrumentList);
        detailViewController.setActiveCameraBlock(shotBlock);
        detailViewController.setActiveInstrumentBoxes(list);
        when(manager.getScriptingProject()).thenReturn(project);
        when(project.getInstruments()).thenReturn(instrumentList);
        doReturn(checkbox).when(detailViewController).getStyledCheckbox(anyString(), anyBoolean());

        // Call method under testing
        detailViewController.instrumentsDropdownListener(null, false, true, menuButton);

        // Verify all the things
        assertEquals(1, menuButton.getItems().size());
        Mockito.verify(list, times(1)).add(checkbox);
    }

    @Test
    public void instrumentsDropdownListenerFalse() {
        // Setup mocks
        StyledMenuButton menuButton = new StyledMenuButton();
        List<StyledCheckbox> list = Mockito.mock(List.class);

        // Mock all the methods
        detailViewController.setActiveInstrumentBoxes(list);

        // Call method under testing
        detailViewController.instrumentsDropdownListener(null, true, false, menuButton);

        // Verify all the things
        Mockito.verify(list, times(1)).clear();
    }

    @Test
    public void cameraDropdownListenerFalse() {
        // Setup mocks
        StyledMenuButton menuButton = new StyledMenuButton();
        List<StyledCheckbox> list = Mockito.mock(List.class);

        // Mock all the methods
        detailViewController.setActiveCameraBoxes(list);

        // Call method under testing
        detailViewController.cameraDropdownListener(null, true, false, menuButton);

        // Verify all the things
        Mockito.verify(list, times(1)).clear();
    }

    @Test
    public void camerasDropdownListener() {
        // Setup mocks
        StyledMenuButton menuButton = new StyledMenuButton();
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        Camera camera = Mockito.mock(Camera.class);
        ArrayList<Camera> cameraList = new ArrayList<>(Arrays.asList(camera));
        ScriptingProject project = Mockito.mock(ScriptingProject.class);
        List<StyledCheckbox> list = Mockito.mock(List.class);
        StyledCheckbox checkbox = Mockito.mock(StyledCheckbox.class);

        // Mock all the methods
        detailViewController.setActiveDirectorBlock(shotBlock);
        detailViewController.setActiveCameraBoxes(list);
        when(manager.getScriptingProject()).thenReturn(project);
        when(project.getCameras()).thenReturn(cameraList);
        doReturn(checkbox).when(detailViewController).getStyledCheckbox(anyString(), anyBoolean());

        // Call method under testing
        detailViewController.cameraDropdownListener(null, false, true, menuButton);

        // Verify all the things
        assertEquals(1, menuButton.getItems().size());
        Mockito.verify(list, times(1)).add(checkbox);
    }

    @Test
    public void getDetailView() {
        assertEquals(detailView, detailViewController.getDetailView());
    }

    @Test
    public void getActiveDirectorBlock() {
        DirectorShotBlock shotBlock = Mockito.mock(DirectorShotBlock.class);
        detailViewController.setActiveDirectorBlock(shotBlock);
        assertEquals(shotBlock, detailViewController.getActiveDirectorBlock());
    }

    @Test
    public void getActiveCameraBlock() {
        CameraShotBlock shotBlock = Mockito.mock(CameraShotBlock.class);
        detailViewController.setActiveCameraBlock(shotBlock);
        assertEquals(shotBlock, detailViewController.getActiveCameraBlock());
    }

    @Test
    public void getActiveCameraBoxes() {
        List<StyledCheckbox> list = Mockito.mock(List.class);
        detailViewController.setActiveCameraBoxes(list);
        assertEquals(list, detailViewController.getActiveCameraBoxes());
    }

    @Test
    public void getActiveInstrumentBoxes() {
        List<StyledCheckbox> list = Mockito.mock(List.class);
        detailViewController.setActiveInstrumentBoxes(list);
        assertEquals(list, detailViewController.getActiveInstrumentBoxes());
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}