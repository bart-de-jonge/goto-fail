package control;

import gui.modal.CameraShotCreationModalView;
import gui.modal.DirectorShotCreationModalView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * Created by Bart on 11/05/2016.
 */
public class CreationModalViewController {

    private ControllerManager controllerManager;

    private CameraShotCreationModalView cameraShotCreationModalView;
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
                this.controllerManager.getScriptingProject().getCameraTimelines().size(),
                this::createCameraShot);

        // Add listeners for parsing to startfield
        cameraShotCreationModalView.getStartField().setOnKeyPressed(e -> {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    cameraShotCreationModalView.getStartField().setText(
                            CountUtilities.parseCountNumber(
                                    cameraShotCreationModalView.getStartField().getText()));
                }
            });
        cameraShotCreationModalView.getStartField().focusedProperty().addListener(
                (observable, oldValue, newValue) -> {
                if (!newValue) {
                    cameraShotCreationModalView.getStartField().setText(
                            CountUtilities.parseCountNumber(
                                    cameraShotCreationModalView.getStartField().getText()));
                }
            });

        // Add listeners for parsing to endfield
        cameraShotCreationModalView.getEndField().setOnKeyPressed(e -> {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    cameraShotCreationModalView.getEndField().setText(
                            CountUtilities.parseCountNumber(
                                    cameraShotCreationModalView.getEndField().getText()));
                }
            });
        cameraShotCreationModalView.getEndField().focusedProperty().addListener(
                (observable, oldValue, newValue) -> {
                if (!newValue) {
                    cameraShotCreationModalView.getEndField().setText(
                            CountUtilities.parseCountNumber(
                                    cameraShotCreationModalView.getEndField().getText()));
                }
            });
    }

    /**
     * Event handler for the creation of a camera shot.
     * It adds the CameraShot to the CameraTimeline and adds the corresponding
     * camera shots via the TimelineController.
     * @param event - the event to handle
     */
    private void createCameraShot(MouseEvent event) {
        TimelineController timelineController = this.controllerManager.getTimelineControl();

        cameraShotCreationModalView.getCamerasInShot().forEach(cameraIndex -> {
                timelineController.addCameraShot(cameraIndex,
                        cameraShotCreationModalView.getNameField().getText(),
                        cameraShotCreationModalView.getDescriptionField().getText(),
                        Double.parseDouble(cameraShotCreationModalView.getStartField().getText()),
                        Double.parseDouble(cameraShotCreationModalView.getEndField().getText()));
            });
    }


    /**
     * When triggered, this initializes and displays the modal view for the creation of
     * a new DirectorBlock.
     */
    public void showDirectorCreationWindow() {
        directorShotCreationModalView = new DirectorShotCreationModalView(
                this.controllerManager.getRootPane(),
                this.controllerManager.getScriptingProject().getCameraTimelines().size(),
                this::createDirectorShot);

        // todo: add parsing of counts to the right fields
        // see showCameraCreationWindow
    }



    /**
     * Event handler for the creation of a director shot.
     * It adds the DirectorShot to the DirectorTimeline.
     * @param event shot creation event
     */
    private void createDirectorShot(MouseEvent event) {
        // TODO: Implement adding a DirectorShot

        // Placeholder variables for creating director shot, please inline them when used
        String shotName = directorShotCreationModalView.getNameField().getText();
        String shotDescrip = directorShotCreationModalView.getDescriptionField().getText();
        double startPoint = Double.parseDouble(
                directorShotCreationModalView.getStartField().getText());
        double endPoint = Double.parseDouble(
                directorShotCreationModalView.getEndField().getText());
        double frontPadding = Double.parseDouble(
                directorShotCreationModalView.getFrontPaddingField().getText());
        double endPadding = Double.parseDouble(
                directorShotCreationModalView.getEndPaddingField().getText());

        List<Integer> camerasInShot = directorShotCreationModalView.getCamerasInShot();
    }
}
