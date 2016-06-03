package gui.centerarea;

import control.CountUtilities;

import java.util.ArrayList;

/**
 * Class that represents the grid pane in the scrollable director timeline.
 */
public class DirectorGridPane extends ScrollableGridPane {

    /**
     * Constructor for a DirectorGridPane.
     * @param numberOfCounts - The number of counts in this pane.
     * @param width -  The total width of this pane.
     * @param verticalElementSize -  size of vertical grid lanes.
     */
    public DirectorGridPane(int numberOfCounts, int width, int verticalElementSize) {
        super(1, numberOfCounts, width, verticalElementSize);
        setStyle("-fx-background-color: rgba(0,0,0,0.04);");
        addPanes();
    }

    /**
     * Adds a DirectorShotBlock to the gridpane.
     * @param block to add to the grid
     */
    public void addDirectorShotBlock(DirectorShotBlock block) {
        this.add(block.getTimetableBlock(), 0,
            (int) Math.round(block.getBeginCount() * CountUtilities.NUMBER_OF_CELLS_PER_COUNT),
                1, (int) Math.round(block.getEndCount() - block.getBeginCount())
                    * CountUtilities.NUMBER_OF_CELLS_PER_COUNT);
        ((DirectorTimetableBlock) block.getTimetableBlock()).getPaddingBeforeLabel().setText(
                "Front P: " + Double.toString(block.getPaddingBefore()));
        ((DirectorTimetableBlock) block.getTimetableBlock()).getPaddingAfterLabel().setText(
                "Back P: " + Double.toString(block.getPaddingAfter()));
        
    }

    public void removeDirectorShotBlock(DirectorShotBlock block) {
        this.getChildren().remove(block.getTimetableBlock());
    }

    /**
     * Add snapping panes to grid. Also apply line separators to grid, once every few skips.
     */
    private void addPanes() {
        setPanes(new ArrayList<>());
        int c;
        for (int i = 0; i < getNumberOfHorizontalGrids(); i++) {
            c = 1;
            for (int j = 0; j < getNumberOfVerticalGrids(); j++) {
                SnappingPane pane = new SnappingPane(j, i);
                this.add(pane, i, j);
                getPanes().add(pane);
                if (c > CountUtilities.NUMBER_OF_CELLS_PER_COUNT) {
                    pane.getStyleClass().add("director_Background_Lines");
                    c = 2;
                } else {
                    pane.getStyleClass().add("director_Background_Empty");
                    c++;
                }
            }
        }
    }
}
