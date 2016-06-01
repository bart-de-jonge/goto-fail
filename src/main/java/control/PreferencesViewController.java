package control;

import gui.misc.TweakingHelper;
import gui.modal.PreferencesModalView;
import gui.modal.ReloadModalView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;

/**
 * Controller for the PreferencesModalView.
 */
public class PreferencesViewController {

    private ControllerManager controllerManager;

    @Getter
    private PreferencesModalView preferencesModalView;
    @Getter
    private ReloadModalView reloadModalView;

    /**
     * Class constructor.
     * @param controllerManager the controller manager.
     */
    public PreferencesViewController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
    }

    /**
     * When triggered, this initializes and displays the modal view for
     * preferences/settings.
     */
    public void showPreferencesWindow() {
        preferencesModalView = new PreferencesModalView(
                this.controllerManager.getRootPane());

        preferencesModalView.getSaveButton().setOnMouseClicked(this::handleApplyButton);
        preferencesModalView.getCancelButton().setOnMouseClicked(this::handleCancelButton);
    }

    /**
     * When triggered, initializes and displays modal view for reloading project.
     */
    public void showReloadWindow() {
        reloadModalView = new ReloadModalView(
                this.controllerManager.getRootPane());

        reloadModalView.getSaveButton().setOnMouseClicked(this::handleSavesaveButton);
        reloadModalView.getDontSaveButton().setOnMouseClicked(this::handleNoSavesaveButton);
        reloadModalView.getCancelButton().setOnMouseClicked(this::handleCancelsaveButton);
    }

    /**
     * Handles apply button event.
     * @param event the mouseEvent called.
     */
    public void handleApplyButton(MouseEvent event) {
        showReloadWindow();
    }

    /**
     * Handles cancel button event to close the preferences view.
     * @param event the mouseEvent called.
     */
    public void handleCancelButton(MouseEvent event) {
        preferencesModalView.hideModal();
    }

    /**
     * Handles save button event to close the reload view.
     * @param event the mouseEvent called.
     */
    public void handleSavesaveButton(MouseEvent event) {
        controllerManager.getProjectController().save();
        handleNoSavesaveButton(event);
    }

    /**
     * Handles no-save button event to close the reload view.
     * @param event the mouseEvent called.
     */
    public void handleNoSavesaveButton(MouseEvent event) {
        TweakingHelper.setColorChoice(
                preferencesModalView.getColorList().getSelectionModel().getSelectedIndex());

        controllerManager.getRootPane().getPrimaryStage().close();
        controllerManager.getRootPane().showRootPane();

        preferencesModalView.hideModal();
        reloadModalView.hideModal();

    }

    /**
     * Handles cancel button event to close the reload view.
     * @param event the mouseEvent called.
     */
    public void handleCancelsaveButton(MouseEvent event) {
        reloadModalView.hideModal();
    }

}
