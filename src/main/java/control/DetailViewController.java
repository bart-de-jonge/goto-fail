package control;

import data.ScriptingProject;
import gui.CameraShotBlock;
import gui.DetailView;
import gui.ShotBlock;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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

        detailView.getBeginCountField().textProperty().addListener((
                observable, oldValue, newValue) -> {
                if (manager.getActiveBlock() != null) {
                    int newVal = !newValue.isEmpty() ? Integer.parseInt(newValue) : 0;
                    manager.getActiveBlock().setBeginCount(newVal);
                    if (manager.getActiveBlock() instanceof CameraShotBlock) {
                        ((CameraShotBlock) manager.getActiveBlock()).getShot()
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

        detailView.getEndCountField().textProperty().addListener((
                observable, oldValue, newValue) -> {
                if (manager.getActiveBlock() != null) {
                    int newVal = !newValue.isEmpty() ? Integer.parseInt(newValue) : 0;
                    ShotBlock block = manager.getActiveBlock();
                    block.setEndCount(newVal);
                    if (manager.getActiveBlock() instanceof CameraShotBlock) {
                        ((CameraShotBlock) manager.getActiveBlock()).getShot().setEndCount(newVal);
                    }
                }
            });
    }

    /**
     * Init the description handlers.
     */
    private void initDescription() {
        detailView.setDescription("");

        detailView.getDescriptionField().textProperty().addListener((
                observable, oldValue, newValue) -> {
                if (manager.getActiveBlock() != null) {
                    manager.getActiveBlock().setDescription(newValue);
                    if (manager.getActiveBlock() instanceof CameraShotBlock) {
                        ((CameraShotBlock) manager.getActiveBlock()).getShot()
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

        detailView.getNameField().textProperty().addListener((
                observable, oldValue, newValue) -> {
                if (manager.getActiveBlock() != null) {
                    manager.getActiveBlock().setName(newValue);
                    if (manager.getActiveBlock() instanceof CameraShotBlock) {
                        ((CameraShotBlock) manager.getActiveBlock()).getShot().setName(newValue);
                    }
                }
            });
    }

    /**
     * Method to signal that the active block is changed so we can update it.
     */
    public void activeBlockChanged() {
        if (manager.getActiveBlock() != null) {
            detailView.setDescription(manager.getActiveBlock().getDescription());
            detailView.setName(manager.getActiveBlock().getName());

            // TODO: Make doubles possible in detailview
            detailView.setBeginCount((int) manager.getActiveBlock().getBeginCount());
            detailView.setEndCount((int) manager.getActiveBlock().getEndCount());
        }
    }
}
