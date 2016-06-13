package gui.centerarea;

import java.util.ArrayList;

import control.CountUtilities;
import javafx.scene.control.Label;
import lombok.extern.log4j.Log4j2;

/**
 * Class that represents the grid pane in the scrollable camera centerarea.
 */
@Log4j2
public class TimelinesGridPane extends ScrollableGridPane {

    /**
     * Constructor.
     * @param numberOfHorizontalGrids - number of horizontal grid lanes.
     * @param numberOfVerticalGrids - number of vertical grid lanes.
     * @param horizontalElementMinimumSize -  minimal size of horizontal grid lanes.
     * @param verticalElementSize -  size of vertical grid lanes.
     */
    public TimelinesGridPane(int numberOfHorizontalGrids, int numberOfVerticalGrids,
                             int horizontalElementMinimumSize, int verticalElementSize) {

        super(numberOfHorizontalGrids, numberOfVerticalGrids,
                horizontalElementMinimumSize, verticalElementSize);

        // add snapping panes
        addPanes();
    }

    /**
     * Add a camerashotblock to this gridpane.
     * @param block - the block to add
     */
    public void addCameraShotBlock(CameraShotBlock block) {
        log.error("Stuff {}", block.getTimetableNumber());
        log.error("BeginCount {}", block.getBeginCount());
        this.add(block.getTimetableBlock(), block.getTimetableNumber(),
                (int) Math.round(block.getBeginCount() * CountUtilities.NUMBER_OF_CELLS_PER_COUNT),
                1, (int) Math.round((block.getEndCount()
                        - block.getBeginCount()) * CountUtilities.NUMBER_OF_CELLS_PER_COUNT));
        block.getTimetableBlock().getInstrumentBox().getChildren().clear();
        block.getInstruments().forEach(e -> {
                block.getTimetableBlock().getInstrumentBox().getChildren()
                .add(new Label(e.getName()));
            });
    }

    /**
     * Add snapping panes to grid. Also apply line separators to grid, once every few skips.
     */
    private void addPanes() {
        setPanes(new ArrayList<>());
        for (int i = 0; i < getNumberOfHorizontalGrids(); i++) {
            int c = 1;
            for (int j = 0; j < getNumberOfVerticalGrids(); j++) {
                SnappingPane pane = new SnappingPane(j, i);
                this.add(pane, i, j);
                getPanes().add(pane);
                if (c > CountUtilities.NUMBER_OF_CELLS_PER_COUNT) {
                    pane.getStyleClass().add("timeline_Background_Lines");
                    c = 2;
                } else {
                    pane.getStyleClass().add("timeline_Background_Empty");
                    c++;
                }
            }
        }
    }
    
    /**
     * Remove a CameraShotBlock from this gridpane.
     * @param block the block to remove
     */
    public void removeCameraShotBlock(CameraShotBlock block) {
        this.getChildren().remove(block.getTimetableBlock());
    }


}
