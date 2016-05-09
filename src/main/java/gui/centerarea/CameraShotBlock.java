package gui.centerarea;

import data.CameraShot;
import gui.root.RootCenterArea;
import gui.events.CameraShotBlockUpdatedEvent;
import gui.events.ShotblockUpdatedEvent;
import javafx.event.EventHandler;
import lombok.Getter;

/**
 * Created by Bart.
 */
public class CameraShotBlock extends ShotBlock {

    // The number of the timetable this shot belongs to
    @Getter
    private int timetableNumber;

    // The id of the shot
    @Getter
    private int shotId;

    // The grid this camershotblock belongs to
    @Getter
    private TimelinesGridPane grid;
    
    public CameraShotBlock(CameraShot shot, int timetableNumber, RootCenterArea rootCenterArea,
                           EventHandler<CameraShotBlockUpdatedEvent> handler) {
        this(shot.getInstance(), timetableNumber, rootCenterArea, shot.getBeginCount(),
                shot.getEndCount(), shot.getDescription(), shot.getName(), handler, shot);
    }

    /**
     * Constructor.
     * @param shotId the shot's unique id
     * @param timetableNumber - the timeTableNumber this shot belongs to
     * @param rootCenterArea - the rootCenterArea this shot belongs to
     * @param beginCount - the begin count of this shot
     * @param endCount - the end count of this shot
     * @param handler - The handler for this camerashotblock
     * @param description - the description of this camerashotblock
     * @param name - the name of this camerashotblock
     * @param shot - The shot in the model belonging to this camerashotblock
     */
    public CameraShotBlock(int shotId, int timetableNumber, RootCenterArea rootCenterArea,
                           double beginCount, double endCount, String description, String name,
                           EventHandler<CameraShotBlockUpdatedEvent> handler, CameraShot shot) {

        super(rootCenterArea, beginCount, endCount, description,
                name, shot, CameraTimetableBlock.class);

        this.shotId = shotId;
        this.timetableNumber = timetableNumber;
        this.grid = rootCenterArea.getMainTimeLineGridPane();

        this.getTimetableBlock().addEventHandler(ShotblockUpdatedEvent.SHOTBLOCK_UPDATED, e -> {
                this.setBeginCount(TimelinesGridPane.getRowIndex(
                        this.getTimetableBlock())/4f, false);
                this.setEndCount((TimelinesGridPane.getRowSpan(
                    this.getTimetableBlock()) + this.getBeginCount()) /4f, false);
                this.timetableNumber = TimelinesGridPane.getColumnIndex(
                    this.getTimetableBlock());

                if (e instanceof CameraShotBlockUpdatedEvent) {
                    handler.handle((CameraShotBlockUpdatedEvent) e);
                }
            });

        this.getTimetableBlock().setPrefWidth(grid.getHorizontalElementMinimumSize());
        this.getTimetableBlock().setPrefHeight(grid.getNumberOfVerticalGrids()
                * grid.getVerticalElementSize());
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
        TimelinesGridPane.setColumnIndex(this.getTimetableBlock(),
                timetableNumber);
        super.recompute();
    }

    @Override
    public ShotblockUpdatedEvent getShotBlockUpdatedEvent() {
        return new CameraShotBlockUpdatedEvent(this, timetableNumber);
    }

    @Override
    public CameraShot getShot() {
        return (CameraShot) super.getShot();
    }

    /**
     * Remove this CameraShotBlock's view from the view that it's in.
     */
    public void removeFromView() {
        this.grid.removeCameraShotBlock(this);
    }
}
