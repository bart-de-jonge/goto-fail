package gui;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

import java.sql.RowIdLifetime;
import java.util.ArrayList;

/**
 * Class representing the center (main section) of the gui.
 * In other words, the time line view goes here.
 */
class RootCenterArea extends ScrollPane {

    //private double width, height;

    int countHeight = 50;
    int timelineWidth = 200;
    int numberOfTimelines = 5;
    int numberOfCounts = 10;

    @Getter
    private GridPane grid;

    @Getter
    private AnchorPane parentPane;

    private ArrayList<MyPane> panes;

    /**
     * Constructor class
     * @param rootPane parent pane passed through.
     */
    RootCenterArea(RootPane rootPane) {

        parentPane = new AnchorPane();
        grid = new GridPane();
        parentPane.getChildren().add(grid);


        parentPane.setMaxWidth(1000);
        parentPane.setMinWidth(1000);
        parentPane.setMinHeight(1000);
        parentPane.setMinWidth(1000);
        grid.setMaxWidth(1000.0);
        grid.setMinWidth(1000.0);
        grid.setMaxHeight(1000.0);
        grid.setMinHeight(1000.0);
        setContent(parentPane);

        grid.setGridLinesVisible(true);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // set constraints
        for (int i = 0; i < numberOfTimelines; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(timelineWidth));
        }
        for (int i = 0; i < numberOfCounts; i++) {
            grid.getRowConstraints().add(new RowConstraints(countHeight));
        }

        TimetableBlock rect1 = new TimetableBlock(this);
        TimetableBlock rect2 = new TimetableBlock(this);

        addPanes();

        grid.add(rect1, 0, 0, 1, 2);
        grid.add(rect2, 1, 1, 1, 3);
    }

    private void addPanes() {
        panes = new ArrayList<MyPane>();

        for(int i = 0; i < numberOfCounts; i++) {
            for (int j = 0; j < numberOfTimelines; j++) {
                MyPane pane = new MyPane(i, j, 200, 50);
                grid.add(pane, j, i);
                panes.add(pane);
            }
        }
    }

    public MyPane getMyPane(double x, double y) {
        for (MyPane pane : panes) {
            Bounds bounds = pane.localToScene(pane.getBoundsInLocal());
            if (bounds.contains(x, y)) {
                if (((y - bounds.getMinY()) * 2) > pane.getHeight()) {
                    pane.bottomHalf = true;
                    System.out.println("Bottom half");
                } else {
                    pane.bottomHalf = false;
                }
                return pane;
            }
        }
        return null;
    }
}

class MyPane extends Pane {
    int row, column;
    boolean bottomHalf;

    public MyPane(int row, int column, double width, double height) {
        this.row = row;
        this.column = column;
        this.setWidth(width);
        this.setHeight(height);
        this.bottomHalf = false;
    }
}
