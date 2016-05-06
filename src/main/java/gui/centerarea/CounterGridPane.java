package gui.centerarea;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * Class that represents the grid pane in the scrollable counter bar.
 * @author Mark
 */
public class CounterGridPane extends ScrollableGridPane {

    /**
     * Styling string here is temporary, until my (Mark's) big CSS branch is ready.
     * It has separate CSS files for all stylized elements, but I'm not merging it
     * until its done.
     */
    private String labelStyle = "-fx-padding: 0 5 0 0;";

    /**
     * Constructor for a CounterGridPane.
     * @param numberOfCounts - The number of counts in this pane.
     * @param width -  The total width of this pane.
     * @param verticalElementSize -  size of vertical grid lanes.
     */
    public CounterGridPane(int numberOfCounts, int width, int verticalElementSize) {
        super(1, numberOfCounts, width, verticalElementSize);

        initGridConstaints(numberOfCounts, verticalElementSize);
        initGridNumbers(numberOfCounts, verticalElementSize);
    }

    /**
     * Initialize grid constraints, offset by half a count size.
     * @param numberOfCounts number of counts used.
     * @param verticalElementSize size of usual vertical elements.
     */
    private void initGridConstaints(int numberOfCounts, int verticalElementSize) {
        // remove row constraints, as we want to override them
        getRowConstraints().clear();

        // offset our grid by half a grid size, so grid numbers are located in the middle!
        RowConstraints rcPre = new RowConstraints();
        rcPre.setMinHeight(verticalElementSize / 2.0);
        rcPre.setVgrow(Priority.ALWAYS);
        this.getRowConstraints().add(rcPre);

        // set actual vertical constraints to grid.
        for (int i = 0; i < numberOfCounts; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setMinHeight(verticalElementSize);
            rc.setPercentHeight(100.0 / numberOfCounts);
            rc.setVgrow(Priority.ALWAYS);
            this.getRowConstraints().add(rc);
        }
    }

    /**
     * Add number labels to all the correct grids.
     * @param numberOfCounts number of counts used.
     * @param verticalElementSize size of usual vertical elements.
     */
    private void initGridNumbers(int numberOfCounts, int verticalElementSize) {
        for (int i = 4; i < numberOfCounts; i += 4) {
            Label label = new Label(Integer.toString(i));
            label.setStyle(labelStyle);
            setHalignment(label, HPos.RIGHT);
            this.add(label, 0, i);
        }
    }
}
