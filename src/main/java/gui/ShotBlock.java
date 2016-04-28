package gui;

import javafx.event.EventHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Bart.
 */
public abstract class ShotBlock {

    // The timetableBlock used for displaying this block
    @Getter @Setter
    private TimetableBlock timetableBlock;

    // The begin count of this shot
    @Getter
    private double beginCount;

    // The end shot of this count
    @Getter @Setter
    private double endCount;

    /**
     * Constructor.
     * @param rootCenterArea - the rootCenterArea this shot belongs to
     * @param beginCount - the begin count of this shot
     * @param endCount = the end count of this shot
     */
    public ShotBlock(RootCenterArea rootCenterArea, double beginCount, double endCount) {
        this.timetableBlock = new TimetableBlock(rootCenterArea);
        this.timetableBlock.setStyle("-fx-background-color: orange");
        this.beginCount = beginCount;
        this.endCount = endCount;
    }

    /**
     * Set the begin count of this shotblock.
     * @param count - the new begincount
     */
    public void setBeginCount(double count) {
        this.beginCount = count;
        this.repaint();
    }

    /**
     * Set the end count of this shotblock.
     * @param count - the new endcount
     */
    public void setEndCount(double count) {
        this.endCount = count;
        this.repaint();;
    }

    /**
     * Recompute position in grid and repaint with these settings.
     */
    public void repaint() {
        TimelinesGridPane.setRowIndex(this.timetableBlock,
                (int) Math.round(beginCount));
        TimelinesGridPane.setRowSpan(this.timetableBlock,
                (int) Math.round(endCount - beginCount));
    }

    /**
     * Attach an eventhandler to this block.
     * @param handler - the handler to attach
     */
    public void attachEventHandler(EventHandler<ShotblockUpdatedEvent> handler) {
        this.getTimetableBlock().addEventHandler(ShotblockUpdatedEvent.SHOTBLOCK_UPDATED, handler);
    }

    /**
     * Set style of this ShotBlock.
     * @param style - the style to set
     */
    public void setStyle(String style) {
        this.timetableBlock.setStyle(style);
    }
}
