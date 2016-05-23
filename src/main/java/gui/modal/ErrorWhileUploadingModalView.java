package gui.modal;

import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class ErrorWhileUploadingModalView extends ModalView {
    
    private static final int WIDTH = 400;
    private static final int HEIGHT = 150;
    
    @Getter
    private StyledButton OKButton;
    
    private VBox viewPane;
    
    public ErrorWhileUploadingModalView(RootPane rootPane) {
        this(rootPane, WIDTH, HEIGHT);
    }
    
    public ErrorWhileUploadingModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);
        initView();
        this.setModalView(viewPane);
        this.displayModal();
    }
    
    private void initView() {
        viewPane = new VBox(20);
        OKButton = this.createButton("OK", false);
        Label label = new Label("Something went wrong while uploading your project file");
        viewPane.getChildren().addAll(label, OKButton);
    }

}
