package gui.styling;

import javafx.scene.control.CheckBox;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a stylized checkbox.
 * @author Mark
 */
public class StyledCheckbox extends CheckBox {

    @Getter @Setter
    private ColorAdjust colorAdjust; // can be used for highlighting.

    @Getter @Setter
    private InnerShadow innerShadow; // adds inner bound bezel.

    // effect tweaking
    private double shadowRadius = 15;
    private double shadowOpacity = 0.25;
    private double bezelOpacity = 0.1;

    // transition tweaking
    private int mouseOverDuration = 100;
    private int mouseClickDuration = 50;


    /**
     * Constructor of class.
     */
    public StyledCheckbox() {
        init();
    }

    /**
     * Constructor of class.
     * @param text displayed to right of checkbox.
     */
    public StyledCheckbox(String text) {
        setText(text);
        init();

    }

    /**
     * Initialization helper function.
     */
    private void init() {
        // initialize effects that can't be done by css all at once
        this.colorAdjust = new ColorAdjust();
        this.innerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, bezelOpacity),
                1, 1, -1, -1);

        // chain effects together
        colorAdjust.setInput(innerShadow);
        this.setEffect(colorAdjust);
    }

}
