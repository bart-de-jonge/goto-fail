package gui.styling;

import gui.misc.TransitionHelper;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a stylized checkbox.
 * @author Mark
 */
public class StyledCheckbox extends CheckBox {

    /*
     * Color tweaking
     */
    private Color borderColor = Color.rgb(60, 190, 255);
    private Color fillColor = Color.rgb(60, 190, 255);

    /*
     * Effect and transition tweaking.
     */
    private double shadowRadius = 5;
    private double shadowOpacity = 0.2;
    @Getter @Setter
    private int transitionTime = 100;
    private double markPositionLeft = 3.0;
    private double markPositionRight = 21.0;
    @Getter @Setter
    private Interpolator interpolator = Interpolator.EASE_BOTH;

    /*
     * Effects used on checkbox and subpanes.
     */
    @Getter
    private DropShadow dropShadow; // adds simple drop shadow.

    private TransitionHelper transitionHelper;

    private ObjectProperty<Color> fillColorProperty = new SimpleObjectProperty<>(Color.WHITE);
    private StringProperty fillStringProperty = createFillStringProperty(fillColorProperty);

    /*
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

                // set start position (in off mode!)
                mark.setTranslateX(markPositionLeft);

                //box.setStyle(getStringFromColor(borderColor));
                //System.out.println(box.getStyle().toString());
                //box.setStyle("-fx-border-color: green;");

                // Initialize effects that can't be done by css all at once.
                this.dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, shadowOpacity),
                        shadowRadius, 0.01, 0, 1);
                mark.setEffect(dropShadow);

                mark.styleProperty().bind(new SimpleStringProperty("-fx-background-color: ")
                .concat(fillStringProperty).concat(";").concat("-fx-border-color: ")
                .concat(getStringFromColor(borderColor)).concat(";"));

                box.setStyle("-fx-border-color: " + getStringFromColor(borderColor) + ";");

                initialized = true;
            }

            // perform transitions.
            if (self.isSelected()) {
                transitionHelper.runTransitionToValue(mark.translateXProperty(),
                        transitionTime, markPositionRight, interpolator);
                transitionHelper.runTransitionToValue(fillColorProperty,
                        transitionTime, fillColor, interpolator);
            } else {
                transitionHelper.runTransitionToValue(mark.translateXProperty(),
                        transitionTime, markPositionLeft, interpolator);
                transitionHelper.runTransitionToValue(fillColorProperty,
                        transitionTime, Color.WHITE, interpolator);
            }
        };
    }

    /**
     * Simple function to set color of active element,
     * rgb style, 0-255.
     * @param color 3d vector of 0-255 values.
     */
    public void setMarkColor(Point3D color) {
        setMarkColor((int) Math.round(color.getX()),
                (int) Math.round(color.getY()),
                (int) Math.round(color.getZ()));
    }

    /**
     * set color of active check element of checkbutton.
     * @param r component of color.
     * @param g component of color.
     * @param b component of color.
     */
    public void setMarkColor(int r, int g, int b) {
        // keep for now. May be useful.
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                mark.setStyle(mark.getStyle().concat("-fx-background-color: rgb("
//                    + r + "," + g + "," + b + ");"));
//            }
//        });
        setMarkColor(Color.rgb(r, g, b));
    }

    /**
     * Set color of active check element of checkbutton.
     * @param color the color to set.
     */
    public void setMarkColor(Color color) {
        fillColorProperty.setValue(color);
    }

    /**
     * Simple function to set color of background element,
     * rgb style, 0-255.
     * @param color 3d vector of 0-255 values.
     */
    public void setBoxColor(Point3D color) {
        setBoxColor((int) Math.round(color.getX()),
                (int) Math.round(color.getY()),
                (int) Math.round(color.getZ()));
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
                        + r + "," + g + "," + b + ");"));
            }
        });
    }

    /**
     * Set opacity of drop shadow to a certain value.
     * @param value to set.
     */
    public void setShadowOpacity(double value) {
        shadowOpacity = value;
        dropShadow.setColor(Color.rgb(0, 0, 0, shadowOpacity));
    }

    /**
     * Helper function for the mark fill color. Creates a string property used
     * to modify the style at runtime.
     * @param colorProperty the colorProperty whose color we want to show.
     * @return The StringProperty which we'll use to set the style.
     */
    private StringProperty createFillStringProperty(ObjectProperty<Color> colorProperty) {
        StringProperty stringProperty = new SimpleStringProperty();
        //setStringFromColor(stringProperty, colorProperty);
        stringProperty.set(getStringFromColor(colorProperty.get()));
        colorProperty.addListener(
            e -> {
                //setStringFromColor(stringProperty, colorProperty);
                stringProperty.set(getStringFromColor(colorProperty.get()));
            });
        return stringProperty;
    }

//    /**
//     * Parses color from our ColorProperty to our Stringproperty.
//     * @param string the StringProperty.
//     * @param color the ColorProperty.
//     */
//    private void setStringFromColor(StringProperty string, ObjectProperty<Color> color) {
////        string.set("rgba(" + ((int) (color.get().getRed()   * 255)) + ","
////                           + ((int) (color.get().getGreen() * 255)) + ","
////                           + ((int) (color.get().getBlue()  * 255)) + ","
////                           + color.get().getOpacity() + ")");
//        string.set(getStringFromColor(color.get()));
//    }

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
