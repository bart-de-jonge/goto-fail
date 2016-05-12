package gui.styling;

import gui.misc.TransitionHelper;
import javafx.animation.Interpolator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a stylized button.
 * @author Mark
 */
public class StyledButton extends Button {

    /*
     * Tweakable variables
     */

    // effects
    private double shadowRadius = 10;
    private double shadowOpacity = 0.1;
    private double shadowClickOpacity = 0.15;

    // transitions
    private int mouseOverDuration = 100;
    private int mouseClickDuration = 75;

    // colors
    @Getter @Setter
    // Color of normal borders and text. Becomes fillColor on click.
    private Color borderColor = Color.rgb(60, 190, 255);
    @Getter @Setter
    // Color of (blank) background. Becomes borderColor on click.
    // Can be set transparent?
    private Color fillColor = Color.rgb(255, 255, 255);

    /*
     * Misc variables
     */

    private DropShadow dropShadow; // adds simple drop shadow.
    private TransitionHelper transitionHelper; // provides transition-effects.
    private ObjectProperty<Color> borderColorProperty = new SimpleObjectProperty<>(borderColor);
    private StringProperty borderStringProperty = createColorStringProperty(borderColorProperty);
    private ObjectProperty<Color> fillColorProperty = new SimpleObjectProperty<>(Color.WHITE);
    private StringProperty fillStringProperty = createColorStringProperty(fillColorProperty);

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
        this.dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, shadowOpacity),
                shadowRadius, 0.05, 2, 2);
        // chain effects together
        this.setEffect(dropShadow);

        // bind style properties. This gives us so much more control than before!
        this.styleProperty().bind(new SimpleStringProperty("-fx-background-color: ")
                .concat(fillStringProperty).concat(";").concat("-fx-border-color: ")
                .concat(borderStringProperty).concat(";").concat("-fx-text-fill: ")
                .concat(borderStringProperty).concat(";"));

        // add transitions
        this.transitionHelper = new TransitionHelper(this);
        initMouseOverTransitions();
        initMouseClickTransitions();
    }

    /**
     * Initializes mouse over transitions.
     */
    private void initMouseOverTransitions() {
        transitionHelper.addMouseOverTransition(fillColorProperty, mouseOverDuration,
                fillColor, borderColor, Interpolator.LINEAR);
        transitionHelper.addMouseOverTransition(borderColorProperty, mouseOverDuration,
                borderColor, fillColor, Interpolator.LINEAR);
    }

    /**
     * Initializes mouse click transitions.
     */
    private void initMouseClickTransitions() {
        transitionHelper.addMouseClickTransition(this.translateYProperty(),
                mouseClickDuration, 0.5, Interpolator.LINEAR);
        transitionHelper.addMouseClickTransition(dropShadow.radiusProperty(),
                mouseClickDuration, -5, Interpolator.LINEAR);
        transitionHelper.addMouseClickTransition(dropShadow.colorProperty(),
                mouseClickDuration, Color.rgb(0, 0, 0, shadowOpacity),
                Color.rgb(0, 0, 0, shadowClickOpacity), Interpolator.LINEAR);
//        transitionHelper.addMouseClickTransition(dropShadow.offsetXProperty(),
//                mouseOverDuration, 2, Interpolator.LINEAR);
//        transitionHelper.addMouseClickTransition(dropShadow.offsetYProperty(),
//                mouseOverDuration, 2, Interpolator.LINEAR);
    }

// TODO: remove after properly testing everything.
// TODO: bind property to font size

//    /**
//     * Simple function to set color of button, rgb style, 0-255.
//     * @param color 3d vector of 0-255 values.
//     */
//    public void setButtonColor(Point3D color) {
//        setButtonColor((int) Math.round(color.getX()),
//                (int) Math.round(color.getY()),
//                (int) Math.round(color.getZ()));
//    }
//
//    /**
//     * Simple function to set color of button, rgb style, 0-255.
//     * @param r red component of color.
//     * @param g green component of color.
//     * @param b blue component of color.
//     */
//    public void setButtonColor(int r, int g, int b) {
//        setStyle(getStyle().concat("-fx-background-color: rgb("
//                + r + "," + g + "," + b + ");"));
//    }

//    /**
//     * Simple function to set color of text, rgb style, 0-255.
//     * @param color 3d vector of 0-255 values.
//     */
//    public void setTextColor(Point3D color) {
//        setTextColor((int) Math.round(color.getX()),
//                (int) Math.round(color.getY()),
//                (int) Math.round(color.getZ()));
//    }
//
//    /**
//     * Simple function to set color of text, rgb style, 0-255.
//     * @param r red component of color.
//     * @param g green component of color.
//     * @param b blue component of color.
//     */
//    public void setTextColor(int r, int g, int b) {
//        setStyle(getStyle().concat("-fx-text-fill: rgb("
//                + r + "," + g + "," + b + ");"));
//    }
//
//    /**
//     * Simple function to set size of text, with integer value.
//     * @param s size to set.
//     */
//    public void setFontSize(int s) {
//        setStyle(getStyle().concat("-fx-font-size: " + s + ";"));
//    }

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
        return "rgba(" + ((int) (color.getRed()   * 255)) + ","
                + ((int) (color.getGreen() * 255)) + ","
                + ((int) (color.getBlue()  * 255)) + ","
                + color.getOpacity() + ")";
    }

}
