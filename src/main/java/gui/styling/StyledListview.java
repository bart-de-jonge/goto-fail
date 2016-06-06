package gui.styling;

import gui.misc.TransitionData;
import gui.misc.TransitionHelper;
import gui.misc.TweakingHelper;
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

    private String style = "-fx-border-color:"
            + TweakingHelper.getColorString(0) + ";"
            + "-c-color-primary: "
            + TweakingHelper.getColorString(0) + ";";

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
        transitionHelper.addDefaultMouseOverTransition(innerShadow, shadowTotalRadius, transitionMouseoverTime, shadowOpacity);

        this.setStyle(style);
    }

    

}
