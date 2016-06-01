package gui.headerarea;

import org.controlsfx.control.CheckComboBox;

import gui.misc.TweakingHelper;
import gui.styling.StyledTextfield;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class DirectorDetailView extends DetailView {
    
    private StyledTextfield paddingBeforeField;
    
    private StyledTextfield paddingAfterField;
    
    private CheckComboBox<String> selectCamerasDropDown;
    
    public DirectorDetailView() {
        super();
        initPaddingBefore();
        initPaddingAfter();
        initSelectCameras();
    }
    
    private void initPaddingBefore() {
        paddingBeforeField = new StyledTextfield("");
        paddingBeforeField.setAlignment(Pos.CENTER);
        HBox paddingBeforeBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label paddingBeforeLabel = new Label("Padding before:");
        paddingBeforeBox.getChildren().addAll(paddingBeforeLabel, paddingBeforeField);
        paddingBeforeBox.setAlignment(Pos.CENTER);
        this.getChildren().add(descriptionBox);
    }
    
    private void initPaddingAfter() {
        paddingAfterField = new StyledTextfield("");
        paddingAfterField.setAlignment(Pos.CENTER);
        HBox paddingAfterBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label paddingAfterLabel = new Label("Padding after:");
        paddingAfterBox.getChildren().addAll(paddingAfterLabel, paddingBeforeField);
        paddingAfterBox.setAlignment(Pos.CENTER);
        this.getChildren().add(descriptionBox);
    }
    
    private void initSelectCameras() {
        selectCamerasDropDown = new CheckComboBox<>();
        selectCamerasDropDown.getItems().add("Test");
        selectCamerasDropDown.getItems().add("Test 2");
        this.getChildren().add(selectCamerasDropDown);
    }

}
