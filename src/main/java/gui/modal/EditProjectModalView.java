package gui.modal;

import gui.root.RootPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class EditProjectModalView extends ModalView {
    
    private static final int width = 600;
    private static final int height = 600;
    
    @Getter
    private RootPane rootPane;
    @Getter
    private VBox viewPane;
    
    
    public EditProjectModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }
    
    public EditProjectModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);
        this.rootPane = rootPane;
    }

}
