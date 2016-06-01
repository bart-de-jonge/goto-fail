package gui.headerarea;

import org.controlsfx.control.CheckComboBox;

import gui.misc.TweakingHelper;
import gui.styling.StyledTextfield;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class DirectorDetailView extends DetailView {
    
    private StyledTextfield paddingBeforeField;
    
    private StyledTextfield paddingAfterField;
    
    private CheckComboBox<String> selectCamerasDropDown;
    
    private HBox paddingBeforeBox;
    private HBox paddingAfterBox;
    
    public DirectorDetailView() {
        super();
        initPaddingBefore();
        initPaddingAfter();
        initSelectCameras();
        
    }
    
    private void initPaddingBefore() {
        paddingBeforeField = new StyledTextfield("");
        paddingBeforeField.setAlignment(Pos.CENTER);
        paddingBeforeBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label paddingBeforeLabel = new Label("Padding before:");
        paddingBeforeBox.getChildren().addAll(paddingBeforeLabel, paddingBeforeField);
        paddingBeforeBox.setAlignment(Pos.CENTER);
        this.getChildren().add(paddingBeforeBox);
    }
    
    private void initPaddingAfter() {
        paddingAfterField = new StyledTextfield("");
        paddingAfterField.setAlignment(Pos.CENTER);
        paddingAfterBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label paddingAfterLabel = new Label("Padding after:");
        paddingAfterBox.getChildren().addAll(paddingAfterLabel, paddingBeforeField);
        paddingAfterBox.setAlignment(Pos.CENTER);
        this.getChildren().add(paddingAfterBox);
    }
    
    private void initSelectCameras() {
        selectCamerasDropDown = new CheckComboBox<>();
        selectCamerasDropDown.getItems().add("Test");
        selectCamerasDropDown.getItems().add("Test 2");
        this.getChildren().add(selectCamerasDropDown);
    }
    
    @Override
    public void setVisible() {
        if (!getVisible()) {
            this.setPadding(new Insets(0, 0, 0, TweakingHelper.GENERAL_PADDING));
            this.setSpacing(TweakingHelper.GENERAL_SPACING * 2);
            this.getChildren().clear();
            this.getChildren().addAll(getNameBox(), getDescriptionBox(), getBeginCountBox(), getEndCountBox(), paddingBeforeBox, paddingAfterBox, selectCamerasDropDown);
            this.setVisibleForView(true);
        }
    }
    
    @Override
    public void setInvisible() {
        if (getVisible()) {
            this.setPadding(new Insets(0));
            this.setSpacing(0);
            this.getChildren().removeAll(getNameBox(), getDescriptionBox(), getBeginCountBox(), getEndCountBox(), paddingBeforeBox, paddingAfterBox, selectCamerasDropDown);
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
