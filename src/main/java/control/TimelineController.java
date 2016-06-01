package control;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import data.CameraShot;
import data.CameraTimeline;
import data.DirectorShot;
import data.Shot;
import gui.centerarea.CameraShotBlock;
import gui.events.CameraShotBlockUpdatedEvent;
import gui.modal.ShotDecouplingModalView;
import gui.root.RootPane;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Class that controls the timeline.
 */
@Log4j2
public class TimelineController {
    
    private final int defaultAmountTimelines = 8;

    @Setter
    private RootPane rootPane;

    // TODO: replace number of centerarea with xml data
    @Setter
    private int numTimelines;

    @Getter
    private ControllerManager controllerManager;

    @Getter
    // List of all camerashotblocks in this timelinecontroller
    private List<CameraShotBlock> cameraShotBlocks;

    // List of all currently colliding camerashotblocks
    @Getter
    private List<CameraShotBlock> overlappingCameraShotBlocks;

    private Map<CameraShot, CameraShotBlock> cameraShotBlockMap;

    /**
     * Constructor.
     * @param controllerManager Root Pane.
     */
    public TimelineController(ControllerManager controllerManager) {
        log.debug("Constructing new TimelineController(controllerManager={})", controllerManager);

        this.controllerManager = controllerManager;
        this.rootPane = controllerManager.getRootPane();
        this.cameraShotBlocks = new ArrayList<>();
        this.overlappingCameraShotBlocks = new ArrayList<>();
        this.cameraShotBlockMap = new HashMap<>();
    }

    /**
     * Add a camera shot to the corresponding timeline.
     * @param cameraIndex Index of the camera track.
     * @param name Name of the shot.
     * @param description Shot description.
     * @param startCount Start count.
     * @param endCount End count.
     */
    public void addCameraShot(int cameraIndex, String name, String description,
                              double startCount, double endCount) {

        CameraShot newShot = new CameraShot(name,description, startCount, endCount);
        this.addCameraShot(cameraIndex, newShot);
    }

    /**
     * Add an existing CameraShot to the corresponding timeline.
     * @param cameraIndex Index of the camera track
     * @param newShot CameraShot to add to the timeline
     */
    public void addCameraShot(int cameraIndex, CameraShot newShot) {
        log.info("Adding CameraShot to Timeline");

        this.controllerManager.getScriptingProject()
                              .getCameraTimelines()
                              .get(cameraIndex)
                              .addShot(newShot);
        initShotBlock(cameraIndex, newShot);
    }

    /**
     * Display an existing (and linked) CameraShot in the view.
     * @param cameraIndex Index of the camera timeline
     * @param newShot CameraShot to display
     */
    protected void initShotBlock(int cameraIndex,
                               CameraShot newShot) {
        CameraShotBlock shotBlock = new CameraShotBlock(newShot.getInstance(),
            cameraIndex, rootPane.getRootCenterArea(), newShot.getBeginCount(),
            newShot.getEndCount(), newShot.getDescription(), newShot.getName(),
            this::shotChangedHandler, newShot);

        controllerManager.setActiveShotBlock(shotBlock);
        this.cameraShotBlocks.add(shotBlock);
        this.cameraShotBlockMap.put(newShot, shotBlock);
        controllerManager.getScriptingProject().changed();

        // Check for collisions
        checkCollisions(cameraIndex, shotBlock);
    }

    /**
     * Remove a camera shot from both the display and the timeline.
     * @param cameraShotBlock CameraShotBlock to be removed
     */
    public void removeCameraShot(CameraShotBlock cameraShotBlock) {
        // If we are removing the active shot, then this must be updated accordingly
        if (cameraShotBlock.equals(this.controllerManager.getActiveShotBlock())) {
            this.controllerManager.setActiveShotBlock(null);
        }

        // Remove the shot from the model
        CameraTimeline cameraTimeline = this.controllerManager.getScriptingProject()
                                                              .getCameraTimelines()
                .get(cameraShotBlock.getTimetableNumber());
        cameraTimeline.removeShot(cameraShotBlock.getShot());
        controllerManager.getScriptingProject().changed();

        this.decoupleShot(cameraShotBlock.getTimetableNumber(), cameraShotBlock.getShot());

        this.cameraShotBlockMap.remove(cameraShotBlock.getShot());

        // Then remove the shot from the view
        cameraShotBlock.removeFromView();
    }

    /**
     * Works like {@link TimelineController#removeCameraShot(CameraShotBlock)}.
     * @param shot Camera Shot to be removed
     */
    public void removeCameraShot(CameraShot shot) {
        CameraShotBlock shotBlock = cameraShotBlockMap.get(shot);

        if (shotBlock != null) {
            this.removeCameraShot(shotBlock);
        }
    }

