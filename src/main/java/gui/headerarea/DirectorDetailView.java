package gui.headerarea;

import gui.misc.TweakingHelper;
import gui.styling.StyledMenuButton;
import gui.styling.StyledTextfield;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;

public class DirectorDetailView extends DetailView {
    
    @Getter
    private StyledTextfield paddingBeforeField;
    @Getter
    private StyledTextfield paddingAfterField;
    @Getter
    private StyledMenuButton selectCamerasButton;

    private HBox paddingBeforeBox;
    private HBox paddingAfterBox;
    
    /**
     * Construct a new DirectorDetailView.
     */
    public DirectorDetailView() {
        super();
        initPaddingBefore();
        initPaddingAfter();
    }
    
    /**
     * Set the before padding field.
     * @param padding the padding to set it with
     */
    public void setBeforePadding(double padding) {
        this.paddingBeforeField.setText(formatDouble(padding));
    }
    
    /**
     * Set the after padding field.
     * @param padding the padding to set it with
     */
    public void setAfterPadding(double padding) {
        this.paddingAfterField.setText(formatDouble(padding));
    }
    
    /**
     * Init the padding before field.
     */
    private void initPaddingBefore() {
        paddingBeforeField = new StyledTextfield("");
        paddingBeforeField.setAlignment(Pos.CENTER);
        paddingBeforeBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label paddingBeforeLabel = new Label("Padding before:");
        paddingBeforeBox.getChildren().addAll(paddingBeforeLabel, paddingBeforeField);
        paddingBeforeBox.setAlignment(Pos.CENTER);
        this.getChildren().add(paddingBeforeBox);
    }
    
    /**
     * Init the padding after field.
     */
    private void initPaddingAfter() {
        paddingAfterField = new StyledTextfield("");
        paddingAfterField.setAlignment(Pos.CENTER);
        paddingAfterBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label paddingAfterLabel = new Label("Padding after:");
        paddingAfterBox.getChildren().addAll(paddingAfterLabel, paddingAfterField);
        paddingAfterBox.setAlignment(Pos.CENTER);
        this.getChildren().add(paddingAfterBox);
    }

    /**
     * Init experimental dropdown menu to select cameras.
     */
    private void createSelectCamerasButton() {
        selectCamerasButton = new StyledMenuButton("Edit Camera selection");
        this.getChildren().add(selectCamerasButton);
    }

    @Override
    public void setVisible() {
        if (!getVisible()) {
            this.getChildren().clear();
            this.getChildren().addAll(getNameBox(), getDescriptionBox(),
                    getBeginCountBox(), getEndCountBox(),
                    paddingBeforeBox, paddingAfterBox);
            createSelectCamerasButton();
            this.setVisibleForView(true);
        }
    }
    
    @Override
    public void setInvisible() {
        if (getVisible()) {
            this.getChildren().clear();
            this.getChildren().add(invisibleLabel);
            this.setVisibleForView(false);
        }
    }
    
    @Override
    public void resetDetails() {
        this.getNameField().setText(defaultEmptyString);
        this.getNameField().setText(defaultEmptyString);
        this.getDescriptionField().setText(defaultEmptyString);
        this.getBeginCountField().setText(defaultEmptyNumber);
        this.getEndCountField().setText(defaultEmptyNumber);
        this.paddingBeforeField.setText(defaultEmptyString);
        this.paddingAfterField.setText(defaultEmptyString);
    }

}
