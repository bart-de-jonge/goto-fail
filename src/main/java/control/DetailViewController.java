package control;

import data.CameraShot;
import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.headerarea.DetailView;
import gui.centerarea.ShotBlock;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.text.DecimalFormat;

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

        detailView.getBeginCountField().focusedProperty().addListener((observable, oldValue, newValue) -> {
            // exiting focus
            if (!newValue) {
                beginCountUpdateHelper();
            }
        });

        detailView.getBeginCountField().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                beginCountUpdateHelper();
            }
        });
    }

    private String formatDouble(double d) {
        if (d == (long) d) {
            return String.format("%d", (long) d);
        } else {
            return String.format("%s", d);
        }
    }

    private void beginCountUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {

            String newValue = detailView.getBeginCountField().getText();
            double newVal = newValue.isEmpty() ? 0 : Double.parseDouble(newValue);
            newVal = Math.round(newVal*4)/4f;
            detailView.getBeginCountField().setText(formatDouble(newVal));

            manager.getActiveShotBlock().setBeginCount(newVal);
            if (manager.getActiveShotBlock() instanceof CameraShotBlock) {
                ((CameraShotBlock) manager.getActiveShotBlock()).getShot()
                        .setBeginCount(newVal);
            }
        }
    }

    /**
     * Init the endcuont handlers.
     */
    private void initEndCount() {
        detailView.setEndCount(0);

        detailView.getEndCountField().focusedProperty().addListener((observable, oldValue, newValue) -> {
            // exiting focus
            if (!newValue) {
                beginCountUpdateHelper();
            }
        });

        detailView.getEndCountField().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                beginCountUpdateHelper();
            }
        });
    }

    private void endCountUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {

            String newValue = detailView.getEndCountField().getText();
            double newVal = newValue.isEmpty() ? 0 : Double.parseDouble(newValue);
            newVal = Math.round(newVal*4)/4f;
            detailView.getEndCountField().setText(formatDouble(newVal));

            manager.getActiveShotBlock().setEndCount(newVal);
            if (manager.getActiveShotBlock() instanceof CameraShotBlock) {
                ((CameraShotBlock) manager.getActiveShotBlock()).getShot()
                        .setEndCount(newVal);
            }
        }
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
