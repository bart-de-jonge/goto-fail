package gui.modal;

import data.CameraType;
import gui.headerarea.DoubleTextField;
import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * Class responsible for displaying a modal view for the addition of a camera type.
 */
public class AddCameraTypeModalView extends CameraModificationModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 450 and 300 work very, very well.
    private static final int width = 400;
    private static final int height = 300;

    /*
     * Other variables.
     */

    @Getter
    private DoubleTextField movementMarginField;
    @Getter
    private StyledButton addCameraTypeButton;

    public AddCameraTypeModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }
    
    /**
     * Construct a new AddCameraTypeModalView
     * @param rootPane the rootPane that uses this modal.
     * @param width the width of the modal screen
     * @param height the height of the modal screen
     */
    public AddCameraTypeModalView(RootPane rootPane,
                                  int width,
                                  int height) {
        super(rootPane, width, height);
        initializeView();
    }
    
    public AddCameraTypeModalView(RootPane rootPane, CameraType type) {
        this(rootPane, width, height);
        this.nameField.setText(type.getName());
        this.descriptionField.setText(type.getDescription());
        this.movementMarginField.setText(Double.toString(type.getMovementMargin()));
        this.addCameraTypeButton.setText("Save");
        this.titleLabel.setText("Edit a camera type");
    }
    
    /**
     * Initialize the view of this modal.
     */
    private void initializeView() {
        // force minimum size
        getModalStage().setHeight(height);
        getModalStage().setWidth(width);
        getModalStage().setMinWidth(width);
        getModalStage().setMinHeight(height);

        // Create a new VBox for vertical layout
        this.viewPane = new VBox();

        // Add label at top
        initTitleLabel();

        // Add textfields in the middle.
        initFields();

        // Add buttons at the bottom.
        initButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }

   
    
    /**
     * Initialize the fields.
     */
    private void initFields() {
        final Label marginLabel = new Label("Movement margin (in seconds): ");
        movementMarginField = new DoubleTextField();
        movementMarginField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        movementMarginField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        movementMarginField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        movementMarginField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        HBox movementMarginBox = new HBox(TweakingHelper.GENERAL_SPACING);
        movementMarginBox.getChildren().addAll(marginLabel, movementMarginField);
        movementMarginBox.setAlignment(Pos.CENTER_RIGHT);
        VBox content = initNameDescriptionFields();
        content.getChildren().add(movementMarginBox);
        this.viewPane.getChildren().add(content);
    }

    /**
     * Initialize the buttons.
     */
    private void initButtons() {
        // Add adding button
        addCameraTypeButton = createButton("Add", false);
        initCancelButton();
        initHBoxForButtons().getChildren().addAll(addCameraTypeButton, cancelButton);
    }

}
