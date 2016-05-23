package control;

import gui.modal.PreferencesModalView;
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
    }

}
