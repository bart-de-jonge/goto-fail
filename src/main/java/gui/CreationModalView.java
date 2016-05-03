package gui;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for displaying a modal view for the creation of shots.
 * @author alex
 */
public class CreationModalView extends ModalView {

    private static final int width = 600;
    private static final int height = 600;

    private int numberOfCameras;

    private VBox viewPane;
    private List<CheckBox> cameraCheckboxes;

    /**
     * Constructor.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     */
    public CreationModalView(RootPane rootPane, int numberOfCamerasInTimeline) {
        super(rootPane, width, height);
        this.numberOfCameras = numberOfCamerasInTimeline;
        initializeCreationView();
    }

    /**
     * Initialize and display the modal view.
     */
    private void initializeCreationView() {
        this.viewPane = new VBox(20);
        this.viewPane.getChildren().add(new Text("Add a new shot"));

        initNameDescriptionFields();
        initCountFields();
        initCamCheckBoxes();

        Button creationButton = new Button("Create");
        creationButton.setOnMouseClicked(this::createShot);
        this.viewPane.getChildren().add(creationButton);

        super.setModalView(this.viewPane);
        super.displayModal();
    }

    /**
     * Initialize the shot name and description fields.
     */
    private void initNameDescriptionFields() {
        // Add the textfields for shot's name & description
        Label nameLabel = new Label("Shot Name: ");
        TextField nameField = new TextField();
        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setSpacing(10);

        Label descripLabel = new Label("Shot Description: ");
        TextField descripField = new TextField();
        HBox descripBox = new HBox();
        descripBox.getChildren().addAll(descripLabel, descripField);
        descripBox.setSpacing(10);

        this.viewPane.getChildren().addAll(nameBox, descripBox);
    }

    /**
     * Initialize the start and end count fields.
     */
    private void initCountFields() {
        // Start and end points
        Label startLabel = new Label("Start:");
        NumberTextField startField = new NumberTextField();
        Label endLabel = new Label("End:");
        NumberTextField endField = new NumberTextField();

        // Add default values as a cue
        startField.setText("0");
        endField.setText("1");

        HBox countBox = new HBox();
        countBox.getChildren()
                .addAll(startLabel, startField, endLabel, endField);
        this.viewPane.getChildren().add(countBox);
    }

    /**
     * Initialize the checkboxes with labels for each camera.
     */
    private void initCamCheckBoxes() {
        cameraCheckboxes = new ArrayList<>();
        for (int i = 0; i < numberOfCameras; i++) {
            String checkBoxString = "Camera " + (i + 1);
            CheckBox checkBox = new CheckBox(checkBoxString);
            cameraCheckboxes.add(checkBox);
        }
        this.viewPane.getChildren().addAll(cameraCheckboxes);
    }

    /**
     * Validate and then pass shot information along.
     * @param event Creation button event
     */
    private void createShot(MouseEvent event) {
        super.hideModal();
        // TODO: Add field validation (i.e. at least 1 camera, no non-negatives)
        System.out.println("Should create shot here");
    }
}
