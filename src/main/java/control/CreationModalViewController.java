package control;

import data.CameraShot;
import data.DirectorShot;
import data.GeneralShotData;
import gui.modal.CameraShotCreationModalView;
import gui.modal.DirectorShotCreationModalView;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;


/**
 * Controller for the CreationModalView.
 */
public class CreationModalViewController {

    private ControllerManager controllerManager;

    @Getter @Setter
    private CameraShotCreationModalView cameraShotCreationModalView;
    @Getter @Setter
    private DirectorShotCreationModalView directorShotCreationModalView;

    public CreationModalViewController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
    }

    /**
     * When triggered, this initializes and displays the modal view for the creation of
     * a new CameraBlock.
     */
    public void showCameraCreationWindow() {
        cameraShotCreationModalView = new CameraShotCreationModalView(
                this.controllerManager.getRootPane(),
                this.controllerManager.getScriptingProject().getCameraTimelines(),
                this.controllerManager.getScriptingProject().getInstruments());

        // Add mouse handlers
        cameraShotCreationModalView.getCreationButton().setOnMouseReleased(
                this::createCameraShot);
        cameraShotCreationModalView.getCancelButton().setOnMouseReleased(
                this::cameraCreationCancelButtonHandler);

        // Add listeners for parsing to startfield
        cameraShotCreationModalView.getStartField().setOnKeyPressed(
                this::cameraShotStartCountEnterHandler);
        cameraShotCreationModalView.getStartField().focusedProperty().addListener(
                this::cameraShotStartCountFocusHandler);

        // Add listeners for parsing to endfield
        cameraShotCreationModalView.getEndField().setOnKeyPressed(
                this::cameraShotEndCountEnterHandler);
        cameraShotCreationModalView.getEndField().focusedProperty().addListener(
                this::cameraShotEndCountFocusHandler);
    }

    /**
     * Handler for when enter is pressed on the startcount field in camera creation.
     * @param event - the keyevent
     */
    private void cameraShotStartCountEnterHandler(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            cameraShotCreationModalView.getStartField().setText(
                    CountUtilities.parseCountNumber(
                            cameraShotCreationModalView.getStartField().getText()));
        }
    }

    /**
     * Handler for when focus is lost on the startcount field in camera creation.
     * @param observable - the observable
     * @param oldValue - the old value of the focus
     * @param newValue - the new value of the focus
     */
    private void cameraShotEndCountFocusHandler(ObservableValue<? extends Boolean> observable,
                                                Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            cameraShotCreationModalView.getEndField().setText(
                    CountUtilities.parseCountNumber(
                            cameraShotCreationModalView.getEndField().getText()));
        }
    }

    /**
     * Handler for when enter is pressed on the endcount field in camera creation.
     * @param event - the keyevent
     */
    private void cameraShotEndCountEnterHandler(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            cameraShotCreationModalView.getEndField().setText(
                    CountUtilities.parseCountNumber(
                            cameraShotCreationModalView.getEndField().getText()));
        }
    }

    /**
     * Handler for when focus is lost on the endcount field in camera creation.
     * @param observable - the observable
     * @param oldValue - the old value of the focus
     * @param newValue - the new value of the focus
     */
    private void cameraShotStartCountFocusHandler(ObservableValue<? extends Boolean> observable,
                                                  Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            cameraShotCreationModalView.getStartField().setText(
                    CountUtilities.parseCountNumber(
                            cameraShotCreationModalView.getStartField().getText()));
        }
    }

    /**
     * Event handler for the creation of a camera shot.
     * It adds the CameraShot to the CameraTimeline and adds the corresponding
     * camera shots via the TimelineController.
     * @param event - the event to handle
     */
    private void createCameraShot(MouseEvent event) {
        if (validateCameraShot()) {
            TimelineController timelineController = this.controllerManager.getTimelineControl();
            
            CameraShot shot = new CameraShot(
                    cameraShotCreationModalView.getNameField().getText(),
                    cameraShotCreationModalView.getDescriptionField().getText(),
                Double.parseDouble(
                        cameraShotCreationModalView.getStartField().getText()),
                Double.parseDouble(
                        cameraShotCreationModalView.getEndField().getText()));

            cameraShotCreationModalView.getInstrumentsInShot().forEach(shotIndex -> {
                    shot.addInstrument(controllerManager.getScriptingProject()
                            .getInstruments().get(shotIndex));
                });

            cameraShotCreationModalView.getCamerasInShot().forEach(cameraIndex -> {
                    timelineController.addCameraShot(cameraIndex, shot.clone());
                });

            cameraShotCreationModalView.getModalStage().close();
        }
    }

    private void cameraCreationCancelButtonHandler(MouseEvent event) {
        cameraShotCreationModalView.getModalStage().close();
    }

    /**
     * Validates that the fields are correctly filled, and if not, isplays
     * a corresponding error message.
     * @return whether or not the fields are valid
     */
    protected boolean validateCameraShot() {
        String errorString = "";

        boolean aCameraSelected = false;
        for (CheckBox cb : cameraShotCreationModalView.getCameraCheckboxes()) {
            if (cb.isSelected()) {
                aCameraSelected = true;
            }
        }

        if (!aCameraSelected) {
            errorString = "Please select at least one camera for this shot.";
        }

        double startVal = Double.parseDouble(cameraShotCreationModalView.getStartField().getText());
        double endVal = Double.parseDouble(cameraShotCreationModalView.getEndField().getText());
        if (startVal >= endVal) {
            errorString = "Please make sure that the shot ends after it begins.\n";
        }

        if (cameraShotCreationModalView.getDescriptionField().getText().isEmpty()) {
            errorString = "Please add a description.\n";
        }

        if (cameraShotCreationModalView.getNameField().getText().isEmpty()) {
            errorString = "Please name your shot.\n";
        }

        if (errorString.isEmpty()) {
            return true;
        } else {
            cameraShotCreationModalView.getTitleLabel().setText(errorString);
            cameraShotCreationModalView.getTitleLabel().setTextFill(Color.RED);
            return false;
        }
    }

    /**
     * When triggered, this initializes and displays the modal view for
     * the creation of a new DirectorBlock.
     */
    public void showDirectorCreationWindow() {
        directorShotCreationModalView = new DirectorShotCreationModalView(
                this.controllerManager.getRootPane(),
                this.controllerManager.getScriptingProject().getCameraTimelines(),
                this.controllerManager.getScriptingProject().getInstruments());

        // add mouse handlers
        directorShotCreationModalView.getCancelButton().setOnMouseReleased(
                event -> directorShotCreationModalView.getModalStage().close());
        directorShotCreationModalView.getCreationButton().setOnMouseReleased(
                this::createDirectorShot);

        // Add listeners for parsing to startfield
        directorShotCreationModalView.getStartField().setOnKeyPressed(
                this::directorShotStartCountEnterHandler);
        directorShotCreationModalView.getStartField().focusedProperty().addListener(
                this::directorShotStartCountFocusHandler);

        // Add listeners for parsing to endfield
        directorShotCreationModalView.getEndField().setOnKeyPressed(
                this::directorShotEndCountEnterHandler);
        directorShotCreationModalView.getEndField().focusedProperty().addListener(
                this::directorShotEndCountFocusHandler);
    }
    
    /**
     * Handler for when enter is pressed on the startcount field in directorshot creation.
     * @param event - the keyevent
     */
    private void directorShotStartCountEnterHandler(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            directorShotCreationModalView.getStartField().setText(
                    CountUtilities.parseCountNumber(
                            directorShotCreationModalView.getStartField().getText()));
        }
    }

    /**
     * Handler for when focus is lost on the startcount field in directorshot creation.
     * @param observable - the observable
     * @param oldValue - the old value of the focus
     * @param newValue - the new value of the focus
     */
    private void directorShotEndCountFocusHandler(ObservableValue<? extends Boolean> observable,
                                                Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            directorShotCreationModalView.getEndField().setText(
                    CountUtilities.parseCountNumber(
                            directorShotCreationModalView.getEndField().getText()));
        }
    }

    /**
     * Handler for when enter is pressed on the endcount field in directorshot creation.
     * @param event - the keyevent
     */
    private void directorShotEndCountEnterHandler(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            directorShotCreationModalView.getEndField().setText(
                    CountUtilities.parseCountNumber(
                            directorShotCreationModalView.getEndField().getText()));
        }
    }

    /**
     * Handler for when focus is lost on the endcount field in directorshot creation.
     * @param observable - the observable
     * @param oldValue - the old value of the focus
     * @param newValue - the new value of the focus
     */
    private void directorShotStartCountFocusHandler(ObservableValue<? extends Boolean> observable,
                                                  Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            directorShotCreationModalView.getStartField().setText(
                    CountUtilities.parseCountNumber(
                            directorShotCreationModalView.getStartField().getText()));
        }
    }

    /**
     * Event handler for the creation of a director shot.
     * It adds the DirectorShot to the DirectorTimeline.
     * @param event shot creation event
     */
    private void createDirectorShot(MouseEvent event) {
        if (validateDirectorShot()) {
            DirectorTimelineController directorTimelineController =
                    this.controllerManager.getDirectorTimelineControl();
            
            DirectorShot shot = new DirectorShot(
                new GeneralShotData(
                        directorShotCreationModalView.getNameField().getText(),
                        directorShotCreationModalView.getDescriptionField().getText(),
                        Double.parseDouble(directorShotCreationModalView.getStartField().getText()
                    ),
                Double.parseDouble(directorShotCreationModalView.getEndField().getText())),
                Double.parseDouble(directorShotCreationModalView.getFrontPaddingField().getText()),
                Double.parseDouble(directorShotCreationModalView.getEndPaddingField().getText()),
                directorShotCreationModalView.getCamerasInShot());
            
            directorShotCreationModalView.getInstrumentsInShot().forEach(e -> {
                    shot.addInstrument(this.controllerManager.getScriptingProject()
                        .getInstruments().get(e));
                });

            directorTimelineController.addDirectorShot(shot);

            // keep at end of if statement
            directorShotCreationModalView.getModalStage().close();
        }
    }

    /**
     * Validates that the fields are correctly filled, and if not, displays
     * a corresponding error message.
     * @return whether or not the fields are valid
     */
    protected boolean validateDirectorShot() {
        String errorString = "";
        if (directorShotCreationModalView.getNameField().getText().isEmpty()) {
            errorString += "Please name your shot.\n";
        }

        if (directorShotCreationModalView.getDescriptionField().getText().isEmpty()) {
            errorString += "Please add a description.\n";
        }

        errorString = validateDirectorShotCounts(errorString);

        if (errorString.isEmpty()) {
            return true;
        } else {
            directorShotCreationModalView.displayError(errorString);
            return false;
        }
    }

    /**
     * Validates the fields with numbers in the CreationModalView.
     * @param errorString the string to add the error messages to
     * @return returns the appended errorstring
     */
    private String validateDirectorShotCounts(String errorString) {
        double startVal = Double.parseDouble(
                directorShotCreationModalView.getStartField().getText());
        double endVal = Double.parseDouble(
                directorShotCreationModalView.getEndField().getText());
        if (startVal >= endVal) {
            errorString += "Please make sure that the shot ends after it begins.\n";
        }

        double frontPadding = Double.parseDouble(
                directorShotCreationModalView.getFrontPaddingField().getText());
        double endPadding = Double.parseDouble(
                directorShotCreationModalView.getEndPaddingField().getText());
        if (frontPadding < 0 || endPadding < 0) {
            errorString += "Please make sure that the padding before "
                    + "and after the shot is positive.\n";
        }
        return errorString;
    }
}
