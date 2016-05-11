package control;

import gui.centerarea.CameraShotBlock;
import gui.centerarea.ShotBlock;
import gui.headerarea.ToolButton;

/**
 * Controller that manages the tool menu (e.g. shot creation menu).
 * @author alex
 */
public class ToolViewController {

    private ControllerManager controllerManager;
    private ToolButton blockDeletionTool;

    private CreationModalViewController creationModalViewController;

    /**
     * Constructor.
     * @param controllerManager Manager of the controllers.
     */
    public ToolViewController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
        this.creationModalViewController = new CreationModalViewController(this.controllerManager);
        initializeTools();
    }

    /**
     * Initialize all tools in the toolbox. Adds the corresponding buttons to the views and sets up
     * the event handlers.
     */
    private void initializeTools() {
        // Toolbutton for creating a camerablock
        ToolButton cameraBlockCreationTool = new ToolButton("Add camerashot",
                this.controllerManager.getRootPane().getRootHeaderArea());
        cameraBlockCreationTool.getButton().setOnMouseClicked(event -> creationModalViewController.showCameraCreationWindow());

        // Toolbutton for creating a directorblock
        ToolButton directorBlockCreationTool = new ToolButton("Add directorshot",
                this.controllerManager.getRootPane().getRootHeaderArea());
        directorBlockCreationTool.getButton().setOnMouseClicked(event -> creationModalViewController.showDirectorCreationWindow());

        // Toolbutton for deleting shots
        blockDeletionTool = new ToolButton("Delete shot",
                this.controllerManager.getRootPane().getRootHeaderArea());
        blockDeletionTool.getButton().setOnMouseClicked(event -> deleteActiveCameraShot());

        // If there is no active ShotBlock, then disable the delete button
        if (this.controllerManager.getActiveShotBlock() == null) {
            blockDeletionTool.disableButton();
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
     * Called when the active shot selection changed.
     * ToolViewController then updates the buttons accordingly.
     */
    public void activeBlockChanged() {
        if (this.controllerManager.getActiveShotBlock() != null) {
            this.blockDeletionTool.enableButton();
        } else {
            this.blockDeletionTool.disableButton();
        }
    }
}
