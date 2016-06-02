package control;

import java.util.Iterator;
import java.util.Set;

import data.CameraShot;
import data.DirectorShot;
import gui.centerarea.CameraShotBlock;
import gui.centerarea.DirectorShotBlock;
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
    
    public void reInitForCameraBlock() {
        initDescription();
        initName();
        initBeginCount();
        initEndCount();
    }
    
    public void reInitForDirectorBlock() {
        reInitForCameraBlock();
        initBeginPadding();
        initEndPadding();
        initCamerasDropDown();
    }
    
    private void initBeginPadding() {
        //((DirectorDetailView) detailView).setBeforePadding(0);
        
        ((DirectorDetailView) detailView).getPaddingBeforeField().focusedProperty().addListener(this::beforePaddingFocusListener);
        
        ((DirectorDetailView) detailView).getPaddingBeforeField().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                this.beforePaddingUpdateHelper();
            }
        });
    }
    
    private void beforePaddingFocusListener(ObservableValue<? extends Boolean> observable,
                                 Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            this.beforePaddingUpdateHelper();
        }
    }
    
    private void beforePaddingUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {
            String newValue = CountUtilities.parseCountNumber(((DirectorDetailView) detailView).getPaddingBeforeField().getText());
            ((DirectorDetailView) detailView).getPaddingBeforeField().setText(newValue);
            double newVal = Double.parseDouble(newValue);
            
            ((DirectorShotBlock) manager.getActiveShotBlock()).setPaddingBefore(newVal);
            ((DirectorShot) manager.getActiveShotBlock().getShot()).setFrontShotPadding(newVal);
            ((DirectorShot) manager.getActiveShotBlock().getShot()).getCameraShots().forEach(e -> {
                CameraShotBlock shotBlock = manager.getTimelineControl().getShotBlockForShot(e);
                shotBlock.setBeginCount(((DirectorShot) manager.getActiveShotBlock().getShot()).getBeginCount() - newVal, true);
            });
            
        }
    }
    
    private void initEndPadding() {
        ((DirectorDetailView) detailView).getPaddingAfterField().focusedProperty().addListener(this::afterPaddingFocusListener);
        ((DirectorDetailView) detailView).getPaddingAfterField().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                this.afterPaddingUpdateHelper();
            }
        });
    }
    
    private void afterPaddingFocusListener(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            this.afterPaddingUpdateHelper();
        }
    }
    
    private void afterPaddingUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {
            String newValue = CountUtilities.parseCountNumber(((DirectorDetailView) detailView).getPaddingAfterField().getText());
            ((DirectorDetailView) detailView).getPaddingAfterField().setText(newValue);
            double newVal = Double.parseDouble(newValue);
            
            ((DirectorShotBlock) manager.getActiveShotBlock()).setPaddingAfter(newVal);
            ((DirectorShot) manager.getActiveShotBlock().getShot()).setEndShotPadding(newVal);
            ((DirectorShot) manager.getActiveShotBlock().getShot()).getCameraShots().forEach(e -> {
                CameraShotBlock shotBlock = manager.getTimelineControl().getShotBlockForShot(e);
                shotBlock.setEndCount(((DirectorShot) manager.getActiveShotBlock().getShot()).getEndCount() + newVal, true);
            });
        }
    }
    
    private void initCamerasDropDown() {
        ((DirectorDetailView) detailView).getSelectCamerasDropDown().getCheckModel().getCheckedIndices().addListener(this::camerasDropdownChangeListener);
    }
    
    private void camerasDropdownChangeListener(ListChangeListener.Change c) {
        System.out.println("CHANGE LISTENER");
        DirectorShot shot = ((DirectorShot) manager.getActiveShotBlock().getShot());
        c.next();
        if (c.wasAdded()) {
            cameraAddedInDropdown((int) c.getAddedSubList().get(0));
        } else {
            cameraDeletedInDropdown((int) c.getRemoved().get(0));
        }
        //camerasDropdownUpdateHelper();
    }
    
    private void cameraDeletedInDropdown(int index) {
        DirectorShotBlock dShotBlock = ((DirectorShotBlock) manager.getActiveShotBlock());
        DirectorShot dShot = ((DirectorShot) manager.getActiveShotBlock().getShot());
        Iterator<CameraShot> iterator = dShot.getCameraShots().iterator();
        CameraShot toRemove = null;
        while (iterator.hasNext()) {
            CameraShot shot = iterator.next();
            if (manager.getTimelineControl().getShotBlockForShot(shot).getTimetableNumber() == index) {
                toRemove = shot;
                break;
                
            }
        }
        manager.getTimelineControl().removeCameraShot(toRemove);
        dShot.getCameraShots().remove(toRemove);
        dShot.getTimelineIndices().remove(index);
        

    }
    
    private void cameraAddedInDropdown(int index) {
        CameraShot shot = new CameraShot();
        DirectorShot dShot = ((DirectorShot) manager.getActiveShotBlock().getShot());
        shot.setName(dShot.getName());
        shot.setDescription(dShot.getDescription());
        shot.setBeginCount(dShot.getBeginCount() - dShot.getFrontShotPadding());
        shot.setEndCount(dShot.getEndCount() + dShot.getEndShotPadding());
        shot.setDirectorShot(dShot);
        dShot.getCameraShots().add(shot);
        dShot.getTimelineIndices().add(index);
        manager.getScriptingProject().getCameraTimelines().get(index).addShot(shot);
        manager.getTimelineControl().initShotBlock(index, shot);
        
    }
    
    
    
    private void camerasDropdownUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {
            DirectorShotBlock directorShotBlock = (DirectorShotBlock) manager.getActiveShotBlock();
            directorShotBlock.getTimelineIndices().clear();
            Iterator<CameraShot> cameraShotIterator = directorShotBlock.getShot().getCameraShots().iterator();
            directorShotBlock.getShot().getCameraShots().forEach(shot -> {
                manager.getTimelineControl().removeCameraShot(shot);
            });
//            while (cameraShotIterator.hasNext()) {
//                CameraShot shot = cameraShotIterator.next();
//                manager.getTimelineControl().getShotBlockForShot(shot).removeFromView();
//                manager.getTimelineControl().getCameraShotBlocks().remove(manager.getTimelineControl().getShotBlockForShot(shot));
//                cameraShotIterator.remove();
//            }
//            directorShotBlock.getShot().getCameraShots().forEach(shot -> {
//                manager.getTimelineControl().getShotBlockForShot(shot).removeFromView();
//                manager.getTimelineControl().getCameraShotBlocks().remove(manager.getTimelineControl().getShotBlockForShot(shot));
//            });
            
            
            directorShotBlock.getShot().getCameraShots().clear();
//            ((DirectorDetailView) detailView).getSelectCamerasDropDown().getCheckModel().getCheckedIndices().forEach(directorShotBlock.getTimelineIndices()::add);
//            manager.getToolViewController().generateCameraShots(directorShotBlock);
//            System.out.println("THE STUFF HAS BEEN DONE");
        }
    }

    /**
     * Init the begincount handlers.
     */
    private void initBeginCount() {
        //detailView.setBeginCount(0);

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
        //detailView.setEndCount(0);

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
        //detailView.setDescription("");

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
        //detailView.setName("");

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
            System.out.println("PASSED NULL CHECK");
            if (manager.getActiveShotBlock() instanceof CameraShotBlock) {
                System.out.println("IS CAMERA SHOT BLOCK");
                detailView = new DetailView();
                detailView.setDescription(manager.getActiveShotBlock().getDescription());
                detailView.setName(manager.getActiveShotBlock().getName());
                detailView.setBeginCount(manager.getActiveShotBlock().getBeginCount());
                detailView.setEndCount(manager.getActiveShotBlock().getEndCount());
                detailView.setVisible();
                detailView.setVisible(true);
                manager.getRootPane().getRootHeaderArea().setDetailView(detailView);  
                manager.getRootPane().getRootHeaderArea().reInitHeaderBar(detailView);
                this.reInitForCameraBlock();
            } else {
                System.out.println("IS DIRECTOR SHOT BLOCK");
                DirectorShotBlock shotBlock = (DirectorShotBlock) manager.getActiveShotBlock();
                detailView = new DirectorDetailView();
                detailView.setDescription(shotBlock.getDescription());
                detailView.setName(shotBlock.getName());
                detailView.setBeginCount(shotBlock.getBeginCount());
                detailView.setEndCount(shotBlock.getEndCount());
                ((DirectorDetailView) detailView).getPaddingBeforeField().setText(detailView.formatDouble(shotBlock.getPaddingBefore()));
                ((DirectorDetailView) detailView).getPaddingAfterField().setText(detailView.formatDouble(shotBlock.getPaddingAfter()));
                initDropDown(shotBlock);
                detailView.setVisible();
                detailView.setVisible(true);
                manager.getRootPane().getRootHeaderArea().reInitHeaderBar(detailView);
                this.reInitForDirectorBlock();

            }
        } else {
            detailView.resetDetails();
            detailView.setInvisible();
        }
    }
    
    private void initDropDown(DirectorShotBlock shotBlock) {
        Set<Integer> indices = shotBlock.getTimelineIndices();
        ((DirectorDetailView) detailView).getSelectCamerasDropDown().getItems().clear();
        manager.getScriptingProject().getCameras().forEach(camera -> {
            ((DirectorDetailView) detailView).getSelectCamerasDropDown().getItems().add(camera.getName());
        });
        indices.forEach(e -> {
            ((DirectorDetailView) detailView).getSelectCamerasDropDown().getCheckModel().check(e);
        });
    }
}
