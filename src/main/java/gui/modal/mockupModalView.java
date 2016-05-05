package gui.modal;

import gui.misc.TransitionHelper;
import gui.root.RootPane;
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
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

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

        dropShadowForButtons = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.35),
                20, 0.1, 1, 3);

        innerShadowForButtons = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.15),
                1, 1, -3, -2);

        dropShadowForButtons.setInput(innerShadowForButtons);

        enabledButton = new Button("I want to be clicked! :D");
        enabledButton.setPadding(new Insets(10, 30, 10, 30));
        enabledButton.setEffect(dropShadowForButtons);
        TransitionHelper transitionHelper = new TransitionHelper(enabledButton);

//        transitionHelper.addMouseOverTransition(enabledButton, dropShadowForButtons.radiusProperty(),
//                200, 20, 10);
//        transitionHelper.addMouseClickTransition(enabledButton.translateYProperty(),
//                100, 0, -2);
        transitionHelper.addMouseOverTransition(enabledButton.translateXProperty(), 200, 20);
//        transitionHelper.addMouseOverTransition(enabledButton, dropShadowForButtons.offsetYProperty(),
//                200, 3, 0);
//        transitionHelper.addMouseOverTransition(enabledButton, dropShadowForButtons.colorProperty(),
//                200, Color.gray(0, 0.35), Color.gray(0, 0.45));

        this.viewPane.getChildren().add(enabledButton);

    }

    @Override
    public void setModalView(Pane modalView) {
        setDisplayScene(new Scene(modalView));
        getDisplayScene().getStylesheets().add("mockupstylesheet.css");
        getModalStage().setScene(getDisplayScene());
    }

}
