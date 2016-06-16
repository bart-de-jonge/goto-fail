package gui.modal;

import gui.misc.TweakingHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ModalUtilities {
    
    private static final int BUTTON_SPACING = 20;
    private static final int BOTTOM_AREA_HEIGHT = 60;
    private static final int TITLE_LABEL_OFFSET_FROM_LEFT = 20;
    private static final String BOTTOM_STYLE = "-fx-background-color: "
            + TweakingHelper.getColorString(0) + ";";
    
    /**
     * Construct a default button pane.
     * @return the button pane
     */
    public static HBox constructButtonPane() {
        HBox buttonPane = new HBox();
        buttonPane.setSpacing(BUTTON_SPACING);
        buttonPane.setAlignment(Pos.CENTER_LEFT);
        buttonPane.setMinHeight(BOTTOM_AREA_HEIGHT);
        buttonPane.setPrefHeight(BOTTOM_AREA_HEIGHT);
        buttonPane.setMaxHeight(BOTTOM_AREA_HEIGHT);
        buttonPane.setPadding(new Insets(0, TITLE_LABEL_OFFSET_FROM_LEFT,
                0, TITLE_LABEL_OFFSET_FROM_LEFT));
        return buttonPane;
    }
    
    /**
     * Construct a default title label.
     * @param style the style to use
     * @param topAreaHeight the top area height to use
     * @return the title label
     */
    public static Label constructTitleLabel(String style, int topAreaHeight) {
        Label titleLabel = new Label("Test title, please ignore...");
        titleLabel.setStyle(style);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setPadding(new Insets(0, TITLE_LABEL_OFFSET_FROM_LEFT,
                0, TITLE_LABEL_OFFSET_FROM_LEFT));
        titleLabel.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        titleLabel.setMinHeight(topAreaHeight);
        titleLabel.setPrefHeight(topAreaHeight);
        titleLabel.setMaxHeight(topAreaHeight);
        return titleLabel;
    }
    
    /**
     * Construct a default fields pane.
     * @return the fields pane
     */
    public static VBox constructFieldsPane() {
        VBox pane = new VBox(TweakingHelper.GENERAL_SPACING);
        pane.setAlignment(Pos.CENTER);
        pane.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        pane.setPrefHeight(TweakingHelper.GENERAL_SIZE);
        pane.setPadding(new Insets(TweakingHelper.GENERAL_PADDING));
        return pane;
    }
    
    /**
     * Construct the default modal top style.
     * @param fontSize the font size to use
     * @return the style string
     */
    public static String constructDefaultModalTopStyle(int fontSize) {
        return "-fx-background-color: "
                + TweakingHelper.getColorString(0) + ";"
                + "-fx-text-fill: white; -fx-font-size: " + fontSize + ";"
                + "-fx-border-width: 0 0 10 0;"
                + "-fx-border-color: " + TweakingHelper.getColorString(1) + ";";
    }
    

}
