package gui.modal;

import gui.misc.TransitionHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledTextfield;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Modal view for gui testing and mockups. Temporary until style basics are pretty much worked out.
 * @author Mark
 */
public class mockupModalView extends ModalView {

    private VBox viewPane;

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
        initExampleTextfields();
        initExampleButtons2();
        super.setModalView(this.viewPane);
        super.displayModal();
    }

    private void initExampleTextfields() {
        StyledTextfield textTextField = new StyledTextfield("I'm a textfield!");

        this.viewPane.getChildren().add(textTextField);
    }

    private void initExampleButtons() {
        StyledButton testButton = new StyledButton("I'm a button!");
        testButton.setButtonColor(100, 195, 50);
        this.viewPane.getChildren().add(testButton);

        StyledButton testButton2 = new StyledButton("I'm another button!");
        testButton2.setButtonColor(200, 75, 175);
        this.viewPane.getChildren().add(testButton2);

    }

    private void initExampleButtons2() {
        StyledButton testRoundButton = new StyledButton("+");
        testRoundButton.setStyle("-fx-font-size: 32;"
                + "-fx-background-radius: 5em;"
                + "-fx-min-width: 60; -fx-max-width: 60;"
                + "-fx-min-height: 60; -fx-max-height: 60;");
        testRoundButton.setButtonColor(54, 200, 178);
        this.viewPane.getChildren().add(testRoundButton);

        StyledButton testRoundButton2 = new StyledButton("-");
        testRoundButton2.setStyle("-fx-font-size: 32;"
                + "-fx-background-radius: 5em;"
                + "-fx-min-width: 60; -fx-max-width: 60;"
                + "-fx-min-height: 60; -fx-max-height: 60;");
        testRoundButton2.setButtonColor(120, 180, 215);
        this.viewPane.getChildren().add(testRoundButton2);
    }

    @Override
    public void setModalView(Pane modalView) {
        setDisplayScene(new Scene(modalView));
        getDisplayScene().getStylesheets().add("mockupstylesheet.css");
        getModalStage().setScene(getDisplayScene());
    }

}
