package gui.centerarea;

import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

/**
 * Snapping Panes are invisible panes that are used for snapping detection.
 * Each cell in the grid gets a snapping pane
 */
public class SnappingPane extends Pane {

    @Getter @Setter
    private int row;
    @Getter @Setter
    private int column;
    @Getter @Setter
    private boolean bottomHalf;

    /**
     * Constructor.
     * @param row - the row number of this snapping pane
     * @param column - the column number of this snapping pane
     */
    public SnappingPane(int row, int column) {
        this.row = row;
        this.column = column;
        this.bottomHalf = false;
    }
}
