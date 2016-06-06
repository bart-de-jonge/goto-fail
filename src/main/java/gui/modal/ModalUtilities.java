package gui.modal;

import gui.misc.TweakingHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class ModalUtilities {
    
    private static final int BUTTON_SPACING = 20;
    private static final int BOTTOM_AREA_HEIGHT = 60;
    private static final int TITLE_LABEL_OFFSET_FROM_LEFT = 20;
    private static final String BOTTOM_STYLE = "-fx-background-color: "
            + TweakingHelper.getColorString(0) + ";";
    
    public static HBox constructButtonPane() {
        HBox buttonPane = new HBox();
        buttonPane.setSpacing(BUTTON_SPACING);
        buttonPane.setAlignment(Pos.CENTER_LEFT);
        buttonPane.setMinHeight(BOTTOM_AREA_HEIGHT);
        buttonPane.setPrefHeight(BOTTOM_AREA_HEIGHT);
        buttonPane.setMaxHeight(BOTTOM_AREA_HEIGHT);
        buttonPane.setStyle(BOTTOM_STYLE);
        buttonPane.setPadding(new Insets(0, TITLE_LABEL_OFFSET_FROM_LEFT,
                0, TITLE_LABEL_OFFSET_FROM_LEFT));
        return buttonPane;
    }

}
