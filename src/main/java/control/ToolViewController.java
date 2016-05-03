package control;

import gui.CreationModalView;
import gui.ModalView;
import gui.ToolButton;
import javafx.scene.input.MouseEvent;

/**
 * Controller that manages the tool menu (e.g. shot creation menu).
 * @author alex
 */
public class ToolViewController {

    private ControllerManager controllerManager;

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
        ToolButton blockCreationTool = new ToolButton("Add a block",
                                                      this.controllerManager
                                                              .getRootPane().getRootHeaderArea(),
                                                      this::showBlockCreationWindow);
    }

    /**
     * When triggered, this initializes and displays the modal view for the creation of a new block.
     * TODO: Add modal and get rid of placeholder block
     * @param event mouse event
     */
    private void showBlockCreationWindow(MouseEvent event) {
        this.controllerManager.getTimelineControl()
                .addCameraShot(1, "BOOM", "Een description", 1, 2);
        new CreationModalView(this.controllerManager.getRootPane(),
                              this.controllerManager.getScriptingProject()
                                      .getCameraTimelines().size());
    }
}
