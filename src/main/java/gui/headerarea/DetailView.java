package gui.headerarea;

import java.util.ArrayList;

import org.controlsfx.control.CheckComboBox;

import data.Instrument;
import gui.misc.TweakingHelper;
import gui.styling.StyledTextfield;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * The detail view is the view with details for shots.
 */
public class DetailView extends VBox {

    private String style = "-fx-background-color: "
            + TweakingHelper.getBackgroundHighString() + ";"
            + "-fx-min-height: 100;"
            + "-fx-border-width: 0 0 1px 0;"
            + "-fx-border-color: rgba(0,0,0,0.40);";

    protected static final String defaultEmptyString = "";
    protected static final String defaultEmptyNumber = "0";

    
    private boolean visible = false;
    
    @Getter
    private HBox itemBox;

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
    private CheckComboBox<String> instrumentsDropdown;

    /**
     * Constructor.
     */
    public DetailView() {
        itemBox = new HBox();
        itemBox.setSpacing(TweakingHelper.GENERAL_SPACING);
        this.setStyle(style);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0));
        this.setSpacing(TweakingHelper.GENERAL_SPACING * 2);
        initName();
        initDescription();
        initBeginCount();
        initEndCount();
        initInstruments();
        initInvisible();
        this.getChildren().add(itemBox);
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
    
    public void setInstruments(ArrayList<Instrument> instruments) {
        System.out.println("Setting instruments");
        System.out.println("size is " + instruments.size());
        this.instrumentsDropdown.getItems().clear();
        instruments.forEach(e -> {
            this.instrumentsDropdown.getItems().add(e.getName());
        });
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
        itemBox.getChildren().add(beginCountBox);
    }
    
    private void initInstruments() {
        System.out.println("Initing New ComboCheckBox");
        instrumentsDropdown = new CheckComboBox<>();
        instrumentsBox = new HBox(TweakingHelper.GENERAL_SPACING);
        Label instrumentsLabel = new Label("Instruments:");
        instrumentsBox.getChildren().addAll(instrumentsLabel, instrumentsDropdown);
        instrumentsBox.setAlignment(Pos.CENTER);
        itemBox.getChildren().add(instrumentsBox);
        
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
        itemBox.getChildren().add(endCountBox);
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
        itemBox.getChildren().add(nameBox);
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
        itemBox.getChildren().add(descriptionBox);
    }

    /**
     * Initializes the detailview with no selected shot.
     */
    private void initInvisible() {
        this.getChildren().removeAll(nameBox, descriptionBox, beginCountBox, endCountBox, instrumentsBox);
        this.setPadding(new Insets(0));
        this.setSpacing(0);
        invisibleLabel = new Label("Select a shot to edit it.");
        invisibleLabel.setAlignment(Pos.CENTER);
        invisibleLabel.setPrefWidth(TweakingHelper.GENERAL_SIZE);
        itemBox.getChildren().add(invisibleLabel);
    }

    /**
     * Make content of the DetailView visible.
     */
    public void setVisible() {
        if (!visible) {
            this.setPadding(new Insets(0, 0, 0, TweakingHelper.GENERAL_PADDING));
            this.setSpacing(TweakingHelper.GENERAL_SPACING * 2);
            itemBox.getChildren().clear();
            itemBox.getChildren().remove(invisibleLabel);
            itemBox.getChildren().addAll(nameBox, descriptionBox, beginCountBox, endCountBox, instrumentsBox);
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
            itemBox.getChildren().removeAll(nameBox, descriptionBox, beginCountBox, endCountBox, instrumentsBox);
            itemBox.getChildren().add(invisibleLabel);
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
