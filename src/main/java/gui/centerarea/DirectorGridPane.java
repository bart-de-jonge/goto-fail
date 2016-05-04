package gui.centerarea;

/**
 * Class that represents the grid pane in the scrollable director timeline.
 * @author Mark
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

        setGridLinesVisible(true);
    }

}
