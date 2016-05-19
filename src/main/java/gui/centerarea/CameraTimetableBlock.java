package gui.centerarea;

import gui.misc.TweakingHelper;
import gui.root.RootCenterArea;

/**
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
    }

    /**
     * Initialize the dragpane with blur, title, count and description labels.
     */
    void initDraggedPane() {
        super.initDraggedPane(getRootCenterArea().getMainTimeLineAnchorPane());
    }

    void initFeedbackPane() {
        // Init feedbackpane with blur and darken
        super.initFeedbackPane(getRootCenterArea().getMainTimeLineGridPane());
    }
}
