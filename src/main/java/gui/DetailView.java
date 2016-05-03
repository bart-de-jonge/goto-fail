package gui;

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

    @Getter
    private NumberTextField startCountField;

    @Getter
    private NumberTextField endCountField;

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
        initStartCount();
        initEndCount();
    }

    public void setDescription(String description) {
        descriptionField.setText(description);
    }

    public void setName(String name) {
        nameField.setText(name);
    }

    public void setStartCount(int count) {
        startCountField.setText(String.format("%d", count));
    }

    public void setEndCount(int count) {
        endCountField.setText(String.format("%d", count));
    }

    private  void initStartCount() {
        startCountField = new NumberTextField();
        startCountField.setText("123");
        startCountField.setPrefWidth(50);

        HBox startCountBox = new HBox();

        Label specifierLabel = new Label("Start Count:");
        startCountBox.getChildren().addAll(specifierLabel, startCountField);
        grid.add(startCountBox, 1, 0);
    }

    private  void initEndCount() {
        endCountField = new NumberTextField();
        endCountField.setText("123");
        endCountField.setPrefWidth(50);

        HBox endCountBox = new HBox();

        Label specifierLabel = new Label("End Count:");
        endCountBox.getChildren().addAll(specifierLabel, endCountField);
        grid.add(endCountBox, 2, 0);
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
        descriptionField.setPrefWidth(300);

        HBox descriptionBox = new HBox();

        Label specifierLabel = new Label("Description:");
        descriptionBox.getChildren().addAll(specifierLabel, descriptionField);
        grid.add(descriptionBox, 0, 1);
        grid.setColumnSpan(descriptionBox, 2);
    }

}
