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

    public DetailViewController(ControllerManager manager) {
        this.detailView = manager.getRootPane().getRootHeaderArea().getDetailView();
        this.manager = manager;
        this.project = manager.getScriptingProject();
        initDescription();
        initName();
        initStartCount();
        initEndCount();
    }

    private void initStartCount() {
        detailView.setStartCount(0);

        detailView.getStartCountField().textProperty().addListener((observable, oldValue, newValue) -> {
            int newVal = !newValue.isEmpty() ? Integer.parseInt(newValue) : 0;
            manager.getActiveBlock().setBeginCount(newVal);
            if (manager.getActiveBlock() instanceof CameraShotBlock) {
                ((CameraShotBlock) manager.getActiveBlock()).getShot().setStartCount(newVal);
            }
        });
    }

    private void initEndCount() {
        detailView.setEndCount(0);

        detailView.getEndCountField().textProperty().addListener((observable, oldValue, newValue) -> {
            int newVal = !newValue.isEmpty() ? Integer.parseInt(newValue) : 0;
            ShotBlock block = manager.getActiveBlock();
            block.setEndCount(newVal);
            if (manager.getActiveBlock() instanceof CameraShotBlock) {
                ((CameraShotBlock) manager.getActiveBlock()).getShot().setEndCount(newVal);
            }
        });
    }

    private void initDescription() {
        detailView.setDescription("");

        detailView.getDescriptionField().textProperty().addListener((observable, oldValue, newValue) -> {
            manager.getActiveBlock().setDescription(newValue);
            if (manager.getActiveBlock() instanceof CameraShotBlock) {
                ((CameraShotBlock) manager.getActiveBlock()).getShot().setDescription(newValue);
            }
        });
    }

    private void initName() {
        detailView.setName("");

        detailView.getNameField().textProperty().addListener((observable, oldValue, newValue) -> {
            manager.getActiveBlock().setName(newValue);
            if (manager.getActiveBlock() instanceof CameraShotBlock) {
                ((CameraShotBlock) manager.getActiveBlock()).getShot().setName(newValue);
            }
        });
    }

    public void activeBlockChanged() {
        if (manager.getActiveBlock() != null) {
            detailView.setDescription(manager.getActiveBlock().getDescription());
            detailView.setName(manager.getActiveBlock().getName());

            // TODO: Make doubles possible in detailview
            detailView.setStartCount((int) manager.getActiveBlock().getBeginCount());
            detailView.setEndCount((int) manager.getActiveBlock().getEndCount());
        }
    }
}
