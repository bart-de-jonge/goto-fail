package gui.styling;

import gui.misc.TransitionHelper;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a stylized button.
 * @author Mark
 */
public class StyledButton extends Button {

    @Getter @Setter
    private ColorAdjust colorAdjust; // can be used for highlighting.

    @Getter @Setter
    private DropShadow dropShadow; // adds simple drop shadow.

    @Getter @Setter
    private InnerShadow innerShadow; // adds inner bound bezel.

    private TransitionHelper transitionHelper; // provides transition-effects.

    // effect tweaking
    private double shadowRadius = 15;
    private double shadowOpacity = 0.25;
    private double bezelOpacity = 0.1;

    // transition tweaking
    private int mouseOverDuration = 100;
    private int mouseClickDuration = 50;

    /**
     * Constructor class.
     */
    public StyledButton() {
        init();
    }

    /**
     * Constructor class.
     * @param text text inside button.
     */
    public StyledButton(String text) {
        setText(text);
        init();
    }

    /**
     * Initialization helper function.
     */
    private void init() {
        // initialize effects that can't be done by css all at once
        this.colorAdjust = new ColorAdjust();
        this.dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, shadowOpacity),
                shadowRadius, 0.1, 1, 3);
        this.innerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, bezelOpacity),
                1, 1, -1, -1);

        // chain effects together
        dropShadow.setInput(innerShadow);
        colorAdjust.setInput(dropShadow);
        this.setEffect(colorAdjust);

        // add transitions
        this.transitionHelper = new TransitionHelper(this);
        initMouseOverTransitions();
        initMouseClickTransitions();
    }

    /**
     * Initializes mouse over transitions.
     */
    private void initMouseOverTransitions() {
        transitionHelper.addMouseOverTransition(this.translateYProperty(), mouseOverDuration, -1);
        transitionHelper.addMouseOverTransition(dropShadow.radiusProperty(), mouseOverDuration, 10);
        transitionHelper.addMouseOverTransition(dropShadow.offsetXProperty(), mouseOverDuration, 3);
        transitionHelper.addMouseOverTransition(dropShadow.offsetYProperty(), mouseOverDuration, 3);
        transitionHelper.addMouseOverTransition(colorAdjust.brightnessProperty(), mouseOverDuration, 0.1);
    }

    /**
     * Initializes mouse click transitions.
     */
    private void initMouseClickTransitions() {
        transitionHelper.addMouseClickTransition(this.translateYProperty(), mouseClickDuration, 2);
        transitionHelper.addMouseClickTransition(dropShadow.radiusProperty(), mouseClickDuration, -20);
        transitionHelper.addMouseClickTransition(dropShadow.offsetXProperty(), mouseClickDuration, -5);
        transitionHelper.addMouseClickTransition(dropShadow.offsetYProperty(), mouseClickDuration, -5);
        transitionHelper.addMouseClickTransition(colorAdjust.brightnessProperty(), mouseClickDuration, -0.15);
    }

    /**
     * Simple function to set color of button, rgb style, 0-255.
     * @param r red component of color.
     * @param g green component of color.
     * @param b blue component of color.
     */
    public void setButtonColor(int r, int g, int b) {
        setStyle(getStyle().concat("-fx-background-color: rgb("
                + r + ","
                + g + ","
                + b + ");"));
    }

    /**
     * Simple function to set color of text, rgb style, 0-255.
     * @param r red component of color.
     * @param g green component of color.
     * @param b blue component of color.
     */
    public void setTextColor(int r, int g, int b) {
        setStyle(getStyle().concat("-fx-text-fill: rgb("
                + r + ","
                + g + ","
                + b + ");"));
    }

}
