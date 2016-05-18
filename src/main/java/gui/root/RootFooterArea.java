package gui.root;

import gui.misc.TweakingHelper;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import lombok.Getter;

/**
 * Class that represents the whole of bottom-level elements in the gui.
 * In other words, the bar at the bottom, at the least, goes here.
 */
class RootFooterArea extends HBox {

    @Getter
    private Label textOutputLabel;

    private DropShadow dropShadow;

    /**
     * RootFooterArea Constructor.
     */
    RootFooterArea() {
        // border style to mark it, for debugging for now.
        setStyle("-fx-background-color: " + TweakingHelper.STRING_PRIMARY + ";");
        setPadding(new Insets(5, 10, 5, 10));


//        dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0,0,0,0.2), 20, 0.1, 0, -5);
//        this.setEffect(dropShadow);

        textOutputLabel = new Label("Text output goes here.");
        textOutputLabel.setStyle("-fx-text-fill: " + TweakingHelper.STRING_BACKGROUND + ";");
        getChildren().add(textOutputLabel);

        this.setPrefHeight(30);
    }
}
