package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import data.CameraShot;
import data.DirectorShot;
import data.DirectorTimeline;
import data.GeneralShotData;
import data.Instrument;
import data.Shot;
import gui.centerarea.DirectorShotBlock;
import gui.centerarea.ShotBlock;
import gui.events.DirectorShotBlockUpdatedEvent;
import gui.root.RootPane;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Controller for the DirectorTimeline.
 */
@Log4j2
public class DirectorTimelineController {

    private RootPane rootPane;

    @Getter
    private final ControllerManager controllerManager;

    // List of all currently colliding DirectorShotBlocks
    @Getter @Setter
    private ArrayList<DirectorShotBlock> overlappingShotBlocks;

    // Map of DirectorShots to their corresponding shot blocks
    @Getter @Setter
    private Map<DirectorShot, DirectorShotBlock> directorShotBlockMap;

    /**
     * Constructor.
     * @param controllerManager Root controller
     */
    public DirectorTimelineController(ControllerManager controllerManager) {
        log.debug("Constructing new TimelineController(controllerManager={})", controllerManager);

        this.controllerManager = controllerManager;
        this.rootPane = controllerManager.getRootPane();
        this.overlappingShotBlocks = new ArrayList<>();
        this.directorShotBlockMap = new HashMap<>();
    }

    /**
     * Add a directorShot to the directortimeline.
     * @param shot the shot to add
     */
    public void addDirectorShot(DirectorShot shot) {
        log.info("Adding DirectorShot to DirectorTimeline");

        this.controllerManager.getScriptingProject()
                .getDirectorTimeline()
                .addShot(shot);
        initShotBlock(shot);
    }
    
    /**
     * Remove a instrument from all director shots.
     * @param instrument the instrument to remove
     */
    public void removeInstrumentFromAllShots(Instrument instrument) {
        this.directorShotBlockMap.keySet().forEach(e -> {
                ShotBlock shotBlock = this.directorShotBlockMap.get(e);
                shotBlock.getInstruments().remove(instrument);
                shotBlock.getShot().getInstruments().remove(instrument);
                shotBlock.getTimetableBlock().removeInstrument(instrument);
                shotBlock.recompute();
            });
    }

    /**
     * Display an existing DirectorShot in the view.
     * @param shot DirectorShot to display
     */
    protected void initShotBlock(DirectorShot shot) {
        DirectorShotBlock shotBlock = new DirectorShotBlock(
            rootPane.getRootCenterArea(), this::shotChangedHandler, shot);
       

        controllerManager.setActiveShotBlock(shotBlock);
        this.directorShotBlockMap.put(shot, shotBlock);
        controllerManager.getScriptingProject().changed();

        // Check for collisions
        checkCollisions(shotBlock);
    }

    /**
     * Handle updated director shot. The previous timeline is used to retrieve the corresponding
     * shot. The correct {@link DirectorShot} is then updated using the latest
     * {@link DirectorShotBlock} position and counts. As the event is unclear as to
     * whether the shot has switched centerarea, it is removed from the previous timeline
     * and added to the new one.
     * @param event Camera shot change event.
     */
    public void shotChangedHandler(DirectorShotBlockUpdatedEvent event) {
        if (event.getDirectorShotBlock().getBeginCount()
                - event.getDirectorShotBlock().getPaddingBefore() < 0) {
            event.getDirectorShotBlock().moveAsCloseToTopAsPossible();
            // negative flight
        }
        controllerManager.getScriptingProject().changed();
        log.info("Shot moved");

        DirectorShotBlock changedBlock = event.getDirectorShotBlock();
        
        DirectorShot shot = changedBlock.getShot();

        
     // Adjust model
        shot.setBeginCount(changedBlock.getBeginCount());
        shot.setEndCount(changedBlock.getEndCount());
        
        controllerManager.getTimelineControl().getCameraShotBlocks().forEach(shotBlock -> {
                controllerManager.getTimelineControl()
                    .checkCollisions(shotBlock.getTimetableNumber(), shotBlock);
                shotBlock.recompute();
            });

        controllerManager.setActiveShotBlock(changedBlock);
        
        log.error("Before check colissions");
        // check for collisions
        checkCollisions(changedBlock);
        this.recomputeAllCollisions();
    
    }


    /**
     * Remove a director shot from both the display and the timeline.
     * @param shotBlock DirectorShotBlock to be removed
     */
    public void removeShot(DirectorShotBlock shotBlock) {
        DirectorShot directorShot = shotBlock.getShot();
        this.removeShotNoCascade(directorShot);

        directorShot.getCameraShots().forEach(cameraShot -> {
                this.controllerManager.getTimelineControl().removeCameraShot(cameraShot);
            });
    }

    /**
     * Removes a director shot from the display and timeline WITHOUT cascade to camera shots.
     * @param shot DirectorShot to be removed
     */
    public void removeShotNoCascade(DirectorShot shot) {
        // Remove the shot from the model
        DirectorTimeline directorTimeline = this.controllerManager.getScriptingProject()
               .getDirectorTimeline();
        directorTimeline.removeShot(shot);
        controllerManager.getScriptingProject().changed();

        DirectorShotBlock shotBlock = directorShotBlockMap.get(shot);

        if (shotBlock != null) {
            // If we are removing the active shot then this must be updated
            if (shotBlock.equals(controllerManager.getActiveShotBlock())) {
                controllerManager.setActiveShotBlock(null);
            }

            // Remove this shotBlock from view to reflect the model
            shotBlock.removeFromView();
        }

        directorShotBlockMap.remove(shot);
    }

