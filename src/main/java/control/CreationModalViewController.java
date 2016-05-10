package control;

import data.ScriptingProject;
import gui.events.DirectorShotCreationEvent;
import gui.modal.CreationModalView;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Class that controls the Creation Modal View.
 * @author Mark
 */
public class CreationModalViewController {

    private ControllerManager controllerManager;
    private CreationModalView creationModalView;

    public CreationModalViewController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
    }

    /**
     * When triggered, initializes and displays the modal view for
     * creation of a new block.
     * @param event mouse event.
     */
    public void showBlockCreationWindow(MouseEvent event) {
        // create the view
        creationModalView = new CreationModalView(this.controllerManager.getRootPane(),
                this.controllerManager.getScriptingProject().getCameraTimelines().size());

        // add escape key listener (this kills the window!)
        creationModalView.getDisplayScene().setOnKeyPressed(
            e -> {
                if (e.getCode().equals(KeyCode.ESCAPE)) {
                    creationModalView.getModalStage().close();
                }
            });

        // add all the other listeners for specific gui components.
        addStartfieldListeners();
        addEndfieldListeners();
        addCreateButtonListeners();
        addCancelButtonListeners();
    }

    /**
     * Add listeners for parsing to startfield.
     */
    private void addStartfieldListeners() {
        creationModalView.getStartField().setOnKeyPressed(
            e -> {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    creationModalView.getStartField().setText(
                            CountUtilities.parseCountNumber(
                                    creationModalView.getStartField().getText()));
                }
            });
        creationModalView.getStartField().focusedProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (!newValue) {
                    creationModalView.getStartField().setText(
                            CountUtilities.parseCountNumber(
                                    creationModalView.getStartField().getText()));
                }
            });
    }

    /**
     * Add listeners for parsing to endfield.
     */
    private void addEndfieldListeners() {
        creationModalView.getEndField().setOnKeyPressed(
            e -> {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    creationModalView.getEndField().setText(
                            CountUtilities.parseCountNumber(
                                    creationModalView.getEndField().getText()));
                }
            });
        creationModalView.getEndField().focusedProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (!newValue) {
                    creationModalView.getEndField().setText(
                            CountUtilities.parseCountNumber(
                                    creationModalView.getEndField().getText()));
                }
            });
    }

    /**
     * Add listeners for the create button.
     */
    private void addCreateButtonListeners() {
        creationModalView.getCreationButton().setOnAction(
            e -> {
                if (validateShot()) {
                    creationModalView.hideModal();
                    createDirectorShot(createDirectorShotEvent());
                }
            });
    }

    /**
     * Add listeners for the cancel button. (kills the window)
     */
    private void addCancelButtonListeners() {
        creationModalView.getCancelButton().setOnAction(
            e -> {
                creationModalView.getModalStage().close();
            });
    }

    /**
     * Validate current shot settings, update GUI accordingly.
     * @return whether the shot is valid.
     */
    private boolean validateShot() {
        String errorString = "";

        boolean aCameraSelected = false;
        for (CheckBox cb : creationModalView.getCameraCheckboxes()) {
            if (cb.isSelected()) {
                aCameraSelected = true;
            }
        }
        if (!aCameraSelected) {
            errorString = "Please select at least one camera for this shot.";
        }

        double startVal = Double.parseDouble(
                creationModalView.getStartField().getText());
        double endVal = Double.parseDouble(
                creationModalView.getEndField().getText());
        if (startVal >= endVal) {
            errorString = "Please make sure that the shot ends after it begins.\n";
        }

        if (creationModalView.getDescripField().getText().isEmpty()) {
            errorString = "Please add a description.\n";
        }

        if (creationModalView.getNameField().getText().isEmpty()) {
            errorString = "Please name your shot.\n";
        }

        if (errorString.isEmpty()) {
            return true;
        } else {
            creationModalView.getTitleLabel().setText(errorString);
            creationModalView.getTitleLabel().setTextFill(Color.RED);
            return false;
        }
    }

    /**
     * Build the shot creation event.
     * @return the shot creation event that was built.
     */
    private DirectorShotCreationEvent createDirectorShotEvent() {
        String shotName = creationModalView.getNameField().getText();
        String shotDescrip = creationModalView.getDescripField().getText();
        List<Integer> camerasInShot = getCamerasInShot();
        double startPoint = Double.parseDouble(creationModalView.getStartField().getText());
        double endPoint = Double.parseDouble(creationModalView.getEndField().getText());

        return new DirectorShotCreationEvent(shotName, shotDescrip, camerasInShot,
                startPoint, endPoint);
    }

    /**
     * Build list of cameras which are selected.
     * @return list of integers representing selected cameras
     */
    private List<Integer> getCamerasInShot() {
        List<Integer> camsInShot = new ArrayList<>();

        for (int i = 0; i < creationModalView.getCameraCheckboxes().size(); i++) {
            if (creationModalView.getCameraCheckboxes().get(i).isSelected()) {
                camsInShot.add(i);
            }
        }
        return camsInShot;
    }

    /**
     * Event handler for the creation of a director shot.
     * It adds the DirectorShot to the DirectorTimeline and adds the corresponding
     * camera shots via the TimelineController.
     * @param event shot creation event
     */
    private void createDirectorShot(DirectorShotCreationEvent event) {
        ScriptingProject script = this.controllerManager.getScriptingProject();

        script.getDirectorTimeline().addShot(event.getShotName(), event.getShotDescription(),
                event.getShotStart(), event.getShotEnd());

        TimelineController timelineController = this.controllerManager.getTimelineControl();
        event.getCamerasInShot().forEach(
            camInd -> {
                timelineController.addCameraShot(camInd, event.getShotName(),
                        event.getShotDescription(), event.getShotStart(),
                        event.getShotEnd());
            });
    }
}
