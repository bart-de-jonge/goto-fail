package gui;

import data.CameraShot;
import data.Shot;
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

    // The description of this shotblock
    @Getter @Setter
    private String description;

    // The name of this shotblock
    @Getter @Setter
    private String name;

    // The actual shot in the model that belongs to this shotblock
    // For updating the model using the controller o eventss
    @Getter
    private Shot shot;

    /**
     * Constructor.
     * @param rootCenterArea - the rootCenterArea this shot belongs to
     * @param beginCount - the begin count of this shot
     * @param endCount - the end count of this shot
     * @param description - the description of this shot
     * @param name - the name of this shot
     * @param shot - the shot of this shotblock
     */
    public ShotBlock(RootCenterArea rootCenterArea, double beginCount, double endCount,
                     String description, String name, Shot shot) {
        this.description = description;
        this.name = name;
        this.timetableBlock = new TimetableBlock(rootCenterArea, this);
        this.timetableBlock.setStyle("-fx-background-color: orange");
        this.beginCount = beginCount;
        this.endCount = endCount;
        this.shot = shot;
    }

    /**
     * Set the begin count of this shotblock.
     * @param count - the new begincount
     * @param recompute - should we recompute after setting
     */
    public void setBeginCount(double count, boolean recompute) {
        if (count <= this.endCount - 1) {
            this.beginCount = count;
        }
        if (recompute) {
            this.recompute();
        }
    }

    /**
     * Set the begin count of this shotblock.
     * @param count - the new begincount
     */
    public void setBeginCount(double count) {
        this.setBeginCount(count, true);
    }

    /**
     * Set the end count of this shotblock.
     * @param count - the new endcount
     * @param recompute - should we recompute after setting
     */
    public void setEndCount(double count, boolean recompute) {
        if (count >= this.beginCount + 1) {
            this.endCount = count;
        }
        if (recompute) {
            this.recompute();
        }
    }

    /**
     * Set the end count of this shotblock.
     * @param count - the new endcount
     */
    public void setEndCount(double count) {
        this.setEndCount(count, true);
    }

    /**
     * Recompute position in grid and repaint with these settings.
     */
    public void recompute() {
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
     * Return correct event for the specific shotblock.
     * @return - the event
     */
    public abstract ShotblockUpdatedEvent getShotBlockUpdatedEvent();

    /**
     * Set style of this ShotBlock.
     * @param style - the style to set
     */
    public void setStyle(String style) {
        this.timetableBlock.setStyle(style);
    }
}
