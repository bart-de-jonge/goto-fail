package control;

import gui.misc.TweakingHelper;
import gui.modal.PreferencesModalView;
import gui.root.RootCenterArea;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * Controller for the PreferencesModalView.
 */
public class PreferencesViewController {

    private ControllerManager controllerManager;

    @Getter @Setter
    private PreferencesModalView preferencesModalView;

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

        preferencesModalView.getSaveButton().setOnMouseClicked(this::handleSaveButton);
        preferencesModalView.getCancelButton().setOnMouseClicked(this::handleCancelButton);
    }

    /**
     * Handles save button event
     * @param event the mouseEvent called.
     */
    public void handleSaveButton(MouseEvent event) {
        TweakingHelper.setColorChoice(
                preferencesModalView.getColorList().getSelectionModel().getSelectedIndex());
        preferencesModalView.hideModal();

    }

    /**
     * Handles cancel button event.
     * @param event the mouseEvent called.
     */
    public void handleCancelButton(MouseEvent event) {
        preferencesModalView.hideModal();
    }

}
