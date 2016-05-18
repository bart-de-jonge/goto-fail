package control;

import data.ScriptingProject;
import gui.centerarea.ShotBlock;
import gui.modal.SaveModalView;
import gui.root.RootPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Class wrapper for model management controllers.
 */
@Log4j2
public class ControllerManager {

    @Getter
    private RootPane rootPane;

    @Getter
    private TimelineController timelineControl;

    @Getter
    private DirectorTimelineController directorTimelineControl;

    @Getter
    private ToolViewController toolViewController;

    @Getter
    private DetailViewController detailViewController;

    @Getter
    private FileMenuController fileMenuController;
    
    @Getter
    private EditMenuController editMenuController;

    @Getter
    private ShotBlock activeShotBlock;

    private SaveModalView saveModal;
    // Placeholder project in lieu of XML loading
    @Getter
    private ScriptingProject scriptingProject;

    /**
     * Constructor.
     *
     * @param rootPane Root Window
     */
    public ControllerManager(RootPane rootPane) {
        log.debug("Initializing new ControllerManager");

        this.rootPane = rootPane;
        initializeControllers();
        initOnCloseOperation();
    }

    /**
     * Overloaded constructor to directly pass controllers.
     *
     * @param rootPane             - the root window of the application
     * @param timelineController   - the controller that controls the centerarea
     * @param detailViewController - the controller that controls the detailview
     * @param toolViewController   - the controller that controls the toolview
     */
    public ControllerManager(RootPane rootPane, TimelineController timelineController,
                             DetailViewController detailViewController,
                             ToolViewController toolViewController) {
        log.debug("Initializing new ControllerManager");

        this.rootPane = rootPane;
        this.timelineControl = timelineController;
        this.detailViewController = detailViewController;
        this.toolViewController = toolViewController;
    }

    /**
     * Initialize all necessary controllers.
     */
    private void initializeControllers() {
        timelineControl = new TimelineController(this);
        directorTimelineControl = new DirectorTimelineController(this);
        detailViewController = new DetailViewController(this);
        toolViewController = new ToolViewController(this);
        fileMenuController = new FileMenuController(this);
        editMenuController = new EditMenuController(this);
    }

    /**
     * Set up a handler for when the close button is clicked.
     */
    private void initOnCloseOperation() {
        rootPane.getPrimaryStage().setOnCloseRequest(this::handleOnClose);
    }

    /**
     * Handler for the on close event.
     * @param event the WindowEvent for this handler
     */
    private void handleOnClose(WindowEvent event) {
        if (scriptingProject != null) {
            if (scriptingProject.isChanged()) {
                event.consume();
                initSaveModal();
            }
        }
    }

    /**
     * Init the modal that will be displayed if there are unsaved changes.
     */
    public void initSaveModal() {
        saveModal = new SaveModalView(rootPane);
        saveModal.getSaveButton().setOnMouseClicked(this::handleSave);
        saveModal.getDontSaveButton().setOnMouseClicked(this::handleDontSave);
        saveModal.getCancelButton().setOnMouseClicked(this::handleCancel);
    }

    /**
     * Handle a click on the save button.
     * @param event the MouseEvent for this handler.
     */
    private void handleSave(MouseEvent event) {
        fileMenuController.save();
        saveModal.hideModal();
        rootPane.getPrimaryStage().close();
    }

    /**
     * Handle a click on the don't save button.
     * @param event the MouseEvent for this handler
     */
    private void handleDontSave(MouseEvent event) {
        rootPane.getPrimaryStage().close();
        saveModal.hideModal();
    }

    /**
     * Handle a click on the cancel button.
     * @param event the MouseEvent for this handler
     */
    private void handleCancel(MouseEvent event) {
        saveModal.hideModal();
    }

    /**
     * Sets the active ShotBlock and notifies necessary controllers.
     *
     * @param block ShotBlock to set as active
     */
    public void setActiveShotBlock(ShotBlock block) {
        this.activeShotBlock = block;
        detailViewController.activeBlockChanged();
        toolViewController.activeBlockChanged();
    }

    public void setScriptingProject(ScriptingProject scriptingProject) {
        this.scriptingProject = scriptingProject;
    }

    /**
     * Changes the name the window currently has.
     */
    public void updateWindowTitle() {
        rootPane.getPrimaryStage().setTitle(getScriptingProject().getName());
    }
}
