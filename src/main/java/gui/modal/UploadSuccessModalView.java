package gui.modal;

import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class UploadSuccessModalView extends ModalView {
    
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    
    @Getter
    private StyledButton closeButton;
    
    @Getter
    private StyledButton goToWebsiteButton;
    
    private VBox viewPane;
    
    public UploadSuccessModalView(RootPane rootPane) {
        this(rootPane, WIDTH, HEIGHT);
    }
    
    public UploadSuccessModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);
        initView();
        this.setModalView(viewPane);
        this.displayModal();
    }
    
    private void initView() {
        viewPane = new VBox(20);
        Label label = new Label("Upload successful!");
        viewPane.getChildren().add(label);
        initButtons();
    }
    
    private void initButtons() {
        closeButton = createButton("Close", false);
        goToWebsiteButton = createButton("Go to website", false);
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(closeButton, goToWebsiteButton);
        viewPane.getChildren().add(buttonBox);
    }

}
