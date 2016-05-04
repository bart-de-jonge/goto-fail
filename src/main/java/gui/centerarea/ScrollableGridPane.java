package gui.centerarea;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;

/**
 * Class representing the grid inside a timeline.
 * Created by markv on 5/4/2016.
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

}
