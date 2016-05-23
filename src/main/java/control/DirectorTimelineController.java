package control;

import data.CameraShot;
import data.DirectorShot;
import data.DirectorTimeline;
import data.Shot;
import gui.centerarea.CameraShotBlock;
import gui.centerarea.DirectorShotBlock;
import gui.events.DirectorShotBlockUpdatedEvent;
import gui.root.RootPane;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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

    @Getter
    // List of all DirectorShotBlocks in this timeline
    private ArrayList<DirectorShotBlock> shotBlocks;

    // List of all currently colliding DirectorShotBlocks
    private ArrayList<DirectorShotBlock> overlappingShotBlocks;

    /**
     * Constructor.
     * @param controllerManager Root controller
     */
    public DirectorTimelineController(ControllerManager controllerManager) {
        log.debug("Constructing new TimelineController(controllerManager={})", controllerManager);

        this.controllerManager = controllerManager;
        this.rootPane = controllerManager.getRootPane();
        this.shotBlocks = new ArrayList<>();
        this.overlappingShotBlocks = new ArrayList<>();
    }

    /**
     * Add a director shot to the corresponding timeline.
     * @param name Name of the shot
     * @param description Shot description
     * @param startCount Start count
     * @param endCount End count
     * @param frontPadding additional recording time at the front
     * @param endPadding additional recording time at the end
     * @param cameras the cameras to generate the shot to
     */
    public void addDirectorShot(String name, String description,
                                double startCount, double endCount,
                                double frontPadding, double endPadding,
                                List<Integer> cameras) {
        log.info("Adding DirectorShot to DirectorTimeline");

        DirectorShot newShot = new DirectorShot(name, description, startCount, endCount,
                frontPadding, endPadding, cameras);
        this.controllerManager.getScriptingProject()
                .getDirectorTimeline()
                .addShot(newShot);
        DirectorShotBlock shotBlock = new DirectorShotBlock(newShot.getInstance(),
                rootPane.getRootCenterArea(), startCount, endCount,
                description, name, this::shotChangedHandler, newShot);

        controllerManager.setActiveShotBlock(shotBlock);
        this.shotBlocks.add(shotBlock);
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
        controllerManager.getScriptingProject().changed();
        log.info("Shot moved");

        DirectorShotBlock changedBlock = event.getDirectorShotBlock();

        controllerManager.setActiveShotBlock(changedBlock);

        DirectorShot shot = changedBlock.getShot();

        // Adjust model
        shot.setBeginCount(changedBlock.getBeginCount());
        shot.setEndCount(changedBlock.getEndCount());

        // check for collisions
        checkCollisions(changedBlock);
    }


    /**
     * Remove a director shot from both the display and the timeline.
     * @param shotBlock DirectorShotBlock to be removed
     */
    public void removeShot(DirectorShotBlock shotBlock) {
        // If we are removing the active shot, then this must be updated accordingly
        if (this.controllerManager.getActiveShotBlock().equals(shotBlock)) {
            this.controllerManager.setActiveShotBlock(null);
        }

        // Remove the shot from the model
        DirectorTimeline directorTimeline = this.controllerManager.getScriptingProject()
                .getDirectorTimeline();
        directorTimeline.removeShot(shotBlock.getShot());
        controllerManager.getScriptingProject().changed();

        // Then remove the shot from the view
        shotBlock.removeFromView();
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
            this.shotBlocks.stream().filter(
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

    public void generateAllShots() {
        log.info("CALLED GENERATE ALL SHOTS");
        shotBlocks.forEach(directorShotBlock -> {
            DirectorShot shot = directorShotBlock.getShot();
            if (shot.getCameraShots().isEmpty()) {
                // Camera shots need to take the director shot's padding into account when making a shot
                double cameraStart = shot.getBeginCount() - shot.getFrontShotPadding();
                double cameraEnd = shot.getEndCount() + shot.getEndShotPadding();


                shot.getTimelineIndices().forEach(index -> {
                    CameraShot subShot = new CameraShot(shot.getName(), shot.getDescription(),
                                                        cameraStart, cameraEnd, shot);
                    shot.addCameraShot(subShot);
                    this.controllerManager.getTimelineControl().addCameraShot(index, subShot);
                });
            }
        });
    }

    /**
     * Remove all collisions and from this shotblock.
     * Removes the collisions from the counterpart in each collision as well
     * @param shotBlock - the shotblock to remove the collisions from
     */
    private void removeCollisionFromCameraShotBlock(CameraShotBlock shotBlock) {
        ArrayList<Shot> toRemove = new ArrayList<>();
        for (Shot shot : shotBlock.getShot().getCollidesWith()) {
            toRemove.add(shot);
            if (shot.getCollidesWith().contains(shotBlock.getShot())) {
                shot.getCollidesWith().remove(shotBlock.getShot());
            }
        }
        shotBlock.getShot().getCollidesWith().removeAll(toRemove);
    }

}
