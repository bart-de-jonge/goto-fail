package gui.modal;

import gui.root.RootPane;
import gui.events.DirectorShotCreationEvent;
import gui.headerarea.NumberTextField;
import gui.styling.StyledButton;
import gui.styling.StyledCheckbox;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for displaying a modal view for the creation of shots.
 * @author alex
 */
public class CreationModalView extends ModalView {

    private static final int width = 680;
    private static final int height = 290;

    private int numberOfCameras;

    @Setter
    private String defaultStartCount = "0";
    @Setter
    private String defaultEndCount = "1";

    private VBox viewPane;
    private HBox contentPane;
    private VBox textfieldPane;
    private HBox buttonPane;
    private FlowPane checkboxPane;
    private List<StyledCheckbox> cameraCheckboxes;
    private TextField descripField;
    private TextField nameField;
    private NumberTextField startField;
    private NumberTextField endField;
    private StyledButton creationButton;
    private StyledButton cancelButton;

    private EventHandler<DirectorShotCreationEvent> shotCreationEventHandler;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     * @param creationHandler Event handler for the creation of a shot
     */
    public CreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                             EventHandler<DirectorShotCreationEvent> creationHandler) {
        this(rootPane, numberOfCamerasInTimeline, creationHandler, width, height);
    }

    /**
     * Constructor.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     * @param creationHandler Event handler for the creation of a shot
     * @param modalWidth Modal display width
     * @param modalHeight Modal display height
     */
    public CreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                             EventHandler<DirectorShotCreationEvent> creationHandler,
                             int modalWidth, int modalHeight) {
        super(rootPane, modalWidth, modalHeight);
        this.numberOfCameras = numberOfCamerasInTimeline;
        this.shotCreationEventHandler = creationHandler;
        initializeCreationView();
    }

    /**
     * Initialize and display the modal view.
     */
    private void initializeCreationView() {
        // force minimum size
        getModalStage().setMinWidth(width);
        getModalStage().setMinHeight(height);

        // Create a new VBox for vertical layout
        this.viewPane = new VBox();

        // Add label at top
        Label label = new Label("Add a new shot");
        label.setStyle("-fx-background-color: rgba(0,0,0,0.1);"
            + "-fx-text-fill: black; -fx-font-size: 20;");
        label.setAlignment(Pos.CENTER);
        label.setPrefWidth(10000);
        label.setPrefHeight(10000);
        this.viewPane.getChildren().add(label);

        // add space for textfields and checkboxes
        this.contentPane = new HBox();
        this.contentPane.setAlignment(Pos.CENTER);
        this.contentPane.setPadding(new Insets(0, 20, 0, 0));
        this.contentPane.setPrefHeight(10000);
        this.contentPane.setSpacing(40.0);
        this.viewPane.getChildren().add(contentPane);

        // actually add textfields and checkboxes
        initTextFields();
        initCamCheckBoxes();

        // add space for buttons at bottom
        this.buttonPane = new HBox();
        this.buttonPane.setSpacing(20.0);
        this.buttonPane.setAlignment(Pos.CENTER);
        this.buttonPane.setPrefHeight(10000);
        this.buttonPane.setStyle("-fx-background-color: rgba(0,0,0,0.05);");
        this.viewPane.getChildren().add(buttonPane);

        // actually add buttons
        initButtons();

        super.setModalView(this.viewPane);
        super.displayModal();
    }

    private void initButtons() {
        // Add cancel button
        cancelButton = new StyledButton("Cancel");
        cancelButton.setOnMouseReleased(e -> {
            getModalStage().close();
        });
        cancelButton.setAlignment(Pos.CENTER);

        // Add creation button
        creationButton = new StyledButton("Create");
        creationButton.setOnMouseReleased(this::createShot);
        creationButton.setAlignment(Pos.CENTER);

        this.buttonPane.getChildren().addAll(creationButton, cancelButton);
    }

    /**
     * Initialize all textfields, add them to a left-central VBox.
     */
    private void initTextFields() {
        VBox content = new VBox();
        content.setSpacing(10);
        content.setMinWidth(350.0);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPrefWidth(10000);
        content.setPrefHeight(10000);
        content.setPadding(new Insets(20, 20, 20, 20));
        //content.setStyle("-fx-background-color: rgba(0,0,0,0.1);");

        // init name field
        final Label nameLabel = new Label("Shot Name: ");
        nameField = new TextField();
        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setSpacing(10);
        nameBox.setAlignment(Pos.CENTER_RIGHT);

        // init description field
        final Label descripLabel = new Label("Shot Description: ");
        descripField = new TextField();
        HBox descripBox = new HBox();
        descripBox.getChildren().addAll(descripLabel, descripField);
        descripBox.setSpacing(10);;
        descripBox.setAlignment(Pos.CENTER_RIGHT);

        // init start count field
        final Label startLabel = new Label("Start:");
        startField = new NumberTextField();
        startField.setText(this.defaultStartCount);
        HBox startBox = new HBox();
        startBox.getChildren().addAll(startLabel, startField);
        startBox.setSpacing(10);;
        startBox.setAlignment(Pos.CENTER_RIGHT);

        // init end count field
        final Label endLabel = new Label("End:");
        endField = new NumberTextField();
        endField.setText(this.defaultEndCount);
        HBox endBox = new HBox();
        endBox.getChildren().addAll(endLabel, endField);
        endBox.setSpacing(10);;
        endBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(nameBox, descripBox, startBox, endBox);
        this.contentPane.getChildren().add(content);
    }

    /**
     * Initialize the shot name and description fields.
     */
    private void initNameDescriptionFields() {
        // Add the textfields for shot's name & description
        final Label nameLabel = new Label("Shot Name: ");
        nameField = new TextField();
        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setSpacing(10);

        final Label descripLabel = new Label("Shot Description: ");
        descripField = new TextField();
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
        final Label startLabel = new Label("Start:");
        startField = new NumberTextField();
        final Label endLabel = new Label("End:");
        endField = new NumberTextField();

        // Add default values as a cue
        startField.setText(this.defaultStartCount);
        endField.setText(this.defaultEndCount);

        HBox countBox = new HBox();
        countBox.getChildren()
                .addAll(startLabel, startField, endLabel, endField);
        this.viewPane.getChildren().add(countBox);
    }

    /**
     * Initialize the checkboxes with labels for each camera, in a flowpane.
     */
    private void initCamCheckBoxes() {
        // Create new Gridpane to hold the checkboxes instead.
        this.checkboxPane = new FlowPane();
        this.checkboxPane.setHgap(20.0);
        this.checkboxPane.setVgap(20.0);
        this.checkboxPane.setMinWidth(300.0);
        this.checkboxPane.setPrefWidth(10000);
        this.checkboxPane.setPrefWidth(10000);
        this.checkboxPane.setStyle("-fx-background-color: rgba(0,0,0,0.025);");
        this.checkboxPane.setAlignment(Pos.CENTER);

        cameraCheckboxes = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < numberOfCameras; i++) {
            j = (j > 4) ? 0 : j + 1;
            String checkBoxString = "Camera " + (i + 1);
            StyledCheckbox checkBox = new StyledCheckbox(checkBoxString);
            checkBox.setMarkColor(100, 200, 255);
            cameraCheckboxes.add(checkBox);
        }
        this.checkboxPane.getChildren().addAll(cameraCheckboxes);
        this.contentPane.getChildren().add(this.checkboxPane);
    }

    /**
     * Validate and then pass shot information along.
     * @param event Creation button event
     */
    private void createShot(MouseEvent event) {
        if (validateShot()) {
            super.hideModal();
            this.shotCreationEventHandler.handle(this.buildCreationEvent());
        }
    }

    /**
     * Validates that the fields are correctly filled, and if not, displays
     * a corresponding error message.
     * @return whether or not the fields are valid
     */
    private boolean validateShot() {
        String errorString = "";
        if (nameField.getText().isEmpty()) {
            errorString += "Please name your shot.\n";
        }

        if (descripField.getText().isEmpty()) {
            errorString += "Please add a description.\n";
        }

        double startVal = Double.parseDouble(startField.getText());
        double endVal = Double.parseDouble(endField.getText());
        if (startVal >= endVal) {
            errorString += "Please make sure that the shot ends after it begins.\n";
        }

        boolean aCameraSelected = false;
        for (CheckBox cb : this.cameraCheckboxes) {
            if (cb.isSelected()) {
                aCameraSelected = true;
            }
        }

        if (!aCameraSelected) {
            errorString += "Please select at least one camera for this shot.";
        }

        if (errorString.isEmpty()) {
            return true;
        } else {
            displayError(errorString);
            return false;
        }
    }

    /**
     * Displays an error message in the view.
     * @param errorString Error to be displayed.
     */
    private void displayError(String errorString) {
        Text errText = new Text(errorString);
        errText.setFill(Color.RED);
        this.viewPane.getChildren().add(this.viewPane.getChildren().size() - 1, errText);
    }

    /**
     * Build the shot creation event.
     * @return the shot creation event
     */
    private DirectorShotCreationEvent buildCreationEvent() {
        String shotName = this.nameField.getText();
        String shotDescrip = this.descripField.getText();
        List<Integer> camerasInShot = getCamerasInShot();
        double startPoint = Double.parseDouble(this.startField.getText());
        double endPoint = Double.parseDouble(this.endField.getText());

        return new DirectorShotCreationEvent(shotName, shotDescrip, camerasInShot,
                                             startPoint, endPoint);
    }

    /**
     * Builds a list of which camera centerarea are in the shot.
     * @return list of cameras in shot
     */
    private List<Integer> getCamerasInShot() {
        List<Integer> camsInShot = new ArrayList<>();

        for (int i = 0; i < cameraCheckboxes.size(); i++) {
            if (cameraCheckboxes.get(i).isSelected()) {
                camsInShot.add(i);
            }
        }

        return camsInShot;
    }
}
