package control;

import data.Camera;
import data.CameraShot;
import data.DirectorShot;
import data.Instrument;
import data.Shot;
import gui.centerarea.CameraShotBlock;
import gui.centerarea.DirectorShotBlock;
import gui.centerarea.ShotBlock;
import gui.events.CameraShotBlockUpdatedEvent;
import gui.headerarea.DetailView;
import gui.headerarea.DirectorDetailView;
import gui.misc.TweakingHelper;
import gui.styling.StyledCheckbox;
import gui.styling.StyledMenuButton;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Controller for the DetailView.
 */
@Log4j2
public class DetailViewController {

    @Getter
    private DetailView detailView;
    private ControllerManager manager;

    @Getter @Setter
    private DirectorShotBlock activeDirectorBlock;
    @Getter @Setter
    private CameraShotBlock activeCameraBlock;
    @Getter @Setter
    private List<StyledCheckbox> activeCameraBoxes;
    @Getter @Setter
    private List<StyledCheckbox> activeInstrumentBoxes;

    /**
     * Constructor.
     *
     * @param manager - the controller manager this controller belongs to
     */
    public DetailViewController(ControllerManager manager) {
        this.detailView = manager.getRootPane().getRootHeaderArea().getDetailView();
        this.manager = manager;
        initDescription();
        initName();
        initBeginCount();
        initEndCount();
    }

    /**
     * Re-init the tool view for when a camera block is selected.
     */
    public void reInitForCameraBlock() {
        initDescription();
        initName();
        initBeginCount();
        initEndCount();
        initCameraInstrumentDropdown();
    }

    /**
     * Re-init the tool view for when a director block is selected.
     */
    public void reInitForDirectorBlock() {
        reInitForCameraBlock();
        initBeginPadding();
        initEndPadding();
        initDirectorCameraDropdown();
    }

