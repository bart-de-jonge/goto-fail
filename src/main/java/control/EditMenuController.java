package control;

import gui.modal.EditProjectModalView;

public class EditMenuController {
    
    private ControllerManager controllerManager;
    
    private EditProjectModalView editModal;
    
    public EditMenuController(ControllerManager manager) {
        this.controllerManager = manager;
    }
    
    public void editProject() {
        new EditProjectModalView(controllerManager.getRootPane());
    }

}
