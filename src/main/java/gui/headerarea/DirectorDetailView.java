package gui.headerarea;

import gui.styling.StyledCheckbox;
import gui.styling.StyledMenuButton;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;
import org.controlsfx.control.CheckComboBox;

import gui.misc.TweakingHelper;
import gui.styling.StyledTextfield;
import javafx.geometry.Insets;
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
    
    private HBox directorItemsBox;
    
    /**
     * Construct a new DirectorDetailView.
     */
    public DirectorDetailView() {
        super();
        directorItemsBox = new HBox();
        directorItemsBox.setSpacing(TweakingHelper.GENERAL_SPACING);
        initPaddingBefore();
        initPaddingAfter();
        this.getChildren().add(directorItemsBox);
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
        directorItemsBox.getChildren().add(paddingBeforeBox);
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
        directorItemsBox.getChildren().add(paddingAfterBox);
    }

    /**
     * Init experimental dropdown menu to select cameras.
     */
    private void createSelectCamerasButton() {
        selectCamerasButton = new StyledMenuButton("Edit Camera selection");
        directorItemsBox.getChildren().add(selectCamerasButton);
    }

    @Override
    public void setVisible() {
        if (!getVisible()) {
            this.setPadding(new Insets(0, 0, 0, TweakingHelper.GENERAL_PADDING));
            this.setSpacing(TweakingHelper.GENERAL_SPACING * 2);
            this.getItemBox().getChildren().clear();
            this.getItemBox().getChildren().addAll(getNameBox(), getDescriptionBox(),
                    getBeginCountBox(), getEndCountBox());
            directorItemsBox.getChildren().clear();
            directorItemsBox.getChildren().addAll(paddingBeforeBox, paddingAfterBox);
            createSelectCamerasButton();
            this.setVisibleForView(true);
        }
    }
    
    @Override
    public void setInvisible() {
        if (getVisible()) {
            this.setPadding(new Insets(0));
            this.setSpacing(0);
            this.getItemBox().getChildren().clear();
            this.getItemBox().getChildren().add(invisibleLabel);
            directorItemsBox.getChildren().clear();
            this.getChildren().remove(directorItemsBox);
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
