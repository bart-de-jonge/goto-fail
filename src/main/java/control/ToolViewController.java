package control;

import gui.centerarea.CameraShotBlock;
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

        // If there is no active ShotBlock, then disable the delete button
        if (this.controllerManager.getActiveShotBlock() == null) {
            toolView.getBlockDeletionTool().disableButton();
        }
    }

    /**
     * Deletes the active camera block.
     */
    private void deleteActiveCameraShot() {
        ShotBlock currentShot = this.controllerManager.getActiveShotBlock();

        // TODO: Make this a more general deletion for the active timeline (i.e. director)
        if (currentShot instanceof CameraShotBlock) {
            CameraShotBlock cameraShotBlock = (CameraShotBlock) currentShot;
            this.controllerManager.getTimelineControl().removeCameraShot(cameraShotBlock);
        }
    }

    /**
     * Create the director shot's corresponding camera shots.
     */
    private void generateCameraShots() {
        // TODO: Generate the camera shots
    }

    /**
     * Called when the active shot selection changed.
     * ToolViewController then updates the buttons accordingly.
     */
    public void activeBlockChanged() {
        if (this.controllerManager.getActiveShotBlock() != null) {
            toolView.getBlockDeletionTool().enableButton();
        } else {
            toolView.getBlockDeletionTool().disableButton();
        }
    }
}
