package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import lombok.Getter;

/**
 * Created by Bart.
 */
public class DetailView extends VBox {

    private GridPane grid;

    private int numberOfColumns = 3;
    private int numberOfRows = 2;

    private double columnWidth = 200;
    private double rowHeight = 50;

    @Getter
    private TextField nameField;

    @Getter
    private TextField descriptionField;

    public DetailView() {
        this.grid = new GridPane();
        this.getChildren().addAll(grid);

        for(int i = 0; i < numberOfColumns; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        }
        for(int i = 0; i < numberOfRows; i++) {
            grid.getRowConstraints().add(new RowConstraints(rowHeight));
        }

        grid.setGridLinesVisible(true);

        initName();
        initDescription();

    }

    public void setDescription(String description) {
        descriptionField.setText(description);
    }

    public void setName(String name) {
        nameField.setText(name);
    }

    private void initName() {
        nameField = new TextField("Placeholder");
        HBox nameBox = new HBox();

        Label specifierLabel = new Label("Name:");
        nameBox.getChildren().addAll(specifierLabel, nameField);
        grid.add(nameBox, 0, 0);
    }

    private void initDescription() {
        descriptionField = new TextField("");

        HBox descriptionBox = new HBox();

        Label specifierLabel = new Label("Description:");
        descriptionBox.getChildren().addAll(specifierLabel, descriptionField);
        grid.add(descriptionBox, 0, 1);
        grid.setColumnSpan(descriptionBox, 2);
    }

}
