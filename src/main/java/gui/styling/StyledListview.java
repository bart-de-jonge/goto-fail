package gui.styling;

import gui.misc.TransitionHelper;
import javafx.animation.Interpolator;
import javafx.scene.control.ListView;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

/**
 * Represents a stylized listview.
 * @author Mark
 */
public class StyledListview<T> extends ListView {

    /*
     * Tweakable variables
     */

    // effects
    private double shadowRadius = 5;
    private double shadowTotalRadius = 3;
    private double shadowOpacity = 0.2;

    // transitions
    private int transitionMouseoverTime = 100;

    /*
     * Misc variables
     */

    private InnerShadow innerShadow;
    private TransitionHelper transitionHelper;

    /**
     * Constructor of class.
     */
    public StyledListview() {
        init();
    }

    /**
     * Initialization helper function.
     */
    private void init() {
        // initialize effects that can't be done by css all at once
        this.innerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0),
                shadowRadius, 0.2, 1, 1);
        this.setEffect(innerShadow);

        // add some transitions because cool and stuff
        transitionHelper = new TransitionHelper(this);
        transitionHelper.addMouseOverTransition(innerShadow.radiusProperty(),
                100, shadowTotalRadius, Interpolator.LINEAR);
        transitionHelper.addMouseOverTransition(innerShadow.colorProperty(),
                transitionMouseoverTime,  Color.rgb(0, 0, 0, 0),
                Color.rgb(0, 0, 0, shadowOpacity), Interpolator.LINEAR);
    }

    /**
     * Parses color from a Color object to javafx-css-compatible string.
     * @param color the color to parse.
     * @return a representative string.
     */
    private String getStringFromColor(Color color) {
        return "rgba(" + ((int) (color.getRed()   * 255)) + ","
                + ((int) (color.getGreen() * 255)) + ","
                + ((int) (color.getBlue()  * 255)) + ","
                + color.getOpacity() + ")";
    }

}
