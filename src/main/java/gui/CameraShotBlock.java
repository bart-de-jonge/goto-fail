package gui;

import javafx.event.EventHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Bart.
 */
public class CameraShotBlock extends ShotBlock {

    // The number of the timetable this shot belongs to
    @Getter
    private int timetableNumber;
    @Getter
    private int shotId;
    @Getter
    private TimelinesGridPane grid;

    private CameraShotBlock thisBlock;

    /**
     * Constructor.
     * @param shotId the shot's unique id
     * @param timetableNumber - the timeTableNumber this shot belongs to
     * @param rootCenterArea - the rootCenterArea this shot belongs to
     * @param beginCount - the begin count of this shot
     * @param endCount - the end count of this shot
     * @param handler - The handler for this camerashotblock
     */
    public CameraShotBlock(int shotId, int timetableNumber, RootCenterArea rootCenterArea,
                           double beginCount, double endCount,
                           EventHandler<CameraShotBlockUpdatedEvent> handler) {
        super(rootCenterArea, beginCount, endCount);
        this.shotId = shotId;
        this.timetableNumber = timetableNumber;
        thisBlock = this;
        this.grid = rootCenterArea.getGrid();

        this.getTimetableBlock().addEventHandler(ShotblockUpdatedEvent.SHOTBLOCK_UPDATED, e -> {
                this.setBeginCount(TimelinesGridPane.getRowIndex(
                        this.getTimetableBlock()), false);
                this.setEndCount(TimelinesGridPane.getRowSpan(
                        this.getTimetableBlock()) + this.getBeginCount(), false);
                this.timetableNumber = TimelinesGridPane.getColumnIndex(
                        this.getTimetableBlock());

                if (e instanceof CameraShotBlockUpdatedEvent) {
                    handler.handle((CameraShotBlockUpdatedEvent) e);
                }
            });

        this.getTimetableBlock().setPrefWidth(grid.getTimelineWidth());
        this.getTimetableBlock().setPrefHeight(grid.getNumberOfCounts()
                * grid.getCountHeight());
        grid.addCameraShotBlock(this);
    }

    /**
     * Set the timetable number of this shotblock.
     * @param timetableNumber - the timetableNumber to set
     */
    public void setTimetableNumber(int timetableNumber) {
        this.timetableNumber = timetableNumber;
        this.recompute();
    }

    /**
     * Recompute the position in the grid and repaint with new values.
     */
    @Override
    public void recompute() {
        TimelinesGridPane.setColumnIndex(this.getTimetableBlock(), timetableNumber);
        super.recompute();
    }

    @Override
    public ShotblockUpdatedEvent getShotBlockUpdatedEvent() {
        return new CameraShotBlockUpdatedEvent(this, timetableNumber);
    }
}
