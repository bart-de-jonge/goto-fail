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
    }
}
