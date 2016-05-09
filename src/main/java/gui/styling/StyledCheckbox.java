package gui.styling;

import gui.misc.TransitionHelper;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a stylized checkbox.
 * @author Mark
 */
public class StyledCheckbox extends CheckBox {

    /**
     * Effect and transition tweaking.
     */
    private double shadowRadius = 10;
    private double shadowOpacity = 0.25;
    private double bezelThickness = 1;
    private double bezelOpacity = 0.15;
    private double bezelInnerThickness = 1;
    private double bezelInnerOpacity = 0.05;
    @Getter @Setter
    private int transitionTime = 200;
    @Getter @Setter
    private Interpolator interpolator = Interpolator.EASE_BOTH;

    /**
     * Effects used on checkbox and subpanes.
     */
    @Getter
    private ColorAdjust colorAdjust; // can be used for highlighting.
    @Getter
    private ColorAdjust markColorAdjust; // can be used for highlighting.
    @Getter
    private DropShadow dropShadow; // adds simple drop shadow.
    @Getter
    private InnerShadow innerShadow; // adds inner bound bezel.
    @Getter
    private InnerShadow markInnerShadow; // adds inner bound bezel.
    private TransitionHelper transitionHelper;

    /**
     * Subpanes and their initialization.
     */
    private StackPane box; // the surrounding box of a checkbox, extracted.
    private StackPane mark; // the "active" indication element of a checkbox, extracted.
    private boolean initialized = false;
    private StyledCheckbox self;

    /**
     * Constructor of class.
     */
    public StyledCheckbox() {
        init();
    }

    /**
     * Constructor of class.
     * @param value starting value of checkbox.
     */
    public StyledCheckbox(boolean value) {
        setSelected(value);
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
     * Constructor of class.
     * @param text displayed to right of checkbox.
     * @param value starting value of checkbox.
     */
    public StyledCheckbox(String text, boolean value) {
        setText(text);
        setSelected(value);
        init();
    }

    /**
     * Initialization helper function.
     */
    private void init() {
        self = this;
        super.setIndeterminate(false);
        transitionHelper = new TransitionHelper(this);

        // initialize effects that can't be done by css all at once
        this.colorAdjust = new ColorAdjust();
        this.dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, shadowOpacity),
                shadowRadius, 0.1, 1, 3);
        this.innerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, bezelOpacity),
                1, 1, bezelThickness, bezelThickness);

        // chain effects together
        dropShadow.setInput(innerShadow);
        colorAdjust.setInput(dropShadow);
        this.setEffect(colorAdjust);

        // button pressing action
        this.setOnAction(createOnAction());

        // Delayed slightly so at least Scene.show() is called.
        // We fire the button so often to setup the transitions properly.
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                self.fire();
                if (!self.isSelected()) {
                    self.fire();
                    self.fire();
                }
                self.fire();
            }
        });
    }

    /**
     * Returns event handler for when this checkbox action is fired.
     * This is where transitions and checkmark initialization are done.
     * @return the eventhandler it creates.
     */
    private EventHandler<ActionEvent> createOnAction() {
        return e -> {
            // Perform this on first use. We HAVE to do this AFTER construction,
            // because the 'box' and 'mark' subpanes of a checkbox do not exist
            // until after the scene is shown. Which, coincidentally, happens last.
            if (!initialized) {
                // perform lookup. Cast is necessity.
                box = (StackPane) self.lookup(".box");
                mark = (StackPane) self.lookup(".mark");

                // Initialize effects that can't be done by css all at once.
                markColorAdjust = new ColorAdjust();
                markInnerShadow = new InnerShadow(BlurType.GAUSSIAN,
                        Color.rgb(0, 0, 0, bezelInnerOpacity),
                        1, 1, -bezelInnerThickness, -bezelInnerThickness);
                markInnerShadow.setInput(markColorAdjust);
                mark.setEffect(markInnerShadow);
                markColorAdjust.setSaturation(-1.0);
                markColorAdjust.setBrightness(1.0);
                initialized = true;
            }

            // perform transitions.
            if (self.isSelected()) {
                transitionHelper.runTransitionToValue(mark.translateXProperty(),
                        transitionTime, 15.0, interpolator);
                transitionHelper.runTransitionToValue(markColorAdjust.saturationProperty(),
                        transitionTime, 0.0, interpolator);
                transitionHelper.runTransitionToValue(markColorAdjust.brightnessProperty(),
                        transitionTime, 0.0, interpolator);
            } else {
                transitionHelper.runTransitionToValue(mark.translateXProperty(),
                        transitionTime, 5.0, interpolator);
                transitionHelper.runTransitionToValue(markColorAdjust.saturationProperty(),
                        transitionTime, -1.0, interpolator);
                transitionHelper.runTransitionToValue(markColorAdjust.brightnessProperty(),
                        transitionTime, 1.0, interpolator);
            }
        };
    }

    /**
     * set color of active check element of checkbutton.
     * @param r component of color.
     * @param g component of color.
     * @param b component of color.
     */
    public void setMarkColor(int r, int g, int b) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mark.setStyle(mark.getStyle().concat("-fx-background-color: rgb("
                    + r + ","
                    + g + ","
                    + b + ");"));
            }
        });
    }

    /**
     * set color of background element of checkbutton.
     * @param r component of color.
     * @param g component of color.
     * @param b component of color.
     */
    public void setBoxColor(int r, int g, int b) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                box.setStyle(box.getStyle().concat("-fx-background-color: rgb("
                        + r + ","
                        + g + ","
                        + b + ");"));
            }
        });
    }

}
