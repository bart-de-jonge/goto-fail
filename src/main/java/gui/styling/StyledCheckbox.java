package gui.styling;

import gui.misc.TransitionData;
import gui.misc.TransitionHelper;
import gui.misc.TweakingHelper;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
     * Color tweaking. These are generally overwritten from above, though,
     * so they might not do anything.
     */
    @Getter
    private Color borderColor = TweakingHelper.getColor(0);
    @Getter
    private Color markColor = TweakingHelper.getColor(1);
    @Getter
    private Color fillColor = TweakingHelper.getColor(3);

    /*
     * Effect and transition tweaking.
     */
    private double shadowRadius = 5;
    private double shadowOpacity = 0.25;
    @Getter @Setter
    private int transitionTime = 100;
    private double markPositionLeft = 2.0;
    private double markPositionRight = 22.0;
    @Getter @Setter
    private Interpolator interpolator = Interpolator.EASE_BOTH;

    /*
     * Misc styling variables.
     */
    @Getter
    private DropShadow dropShadow; // adds simple drop shadow.
    private TransitionHelper transitionHelper;
    private ObjectProperty<Color> markColorProperty = new SimpleObjectProperty<>(Color.WHITE);
    private StringProperty markStringProperty = createColorStringProperty(markColorProperty);
    private ObjectProperty<Color> fillColorProperty = new SimpleObjectProperty<>(Color.WHITE);
    private StringProperty fillStringProperty = createColorStringProperty(fillColorProperty);

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

            // Perform this on first use.
            if (!initialized) {
                initAction();
                initialized = true;
            }

            // Perform transitions.
            if (self.isSelected()) {
                transitionHelper.runTransitionToValue(new TransitionData<>(mark.translateXProperty(),
                        transitionTime, interpolator), markPositionRight);
                transitionHelper.runTransitionToValue(new TransitionData<>(markColorProperty,
                        transitionTime, interpolator), markColor);
                transitionHelper.runTransitionToValue(new TransitionData<>(fillColorProperty,
                        transitionTime, interpolator), fillColor);
            } else {
                transitionHelper.runTransitionToValue(new TransitionData<>(mark.translateXProperty(),
                        transitionTime, interpolator), markPositionLeft);
                transitionHelper.runTransitionToValue(new TransitionData<>(markColorProperty,
                        transitionTime, interpolator), Color.WHITE);
                transitionHelper.runTransitionToValue(new TransitionData<>(fillColorProperty,
                        transitionTime, interpolator), Color.WHITE);
            }
        };
    }

    /**
     * Performed on first action-use. HAS to be done FAR AFTER construction, because
     * 'box' and 'mark' don't exist until scene is actually drawn. That tends to
     * happen last. So yeah....
     */
    private void initAction() {
        // perform lookup. Cast is necessity.
        box = (StackPane) self.lookup(".box");
        mark = (StackPane) self.lookup(".mark");

        // Initialize stuff that can't be done by css all at once.
        this.dropShadow = new DropShadow(BlurType.GAUSSIAN,
                Color.rgb(0, 0, 0, shadowOpacity), shadowRadius, 0.03, 0, 1);
        mark.setEffect(dropShadow);
        mark.setTranslateX(markPositionLeft);

        mark.styleProperty().bind(new SimpleStringProperty("-fx-background-color: ")
                .concat(markStringProperty).concat(";").concat("-fx-border-color: ")
                .concat(getStringFromColor(borderColor)).concat(";"));
        box.styleProperty().bind(new SimpleStringProperty("-fx-background-color: ")
                .concat(fillStringProperty).concat(";").concat("-fx-border-color: ")
                .concat(getStringFromColor(borderColor)));
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
        return "rgba(" + ((int) (color.getRed()   * 255)) + ","
                + ((int) (color.getGreen() * 255)) + ","
                + ((int) (color.getBlue()  * 255)) + ","
                + color.getOpacity() + ")";
    }

    /**
     * Set the mark color of this checkbox.
     * @param color the color to set.
     */
    public void setMarkColor(Color color) {
        this.markColor = color;
        this.markColorProperty.setValue(markColor);
    }

    /**
     * Set the border color of this checkbox.
     * @param color the color to set.
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
    }

    /**
     * Set the fill color of this checkbox.
     * @param color the colro to set.
     */
    public void setFillColor(Color color) {
        this.fillColor = color;
        this.fillColorProperty.setValue(fillColor);
    }

}
