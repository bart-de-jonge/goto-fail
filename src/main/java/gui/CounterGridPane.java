package gui;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;

/**
 * Class that represents the grid pane in the counter bar or timeline.
 * @author Mark
 */
public class CounterGridPane extends GridPane {

    // The number of counts in this custom gridpane
    @Getter
    private int numberOfCounts;

    // Height and width of count and timelines. Note that timelineWidth is a minimum.
    @Getter
    private double countHeight = 10;

    /**
     * Constructor for a CounterGridPane.
     * @param numberOfCounts - The number of counts in this gridpane.
     * @param width -  The width of this gridpane.
     * @param height -  The height of this gridpane.
     */
    public CounterGridPane(int numberOfCounts, double width, double height) {
        this.numberOfCounts = numberOfCounts;
        this.setMinWidth(width);
        this.setMinHeight(countHeight * numberOfCounts);
        this.setMaxHeight(countHeight * numberOfCounts);

        this.setGridLinesVisible(true); // this is for debugging. NOT. STYLING.

        // set column constraint
        ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(100.0);
        this.getColumnConstraints().add(cc);

        // set row constraints
        for (int i = 0; i < numberOfCounts; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setMinHeight(100.0);
            rc.setPercentHeight(100.0 / numberOfCounts);
            rc.setVgrow(Priority.ALWAYS);
            this.getRowConstraints().add(rc);
        }
    }

}