    /**
     * Handle updated camera shot. The previous timeline is used to retrieve the corresponding
     * shot. The correct {@link CameraShot} is then updated using the latest {@link CameraShotBlock}
     * position and counts. As the event is unclear as to whether the shot has switched centerarea,
     * it is removed from the previous timeline and added to the new one.
     * @param event Camera shot change event.
     */
    public void shotChangedHandler(CameraShotBlockUpdatedEvent event) {
        CameraShotBlock changedBlock = event.getCameraShotBlock();
        cameraShotBlocks.forEach(shotBlock -> {  
                this.checkCollisions(shotBlock.getTimetableNumber(), shotBlock);
            });

        // If coupled to DirectorShot, confirm separation
        this.decoupleAndModify(event, changedBlock);
    }

    /**
     * Modify a CameraShot based on an update event.
     * @param event CameraShot's change event
     * @param changedBlock CameraShot to modify
     */
    protected void modifyCameraShot(CameraShotBlockUpdatedEvent event,
                                  CameraShotBlock changedBlock) {
        controllerManager.getScriptingProject().changed();
        log.info("Shot moved to new TimeLine");

        controllerManager.setActiveShotBlock(changedBlock);

        CameraTimeline previousTimeline = this.controllerManager.getScriptingProject()
                .getCameraTimelines()
                .get(event.getOldTimelineNumber());
        // Locate shot to be updated using id
        CameraShot shot = changedBlock.getShot();

        // Adjust model
        shot.setBeginCount(changedBlock.getBeginCount());
        shot.setEndCount(changedBlock.getEndCount());

        CameraTimeline newCameraTimeline = this.controllerManager.getScriptingProject()
                .getCameraTimelines()
                .get(changedBlock.getTimetableNumber());

        // Remove shot from previous timeline and add to new one if changed
        if (event.getOldTimelineNumber() != changedBlock.getTimetableNumber()) {
            previousTimeline.removeShot(shot);
            newCameraTimeline.addShot(shot);
        }

        // check for collisions
        checkCollisions(changedBlock.getTimetableNumber(),
                        event.getOldTimelineNumber(), changedBlock);
    }

    
    /**
     * Get the shot block corresponding to the shot.
     * @param shot the shot to search for
     * @return the shot block with shot as shot.
     */
    public CameraShotBlock getShotBlockForShot(CameraShot shot) {
        for (int i = 0;i < this.cameraShotBlocks.size();i++) {
            if (cameraShotBlocks.get(i).getShot().getInstance() == shot.getInstance()) {
                return cameraShotBlocks.get(i);
            }
        }
        return null;
    }
    

    /**
     * Check collisions for new camerablock.
     * @param timelineNumber - the timelinenumber where the camerashotblock is added
     * @param cameraShotBlock - the camerashotblock to check collisions with
     */
    protected void checkCollisions(int timelineNumber, CameraShotBlock cameraShotBlock) {
        checkCollisions(timelineNumber, -1, cameraShotBlock);
    }

    /**
     * Check for collisions (added and removed).
     * @param timelineNumber - the current timeline number to check new collisions
     * @param oldTimelineNumber - the old timeline number to check which collisions are not valid
     *                          anymore should be negative when adding new block
     * @param cameraShotBlock - the shotblock to check collisions with
     */
    protected void checkCollisions(int timelineNumber, int oldTimelineNumber,
                                 CameraShotBlock cameraShotBlock) {
       
        CameraTimeline timeline = controllerManager.getScriptingProject()
                                                   .getCameraTimelines()
                                                   .get(timelineNumber);
        if (oldTimelineNumber >= 0) {
            removeCollisionFromCameraShotBlock(cameraShotBlock);
            ArrayList<CameraShotBlock> toRemove = new ArrayList<>();
            this.overlappingCameraShotBlocks.stream()
                    .filter(shotBlock ->
                        shotBlock.getShot().getCollidesWith().isEmpty()).forEach(shotBlock -> {
                                shotBlock.setColliding(false);
                                toRemove.add(shotBlock);
                            });
            this.overlappingCameraShotBlocks.removeAll(toRemove);
        }
        ArrayList<CameraShot> overlappingShots = timeline
                .getOverlappingShots(cameraShotBlock.getShot());
        if (overlappingShots.size() > 1) {
            ArrayList<CameraShotBlock> overlappingShotBlocks = new ArrayList<>();
            Supplier<ArrayList<Integer>> supplier = ArrayList::new;
            ArrayList<Integer> myInts = overlappingShots.stream().map(Shot::getInstance)
                    .collect(Collectors.toCollection(supplier));
            this.cameraShotBlocks.stream().filter(shotBlock ->
                    myInts.contains(shotBlock.getShotId())).forEach(shotBlock -> {
                            overlappingShotBlocks.add(shotBlock);
                            if (!this.overlappingCameraShotBlocks.contains(shotBlock)) {
                                this.overlappingCameraShotBlocks.add(shotBlock);
                            }
                        });
            for (CameraShotBlock shotBlock : overlappingShotBlocks) {
                shotBlock.setColliding(true);
            }
        } else {
            resetColliding(cameraShotBlock);
        }
    }
    
