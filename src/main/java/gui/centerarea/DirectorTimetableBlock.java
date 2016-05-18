package gui.centerarea;

import gui.root.RootCenterArea;

/**
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

    /**
     * Init the dragpane with blur, title, count and description labels.
     */
    private void initDraggedPane() {
        super.initDraggedPane(getRootCenterArea().getDirectorAnchorPane());

        this.getDraggedPane().setStyle("-fx-background-color: black");
        this.getDraggedContentPane().setStyle("-fx-background-color: purple");
    }

    /**
     * Init feedbackpane with blur and darken.
     */
    private void initFeedbackPane() {
        super.initFeedbackPane(getRootCenterArea().getDirectorGridPane());
        this.getFeedbackPane().setStyle("-fx-background-color: red");
    }
}
