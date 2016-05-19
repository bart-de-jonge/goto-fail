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
        setStyle("-fx-background-color: " + TweakingHelper.STRING_SECONDARY + ";");
//        setPadding(new Insets(5, 10, 5, 10));
//
//        textOutputLabel = new Label("Text output goes here.");
//        textOutputLabel.setStyle("-fx-text-fill: " + TweakingHelper.STRING_BACKGROUND + ";");
//        getChildren().add(textOutputLabel);

        this.setPrefHeight(10);
    }
}