    /**
     * Reset colliding status on camera shot block.
     * @param cameraShotBlock the shot block to do that on
     */
    private void resetColliding(CameraShotBlock cameraShotBlock) {
        cameraShotBlock.setColliding(false);
        cameraShotBlock.getShot().setColliding(false);
        cameraShotBlock.getShot().getCollidesWith().forEach(e -> {
                this.checkCollisions(
                    this.getShotBlockForShot((CameraShot) e)
                    .getTimetableNumber(),
                    this.getShotBlockForShot((CameraShot) e));
            });
        removeCollisionFromCameraShotBlock(cameraShotBlock);
    }

    /**
     * Remove all collisions and from this shotblock.
     * Removes the collisions from the counterpart in each collision as well
     * @param shotBlock - the shotblock to remove the collisions from
     */
    protected void removeCollisionFromCameraShotBlock(CameraShotBlock shotBlock) {
        ArrayList<Shot> toRemove = new ArrayList<>();
        for (Shot shot : shotBlock.getShot().getCollidesWith()) {
            toRemove.add(shot);
            if (shot.getCollidesWith().contains(shotBlock.getShot())) {
                shot.getCollidesWith().remove(shotBlock.getShot());
            }
        }
        shotBlock.getShot().getCollidesWith().removeAll(toRemove);
    }

    /**
     * If CameraShot belongs to DirectorShot, confirm/cancel separation and then modify shot.
     * @param event shot changed event.
     * @param shotBlock CameraShot for which to confirm changes.
     */
    protected void decoupleAndModify(CameraShotBlockUpdatedEvent event, CameraShotBlock shotBlock) {
        if (shotBlock.getShot().getDirectorShot() != null && this.shotBlockTimingModified(event)) {
            ShotDecouplingModalView decouplingModalView = new ShotDecouplingModalView(
                    this.rootPane, shotBlock.getShot());

            decouplingModalView.getCancelButton().setOnMouseReleased(e -> {
                    log.info("Shot decoupling cancelled.", shotBlock.getShot());
                    decouplingModalView.hideModal();
                    shotBlock.restorePreviousPosition();
                });

            decouplingModalView.getConfirmButton().setOnMouseReleased(e -> {
                    log.info("Shot decoupling confirmed.", shotBlock.getShot());
                    decouplingModalView.hideModal();
                    this.decoupleShot(event.getOldTimelineNumber(), shotBlock.getShot());
                    this.modifyCameraShot(event, shotBlock);
                });
        } else {
            this.modifyCameraShot(event, shotBlock);
        }
    }

    /**
     * Decouple CameraShot from its DirectorShot & vice versa.
     * @param timelineIndex Index of timeline that the CameraShot belongs to.
     * @param shot CameraShot to decouple
     */
    void decoupleShot(int timelineIndex, CameraShot shot) {
        log.info("Decoupling shot.", shot);
        DirectorShot directorShot = shot.getDirectorShot();
        if (directorShot != null) {
            directorShot.removeCameraShot(shot, timelineIndex);

            // Delete the director shot if it's the last remaining camera shot
            if (directorShot.getCameraShots().isEmpty()) {
                controllerManager.getDirectorTimelineControl().removeShotNoCascade(directorShot);
            } 
            shot.setDirectorShot(null);
        }
    }

    /**
     * Checks whether or not a shot block's timing properties were altered.
     * @param event Camera update event with possible changes
     * @return Whether or not the timing properties changed
     */
    private boolean shotBlockTimingModified(CameraShotBlockUpdatedEvent event) {
        CameraShotBlock shotBlock = event.getCameraShotBlock();
        CameraShot shot = shotBlock.getShot();
        if (shotBlock.getBeginCount() == shot.getBeginCount()
                && shotBlock.getEndCount() == shot.getEndCount()
                && event.getOldTimelineNumber() == shotBlock.getTimetableNumber()) {
            return false;
        }
        return true;
    }
}
