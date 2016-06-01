package gui.root;

import gui.misc.TweakingHelper;
import javafx.scene.layout.HBox;

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
        setStyle("-fx-background-color: " + TweakingHelper.getColorString(1) + ";");

        this.setPrefHeight(FOOTER_AREA_HEIGHT);
    }
}
