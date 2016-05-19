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

    private static final int FOOTER_AREA_HEIGHT = 10;

    /**
     * RootFooterArea Constructor.
     */
    RootFooterArea() {
        setStyle("-fx-background-color: " + TweakingHelper.STRING_SECONDARY + ";");

        this.setPrefHeight(FOOTER_AREA_HEIGHT);
    }
}
