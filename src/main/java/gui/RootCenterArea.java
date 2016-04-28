package gui;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

/**
 * Class representing the center (main section) of the gui.
 * In other words, the time line view goes here.
 */
class RootCenterArea extends ScrollPane {

    //private double width, height;

    @Getter @Setter
    private int countHeight = 50;
    @Getter @Setter
    private int timelineWidth = 200;
    @Getter @Setter
    private int numberOfTimelines = 5;
    @Getter @Setter
    private int numberOfCounts = 10;

    @Getter
    private RootPane rootPane;

    @Getter
    private GridPane grid;

    @Getter
    private AnchorPane parentPane;

    private ArrayList<SnappingPane> panes;

    /**
     * Constructor class
     * @param rootPane parent pane passed through.
     */
    RootCenterArea(RootPane rootPane) {
        this.rootPane = rootPane;
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

        addPanes();

        grid.setGridLinesVisible(true);
        grid.setHgap(50);
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

        grid.add(rect1, 0, 0, 1, 2);
        grid.add(rect2, 1, 1, 1, 3);
    }

    /**
     * Add snapping panes to grid.
     */
    private void addPanes() {
        panes = new ArrayList<>();

        for (int i = 0; i < numberOfCounts; i++) {
            for (int j = 0; j < numberOfTimelines; j++) {
                SnappingPane pane = new SnappingPane(i, j, 200, 50);
//                pane.setVisible(true);
//                pane.setStyle("-fx-border-style: solid inside;"
//                        + "-fx-border-width: 3;"
//                        + "-fx-border-color: red;"
//                        + "-fx-background-color: green;");
                grid.add(pane, j, i);
                panes.add(pane);
            }
        }
    }

    /**
     * Get the pane in which the scene coordinates lie.
     * @param x - the x coordinate
     * @param y - the y coordinate
     * @return - the SnappingPane, null if none applicable
     */
    public SnappingPane getMyPane(double x, double y) {
        for (SnappingPane pane : panes) {
            Bounds bounds = pane.localToScene(pane.getBoundsInLocal());
            if (bounds.contains(x, y)) {
                if (((y - bounds.getMinY()) * 2) > pane.getHeight()) {
                    pane.setBottomHalf(true);
                } else {
                    pane.setBottomHalf(false);
                }
                return pane;
            }
        }
        return null;
    }
}

