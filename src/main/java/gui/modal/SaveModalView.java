package gui.modal;

import gui.root.RootPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class SaveModalView extends ModalView {
    
    private static final int width = 300;
    private static final int height = 300;
    
    private Label informationLabel;
    
    @Getter
    private Button saveButton;
    
    @Getter
    private Button dontSaveButton;
    
    @Getter
    private Button cancelButton;
    
    private VBox viewPane;
    
    
    public SaveModalView(RootPane rootPane) {
        this(rootPane, width, height);
    }
    
    public SaveModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);
        initializeView();
    }
    
    /**
     * Initialize the view of this modal.
     */
    private void initializeView() {
        this.viewPane = new VBox(20);
        informationLabel = new Label("There are unsaved changes\nDo you want to save them?");
        this.viewPane.getChildren().add(informationLabel);
        
        initButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }
    
    /**
     * Initialize the buttons.
     */
    private void initButtons() {
        saveButton = new Button("Save");
        dontSaveButton = new Button("Don't save");
        cancelButton = new Button("Cancel");
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(saveButton, dontSaveButton, cancelButton);
        this.viewPane.getChildren().add(buttonBox);
    }

}
