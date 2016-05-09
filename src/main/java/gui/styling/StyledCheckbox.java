package gui.styling;

import javafx.animation.*;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
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
    private ColorAdjust markColorAdjust; // can be used for highlighting.

    @Getter @Setter
    private DropShadow dropShadow; // adds simple drop shadow.

    @Getter @Setter
    private InnerShadow innerShadow; // adds inner bound bezel.
    @Getter @Setter
    private InnerShadow markInnerShadow; // adds inner bound bezel.

    private StackPane box; // the surrounding box of a checkbox, extracted.
    private StackPane mark; // the "active" indication element of a checkbox, extracted.
    private boolean initialized = false;

    // effect tweaking
    private double shadowRadius = 10;
    private double shadowOpacity = 0.25;
    private double bezelOpacity = 0.1;

    // transition tweaking

    // misc
    private StyledCheckbox self;

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
        
        self = this;
        super.setIndeterminate(false);
        super.setSelected(false);

        // initialize effects that can't be done by css all at once
        this.colorAdjust = new ColorAdjust();
        this.markColorAdjust = new ColorAdjust();
        this.dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, shadowOpacity),
                shadowRadius, 0.1, 1, 3);
        this.innerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.15),
                1, 1, 1, 1);
        this.markInnerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.1),
                1, 1, -1, -1);

        // chain effects together
        dropShadow.setInput(innerShadow);
        colorAdjust.setInput(dropShadow);
        this.setEffect(colorAdjust);


        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Perform this on first use. We HAVE to do this AFTER construction,
                // because the 'box' and 'mark' subpanes of a checkbox do not exist
                // until after the scene is shown. Which, coincidentally, happens last.
                if (!initialized) {
                    box = (StackPane) self.lookup(".box");
                    mark = (StackPane) self.lookup(".mark");

                    markColorAdjust.setInput(markInnerShadow);
                    mark.setEffect(markColorAdjust);
                    markColorAdjust.setSaturation(-1.0);
                    markColorAdjust.setBrightness(1.0);

                    setMarkColor(0, 250, 105);
                }

                int ms = 200;
                Interpolator interpolator = Interpolator.EASE_BOTH;

                if (self.isSelected()) {
                    runTransitionTimeline(mark.translateXProperty(), ms, 16.0, interpolator);
                    runTransitionTimeline(markColorAdjust.saturationProperty(), ms, 0.0, interpolator);
                    runTransitionTimeline(markColorAdjust.brightnessProperty(), ms, 0.0, interpolator);
                } else if (initialized) {
                    runTransitionTimeline(mark.translateXProperty(), ms, 0.0, interpolator);
                    runTransitionTimeline(markColorAdjust.saturationProperty(), ms, -1.0, interpolator);
                    runTransitionTimeline(markColorAdjust.brightnessProperty(), ms, 1.0, interpolator);
                }
                initialized = true;
            }
        });
        
    }

    private <T> void runTransitionTimeline(Property<T> property, int ms, T v) {
        runTransitionTimeline(property, ms, v, Interpolator.LINEAR);
    }

    private <T> void runTransitionTimeline(Property<T> property, int ms, T v,
                                               Interpolator interpolator) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(property,
                                property.getValue(),
                                interpolator)),
                new KeyFrame(Duration.millis(ms),
                        new KeyValue(property,
                                v,
                                interpolator))
        );
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.play();
    }

    public void setValue(boolean value) {
        super.setIndeterminate(value);

    }

    public void setMarkColor(int r, int g, int b) {
        mark.setStyle(mark.getStyle().concat("-fx-background-color: rgb("
                + r + ","
                + g + ","
                + b + ");"));
    }

}
