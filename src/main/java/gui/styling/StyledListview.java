package gui.styling;

import gui.misc.TransitionHelper;
import gui.misc.TweakingHelper;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.scene.Node;
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
            + TweakingHelper.STRING_PRIMARY + ";"
            + "-c-color-primary: "
            + TweakingHelper.STRING_PRIMARY + ";";

    /*
     * Misc variables
     */

    private InnerShadow innerShadow;
    private TransitionHelper transitionHelper;
    private StyledListview<T> self;

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

        this.setStyle(style);
    }

    

}
