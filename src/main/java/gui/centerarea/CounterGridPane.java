package gui.centerarea;

import gui.centerarea.ScrollableGridPane;

/**
 * Class that represents the grid pane in the scrollable counter bar.
 * @author Mark
 */
public class CounterGridPane extends ScrollableGridPane {

    /**
     * Constructor for a CounterGridPane.
     * @param numberOfCounts - The number of counts in this pane.
     * @param width -  The total width of this pane.
     * @param verticalElementSize -  size of vertical grid lanes.
     */
    public CounterGridPane(int numberOfCounts, int width, int verticalElementSize) {

        super(1, numberOfCounts, width, verticalElementSize);

        setGridLinesVisible(true); // this is for debugging. NOT. STYLING.
    }

}
