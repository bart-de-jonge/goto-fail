package gui.modal;

import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledTextfield;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;

public class CameraModificationModalView extends ModalView {
    
    protected static final int topAreaHeight = 50;
    protected static final int bottomAreaHeight = 60;
    
    protected String topStyle = "-fx-background-color: "
            + TweakingHelper.STRING_PRIMARY + ";"
            + "-fx-text-fill: white; -fx-font-size: 22;"
            + "-fx-font-family: helvetica neue; -fx-font-weight: lighter;"
            + "-fx-border-width: 0 0 10 0;"
            + "-fx-border-color: "
            + TweakingHelper.STRING_SECONDARY + ";";
    protected String centerStyle = "-fx-background-color: "
            + TweakingHelper.STRING_BACKGROUND + ";";
    protected String bottomStyle = "-fx-background-color: "
            + TweakingHelper.STRING_PRIMARY + ";";
    
    protected static final int buttonWidth = 90;
    protected static final int buttonHeight = 25;
    protected static final int buttonSpacing = 20;
    
    // variables for the title label
    protected int titlelabelOffsetFromLeft = 20;
    
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
        titleLabel = new Label("Add a camera...");
        titleLabel.setStyle(topStyle);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        titleLabel.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        titleLabel.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        titleLabel.setMinHeight(topAreaHeight);
        titleLabel.setPrefHeight(topAreaHeight);
        titleLabel.setMaxHeight(topAreaHeight);
        this.viewPane.getChildren().add(titleLabel);
    }
    
    /**
     * Init the name and description fields.
     * @return a VBox with these fields
     */
    protected VBox initNameDescriptionFields() {
        VBox content = new VBox(TweakingHelper.GENERAL_SPACING);
        content.setAlignment(Pos.CENTER);
        content.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        content.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        content.setPadding(new Insets(TweakingHelper.GENERAL_PADDING));
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
        // setup button pane
        HBox content = new HBox();
        content.setSpacing(buttonSpacing);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinHeight(bottomAreaHeight);
        content.setPrefHeight(bottomAreaHeight);
        content.setMaxHeight(bottomAreaHeight);
        content.setStyle(bottomStyle);
        content.setPadding(new Insets(0, titlelabelOffsetFromLeft,
                0, titlelabelOffsetFromLeft));
        this.viewPane.getChildren().add(content);
        return content;
    }
}
