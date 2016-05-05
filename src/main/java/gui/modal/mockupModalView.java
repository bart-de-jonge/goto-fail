package gui.modal;

import gui.root.RootPane;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Modal view for gui testing and mockups. Temporary until style basics are worked out.
 * @author Mark
 */
public class mockupModalView extends ModalView {

    private VBox viewPane;

    private Button enabledButton;
    private Button disabledButton;

    private DropShadow dropShadowForButtons;
    private InnerShadow innerShadowForButtons;

    /**
     * Constructor.
     * @param rootPane the root pane at which application starts.
     */
    public mockupModalView(RootPane rootPane) {
        super(rootPane, 400, 600);

        this.viewPane = new VBox();
        this.viewPane.getChildren().add(new Label("Lol ben een label!"));
        this.viewPane.setSpacing(20.0);
        this.viewPane.setPadding(new Insets(10,10,10,10));
        initExampleButtons();
        super.setModalView(this.viewPane);
        super.displayModal();
    }

    private void initExampleButtons() {

        dropShadowForButtons = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.15),
                10, 0.1, 2, 1);

        innerShadowForButtons = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.15),
                1, 1, -2, -1);

        dropShadowForButtons.setInput(innerShadowForButtons);

        enabledButton = new Button("Click me!");
        enabledButton.setEffect(dropShadowForButtons);
        this.viewPane.getChildren().add(enabledButton);

        disabledButton = new Button("No clicking!");
        disabledButton.setEffect(dropShadowForButtons);
        disabledButton.setDisable(true);
        this.viewPane.getChildren().add(disabledButton);

    }

    @Override
    public void setModalView(Pane modalView) {
        setDisplayScene(new Scene(modalView));
        getDisplayScene().getStylesheets().add("mockupstylesheet.css");
        getModalStage().setScene(getDisplayScene());
    }

}
