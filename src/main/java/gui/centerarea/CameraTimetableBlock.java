package gui.centerarea;

import gui.root.RootCenterArea;

/**
 * @author Bart.
 *
 *      This class is used to display CameraShotBlocks on the TimelinesGridPane.
 *      All styling and labels should be added here.
 */
public class CameraTimetableBlock extends TimetableBlock {

    /**
     * Constructor for CameraTimetableBlock class.
     *
     * @param pane   - the parent pane.
     * @param parent - the parent node
     */
    public CameraTimetableBlock(RootCenterArea pane, ShotBlock parent) {
        super(pane, parent);

        // pane helpers
        initNormalPane();
        initDraggedPane();
        initFeedbackPane();

        // Mouse event handlers
        super.addMouseEventHandlers(true);
    }

    @Override
    void initNormalPane() {
        // Add width/height properties, title label, count label and description label
        super.initNormalPane();

        this.getContentPane().getStyleClass().add("block_Foreground_Normal");

    }

    void initDraggedPane() {
        // Init the dragpane with blur, title, count and description labels
        super.initDraggedPane(getRootCenterArea().getMainTimeLineAnchorPane());

        this.getDraggedPane().getStyleClass().add("block_Background_Dragged");
        this.getDraggedContentPane().getStyleClass().add("block_Foreground_Dragged");
    }

    void initFeedbackPane() {
        // Init feedbackpane with blur and darken
        super.initFeedbackPane(getRootCenterArea().getMainTimeLineGridPane());
    }
}
