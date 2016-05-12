package gui.styling;


import gui.misc.TransitionHelper;
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
import lombok.Setter;

/**
 * Represents a stylized textfield.
 * @author Mark
 */
public class StyledTextfield extends TextField {

    /*
     * Tweakable variables
     */

    // effects
    private double shadowRadius = 5;
    private double shadowTotalRadius = 3;
    private double shadowOpacity = 0.15;

    // colors
    @Getter @Setter
    private Color borderColor = Color.rgb(60, 190, 255);
    @Getter @Setter
    private Color fillColor = Color.rgb(255, 255, 255);
    @Getter @Setter
    private Color fillActiveColor = Color.rgb(245, 245, 245);

    // transitions
    private int transitionFocusTime = 50;
    private int transitionMouseoverTime = 100;

    /*
     * Misc variables
     */

    private InnerShadow innerShadow; // adds inner shadow.
    private TransitionHelper transitionHelper;
    private ObjectProperty<Color> borderColorProperty = new SimpleObjectProperty<>(borderColor);
    private StringProperty borderStringProperty = createColorStringProperty(borderColorProperty);
    private ObjectProperty<Color> fillColorProperty = new SimpleObjectProperty<>(Color.WHITE);
    private StringProperty fillStringProperty = createColorStringProperty(fillColorProperty);

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
        this.innerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0),
                shadowRadius, 0.2, 1, 1);
        this.setEffect(innerShadow);

        // bind style properties. This gives us so much more control than before!
        this.styleProperty().bind(new SimpleStringProperty("-fx-background-color: ")
                .concat(fillStringProperty).concat(";").concat("-fx-border-color: ")
                .concat(borderStringProperty).concat(";").concat("-fx-text-fill: ")
                .concat(borderStringProperty).concat(";"));

        transitionHelper = new TransitionHelper(this);
        transitionHelper.addMouseOverTransition(innerShadow.radiusProperty(),
                100, shadowTotalRadius, Interpolator.LINEAR);
        transitionHelper.addMouseOverTransition(innerShadow.colorProperty(), transitionMouseoverTime,
                Color.rgb(0, 0, 0, 0), Color.rgb(0, 0, 0, shadowOpacity),
                Interpolator.LINEAR);

        focusedProperty().addListener(e -> {
            if (isFocused()) {
                transitionHelper.runTransitionToValue(fillColorProperty, transitionFocusTime, fillActiveColor, Interpolator.LINEAR);
            } else {
                transitionHelper.runTransitionToValue(fillColorProperty, transitionFocusTime, fillColor, Interpolator.LINEAR);
            }
        });
    }

    /**
     * Helper function for binding a fill color. Creates a string property used
     * to modify the style at runtime.
     * @param colorProperty the colorProperty whose color we want to show.
     * @return The StringProperty which we'll use to set the style.
     */
    private StringProperty createColorStringProperty(ObjectProperty<Color> colorProperty) {
        StringProperty stringProperty = new SimpleStringProperty();
        stringProperty.set(getStringFromColor(colorProperty.get()));
        colorProperty.addListener(
                e -> {
                    stringProperty.set(getStringFromColor(colorProperty.get()));
                });
        return stringProperty;
    }

    /**
     * Parses color from a Color object to javafx-css-compatible string.
     * @param color the color to parse.
     * @return a representative string.
     */
    private String getStringFromColor(Color color) {
        return "rgba(" + ((int) (color.getRed() * 255)) + ","
                + ((int) (color.getGreen() * 255)) + ","
                + ((int) (color.getBlue() * 255)) + ","
                + color.getOpacity() + ")";
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
     * Set the border color of this textfield.
     * @param color the color to set.
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        this.borderColorProperty.setValue(borderColor);
    }

}
