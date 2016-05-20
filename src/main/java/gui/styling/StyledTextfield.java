package gui.styling;


import gui.misc.TransitionHelper;
import gui.misc.TweakingHelper;
import javafx.animation.Interpolator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import lombok.Getter;

/**
 * Represents a stylized textfield.
 * @author Mark
 */
public class StyledTextfield extends TextField {
    
    private StyledElementHelper helper = new StyledElementHelper();

    /*
     * Tweakable variables
     */

    // effects
    private double shadowRadius = 5;
    private double shadowTotalRadius = 3;
    private double shadowOpacity = 0.2;

    // colors
    @Getter
    private Color borderColor = TweakingHelper.COLOR_PRIMARY;
    @Getter
    private Color fillColor = Color.WHITE;
    @Getter
    private Color fillActiveColor = TweakingHelper.COLOR_BACKGROUND_HIGH;
    @Getter
    private Color textColor = TweakingHelper.COLOR_PRIMARY;
    @Getter
    private Color textActiveColor = TweakingHelper.COLOR_TERTIARY;

    // transitions
    private int transitionFocusTime = 50;
    private int transitionMouseoverTime = 100;

    /*
     * Misc variables
     */

    private InnerShadow innerShadow;
    private TransitionHelper transitionHelper;
    private ObjectProperty<Color> borderColorProperty = new SimpleObjectProperty<>(borderColor);
    private StringProperty borderStringProperty = 
            helper.createColorStringProperty(borderColorProperty);
    private ObjectProperty<Color> fillColorProperty = new SimpleObjectProperty<>(fillColor);
    private StringProperty fillStringProperty = helper.createColorStringProperty(fillColorProperty);
    private ObjectProperty<Color> textColorProperty = new SimpleObjectProperty<>(textColor);
    private StringProperty textStringProperty = helper.createColorStringProperty(textColorProperty);

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
        helper = new StyledElementHelper();
        // initialize effects that can't be done by css all at once
        this.innerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0),
                shadowRadius, 0.2, 1, 1);
        this.setEffect(innerShadow);

        // bind style properties. This gives us so much more control than before!
        this.styleProperty().bind(new SimpleStringProperty("-fx-background-color: ")
                .concat(fillStringProperty).concat(";").concat("-fx-border-color: ")
                .concat(borderStringProperty).concat(";").concat("-fx-text-fill: ")
                .concat(textStringProperty).concat(";"));

        transitionHelper = new TransitionHelper(this);
        transitionHelper.addMouseOverTransition(innerShadow.radiusProperty(),
                100, shadowTotalRadius, Interpolator.LINEAR);
        transitionHelper.addMouseOverTransition(innerShadow.colorProperty(),
                transitionMouseoverTime,  Color.rgb(0, 0, 0, 0),
                Color.rgb(0, 0, 0, shadowOpacity), Interpolator.LINEAR);

        focusedProperty().addListener(
            e -> {
                if (isFocused()) {
                    transitionHelper.runTransitionToValue(fillColorProperty, transitionFocusTime,
                            fillActiveColor, Interpolator.LINEAR);
                    transitionHelper.runTransitionToValue(textColorProperty, transitionFocusTime,
                            textActiveColor, Interpolator.LINEAR);
                } else {
                    transitionHelper.runTransitionToValue(fillColorProperty, transitionFocusTime,
                            fillColor, Interpolator.LINEAR);
                    transitionHelper.runTransitionToValue(textColorProperty, transitionFocusTime,
                            borderColor, Interpolator.LINEAR);
                }
            });
    }

    
    /**
     * Set the fill color of this textfield.
     * @param color the color to set.
     */
    public void setFillColor(Color color) {
        this.fillColor = color;
        this.fillColorProperty.setValue(fillColor);
    }

    /**
     * Set the focused color of this textfield.
     * @param color the color to set.
     */
    public void setFillActiveColor(Color color) {
        this.fillActiveColor = color;
    }

    /**
     * Set the text color of this textfield.
     * @param color the color to set.
     */
    public void setTextColor(Color color) {
        this.textColor = color;
        this.textColorProperty.setValue(textColor);
    }

    /**
     * Set the focused text color of this textfield.
     * @param color the color to set.
     */
    public void setTextActiveColor(Color color) {
        this.textActiveColor = color;
    }

    /**
     * Set the border color of this textfield.
     * @param color the color to set.
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        this.borderColorProperty.setValue(borderColor);
    }

}
