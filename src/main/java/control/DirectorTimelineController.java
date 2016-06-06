package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import data.CameraShot;
import data.DirectorShot;
import data.DirectorTimeline;
import data.Shot;
import gui.centerarea.DirectorShotBlock;
import gui.events.DirectorShotBlockUpdatedEvent;
import gui.root.RootPane;
import lombok.Getter;
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
    private ArrayList<DirectorShotBlock> overlappingShotBlocks;

    // Map of DirectorShots to their corresponding shot blocks
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

        
        
        

        // check for collisions
        checkCollisions(changedBlock);
    
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
        DirectorTimeline timeline = controllerManager.getScriptingProject()
                .getDirectorTimeline();

        // Remove overlaps for non-colliding shotblocks
        ArrayList<DirectorShotBlock> toRemove = new ArrayList<>();
        this.overlappingShotBlocks.stream()
                .filter(shotBlock ->
                    shotBlock.getShot().getCollidesWith().isEmpty()).forEach(
                        shotBlock -> {
                            shotBlock.setColliding(false);
                            toRemove.add(shotBlock);
                        });
        this.overlappingShotBlocks.removeAll(toRemove);

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
                shotBlock.setColliding(true);
            }
        }
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
                            CameraShot subShot = new CameraShot(shot.getName(),
                                                                shot.getDescription(),
                                                                cameraStart,
                                                                cameraEnd,
                                                                shot);
                            shot.addCameraShot(subShot);
                            this.controllerManager.getTimelineControl()
                                    .addCameraShot(index, subShot);
                        });
                }
            });
    }
}
