package gui.headerarea;

import gui.styling.StyledTextfield;
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
public class DetailView extends VBox {

    private GridPane grid;

    private static final String defaultEmptyString = "";
    private static final String defaultEmptyNumber = "0";

    private int numberOfColumns = 3;
    private int numberOfRows = 2;

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
        this.grid = new GridPane();
        this.getChildren().addAll(grid);

        for (int i = 0; i < numberOfColumns; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        }
        for (int i = 0; i < numberOfRows; i++) {
            grid.getRowConstraints().add(new RowConstraints(rowHeight));
        }

        grid.setGridLinesVisible(true);

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
        beginCountField = new DoubleTextField();
        beginCountField.setText("123");
        beginCountField.setPrefWidth(50);

        HBox startCountBox = new HBox();

        Label specifierLabel = new Label("Start Count:");
        startCountBox.getChildren().addAll(specifierLabel, beginCountField);
        grid.add(startCountBox, 1, 0);
    }

    /**
     * Init the endcount part of the detailview.
     */
    private  void initEndCount() {
        endCountField = new DoubleTextField();
        endCountField.setText("123");
        endCountField.setPrefWidth(50);

        HBox endCountBox = new HBox();

        Label specifierLabel = new Label("End Count:");
        endCountBox.getChildren().addAll(specifierLabel, endCountField);
        grid.add(endCountBox, 2, 0);
    }

    /**
     * Init the name part of the detailview.
     */
    private void initName() {
        nameField = new StyledTextfield("Placeholder");
        HBox nameBox = new HBox();

        Label specifierLabel = new Label("Name:");
        nameBox.getChildren().addAll(specifierLabel, nameField);
        grid.add(nameBox, 0, 0);
    }

    /**
     * Init the description part of the detailview.
     */
    private void initDescription() {
        descriptionField = new StyledTextfield("");
        descriptionField.setPrefWidth(300);

        HBox descriptionBox = new HBox();

        Label specifierLabel = new Label("Description:");
        descriptionBox.getChildren().addAll(specifierLabel, descriptionField);
        grid.add(descriptionBox, 0, 1);
        grid.setColumnSpan(descriptionBox, 2);
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
