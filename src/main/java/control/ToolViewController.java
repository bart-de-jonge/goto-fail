package control;

import gui.centerarea.CameraShotBlock;
import gui.centerarea.ShotBlock;
import gui.headerarea.ToolButton;
import javafx.scene.input.MouseEvent;

/**
 * Controller that manages the tool menu (e.g. shot creation menu).
 * @author alex
 */
public class ToolViewController {

    private ControllerManager controllerManager;
    private ToolButton blockCreationTool;
    private ToolButton blockDeletionTool;

    /**
     * Constructor.
     * @param controllerManager Manager of the controllers.
     */
    public ToolViewController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
        initializeTools();
    }

    /**
     * Initialize all tools in the toolbox. Adds the corresponding buttons to the views and sets up
     * the event handlers.
     */
    private void initializeTools() {
        blockCreationTool = new ToolButton("Add a block",
                                           this.controllerManager.getRootPane().getRootHeaderArea(),
                                           this.controllerManager.getCreationModalViewController()
                                                   ::showBlockCreationWindow);
        blockDeletionTool = new ToolButton("Delete shot",
                                           this.controllerManager.getRootPane().getRootHeaderArea(),
                                           this::deleteActiveCameraShot);
        // If there is no active ShotBlock, then disable the delete button
        if (this.controllerManager.getActiveShotBlock() == null) {
            blockDeletionTool.disableButton();
        }
    }

    /**
     * Deletes the active camera block.
     * @param event mouse event
     */
    private void deleteActiveCameraShot(MouseEvent event) {
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
