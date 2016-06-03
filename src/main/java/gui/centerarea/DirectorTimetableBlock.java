package gui.centerarea;

import gui.misc.TweakingHelper;
import gui.root.RootCenterArea;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 *      This class is used to display DirectorShotBlocks on the DirectorGridPane.
 *      All styling and labels should be added here.
 */
public class DirectorTimetableBlock extends TimetableBlock {
    @Getter
    private Label paddingBeforeLabel;
    @Getter
    private Label paddingAfterLabel;

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
        paddingBeforeLabel = initFrontPaddingLabel(this.getContentPane());
        paddingAfterLabel = initEndPaddingLabel(this.getContentPane());
    }
    
    /**
     * Init the front padding label.
     * @param vbox the content pane to put the label in
     * @return the initialized label
     */
    private Label initFrontPaddingLabel(VBox vbox) {
        Label res = new Label("Front P: " + Double.toString(
                (((DirectorShotBlock) this.getParentBlock()).getPaddingBefore())));
        res.maxWidthProperty().bind(super.widthProperty());
        res.getStyleClass().add("block_Text_Normal");
        res.setStyle("-fx-text-fill:" + TweakingHelper.getColorString(2) + ";");
        vbox.getChildren().add(res);
        return res;
    }
    
    /**
     * Init the after padding label.
     * @param vbox the content pane to put the label in
     * @return the initialized label
     */
    private Label initEndPaddingLabel(VBox vbox) {
        Label res = new Label("Back P: " + Double.toString(
                (((DirectorShotBlock) this.getParentBlock()).getPaddingAfter())));
        res.maxWidthProperty().bind(super.widthProperty());
        res.getStyleClass().add("block_Text_Normal");
        res.setStyle("-fx-text-fill:" + TweakingHelper.getColorString(2) + ";");
        vbox.getChildren().add(res);
        return res;
    }

    /**
     * Init the dragpane with blur, title, count and description labels.
     */
    private void initDraggedPane() {
        super.initDraggedPane(getRootCenterArea().getDirectorAnchorPane());
    }

    /**
     * Init feedbackpane with blur and darken.
     */
    private void initFeedbackPane() {
        super.initFeedbackPane(getRootCenterArea().getDirectorGridPane());
    }
}
