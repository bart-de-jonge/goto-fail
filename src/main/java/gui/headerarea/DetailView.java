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

    private boolean visible = false;

    Label invisibleLabel;
    HBox nameBox;
    HBox descriptionBox;
    HBox beginCountBox;
    HBox endCountBox;

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
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0, 0, 0, TweakingHelper.GENERAL_PADDING));
        this.setSpacing(TweakingHelper.GENERAL_SPACING * 2);
        initName();
        initDescription();
        initBeginCount();
        initEndCount();
        initInvisible();
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
        beginCountBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label specifierLabel = new Label("Start Count:");
        beginCountBox.getChildren().addAll(specifierLabel, beginCountField);
        beginCountBox.setAlignment(Pos.CENTER);
        this.getChildren().add(beginCountBox);
    }

    /**
     * Init the endcount part of the detailview.
     */
    private  void initEndCount() {
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
        this.getChildren().removeAll(nameBox, descriptionBox, beginCountBox, endCountBox);
        this.setPadding(new Insets(0));
        this.setSpacing(0);
        invisibleLabel = new Label("Select a shot to edit it.");
        invisibleLabel.setAlignment(Pos.CENTER);
        invisibleLabel.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        this.getChildren().add(invisibleLabel);
    }

    /**
     * Make content of the DetailView visible.
     */
    public void setVisible() {
        if (!visible) {
            this.setPadding(new Insets(0, 0, 0, TweakingHelper.GENERAL_PADDING));
            this.setSpacing(TweakingHelper.GENERAL_SPACING * 2);
            this.getChildren().remove(invisibleLabel);
            this.getChildren().addAll(nameBox, descriptionBox, beginCountBox, endCountBox);
            visible = true;
        }
    }

    /**
     * Make content of the DetailView invisible.
     */
    public void setInvisible() {
        if (visible) {
            this.setPadding(new Insets(0));
            this.setSpacing(0);
            this.getChildren().removeAll(nameBox, descriptionBox, beginCountBox, endCountBox);
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
}
