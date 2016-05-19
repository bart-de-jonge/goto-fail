package gui.headerarea;

import gui.misc.TweakingHelper;
import gui.styling.StyledTextfield;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;

/**
 * The detail view is the view with details for shots.
 */
public class DetailView extends HBox {

    private String style = "-fx-background-color: "
            + TweakingHelper.STRING_BACKGROUND_HIGH + ";"
            + "-fx-min-height: 50;";

    private static final String defaultEmptyString = "";
    private static final String defaultEmptyNumber = "0";

    @Getter
    private StyledTextfield nameField;
    @Getter
    private StyledTextfield descriptionField;
    @Getter
    private DoubleTextField beginCountField;
    @Getter
    private DoubleTextField endCountField;

    /**
     * Constructor.
     */
    public DetailView() {
        this.setStyle(style);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(0, 0, 0, TweakingHelper.GENERAL_PADDING));
        this.setSpacing(TweakingHelper.GENERAL_SPACING * 2);
        initName();
        initDescription();
        initBeginCount();
        initEndCount();
    }

    /**
     * Set the description of the detailview.
     * @param description - the description to set
     */
    public void setDescription(String description) {
        descriptionField.setText(description);
    }

    /**
     * Set the name of the detailview.
     * @param name - the name to set
     */
    public void setName(String name) {
        nameField.setText(name);
    }

    /**
     * Format double into a nice displayable string.
     * @param d - the double to format
     * @return - a formatted string containng the double
     */
    private String formatDouble(double d) {
        if (d == (long) d) {
            return String.format("%d", (long) d);
        } else {
            return String.format("%s", d);
        }
    }

    /**
     * Set the begincount of the detailview.
     * @param count - the count to set
     */
    public void setBeginCount(double count) {
        beginCountField.setText(formatDouble(count));
    }

    /**
     * Set the endcount of the detailview.
     * @param count - the count to set
     */
    public void setEndCount(double count) {
        endCountField.setText(formatDouble(count));
    }

    /**
     * Init the begincount part of the detailview.
     */
    private  void initBeginCount() {
        beginCountField = new DoubleTextField("123");
        beginCountField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        beginCountField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        beginCountField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        beginCountField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        HBox startCountBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label specifierLabel = new Label("Start Count:");
        startCountBox.getChildren().addAll(specifierLabel, beginCountField);
        startCountBox.setAlignment(Pos.CENTER);
        this.getChildren().add(startCountBox);
    }

    /**
     * Init the endcount part of the detailview.
     */
    private  void initEndCount() {
        endCountField = new DoubleTextField("123");
        endCountField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        endCountField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        endCountField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        endCountField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        endCountField.setAlignment(Pos.CENTER);
        HBox endCountBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label specifierLabel = new Label("End Count:");
        endCountBox.getChildren().addAll(specifierLabel, endCountField);
        endCountBox.setAlignment(Pos.CENTER);
        this.getChildren().add(endCountBox);
    }

    /**
     * Init the name part of the detailview.
     */
    private void initName() {
        nameField = new StyledTextfield("Placeholder");
        nameField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        nameField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        nameField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        nameField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        nameField.setAlignment(Pos.CENTER);
        HBox nameBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label specifierLabel = new Label("Name:");
        nameBox.getChildren().addAll(specifierLabel, nameField);
        nameBox.setAlignment(Pos.CENTER);
        this.getChildren().add(nameBox);
    }

    /**
     * Init the description part of the detailview.
     */
    private void initDescription() {
        descriptionField = new StyledTextfield("");
        descriptionField.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        descriptionField.setTextColor(TweakingHelper.COLOR_PRIMARY);
        descriptionField.setTextActiveColor(TweakingHelper.COLOR_SECONDARY);
        descriptionField.setFillActiveColor(TweakingHelper.COLOR_TERTIARY);
        descriptionField.setAlignment(Pos.CENTER);
        HBox descriptionBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label specifierLabel = new Label("Description:");
        descriptionBox.getChildren().addAll(specifierLabel, descriptionField);
        descriptionBox.setAlignment(Pos.CENTER);
        this.getChildren().add(descriptionBox);
    }

    /**
     * Reset the detailview to its default empty values.
     */
    public void resetDetails() {
        this.nameField.setText(defaultEmptyString);
        this.descriptionField.setText(defaultEmptyString);
        this.beginCountField.setText(defaultEmptyNumber);
        this.endCountField.setText(defaultEmptyNumber);
    }
}
