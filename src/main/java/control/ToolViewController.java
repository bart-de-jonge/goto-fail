package control;

import data.CameraShot;
import data.DirectorShot;
import gui.centerarea.CameraShotBlock;
import gui.centerarea.DirectorShotBlock;
import gui.centerarea.ShotBlock;
import gui.headerarea.ToolView;

/**
 * Controller that manages the tool menu (e.g. shot creation menu).
 * @author alex
 */
public class ToolViewController {

    private ControllerManager controllerManager;
    private CreationModalViewController creationModalViewController;
    private ToolView toolView;

    /**
     * Constructor.
     * @param controllerManager Manager of the controllers.
     */
    public ToolViewController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
        this.creationModalViewController = new CreationModalViewController(this.controllerManager);
        this.toolView = controllerManager.getRootPane().getRootHeaderArea().getToolView();
        initializeTools();
    }

    /**
     * Initialize all tools in the toolbox. Adds the corresponding buttons to the views and sets up
     * the event handlers.
     */
    private void initializeTools() {
        // add handlers to toolbuttons
        toolView.getCameraBlockCreationTool().getButton().setOnMouseClicked(
                event -> creationModalViewController.showCameraCreationWindow());
        toolView.getDirectorBlockCreationTool().getButton().setOnMouseClicked(
                event -> creationModalViewController.showDirectorCreationWindow());
        toolView.getBlockDeletionTool().getButton().setOnMouseClicked(
                event -> deleteActiveCameraShot());
        toolView.getShotGenerationTool().getButton().setOnMouseClicked(
                event -> generateCameraShots());

        // If there is no active ShotBlock, then disable the delete button
        if (!this.controllerManager.getActiveShotBlock().isPresent()) {
            toolView.getBlockDeletionTool().disableButton();
            toolView.getShotGenerationTool().disableButton();
        }
    }

    /**
     * Deletes the active camera block.
     */
    private void deleteActiveCameraShot() {
        if (this.controllerManager.getActiveShotBlock().isPresent()) {
            ShotBlock currentShot = this.controllerManager.getActiveShotBlock().get();
            // TODO: Make this a more general deletion for the active timeline (i.e. director)
            if (currentShot instanceof CameraShotBlock) {
                CameraShotBlock cameraShotBlock = (CameraShotBlock) currentShot;
                this.controllerManager.getTimelineControl().removeCameraShot(cameraShotBlock);
            }
        }
    }

    /**
     * Create the director shot's corresponding camera shots.
     */
    private void generateCameraShots() {
        if (this.controllerManager.getActiveShotBlock().get() instanceof DirectorShotBlock) {
            DirectorShotBlock directorShotBlock = (DirectorShotBlock)
                    this.controllerManager.getActiveShotBlock().get();
            DirectorShot shot = directorShotBlock.getShot();
            shot.getTimelineIndices().forEach(index -> {
                    CameraShot subShot = new CameraShot(shot.getName(), shot.getDescription(),
                                                    shot.getBeginCount(), shot.getEndCount(), shot);
                    shot.addCameraShot(subShot);
                    this.controllerManager.getTimelineControl().addCameraShot(index, subShot);
                });
        }
    }

    /**
     * Called when the active shot selection changed.
     * ToolViewController then updates the buttons accordingly.
     */
    public void activeBlockChanged() {
        if (this.controllerManager.getActiveShotBlock().isPresent()) {
            toolView.getBlockDeletionTool().enableButton();
            // Only enable the generation of camera shots if it's a director shot w/o camera shots
            boolean enableGen = false;
            if (this.controllerManager.getActiveShotBlock().get() instanceof DirectorShotBlock) {
                DirectorShotBlock directorShotBlock = (DirectorShotBlock)
                        this.controllerManager.getActiveShotBlock().get();
                if (directorShotBlock.getShot().getCameraShots().isEmpty()) {
                    enableGen = true;
                }
            }

            if (enableGen) {
                toolView.getShotGenerationTool().enableButton();
            } else {
                toolView.getShotGenerationTool().disableButton();
            }
        } else {
            toolView.getBlockDeletionTool().disableButton();
            toolView.getShotGenerationTool().disableButton();
        }
    }
}
