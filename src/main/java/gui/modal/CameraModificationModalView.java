package gui.modal;

import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledTextfield;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class CameraModificationModalView extends ModalView {
    
    protected static final int topAreaHeight = 50;
    protected static final int bottomAreaHeight = 60;
    
    protected String topStyle = ModalUtilities.constructDefaultModalTopStyle(22);
    protected String centerStyle = "-fx-background-color: "
            + TweakingHelper.getBackgroundString() + ";";
    
    protected static final int buttonWidth = 90;
    protected static final int buttonHeight = 25;
    protected static final int buttonSpacing = 20;
    
    
    @Getter
    protected StyledTextfield nameField;
    @Getter
    protected StyledTextfield descriptionField;
    @Getter
    protected Label titleLabel;
    @Getter
    protected VBox viewPane;
    @Getter
    protected StyledButton cancelButton;

    /**
     * Construct a new CameraModficationModalView.
     * @param pane the pane that this modal is built on
     * @param width the width of the modal
     * @param height the height of the modal
     */
    public CameraModificationModalView(RootPane pane, int width, int height) {
        super(pane, width, height);
    }
    
    /**
     * Init the title label.
     */
    protected void initTitleLabel() {
        titleLabel = ModalUtilities.constructTitleLabel(topStyle, topAreaHeight);
        titleLabel.setText("Add a camera...");
        this.viewPane.getChildren().add(titleLabel);
    }
    
    /**
     * Init the name and description fields.
     * @return a VBox with these fields
     */
    protected VBox initNameDescriptionFields() {
        VBox content = ModalUtilities.constructFieldsPane();
        content.setStyle(centerStyle);

        final Label nameLabel = new Label("Name:  ");
        nameField = new StyledTextfield();
        HBox nameBox = new HBox(TweakingHelper.GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);
        
        final Label descriptionLabel = new Label("Description: ");
        descriptionField = new StyledTextfield();
        HBox descriptionBox = new HBox(TweakingHelper.GENERAL_SPACING);
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);
        descriptionBox.setAlignment(Pos.CENTER_RIGHT);
        content.getChildren().addAll(nameBox, descriptionBox);
        return content;
    }
    
    /**
     * Init the cancel button.
     */
    protected void initCancelButton() {
        cancelButton = createButton("Cancel", false);
    }
    
    /**
     * Init a HBox for the buttons.
     * @return a HBox styled and ready to take in some buttons.
     */
    protected HBox initHBoxForButtons() {
        HBox content = ModalUtilities.constructButtonPane();
        this.viewPane.getChildren().add(content);
        return content;
    }
}
