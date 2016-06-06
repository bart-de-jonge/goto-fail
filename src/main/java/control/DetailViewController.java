package control;

import java.util.Iterator;
import java.util.Set;

import data.CameraShot;
import data.DirectorShot;
import gui.centerarea.CameraShotBlock;
import gui.centerarea.DirectorShotBlock;
import gui.events.CameraShotBlockUpdatedEvent;
import gui.headerarea.DetailView;
import gui.headerarea.DirectorDetailView;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.input.KeyCode;
import lombok.extern.log4j.Log4j2;

/**
 * Controller for the DetailView.
 */
@Log4j2
public class DetailViewController {

    private DetailView detailView;
    private ControllerManager manager;

    /**
     * Constructor.
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
    }
    
    /**
     * Re-init the tool view for when a director block is selected.
     */
    public void reInitForDirectorBlock() {
        reInitForCameraBlock();
        initBeginPadding();
        initEndPadding();
        initCamerasDropDown();
    }
    
    /**
     * Initialize the handlers for the begin padding field.
     */
    private void initBeginPadding() {
        //((DirectorDetailView) detailView).setBeforePadding(0);
        
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
     * @param observable the observable value
     * @param oldValue If there was an old value
     * @param newValue If there was a new value
     */
    private void beforePaddingFocusListener(ObservableValue<? extends Boolean> observable,
                                 Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            this.beforePaddingUpdateHelper();
        }
    }
    
    /**
     * Update method for the before padding field.
     */
    private void beforePaddingUpdateHelper() {
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
        ((DirectorDetailView) detailView).getPaddingAfterField().setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    this.afterPaddingUpdateHelper();
                }
            });
    }
    
    /**
     * Listener for focus change on end padding field.
     * @param observable the observable value
     * @param oldValue if there is an old value
     * @param newValue if there is a new value
     */
    private void afterPaddingFocusListener(ObservableValue<? extends Boolean> observable,
            Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            this.afterPaddingUpdateHelper();
        }
    }
    
    /**
     * Update method for the after padding.
     */
    private void afterPaddingUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {
            String newValue = CountUtilities.parseCountNumber(
                    ((DirectorDetailView) detailView).getPaddingAfterField().getText());
            ((DirectorDetailView) detailView).getPaddingAfterField().setText(newValue);
            double newVal = Double.parseDouble(newValue);
            DirectorShotBlock directorShotBlock = ((DirectorShotBlock) manager.getActiveShotBlock());
            
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
     * Init the handlers for the camera selection drop down menu.
     */
    private void initCamerasDropDown() {
        ((DirectorDetailView) detailView).getSelectCamerasDropDown()
                                         .getCheckModel()
                                         .getCheckedIndices()
                                         .addListener(this::camerasDropdownChangeListener);
    }
    
    /**
     * Change listener for the dropdown. Fires whenever a box is selected or deselected.
     * @param c The Change with information about what changed.
     */
    private void camerasDropdownChangeListener(ListChangeListener.Change c) {
        System.out.println("CHANGE LISTENER");
        DirectorShot shot = ((DirectorShot) manager.getActiveShotBlock().getShot());
        c.next();
        if (c.wasAdded()) {
            cameraAddedInDropdown((int) c.getAddedSubList().get(0));
        } else {
            cameraDeletedInDropdown((int) c.getRemoved().get(0));
        }
    }
    
    /**
     * Method for handling a deselect in the drop down.
     * @param index the index of the deselected camera.
     */
    private void cameraDeletedInDropdown(int index) {
        DirectorShotBlock dShotBlock = ((DirectorShotBlock) manager.getActiveShotBlock());
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
    }
    
    /**
     * Method for handling a select in the dropdown.
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
        manager.getTimelineControl().initShotBlock(index, shot);
        manager.setActiveShotBlock(dShotBlock);
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
     * @param observable - the observable
     * @param oldValue - the old value of focus
     * @param newValue - the new value of focus
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
     * @param observable - the observable
     * @param oldValue - the old value of focus
     * @param newValue - the new value of focus
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
     * @param observable - the observable
     * @param oldValue - the old value of the field
     * @param newValue - the new value of the field
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
     * @param observable - the observable
     * @param oldValue - the old value of the field
     * @param newValue - the new value of the field
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
                detailView = new DetailView();
                // set detail view variables
                detailView.setDescription(manager.getActiveShotBlock().getDescription());
                detailView.setName(manager.getActiveShotBlock().getName());
                detailView.setBeginCount(manager.getActiveShotBlock().getBeginCount());
                detailView.setEndCount(manager.getActiveShotBlock().getEndCount());
                detailView.setVisible();
                detailView.setVisible(true);
                // Re-init the detail view with new data
                manager.getRootPane().getRootHeaderArea().setDetailView(detailView);  
                manager.getRootPane().getRootHeaderArea().reInitHeaderBar(detailView);
                this.reInitForCameraBlock();
            } else {
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
                initDropDown(shotBlock);
                detailView.setVisible();
                detailView.setVisible(true);
                // Re-init the detail view with new data
                manager.getRootPane().getRootHeaderArea().reInitHeaderBar(detailView);
                this.reInitForDirectorBlock();

            }
        } else {
            detailView.resetDetails();
            detailView.setInvisible();
        }
    }
    
    /**
     * Initialize the drop down menu.
     * @param shotBlock the shotBlock to do that for
     */
    private void initDropDown(DirectorShotBlock shotBlock) {
        Set<Integer> indices = shotBlock.getTimelineIndices();
        ((DirectorDetailView) detailView).getSelectCamerasDropDown().getItems().clear();
        manager.getScriptingProject().getCameras().forEach(camera -> {
                ((DirectorDetailView) detailView).getSelectCamerasDropDown()
                    .getItems().add(camera.getName());
            });
        indices.forEach(e -> {
                ((DirectorDetailView) detailView).getSelectCamerasDropDown()
                    .getCheckModel().check(e);
            });
    }
}
