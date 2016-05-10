package control;

import data.ScriptingProject;
import gui.centerarea.CameraShotBlock;
import gui.headerarea.DetailView;
import javafx.scene.input.KeyCode;

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

        detailView.getBeginCountField().focusedProperty().addListener(
                (observable, oldValue, newValue) -> {
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

    /**
     * Helper for when the begincount field is edited.
     * Parses entry to quarters and updates all the values
     */
    private void beginCountUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {
            String newValue = CountUtilities.parseCountNumber(detailView.getBeginCountField().getText());
            detailView.getBeginCountField().setText(newValue);
            double newVal = Double.parseDouble(newValue);

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

        detailView.getEndCountField().focusedProperty().addListener(
                (observable, oldValue, newValue) -> {
                // exiting focus
                if (!newValue) {
                    endCountUpdateHelper();
                }
            });

        detailView.getEndCountField().setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    endCountUpdateHelper();
                }
            });
    }

    /**
     * Helper for when the endcount field is edited.
     * Parses entry to quarters and updates all the values
     */
    private void endCountUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {

            String newValue = CountUtilities.parseCountNumber(detailView.getBeginCountField().getText());
            detailView.getBeginCountField().setText(newValue);
            double newVal = Double.parseDouble(newValue);
            detailView.getEndCountField().setText(newValue);

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

            detailView.setBeginCount(manager.getActiveShotBlock().getBeginCount());
            detailView.setEndCount(manager.getActiveShotBlock().getEndCount());
        } else {
            detailView.resetDetails();
        }
    }
}
