package gui.styling;


import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a stylized textfield.
 * @author Mark
 */
public class StyledTextfield extends TextField {

    @Getter @Setter
    private ColorAdjust colorAdjust; // can be used for highlighting.

    @Getter @Setter
    private DropShadow dropShadow; // adds simple drop shadow.

    @Getter @Setter
    private InnerShadow innerShadow; // adds inner bound bezel.

    // effect tweaking
    private double shadowRadius = 15;
    private double shadowOpacity = 0.15;
    private double bezelOpacity = 0.05;

    /**
     * Constructor class.
     */
    public StyledTextfield() {
        init();
    }

    /**
     * Constructor class.
     * @param text text inside textfield.
     */
    public StyledTextfield(String text) {
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
    }

    /**
     * Simple function to set color of textfield, rgb style, 0-255.
     * @param r red component of color.
     * @param g green component of color.
     * @param b blue component of color.
     */
    public void setTextfieldColor(int r, int g, int b) {
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
