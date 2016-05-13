package gui.headerarea;

import gui.misc.TweakingHelper;
import gui.styling.StyledTextfield;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * Created by Bart.
 */
public class DetailView extends HBox {

    //private GridPane grid;

    private String style = "-fx-background-color: "
            + TweakingHelper.STRING_TERTIARY + ";"
            + "-fx-min-height: 50;";

    private static final String defaultEmptyString = "";
    private static final String defaultEmptyNumber = "0";

    private int numberOfColumns = 4;
    private int numberOfRows = 1;

    private double columnWidth = 200;
    private double rowHeight = 50;

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
//        this.grid = new GridPane();
//        this.getChildren().addAll(grid);
//
//        for (int i = 0; i < numberOfColumns - 1; i++) {
//            grid.getColumnConstraints().add(new ColumnConstraints(columnWidth));
//        }
//        for (int i = 0; i < numberOfRows; i++) {
//            grid.getRowConstraints().add(new RowConstraints(rowHeight));
//        }
//
//        grid.setGridLinesVisible(true);
        this.setStyle(style);
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
        beginCountField.setAlignment(Pos.CENTER);
        //beginCountField.setPrefWidth(50);
        HBox startCountBox = new HBox();
        Label specifierLabel = new Label("Start Count:");
        startCountBox.getChildren().addAll(specifierLabel, beginCountField);
        this.getChildren().add(startCountBox);
        //grid.add(startCountBox, 1, 0);
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
        //endCountField.setPrefWidth(50);
        HBox endCountBox = new HBox();
        Label specifierLabel = new Label("End Count:");
        endCountBox.getChildren().addAll(specifierLabel, endCountField);
        this.getChildren().add(endCountBox);
        //grid.add(endCountBox, 2, 0);
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
        HBox nameBox = new HBox();
        Label specifierLabel = new Label("Name:");
        nameBox.getChildren().addAll(specifierLabel, nameField);
        this.getChildren().add(nameBox);
        //grid.add(nameBox, 0, 0);
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
        //descriptionField.setPrefWidth(300);
        HBox descriptionBox = new HBox();
        Label specifierLabel = new Label("Description:");
        descriptionBox.getChildren().addAll(specifierLabel, descriptionField);
        this.getChildren().add(descriptionBox);
        //grid.add(descriptionBox, 3, 0);
        //grid.setColumnSpan(descriptionBox, 2);
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
