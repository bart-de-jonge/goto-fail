package gui.styling;

import gui.misc.TransitionData;
import gui.misc.TransitionHelper;
import gui.misc.TweakingHelper;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.MenuButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * Represents a stylized menu button.
 */
public class StyledMenuButton extends MenuButton {

    private StyledElementHelper helper = new StyledElementHelper();

    /*
     * Tweakable variables
     */

    // effects
    private double shadowRadius = 10;
    private double shadowOpacity = 0.15;
    private double shadowClickOpacity = 0.25;

    // transitions
    private int mouseOverDuration = 100;
    private int mouseClickDuration = 75;

    // colors
    // Color of normal borders and text. Becomes fillColor on click.
    private Color borderColor = TweakingHelper.getColor(0);
    // Color of (blank) background. Becomes borderColor on click.
    // Can be set transparent?
    private Color fillColor = TweakingHelper.getBackgroundColor();

    private String overridingStyle = "-fx-border-radius: 20;"
                                    + "-fx-background-radius: 20;"
            + "-fx-border-width: 1;"
            + "-fx-border-style: solid outside;"
            + "-fx-font-size: 14;";

    /*
     * Misc variables
     */

    private DropShadow dropShadow; // adds simple drop shadow.
    private TransitionHelper transitionHelper; // provides transition-effects.
    private ObjectProperty<Color> borderColorProperty = new SimpleObjectProperty<>(borderColor);
    private StringProperty borderStringProperty =
            helper.createColorStringProperty(borderColorProperty);
    private ObjectProperty<Color> fillColorProperty = new SimpleObjectProperty<>(Color.WHITE);
    private StringProperty fillStringProperty = helper.createColorStringProperty(fillColorProperty);

    /**
     * Constructor class.
     */
    public StyledMenuButton() {
        init();
    }

    /**
     * Constructor class.
     * @param text text inside button.
     */
    public StyledMenuButton(String text) {
        setText(text);
        init();
    }

    /**
     * Initialization helper function.
     */
    private void init() {
        helper = new StyledElementHelper();
        // initialize effects that can't be done by css all at once
        this.dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, shadowOpacity),
                shadowRadius, 0.05, 2, 2);
        // chain effects together
        this.setEffect(dropShadow);

        // bind style properties. This gives us so much more control than before!
        this.styleProperty().bind(new SimpleStringProperty("-fx-background-color: ")
                .concat(fillStringProperty).concat(";").concat("-fx-border-color: ")
                .concat(borderStringProperty).concat(";").concat("-fx-text-fill: ")
                .concat(borderStringProperty).concat(";").concat("-fx-color: ")
                .concat(fillStringProperty).concat(";").concat(overridingStyle));

        // add transitions
        this.transitionHelper = new TransitionHelper(this);
        initMouseOverTransitions();
        initMouseClickTransitions();

        // Delayed slightly so at least Scene.show() is called. Because JAVAFX.
        // I literally couldn't come up with this if I wanted to.
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                        try {
                            lookup(".label").styleProperty().bind(
                                    new SimpleStringProperty("-fx-text-fill: ")
                                    .concat(borderStringProperty).concat(";"));
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    });
            }
        }, 50);
    }

    /**
     * Initializes mouse over transitions.
     */
    private void initMouseOverTransitions() {
        transitionHelper.addMouseOverTransition(new TransitionData<>(
                    fillColorProperty, mouseOverDuration, Interpolator.LINEAR),
                fillColor, borderColor);
        transitionHelper.addMouseOverTransition(new TransitionData<>(
                    borderColorProperty, mouseOverDuration, Interpolator.LINEAR),
                borderColor, fillColor);
    }

    /**
     * Initializes mouse click transitions.
     */
    private void initMouseClickTransitions() {
        transitionHelper.addMouseClickTransition(new TransitionData<>(this.translateYProperty(),
                mouseClickDuration, Interpolator.LINEAR), 0.5);
        transitionHelper.addMouseClickTransition(new TransitionData<>(dropShadow.radiusProperty(),
                mouseClickDuration, Interpolator.LINEAR), -5);
        transitionHelper.addMouseClickTransition(new TransitionData<>(dropShadow.colorProperty(),
                mouseClickDuration, Interpolator.LINEAR), Color.rgb(0, 0, 0, shadowOpacity),
                Color.rgb(0, 0, 0, shadowClickOpacity));
    }

    

    /**
     * Set the fill color of this button.
     * @param color the color to set.
     */
    public void setFillColor(Color color) {
        this.fillColor = color;
        this.fillColorProperty.setValue(fillColor);
        this.transitionHelper.removeTransitions();
        initMouseClickTransitions();
        initMouseOverTransitions();
    }

    /**
     * Set the border color of this button.
     * @param color the color to set.
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        this.borderColorProperty.setValue(borderColor);
        this.transitionHelper.removeTransitions();
        initMouseClickTransitions();
        initMouseOverTransitions();
    }

}
