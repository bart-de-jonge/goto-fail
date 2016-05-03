package control;

import data.ScriptingProject;
import gui.CameraShotBlock;
import gui.DetailView;
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
            manager.getActiveBlock().setBeginCount(Integer.parseInt(newValue));
            if (manager.getActiveBlock() instanceof CameraShotBlock) {
                ((CameraShotBlock) manager.getActiveBlock()).getShot().setStartCount(Integer.parseInt(newValue));
            }
        });
    }

    private void initEndCount() {
        detailView.setEndCount(0);

        detailView.getEndCountField().textProperty().addListener((observable, oldValue, newValue) -> {
            manager.getActiveBlock().setEndCount(Integer.parseInt(newValue));
            if (manager.getActiveBlock() instanceof CameraShotBlock) {
                ((CameraShotBlock) manager.getActiveBlock()).getShot().setEndCount(Integer.parseInt(newValue));
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
        detailView.setDescription(manager.getActiveBlock().getDescription());
        detailView.setName(manager.getActiveBlock().getName());

        // TODO: Make doubles possible in detailview
        detailView.setStartCount((int) manager.getActiveBlock().getBeginCount());
        detailView.setEndCount((int) manager.getActiveBlock().getEndCount());
    }
}
