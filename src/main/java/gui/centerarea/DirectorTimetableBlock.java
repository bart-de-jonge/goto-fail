package gui.centerarea;

import gui.root.RootCenterArea;

/**
 * @author Bart.
 *
 *      This class is used to display DirectorShotBlocks on the DirectorGridPane.
 *      All styling and labels should be added here.
 */
public class DirectorTimetableBlock extends TimetableBlock {

    /**
     * Constructor for DirectorTimetableBlock class.
     *
     * @param pane   - the parent pane.
     * @param parent - the parent node
     */
    public DirectorTimetableBlock(RootCenterArea pane, ShotBlock parent) {
        super(pane, parent);

        // pane helpers
        initNormalPane();
        initDraggedPane();
        initFeedbackPane();

        // Mouse event handlers
        super.addMouseEventHandlers(false);
    }

    @Override
    void initNormalPane() {
        // Add width/height properties, title label, count label and description label
        super.initNormalPane();

        this.getContentPane().getStyleClass().add("block_Foreground_Normal");

    }

    void initDraggedPane() {
        // Init the dragpane with blur, title, count and description labels
        super.initDraggedPane(getRootCenterArea().getDirectorAnchorPane());

        this.getDraggedPane().getStyleClass().add("block_Background_Dragged");
        this.getDraggedContentPane().getStyleClass().add("block_Foreground_Dragged");
    }

    void initFeedbackPane() {
        // Init feedbackpane with blur and darken
        super.initFeedbackPane(getRootCenterArea().getDirectorGridPane());
    }
}
