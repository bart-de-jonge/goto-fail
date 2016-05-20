package gui.centerarea;

import javafx.geometry.Bounds;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Class representing the grid inside a timeline.
 */
public class ScrollableGridPane extends GridPane {

    @Getter
    private int numberOfHorizontalGrids; // number of horizontal grid lanes.
    @Getter
    private int numberOfVerticalGrids; // number of vertical grid lanes.
    @Getter
    private int horizontalElementMinimumSize; // minimal size of every horizontal grid lane.
    @Getter
    private int verticalElementSize; // size of every vertical grid lane.
    @Getter @Setter
    private ArrayList<SnappingPane> panes;

    /**
     * Constructor of class.
     * @param numberOfHorizontalGrids - number of horizontal grid lanes.
     * @param numberOfVerticalGrids - number of vertical grid lanes.
     * @param horizontalElementMinimumSize -  minimal size of horizontal grid lanes.
     * @param verticalElementSize -  size of vertical grid lanes.
     */
    public ScrollableGridPane(int numberOfHorizontalGrids, int numberOfVerticalGrids,
                              int horizontalElementMinimumSize, int verticalElementSize) {

        this.numberOfHorizontalGrids = numberOfHorizontalGrids;
        this.numberOfVerticalGrids = numberOfVerticalGrids;
        this.horizontalElementMinimumSize = horizontalElementMinimumSize;
        this.verticalElementSize = verticalElementSize;

        // Enforce proper height of elements
        this.setMinHeight(numberOfVerticalGrids * verticalElementSize);
        this.setMaxHeight(numberOfVerticalGrids * verticalElementSize);

        // set horizontal constraints to grid.
        for (int i = 0; i < numberOfHorizontalGrids; i++) {
            ColumnConstraints rc = new ColumnConstraints();
            rc.setMinWidth(horizontalElementMinimumSize);
            rc.setPercentWidth(100.0 / (double) numberOfHorizontalGrids);
            rc.setHgrow(Priority.ALWAYS);
            this.getColumnConstraints().add(rc);
        }

        // set vertical constraints to grid.
        for (int i = 0; i < numberOfVerticalGrids; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setMinHeight(verticalElementSize);
            rc.setPercentHeight(100.0 / numberOfVerticalGrids);
            rc.setVgrow(Priority.ALWAYS);
            this.getRowConstraints().add(rc);
        }
    }

    /**
     * Get the pane in which the scene coordinates lie.
     * @param x - the x coordinate
     * @param y - the y coordinate
     * @return - the SnappingPane, null if none applicable
     */
    public SnappingPane getMyPane(double x, double y) {

        // Correct for points outside grid
        Bounds sceneBounds = this.localToScene(this.getLayoutBounds());
        if (sceneBounds.getMinX() > x) {
            return getMyPane(sceneBounds.getMinX(), y);
        } else if (sceneBounds.getMaxX() < x) {
            return getMyPane(sceneBounds.getMaxX(), y);
        } else if (sceneBounds.getMinY() > y) {
            return getMyPane(x, sceneBounds.getMinY());
        } else if (sceneBounds.getMaxY() < y) {
            return getMyPane(x, sceneBounds.getMaxY());
        } 

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
