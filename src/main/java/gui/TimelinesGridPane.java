package gui;

import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;


/**
 * Created by Bart.
 */
public class TimelinesGridPane extends GridPane {

    // The number of timelines in this custom gridpane
    @Getter
    private int numberOfTimelines;

    // The number of counts in this custom gridpane
    @Getter
    private int numberOfCounts;

    private ArrayList<SnappingPane> panes;

    // Height and width of count and timelines. Note that timelineWidth is a minimum.
    @Getter
    private double countHeight = 10;
    @Getter
    private double timelineWidth = 100;

    /**
     * Constructor.
     * @param numberOfTimelines - The number of timelines in this gridpane
     * @param numberOfCounts - The number of counts in this gridpane
     * @param width - The width of this gridpane
     * @param height - the height of this gridpane
     */
    public TimelinesGridPane(int numberOfTimelines, int numberOfCounts,
                             double width, double height) {
        this.numberOfTimelines = numberOfTimelines;
        this.numberOfCounts = numberOfCounts;
        this.panes = new ArrayList<>();
        this.setMinWidth(width);
        this.setMinHeight(countHeight * numberOfCounts);
        this.setMaxHeight(countHeight * numberOfCounts);

        addPanes();

        this.setGridLinesVisible(false);
        // offset to the left
        this.setPadding(new Insets(0, 0, 0, 200));

        // set constraints, with minimum size 100x100, and maximum size infinite.
        for (int i = 0; i < numberOfTimelines; i++) {
            ColumnConstraints rc = new ColumnConstraints();
            rc.setMinWidth(100.0);
            rc.setPercentWidth(100.0 / numberOfTimelines);
            rc.setHgrow(Priority.ALWAYS);
            this.getColumnConstraints().add(rc);
        }
        for (int i = 0; i < numberOfCounts; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setMinHeight(100.0);
            rc.setPercentHeight(100.0 / numberOfCounts);
            rc.setVgrow(Priority.ALWAYS);
            this.getRowConstraints().add(rc);
        }
    }

    /**
     * Add a camerashotblock to this gridpane.
     * @param block - the block to add
     */
    public void addCameraShotBlock(CameraShotBlock block) {
        this.add(block.getTimetableBlock(), block.getTimetableNumber(),
                (int) Math.round(block.getBeginCount()), 1,
                (int) Math.round(block.getEndCount() - block.getBeginCount()));
    }

    /**
     * Add snapping panes to grid.
     */
    private void addPanes() {
        panes = new ArrayList<>();
        int c;
        for (int i = 0; i < numberOfTimelines; i++) {
            c = 1;
            for (int j = 0; j < numberOfCounts; j++) {
                SnappingPane pane = new SnappingPane(j, i, 200, countHeight);
                this.add(pane, i, j);
                panes.add(pane);
                if (c > 4) {
                    pane.setStyle("-fx-border-color: gray;"
                            + "-fx-border-width: 0.1px 1px 0px 1px;");
                    c = 2;
                } else {
                    pane.setStyle("-fx-border-color: gray;"
                            + "-fx-border-width: 0px 1px 0px 1px;");
                    c++;
                }
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