    /**
     * Initialize the handlers for the begin padding field.
     */
    private void initBeginPadding() {
        ((DirectorDetailView) detailView).getPaddingBeforeField()
                .focusedProperty().addListener(this::beforePaddingFocusListener);

        ((DirectorDetailView) detailView).getPaddingBeforeField().setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    this.beforePaddingUpdateHelper();
                }
            });
    }

    /**
     * Listener for focus change on before padding field.
     *
     * @param observable the observable value
     * @param oldValue   If there was an old value
     * @param newValue   If there was a new value
     */
    protected void beforePaddingFocusListener(ObservableValue<? extends Boolean> observable,
                                              Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            this.beforePaddingUpdateHelper();
        }
    }

    /**
     * Update method for the before padding field.
     */
    protected void beforePaddingUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {
            String newValue = CountUtilities.parseCountNumber(
                    ((DirectorDetailView) detailView).getPaddingBeforeField().getText());
            ((DirectorDetailView) detailView).getPaddingBeforeField().setText(newValue);
            double newVal = Double.parseDouble(newValue);
            DirectorShotBlock directorShotBlock =
                    ((DirectorShotBlock) manager.getActiveShotBlock());

            directorShotBlock.setPaddingBefore(newVal);
            DirectorShot directorShot = ((DirectorShot) manager.getActiveShotBlock().getShot());

            directorShot.setFrontShotPadding(newVal);
            directorShot.getCameraShots().forEach(e -> {
                    CameraShotBlock shotBlock = manager.getTimelineControl().getShotBlockForShot(e);
                    shotBlock.setBeginCount(directorShot.getBeginCount() - newVal, true);
                    manager.getTimelineControl().modifyCameraShot(
                            (CameraShotBlockUpdatedEvent) shotBlock.getShotBlockUpdatedEvent(),
                            shotBlock);
                    manager.setActiveShotBlock(directorShotBlock);
                });
            manager.getTimelineControl().recomputeAllCollisions();
        }
    }

    /**
     * Initialize the handlers for end padding field.
     */
    private void initEndPadding() {
        ((DirectorDetailView) detailView).getPaddingAfterField().focusedProperty()
                .addListener(this::afterPaddingFocusListener);
        ((DirectorDetailView) detailView).getPaddingAfterField().setOnKeyPressed(
                event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    this.afterPaddingUpdateHelper();
                }
            });
    }

    /**
     * Listener for focus change on end padding field.
     *
     * @param observable the observable value
     * @param oldValue   if there is an old value
     * @param newValue   if there is a new value
     */
    protected void afterPaddingFocusListener(ObservableValue<? extends Boolean> observable,
                                             Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            this.afterPaddingUpdateHelper();
        }
    }

    /**
     * Update method for the after padding.
     */
    protected void afterPaddingUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {
            String newValue = CountUtilities.parseCountNumber(
                    ((DirectorDetailView) detailView).getPaddingAfterField().getText());
            ((DirectorDetailView) detailView).getPaddingAfterField().setText(newValue);
            double newVal = Double.parseDouble(newValue);
            DirectorShotBlock directorShotBlock =
                    ((DirectorShotBlock) manager.getActiveShotBlock());

            directorShotBlock.setPaddingAfter(newVal);
            ((DirectorShot) manager.getActiveShotBlock().getShot()).setEndShotPadding(newVal);
            ((DirectorShot) manager.getActiveShotBlock().getShot()).getCameraShots().forEach(e -> {
                    CameraShotBlock shotBlock = manager.getTimelineControl().getShotBlockForShot(e);
                    shotBlock.setEndCount(((DirectorShot) manager.getActiveShotBlock().getShot())
                            .getEndCount() + newVal, true);
                    manager.getTimelineControl().modifyCameraShot(
                            (CameraShotBlockUpdatedEvent) shotBlock.getShotBlockUpdatedEvent(),
                            shotBlock);
                    manager.setActiveShotBlock(directorShotBlock);
                });
            manager.getTimelineControl().recomputeAllCollisions();

        }
    }

    /**
     * Listener for changes in checked indices for instruments dropdown.
     *
     * @param c the change that happened
     */
    protected void instrumentsDropdownChangeListener(ListChangeListener.Change c) {
        c.next();
        if (c.wasAdded()) {
            instrumentAddedInDropdown((int) c.getAddedSubList().get(0));
        } else {
            instrumentDeletedInDropdown((int) c.getRemoved().get(0));
        }
    }

    /**
     * Handler for a unchecked index in instrument dropdown.
     *
     * @param index the index that got unchecked
     */
    private void instrumentDeletedInDropdown(int index) {
        ShotBlock shotBlock = manager.getActiveShotBlock();
        shotBlock.getInstruments().remove(manager.getScriptingProject().getInstruments()
                .get(index));
        shotBlock.getShot().getInstruments().remove(manager.getScriptingProject()
                .getInstruments().get(index));
        shotBlock.getTimetableBlock().removeInstrument(manager.getScriptingProject()
                .getInstruments().get(index));
        shotBlock.recompute();
    }

    /**
     * Handler for a checked index in instrument dropdown.
     *
     * @param index the index that got unchecked
     */
    private void instrumentAddedInDropdown(int index) {
        ShotBlock shotBlock = manager.getActiveShotBlock();
        shotBlock.getInstruments().add(manager.getScriptingProject().getInstruments().get(index));
        shotBlock.getTimetableBlock().addInstrument(manager.getScriptingProject()
                .getInstruments().get(index));
        shotBlock.recompute();
    }

    /**
     * Change listener for the dropdown. Fires whenever a box is selected or deselected.
     *
     * @param c The Change with information about what changed.
     */
    protected void camerasDropdownChangeListener(ListChangeListener.Change c) {
        c.next();
        if (c.wasAdded()) {
            cameraAddedInDropdown((int) c.getAddedSubList().get(0));
        } else {
            cameraDeletedInDropdown((int) c.getRemoved().get(0));
        }
    }

    /**
     * Method for handling a deselect in the drop down.
     *
     * @param index the index of the deselected camera.
     */
    private void cameraDeletedInDropdown(int index) {
        DirectorShot dShot = ((DirectorShot) manager.getActiveShotBlock().getShot());
        Iterator<CameraShot> iterator = dShot.getCameraShots().iterator();
        CameraShot toRemove = null;
        while (iterator.hasNext()) {
            CameraShot shot = iterator.next();
            if (manager.getTimelineControl()
                    .getShotBlockForShot(shot).getTimetableNumber() == index) {
                toRemove = shot;
                break;
            }
        }
        manager.getTimelineControl().removeCameraShot(toRemove);
        dShot.getCameraShots().remove(toRemove);
        dShot.getTimelineIndices().remove(index);
        manager.getTimelineControl().recomputeAllCollisions();
        manager.getDirectorTimelineControl().recomputeAllCollisions();
    }

    /**
     * Method for handling a select in the dropdown.
     *
     * @param index the index of the camera that was selected.
     */
    private void cameraAddedInDropdown(int index) {
        CameraShot shot = new CameraShot();
        DirectorShot dShot = ((DirectorShot) manager.getActiveShotBlock().getShot());

        // Set shot variables
        shot.setName(dShot.getName());
        shot.setDescription(dShot.getDescription());
        shot.setBeginCount(dShot.getBeginCount() - dShot.getFrontShotPadding());
        shot.setEndCount(dShot.getEndCount() + dShot.getEndShotPadding());
        shot.setDirectorShot(dShot);

        // Add shot where needed
        dShot.getCameraShots().add(shot);
        dShot.getTimelineIndices().add(index);
        DirectorShotBlock dShotBlock = ((DirectorShotBlock) manager.getActiveShotBlock());
        manager.getScriptingProject().getCameraTimelines().get(index).addShot(shot);
        manager.getTimelineControl().initShotBlock(index, shot, false);
        manager.setActiveShotBlock(dShotBlock);
        manager.getTimelineControl().recomputeAllCollisions();
        manager.getDirectorTimelineControl().recomputeAllCollisions();
    }

    /**
     * Init the begincount handlers.
     */
    private void initBeginCount() {
        detailView.getBeginCountField().focusedProperty()
                .addListener(this::beginCountFocusListener);

        detailView.getBeginCountField().setOnKeyPressed(
                event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    this.beginCountUpdateHelper();
                }
            });
    }

    /**
     * Changelistener for when focus on begincountfield changes.
     *
     * @param observable - the observable
     * @param oldValue   - the old value of focus
     * @param newValue   - the new value of focus
     */
    void beginCountFocusListener(ObservableValue<? extends Boolean> observable,
                                 Boolean oldValue, Boolean newValue) {
        // exiting focus
        if (!newValue) {
            this.beginCountUpdateHelper();
        }
    }

    /**
     * Helper for when the begincount field is edited.
     * Parses entry to quarters and updates all the values
     */
    void beginCountUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {
            String newValue = CountUtilities.parseCountNumber(
                    detailView.getBeginCountField().getText());
            detailView.getBeginCountField().setText(newValue);
            double newVal = Double.parseDouble(newValue);

            manager.getActiveShotBlock().setBeginCount(newVal);
            manager.getActiveShotBlock().getShot().setBeginCount(newVal);
        }
    }

    /**
     * Init the endcuont handlers.
     */
    private void initEndCount() {
        detailView.getEndCountField().focusedProperty()
                .addListener(this::endCountFocusListener);

        detailView.getEndCountField().setOnKeyPressed(
                event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    endCountUpdateHelper();
                }
            });
    }

    /**
     * Changelistener for when focus on endcountfield changes.
     *
     * @param observable - the observable
     * @param oldValue   - the old value of focus
     * @param newValue   - the new value of focus
     */
    void endCountFocusListener(ObservableValue<? extends Boolean> observable,
                               Boolean oldValue, Boolean newValue) {
        // exiting focus
        if (!newValue) {
            this.endCountUpdateHelper();
        }
    }

    /**
     * Helper for when the endcount field is edited.
     * Parses entry to quarters and updates all the values
     */
    private void endCountUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {

            String newValue = CountUtilities.parseCountNumber(
                    detailView.getEndCountField().getText());
            double newVal = Double.parseDouble(newValue);
            detailView.getEndCountField().setText(newValue);

            manager.getActiveShotBlock().setEndCount(newVal);
            manager.getActiveShotBlock().getShot().setEndCount(newVal);
        }
    }

    /**
     * Init the description handlers.
     */
    private void initDescription() {
        detailView.getDescriptionField().textProperty()
                .addListener(this::descriptionTextChangedListener);
    }

    /**
     * Changelistener for when the text in descriptionfield changes.
     *
     * @param observable - the observable
     * @param oldValue   - the old value of the field
     * @param newValue   - the new value of the field
     */
    void descriptionTextChangedListener(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {
        if (manager.getActiveShotBlock() != null) {
            manager.getActiveShotBlock().setDescription(newValue);
            manager.getActiveShotBlock().getShot().setDescription(newValue);
        }
    }

    /**
     * Init the name handler.
     */
    private void initName() {
        detailView.getNameField().textProperty()
                .addListener(this::nameTextChangedListener);
    }

    /**
     * Changelistener for when the text in namefield changes.
     *
     * @param observable - the observable
     * @param oldValue   - the old value of the field
     * @param newValue   - the new value of the field
     */
    void nameTextChangedListener(ObservableValue<? extends String> observable,
                                 String oldValue, String newValue) {
        if (manager.getActiveShotBlock() != null) {
            manager.getActiveShotBlock().setName(newValue);
            manager.getActiveShotBlock().getShot().setName(newValue);
        }
    }

    /**
     * Method to signal that the active block is changed so we can update it.
     */
    public void activeBlockChanged() {
        if (manager.getActiveShotBlock() != null) {
            if (manager.getActiveShotBlock() instanceof CameraShotBlock) {
                activeBlockChangedCamera();
            } else {
                activeBlockChangedDirector();
            }
        } else {
            detailView.resetDetails();
            detailView.setInvisible();
        }
    }

    /**
     * Handler for when the active block is now a camera shot.
     */
    private void activeBlockChangedCamera() {
        detailView = new DetailView();
        detailView.setDescription(manager.getActiveShotBlock().getDescription());
        detailView.setName(manager.getActiveShotBlock().getName());
        detailView.setBeginCount(manager.getActiveShotBlock().getBeginCount());
        detailView.setEndCount(manager.getActiveShotBlock().getEndCount());
        activeCameraBlock = (CameraShotBlock) manager.getActiveShotBlock();
        detailView.setVisible();
        // Re-init the detail view with new data
        manager.getRootPane().getRootHeaderArea().reInitHeaderBar(detailView);
        this.reInitForCameraBlock();
    }

    /**
     * Handler for when the active block is now a director shot.
     */
    private void activeBlockChangedDirector() {
        DirectorShotBlock shotBlock = (DirectorShotBlock) manager.getActiveShotBlock();
        detailView = new DirectorDetailView();
        // Set detail view variables
        detailView.setDescription(shotBlock.getDescription());
        detailView.setName(shotBlock.getName());
        detailView.setBeginCount(shotBlock.getBeginCount());
        detailView.setEndCount(shotBlock.getEndCount());
        ((DirectorDetailView) detailView).getPaddingBeforeField()
                .setText(detailView.formatDouble(shotBlock.getPaddingBefore()));
        ((DirectorDetailView) detailView).getPaddingAfterField()
                .setText(detailView.formatDouble(shotBlock.getPaddingAfter()));
        activeDirectorBlock = shotBlock;
        detailView.setVisible();
        // Re-init the detail view with new data
        manager.getRootPane().getRootHeaderArea().reInitHeaderBar(detailView);
        this.reInitForDirectorBlock();
    }

    /**
     * Initialize drop down menu for director camera selection.
     */
    private void initDirectorCameraDropdown() {
        StyledMenuButton cameraButtons = ((DirectorDetailView) detailView).getSelectCamerasButton();
        cameraButtons.setBorderColor(TweakingHelper.getColor(0));
        cameraButtons.setFillColor(TweakingHelper.getBackgroundColor());
        activeCameraBoxes = new ArrayList<>();

        cameraButtons.showingProperty().addListener((observable, oldValue, newValue) ->
                cameraDropdownListener(observable, oldValue, newValue, cameraButtons));
    }

    /**
     * Initialize drop down menu for camera instrument selection.
     */
    private void initCameraInstrumentDropdown() {
        StyledMenuButton instrumentsButtons =
                ((DetailView) detailView).getSelectInstrumentsButton();
        instrumentsButtons.setBorderColor(TweakingHelper.getColor(0));
        instrumentsButtons.setFillColor(TweakingHelper.getBackgroundColor());
        activeInstrumentBoxes = new ArrayList<>();

        instrumentsButtons.showingProperty().addListener((observable, oldValue, newValue) ->
                instrumentsDropdownListener(observable, oldValue, newValue, instrumentsButtons));
    }

    /**
     * Creates ChangeListener for the Instruments Dropdown checkboxes.
     *
     * @param observable - the observable
     * @param oldValue   - the old value
     * @param newValue   - the new value
     * @param buttons    - the buttons
     */
    protected void instrumentsDropdownListener(ObservableValue<? extends Boolean> observable,
                                               Boolean oldValue, Boolean newValue,
                                               StyledMenuButton buttons) {
        if (newValue) {
            // show the list and give it content
            instrumentsDropdownListenerHelper(buttons);
        } else {
            // empty the list when not shown
            activeInstrumentBoxes.clear();
            buttons.getItems().clear();
        }
    }

    /**
     * Creates ChangeListener for the Camera Dropdown checkboxes.
     * @param observable - the observable
     * @param oldValue - the old value
     * @param newValue - the new avlue
     * @param buttons - the buttons
     */
    protected void cameraDropdownListener(ObservableValue<? extends Boolean> observable,
                                          Boolean oldValue, Boolean newValue,
                                          StyledMenuButton buttons) {
        if (newValue) {
            // show the list and give it content
            cameraDropdownListenerHelper(buttons);
        } else {
            // empty the list when not shown
            activeCameraBoxes.clear();
            buttons.getItems().clear();
        }
    }

    /**
     * Helper method for showing the Camera Dropdown.
     *
     * @param buttons the dropdown menu containing buttons.
     */
    private void cameraDropdownListenerHelper(StyledMenuButton buttons) {
        // loop thourhgh all cameras for the active director block, when the list is shown
        Set<Integer> indices = activeDirectorBlock.getTimelineIndices();
        for (int i = 0; i < manager.getScriptingProject().getCameras().size(); i++) {
            Camera camera = manager.getScriptingProject().getCameras().get(i);
            // create and add checkbox for camera with on/off toggling loaded in.
            StyledCheckbox checkbox = getStyledCheckbox(camera.getName(),
                    indices.contains(i));
            activeCameraBoxes.add(checkbox);
            CustomMenuItem item = new CustomMenuItem(checkbox);
            item.setHideOnClick(false);
            buttons.getItems().add(item);
            // add event handler for mouse clicks on the checkbox
            checkbox.setOnMouseClicked(createCameraDropdownHandler(checkbox, i));
        }
    }

    /**
     * Helper method for showing the Instruments Dropdown.
     *
     * @param buttons the dropdown menu containing buttons.
     */
    private void instrumentsDropdownListenerHelper(StyledMenuButton buttons) {
        // loop thourhgh all instruments for the active block, when the list is shown
        List<Instrument> instruments = activeCameraBlock.getInstruments();
        for (int i = 0; i < manager.getScriptingProject().getInstruments().size();
             i++) {
            Instrument instrument = manager.getScriptingProject()
                    .getInstruments().get(i);
            // create and add checkbox for instrument with on/off toggling loaded in.
            StyledCheckbox checkbox = getStyledCheckbox(instrument.getName(),
                    instruments.contains(instrument));
            activeInstrumentBoxes.add(checkbox);
            CustomMenuItem item = new CustomMenuItem(checkbox);
            item.setHideOnClick(false);
            buttons.getItems().add(item);
            // add event handler for mouse clicks on the checkbox
            checkbox.setOnMouseClicked(createInstrumentDropdownHandler(checkbox, i));
        }
    }

    protected StyledCheckbox getStyledCheckbox(String name, Boolean checked) {
        return new StyledCheckbox(name, checked);
    }

    /**
     * Event handler for when a checkbox in the camera dropdown is clicked.
     *
     * @param box the checkbox that was clicked.
     * @param i   index of the checkbox.
     * @return the Event Handler.
     */
    private EventHandler<MouseEvent> createCameraDropdownHandler(StyledCheckbox box, int i) {
        return e -> {
            if (box.isSelected()) {
                cameraAddedInDropdown(i);
            } else {
                cameraDeletedInDropdown(i);
            }
        };
    }

    /**
     * Event handler for when a checkbox in the instruments dropdown is clicked.
     *
     * @param box the checkbox that was clicked.
     * @param i   index of the checkbox.
     * @return the Event Handler.
     */
    private EventHandler<MouseEvent> createInstrumentDropdownHandler(StyledCheckbox box, int i) {
        return e -> {
            if (box.isSelected()) {
                instrumentAddedInDropdown(i);
            } else {
                instrumentDeletedInDropdown(i);
            }
        };
    }
}
