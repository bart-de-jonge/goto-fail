package control;

import data.ScriptingProject;
import gui.CameraShotBlock;
import gui.DetailView;
import gui.ShotBlock;

/**
 * Created by Bart.
 */
public class DetailViewController {

    private DetailView detailView;
    private ControllerManager manager;
    private ScriptingProject project;

    /**
     * Constructor.
     * @param manager - the controller manager this controller belongs to
     */
    public DetailViewController(ControllerManager manager) {
        this.detailView = manager.getRootPane().getRootHeaderArea().getDetailView();
        this.manager = manager;
        this.project = manager.getScriptingProject();
        initDescription();
        initName();
        initBeginCount();
        initEndCount();
    }

    /**
     * Init the begincount handlers.
     */
    private void initBeginCount() {
        detailView.setBeginCount(0);

        detailView.getBeginCountField().textProperty().addListener(
                (observable, oldValue, newValue) -> {
                if (manager.getActiveShotBlock() != null) {
                    int newVal = !newValue.isEmpty() ? Integer.parseInt(newValue) : 0;
                    manager.getActiveShotBlock().setBeginCount(newVal);
                    if (manager.getActiveShotBlock() instanceof CameraShotBlock) {
                        ((CameraShotBlock) manager.getActiveShotBlock()).getShot()
                                .setBeginCount(newVal);
                    }
                }
            });
    }

    /**
     * Init the endcuont handlers.
     */
    private void initEndCount() {
        detailView.setEndCount(0);

        detailView.getEndCountField().textProperty().addListener(
                (observable, oldValue, newValue) -> {
                if (manager.getActiveShotBlock() != null) {
                    int newVal = !newValue.isEmpty() ? Integer.parseInt(newValue) : 0;
                    ShotBlock block = manager.getActiveShotBlock();
                    block.setEndCount(newVal);
                    if (manager.getActiveShotBlock() instanceof CameraShotBlock) {
                        ((CameraShotBlock) manager.getActiveShotBlock())
                                .getShot().setEndCount(newVal);
                    }
                }
            });
    }

    /**
     * Init the description handlers.
     */
    private void initDescription() {
        detailView.setDescription("");

        detailView.getDescriptionField().textProperty().addListener(
                (observable, oldValue, newValue) -> {
                if (manager.getActiveShotBlock() != null) {
                    manager.getActiveShotBlock().setDescription(newValue);
                    if (manager.getActiveShotBlock() instanceof CameraShotBlock) {
                        ((CameraShotBlock) manager.getActiveShotBlock()).getShot()
                                .setDescription(newValue);
                    }
                }
            });
    }

    /**
     * Init the name handler.
     */
    private void initName() {
        detailView.setName("");

        detailView.getNameField().textProperty().addListener(
                (observable, oldValue, newValue) -> {
                if (manager.getActiveShotBlock() != null) {
                    manager.getActiveShotBlock().setName(newValue);
                    if (manager.getActiveShotBlock() instanceof CameraShotBlock) {
                        ((CameraShotBlock) manager.getActiveShotBlock())
                                .getShot().setName(newValue);
                    }
                }
            });
    }

    /**
     * Method to signal that the active block is changed so we can update it.
     */
    public void activeBlockChanged() {
        if (manager.getActiveShotBlock() != null) {
            detailView.setDescription(manager.getActiveShotBlock().getDescription());
            detailView.setName(manager.getActiveShotBlock().getName());

            // TODO: Make doubles possible in detailview
            detailView.setBeginCount((int) manager.getActiveShotBlock().getBeginCount());
            detailView.setEndCount((int) manager.getActiveShotBlock().getEndCount());
        } else {
            detailView.resetDetails();
        }
    }
}
