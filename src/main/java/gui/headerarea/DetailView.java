package gui.headerarea;

import gui.misc.TweakingHelper;
import gui.styling.StyledMenuButton;
import gui.styling.StyledTextfield;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import lombok.Getter;

/**
 * The detail view is the view with details for shots.
 */
public class DetailView extends FlowPane {

    private String style = "-fx-background-color: "
            + TweakingHelper.getBackgroundHighString() + ";"
            + "-fx-padding: 10 5 10 5;"
            + "-fx-border-width: 0 0 1px 0;"
            + "-fx-border-color: rgba(0,0,0,0.40);";

    protected static final String defaultEmptyString = "";
    protected static final String defaultEmptyNumber = "0";

    private boolean visible = false;

    @Getter
    Label invisibleLabel;
    @Getter
    HBox nameBox;
    @Getter
    HBox descriptionBox;
    @Getter
    HBox beginCountBox;
    @Getter
    HBox endCountBox;
    @Getter
    HBox instrumentsBox;

    @Getter
    private StyledTextfield nameField;
    @Getter
    private StyledTextfield descriptionField;
    @Getter
    private DoubleTextField beginCountField;
    @Getter
    private DoubleTextField endCountField;
    @Getter
    private StyledMenuButton selectInstrumentsButton;

    /**
     * Constructor.
     */
    public DetailView() {
        this.setMinHeight(100);
        this.setHgap(TweakingHelper.GENERAL_SPACING);
        this.setVgap(TweakingHelper.GENERAL_SPACING);
        this.setStyle(style);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0));
        initName();
        initDescription();
        initBeginCount();
        initEndCount();
        createSelectInstrumentsButton();
        initInvisible();

        this.setPadding(new Insets(0));

        this.getChildren().clear();
        this.getChildren().add(invisibleLabel);
    }
    
    public boolean getVisible() {
        return visible;
    }
    
    public void setVisibleForView(boolean visible) {
        this.visible = visible;
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
    public String formatDouble(double d) {
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
    private void initBeginCount() {
        beginCountField = new DoubleTextField("123");
        beginCountBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label specifierLabel = new Label("Start Count:");
        beginCountBox.getChildren().addAll(specifierLabel, beginCountField);
        beginCountBox.setAlignment(Pos.CENTER);
        this.getChildren().add(beginCountBox);
    }

    /**
     * Init the endcount part of the detailview.
     */
    private void initEndCount() {
        endCountField = new DoubleTextField("123");
        endCountField.setAlignment(Pos.CENTER);
        endCountBox = new HBox(TweakingHelper.GENERAL_SPACING);
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
        nameField.setAlignment(Pos.CENTER);
        nameBox = new HBox(TweakingHelper.GENERAL_SPACING);
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
        descriptionField.setAlignment(Pos.CENTER);
        descriptionBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label specifierLabel = new Label("Description:");
        descriptionBox.getChildren().addAll(specifierLabel, descriptionField);
        descriptionBox.setAlignment(Pos.CENTER);
        this.getChildren().add(descriptionBox);
    }

    /**
     * Initializes the detailview with no selected shot.
     */
    private void initInvisible() {
        this.getChildren().removeAll(nameBox, descriptionBox, beginCountBox,
                endCountBox, instrumentsBox);
        invisibleLabel = new Label("Select a shot to edit it.");
        invisibleLabel.setPrefHeight(50);
        invisibleLabel.setMinHeight(50);
        invisibleLabel.setAlignment(Pos.CENTER);
    }

    /**
     * Make content of the DetailView visible.
     */
    public void setVisible() {
        if (!visible) {
            this.getChildren().clear();
            this.getChildren().addAll(nameBox, descriptionBox, beginCountBox, endCountBox);
            createSelectInstrumentsButton();
            visible = true;
        }
    }

    /**
     * Make content of the DetailView invisible.
     */
    public void setInvisible() {
        if (visible) {
            this.getChildren().clear();
            this.getChildren().add(invisibleLabel);
            visible = false;
        }
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

    /**
     * Init experimental dropdown menu to select instruments.
     */
    private void createSelectInstrumentsButton() {
        selectInstrumentsButton = new StyledMenuButton("Edit Instruments selection");
        this.getChildren().add(selectInstrumentsButton);
    }
}
