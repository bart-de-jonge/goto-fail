package control;

import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.modal.CreationModalView;
import gui.events.DirectorShotCreationEvent;
import gui.centerarea.ShotBlock;
import gui.headerarea.ToolButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

/**
 * Controller that manages the tool menu (e.g. shot creation menu).
 * @author alex
 */
public class ToolViewController {

    private ControllerManager controllerManager;
    private ToolButton blockCreationTool;
    private ToolButton blockDeletionTool;
    private CreationModalView creationModalView;

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
                                           this.controllerManager
                                                              .getRootPane().getRootHeaderArea(),
                                           this::showBlockCreationWindow);
        blockDeletionTool = new ToolButton("Delete shot",
                                           this.controllerManager.getRootPane().getRootHeaderArea(),
                                           this::deleteActiveCameraShot);
        // If there is no active ShotBlock, then disable the delete button
        if (this.controllerManager.getActiveShotBlock() == null) {
            blockDeletionTool.disableButton();
        }
    }

    /**
     * When triggered, this initializes and displays the modal view for the creation of a new block.
     * @param event mouse event
     */
    private void showBlockCreationWindow(MouseEvent event) {
        creationModalView = new CreationModalView(this.controllerManager.getRootPane(),
                              this.controllerManager.getScriptingProject()
                                      .getCameraTimelines().size(),
                              this::createDirectorShot);

        // Add listeners for parsing to startfield
        creationModalView.getStartField().setOnKeyPressed(e -> {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    creationModalView.getStartField().setText(
                            CountUtilities.parseCountNumber(
                                    creationModalView.getStartField().getText()));
                }
            });
        creationModalView.getStartField().focusedProperty().addListener(
                (observable, oldValue, newValue) -> {
                if (!newValue) {
                    creationModalView.getStartField().setText(
                            CountUtilities.parseCountNumber(
                                    creationModalView.getStartField().getText()));
                }
            });

        // Add listeners for parsing to endfield
        creationModalView.getEndField().setOnKeyPressed(e -> {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    creationModalView.getEndField().setText(
                            CountUtilities.parseCountNumber(
                                    creationModalView.getEndField().getText()));
                }
            });
        creationModalView.getEndField().focusedProperty().addListener(
                (observable, oldValue, newValue) -> {
                if (!newValue) {
                    creationModalView.getEndField().setText(
                            CountUtilities.parseCountNumber(
                                    creationModalView.getEndField().getText()));
                }
            });
    }

    /**
     * Event handler for the creation of a director shot.
     * It adds the DirectorShot to the DirectorTimeline and adds the corresponding
     * camera shots via the TimelineController.
     * @param event shot creation event
     */
    private void createDirectorShot(DirectorShotCreationEvent event) {
        ScriptingProject script = this.controllerManager.getScriptingProject();

        script.getDirectorTimeline().addShot(event.getShotName(), event.getShotDescription(),
                                             event.getShotStart(), event.getShotEnd());

        TimelineController timelineController = this.controllerManager.getTimelineControl();
        event.getCamerasInShot().forEach(camInd -> {
                timelineController.addCameraShot(camInd, event.getShotName(),
                        event.getShotDescription(), event.getShotStart(),
                        event.getShotEnd());
            });
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