    /**
     * Check for collisions (added and removed).
     * @param directorShotBlock - the shotblock to check collisions with
     */
    private void checkCollisions(DirectorShotBlock directorShotBlock) {
        log.error("Checking colissions for shot block {}", directorShotBlock.getShot().getName());
        log.error("Begin count is {}", directorShotBlock.getShot().getBeginCount());
        DirectorTimeline timeline = controllerManager.getScriptingProject()
                .getDirectorTimeline();

        // Remove overlaps for non-colliding shotblocks
        removeOverlap(directorShotBlock);

        // Check for collisions
        ArrayList<DirectorShot> overlappingShots = timeline
                .getOverlappingShots(directorShotBlock.getShot());
        if (overlappingShots.size() > 1) {
            // Collision detected
            ArrayList<DirectorShotBlock> overlappingShotBlocks = new ArrayList<>();
            Supplier<ArrayList<Integer>> supplier = ArrayList::new;
            ArrayList<Integer> instances = overlappingShots.stream().map(Shot::getInstance)
                    .collect(Collectors.toCollection(supplier));
            // Get CameraShotBlock

            this.directorShotBlockMap.values().stream().filter(
                shotBlock -> instances.contains(shotBlock.getShotId()))
                .forEach(shotBlock -> {
                        overlappingShotBlocks.add(shotBlock);
                        if (!this.overlappingShotBlocks.contains(shotBlock)) {
                            this.overlappingShotBlocks.add(shotBlock);
                        }
                    });
            // Make DirectorShotBlocks red
            for (DirectorShotBlock shotBlock : overlappingShotBlocks) {
                log.error("Overlaps with {}", shotBlock.getName());
                shotBlock.setColliding(true);
            }
        } else {
            resetColliding(directorShotBlock);
        }
    }
    
    /**
     * Remove overlap from this director shot block.
     * @param directorShotBlock the shot block to do this for.
     */
    private void removeOverlap(DirectorShotBlock directorShotBlock) {
        ArrayList<DirectorShotBlock> toRemove = new ArrayList<>();
        this.overlappingShotBlocks.stream()
                .filter(shotBlock ->
                    shotBlock.getShot().getCollidesWith().isEmpty()).forEach(
                        shotBlock -> {
                            shotBlock.setColliding(false);
                            toRemove.add(shotBlock);
                        });
        this.overlappingShotBlocks.removeAll(toRemove);
        directorShotBlock.getShot().getCollidesWith().forEach(shot -> {
                shot.setColliding(false);
                directorShotBlockMap.get(shot).recompute();
                log.error("Setting false for {}", shot.getName());
            });
    }
    
    /**
     * Recompute all the collisions in the director timeline.
     */
    public void recomputeAllCollisions() {
        this.directorShotBlockMap.keySet().forEach(shot -> {
                this.checkCollisions(directorShotBlockMap.get(shot));
            });
    }
    
    /**
     * Reset the colliding status for a shot block.
     * @param directorShotBlock the shot block to do that for
     */
    private void resetColliding(DirectorShotBlock directorShotBlock) {
        directorShotBlock.setColliding(false);
        directorShotBlock.getShot().setColliding(false);
        directorShotBlock.getShot().getCollidesWith().forEach(e -> {
                DirectorShotBlock toReset = directorShotBlockMap.get(e);
                if (toReset != null) {
                    this.checkCollisions(toReset);
                }
            });
        removeCollisionFromDirectorShotBlock(directorShotBlock);
        
    }
    
    /**
     * Remove collissions from a director shot block.
     * @param directorShotBlock the block to do that for
     */
    private void removeCollisionFromDirectorShotBlock(DirectorShotBlock directorShotBlock) {
        ArrayList<Shot> toRemove = new ArrayList<>();
        for (Shot shot : directorShotBlock.getShot().getCollidesWith()) {
            toRemove.add(shot);
            if (shot.getCollidesWith().contains(directorShotBlock.getShot())) {
                shot.getCollidesWith().remove(directorShotBlock.getShot());
            }
        }
        directorShotBlock.getShot().getCollidesWith().removeAll(toRemove);
    }

    /**
     * Generate all linked CameraShots for DirectorShots which hadn't previously generated them.
     */
    public void generateAllShots() {
        log.info("CALLED GENERATE ALL SHOTS");
        directorShotBlockMap.keySet().forEach(shot -> {
                if (shot.getCameraShots().isEmpty()) {
                    // Camera shots need to take the director shot's padding into account
                    double cameraStart = shot.getBeginCount() - shot.getFrontShotPadding();
                    double cameraEnd = shot.getEndCount() + shot.getEndShotPadding();

                    shot.getTimelineIndices().forEach(index -> {
                            CameraShot subShot = new CameraShot(new GeneralShotData(shot.getName(),
                                                                shot.getDescription(),
                                                                cameraStart,
                                                                cameraEnd),
                                                                shot);
                            shot.addCameraShot(subShot);
                            this.controllerManager.getTimelineControl()
                                    .addCameraShot(index, subShot);
                        });
                }
            });
    }
}
