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

    private CameraShotBlock thisBlock;

    /**
     * Constructor.
     * @param timetableNumber - the timeTableNumber this shot belongs to
     * @param rootCenterArea - the rootCenterArea this shot belongs to
     * @param beginCount - the begin count of this shot
     * @param endCount - the end count of this shot
     */
    public CameraShotBlock(int timetableNumber, RootCenterArea rootCenterArea,
                           double beginCount, double endCount) {
        super(rootCenterArea, beginCount, endCount);
        this.timetableNumber = timetableNumber;
        thisBlock = this;

        this.getTimetableBlock().addEventHandler(ShotblockUpdatedEvent.SHOTBLOCK_UPDATED,
                e -> System.out.println("Block updated"));
    }

    /**
     * Set the timetable number of this shotblock.
     * @param timetableNumber - the timetableNumber to set
     */
    public void setTimetableNumber(int timetableNumber) {
        this.timetableNumber = timetableNumber;
        this.repaint();
    }

    /**
     * Recompute the position in the grid and repaint with new values.
     */
    @Override
    public void repaint() {
        TimelinesGridPane.setColumnIndex(this.getTimetableBlock(), timetableNumber);
        super.repaint();
    }
}
