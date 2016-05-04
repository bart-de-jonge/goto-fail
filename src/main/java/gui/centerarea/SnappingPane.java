package gui.centerarea;

import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Bart.
 * Snapping Panes are invisible panes that are used for snapping detection
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
     * @param width - the width of this snapping pane
     * @param height - the height of this snapping pane
     */
    public SnappingPane(int row, int column, double width, double height) {
        this.row = row;
        this.column = column;
        this.bottomHalf = false;
    }
}
