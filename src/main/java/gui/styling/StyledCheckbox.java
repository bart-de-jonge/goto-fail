package gui.styling;

import javafx.scene.Node;
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
    private DropShadow dropShadow; // adds simple drop shadow.

    @Getter @Setter
    private InnerShadow innerShadow; // adds inner bound bezel.

    private Node box; // the surrounding box of a checkbox, extracted.
    private Node mark; // the "active" indication element of a checkbox, extracted.

    // effect tweaking
    private double shadowRadius = 10;
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

        box = lookup(".box");
        mark = lookup(".mark");

        // initialize effects that can't be done by css all at once
        this.colorAdjust = new ColorAdjust();
        this.dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, shadowOpacity),
                shadowRadius, 0.1, 1, 3);
        this.innerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.15),
                1, 1, 1, 1);

        // chain effects together
        dropShadow.setInput(innerShadow);
        colorAdjust.setInput(dropShadow);
        this.setEffect(colorAdjust);

    }

}
