package gui.modal;

import java.util.List;

import data.Camera;
import gui.root.RootPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class DeleteCameraTypeWarningModalView extends ModalView {
    
    @Getter
    private Button confirmButton;
    @Getter
    private Button cancelButton;
    @Getter
    private VBox viewPane;
    @Getter
    private Label titleLabel;
    @Getter
    private List<Camera> camerasToShow;
    
    private static final int width = 300;
    private static final int height = 300;
    
    public DeleteCameraTypeWarningModalView(RootPane rootPane, List<Camera> camerasToShow) {
        this(rootPane, camerasToShow, width, height);
    }
    
    public DeleteCameraTypeWarningModalView(RootPane rootPane, List<Camera> camerasToShow,
            int width, int height) {
        super(rootPane, width, height);
        this.camerasToShow = camerasToShow;
        initView();
    }
    
    private void initView() {
        getModalStage().setHeight(height);
        getModalStage().setWidth(width);
        getModalStage().setMinHeight(height);
        getModalStage().setMinWidth(width);
        
        this.viewPane = new VBox(20);
        
        initTitleLabel();
        initMessage();
        initButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }
    
    private void initTitleLabel() {
        titleLabel = new Label("Are you sure you want to delete this camera?");
        this.viewPane.getChildren().add(titleLabel);
    }
    
    private void initMessage() {
        VBox messageArea = new VBox();
        Label explanationLabel = new Label("The following cameras will be invalid and deleted:");
        messageArea.getChildren().add(explanationLabel);
        camerasToShow.forEach(e -> {
            HBox cameraBox = new HBox();
            cameraBox.getChildren().addAll(
                    new Label(e.getName()), new Label(" - "), new Label(e.getDescription()));
            messageArea.getChildren().add(cameraBox);
        });
        this.viewPane.getChildren().add(messageArea);
    }
    
    private void initButtons() {
        HBox buttonBox = new HBox();
        confirmButton = createButton("Confirm delete", true);
        cancelButton = createButton("Cancel", true);
        buttonBox.getChildren().addAll(confirmButton, cancelButton);
        this.viewPane.getChildren().add(buttonBox);
    }

}
