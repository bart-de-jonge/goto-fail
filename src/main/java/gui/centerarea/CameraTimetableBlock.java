package gui.centerarea;

import gui.root.RootCenterArea;

/**
 * Created by Bart.
 */
public class CameraTimetableBlock extends TimetableBlock {


    /**
     * Constructor for TimetableBlock class.
     *
     * @param pane   - the parent pane.
     * @param parent - the parent node
     */
    CameraTimetableBlock(RootCenterArea pane, ShotBlock parent) {
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

    @Override
    void initDraggedPane() {
        // Init the dragpane with blur, title, count and description labels
        super.initDraggedPane();

        this.getDraggedPane().getStyleClass().add("block_Background_Dragged");
        this.getDraggedContentPane().getStyleClass().add("block_Foreground_Dragged");
    }

    @Override
    void initFeedbackPane() {
        // Init feedbackpane with blur and darken
        super.initFeedbackPane();
    }
}
