package gui.modal;

import gui.headerarea.DoubleTextField;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledCheckbox;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Class responsible for displaying a modal view for the creation of shots.
 * @author alex
 */
public class CreationModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 290 work very well.
    private static final int width = 680;
    private static final int height = 290;

    // background styles of the three main areas.
    private String topStyle = "-fx-background-color: rgb(240,240,240);"
            + "-fx-text-fill: black; -fx-font-size: 20;";
    private String centerStyle = "-fx-background-color: rgb(230, 230, 230);";
    private String bottomStyle = "-fx-background-color: rgb(240, 240, 240);";

    // tweakable variables for the Create and Cancel buttons
    private int buttonWidth = 90;
    private int buttonHeight = 25;
    private Point3D createButtonColor = new Point3D(200, 200, 200);
    private Point3D cancelButtonColor = new Point3D(200, 200, 200);
    private int buttonFontSize = 16;
    private int buttonSpacing = 20;
    private int buttonOffsetFromLeft = 20;

    // color of the "active" element of a checkbox
    private Point3D checkboxColor = new Point3D(250, 120, 50);

    // title panel variables
    private Point3D titlelabelColor = new Point3D(255, 255, 255);
    private int titleOffsetFromLeft = 20;

    // effects variables
    private double softShadowRadius = 10;
    private double softShadowCutoff = 0.1;
    private double softShadowOpacity = 0.05;
    private double hardShadowRadius = 1.0;
    private double hardShadowCutoff = 1.0;
    private double hardShadowOpacity = 0.15;

    /*
     * GUI elements and non-tweakable gui variables
     */

    // Seriously. I'm not kidding. No touchy.
    private int generalSpacing = 10;
    private int checkboxSpacing = 20;
    private int textSpacing = 20;
    private int centerSpacing = 40;
    private int largeSize = 10000;
    private int textPaneMinWidth = 350;
    private int checkboxPaneMinWidth = 250;

    private VBox viewPane;
    private HBox contentPane;
    private HBox buttonPane;
    private FlowPane checkboxPane;

    @Getter
    private List<StyledCheckbox> cameraCheckboxes;
    @Getter
    private Label titleLabel;

    @Getter
    private StyledButton creationButton;
    @Getter
    private StyledButton cancelButton;

    @Getter
    private DoubleTextField startField;
    @Getter
    private DoubleTextField endField;
    @Getter
    private TextField descripField;
    @Getter
    private TextField nameField;

    private InnerShadow topInnerShadow;
    private InnerShadow topOuterShadow;
    private DropShadow bottomOuterShadow;

    /*
     * Misc variables.
     */

    private int numberOfCameras;

    @Setter
    private String defaultStartCount = "0";
    @Setter
    private String defaultEndCount = "1";

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     */
    public CreationModalView(RootPane rootPane, int numberOfCamerasInTimeline) {
        this(rootPane, numberOfCamerasInTimeline, width, height);
    }

    /**
     * Constructor.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     * @param modalWidth Modal display width
     * @param modalHeight Modal display height
     */
    public CreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                             int modalWidth, int modalHeight) {
        super(rootPane, modalWidth, modalHeight);
        this.numberOfCameras = numberOfCamerasInTimeline;
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
        initTitleLabel();

        // add space for textfields and checkboxes
        this.contentPane = new HBox();
        this.contentPane.setAlignment(Pos.CENTER);
        this.contentPane.setPadding(new Insets(0, textSpacing, 0, 0));
        this.contentPane.setPrefHeight(largeSize);
        this.contentPane.setSpacing(centerSpacing);
        this.contentPane.setStyle(centerStyle);
        this.viewPane.getChildren().add(contentPane);

        // actually add textfields and checkboxes
        initTextFields();
        initCamCheckBoxes();

        // add buttons at bottom.
        initButtons();

        // once we're done, setup shadows etc.
        initEffects();

        super.setModalView(this.viewPane);
        super.displayModal();
    }

    /**
     * Initialize title label.
     */
    private void initTitleLabel() {
        titleLabel = new Label("Add a new shot...");
        titleLabel.setStyle(topStyle);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        titleLabel.setPadding(new Insets(0, 0, 0, titleOffsetFromLeft));
        titleLabel.setPrefWidth(largeSize);
        titleLabel.setPrefHeight(largeSize);
        this.viewPane.getChildren().add(titleLabel);
    }

    /**
     * Sets up effects and adds them to the appropriate panes.
     */
    private void initEffects() {
        topInnerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, hardShadowOpacity),
                hardShadowRadius, hardShadowCutoff, 0, -2);
        topOuterShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, softShadowOpacity),
                softShadowRadius, softShadowCutoff, 0, 1);
        bottomOuterShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, softShadowOpacity),
                softShadowRadius, softShadowCutoff, 0, -1);
        titleLabel.setEffect(topInnerShadow);
        contentPane.setEffect(topOuterShadow);
        buttonPane.setEffect(bottomOuterShadow);
    }

    /**
     * Initializes pane with buttons at bottom.
     */
    private void initButtons() {
        // setup button pane
        this.buttonPane = new HBox();
        this.buttonPane.setSpacing(buttonSpacing);
        this.buttonPane.setAlignment(Pos.CENTER_LEFT);
        this.buttonPane.setPrefHeight(largeSize);
        this.buttonPane.setStyle(bottomStyle);
        this.buttonPane.setPadding(new Insets(0, 0, 0, buttonOffsetFromLeft));
        this.viewPane.getChildren().add(buttonPane);

        // Add cancel button
        cancelButton = new StyledButton("Cancel");
        cancelButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setFontSize(buttonFontSize);
        cancelButton.setButtonColor(createButtonColor);
        cancelButton.setAlignment(Pos.CENTER);

        // Add creation button
        creationButton = new StyledButton("Create");
        creationButton.setPrefWidth(buttonWidth);
        creationButton.setPrefHeight(buttonHeight);
        creationButton.setFontSize(buttonFontSize);
        creationButton.setButtonColor(cancelButtonColor);
        creationButton.setAlignment(Pos.CENTER);

        this.buttonPane.getChildren().addAll(creationButton, cancelButton);
    }

    /**
     * Initialize all textfields, add them to a left-central VBox.
     */
    private void initTextFields() {
        VBox content = new VBox(generalSpacing);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinWidth(textPaneMinWidth);
        content.setPrefWidth(largeSize);
        content.setPrefHeight(largeSize);
        content.setPadding(new Insets(textSpacing));

        // init name field
        final Label nameLabel = new Label("Shot Name: ");
        nameField = new TextField();
        HBox nameBox = new HBox(generalSpacing);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);

        // init description field
        final Label descripLabel = new Label("Shot Description: ");
        descripField = new TextField();
        HBox descripBox = new HBox(generalSpacing);
        descripBox.getChildren().addAll(descripLabel, descripField);
        descripBox.setAlignment(Pos.CENTER_RIGHT);

        // init start count field
        final Label startLabel = new Label("Start:");
        startField = new DoubleTextField(this.defaultStartCount);
        HBox startBox = new HBox(generalSpacing);
        startBox.getChildren().addAll(startLabel, startField);
        startBox.setAlignment(Pos.CENTER_RIGHT);

        // init end count field
        final Label endLabel = new Label("End:");
        endField = new DoubleTextField(this.defaultEndCount);
        HBox endBox = new HBox(generalSpacing);
        endBox.getChildren().addAll(endLabel, endField);
        endBox.setAlignment(Pos.CENTER_RIGHT);

        // add all to scene
        content.getChildren().addAll(nameBox, descripBox, startBox, endBox);
        this.contentPane.getChildren().add(content);
    }

    /**
     * Initialize the checkboxes with labels for each camera, in a flowpane.
     */
    private void initCamCheckBoxes() {
        // Create new FlowPane to hold the checkboxes.
        this.checkboxPane = new FlowPane();
        this.checkboxPane.setHgap(checkboxSpacing);
        this.checkboxPane.setVgap(checkboxSpacing);
        this.checkboxPane.setMinWidth(checkboxPaneMinWidth);
        this.checkboxPane.setPrefWidth(largeSize);
        this.checkboxPane.setAlignment(Pos.CENTER);

        // add checkboxes
        cameraCheckboxes = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < numberOfCameras; i++) {
            j = (j > 4) ? 0 : j + 1;
            String checkBoxString = "Camera " + (i + 1);
            StyledCheckbox checkBox = new StyledCheckbox(checkBoxString);
            checkBox.setMarkColor(checkboxColor);
            cameraCheckboxes.add(checkBox);
        }

        // add all to scene
        this.checkboxPane.getChildren().addAll(cameraCheckboxes);
        this.contentPane.getChildren().add(this.checkboxPane);
    }

}
