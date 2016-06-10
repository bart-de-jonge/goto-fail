package gui.modal;


import data.Instrument;
import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class AddInstrumentModalView extends CameraModificationModalView {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    
    @Getter
    private StyledButton addInstrumentButton;
    
    public AddInstrumentModalView(RootPane rootPane) {
        this(rootPane, WIDTH, HEIGHT);
    }
    
    public AddInstrumentModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);
        initializeView();
        this.titleLabel.setText("Create an instrument");
    }
    
    public AddInstrumentModalView(RootPane rootPane, Instrument instrument) {
        this(rootPane, WIDTH, HEIGHT);
        this.nameField.setText(instrument.getName());
        this.descriptionField.setText(instrument.getDescription());
        this.addInstrumentButton.setText("Save");
        this.titleLabel.setText("Edit an instrument");
    }
    
    private void initializeView() {
        // force minimum size
        forceBounds(HEIGHT, WIDTH);
        
        this.viewPane = new VBox();
        initTitleLabel();
        initFields();
        initButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }
    
    private void initFields() {
        VBox content = initNameDescriptionFields();
        this.viewPane.getChildren().add(content);
    }
    
    private void initButtons() {
        addInstrumentButton = createButton("Add", false);
        initCancelButton();
        initHBoxForButtons().getChildren().addAll(addInstrumentButton, cancelButton);
    }
}
