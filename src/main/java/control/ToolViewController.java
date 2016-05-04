package control;

import data.ScriptingProject;
import gui.CreationModalView;
import gui.DirectorShotCreationEvent;
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
     * @param event mouse event
     */
    private void showBlockCreationWindow(MouseEvent event) {
        new CreationModalView(this.controllerManager.getRootPane(),
                              this.controllerManager.getScriptingProject()
                                      .getCameraTimelines().size(),
                              this::createDirectorShot);
    }

    /**
     * Event handler for the creation of a director shot.
     * It adds the DirectorShot to the DirectorTimeline and adds the corresponding
     * camera shots via the TimelineController.
     * @param event shot creation event
     */
    private void createDirectorShot(DirectorShotCreationEvent event) {
        ScriptingProject script = this.controllerManager.getScriptingProject();
        // TODO: Remove cast to int once half-counts etc. are supported
        script.getDirectorTimeline().addShot(event.getShotName(), event.getShotDescription(),
                                             (int) event.getShotStart(), (int) event.getShotEnd());

        TimelineController tlineControl = this.controllerManager.getTimelineControl();
        event.getCamerasInShot().forEach(camInd -> {
                tlineControl.addCameraShot(camInd,
                                           event.getShotName(), event.getShotDescription(),
                                           (int) event.getShotStart(), (int) event.getShotEnd());
            });
    }
}
