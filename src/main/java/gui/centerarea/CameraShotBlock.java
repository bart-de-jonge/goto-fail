package gui.centerarea;

import data.CameraShot;
import gui.root.RootCenterArea;
import gui.events.CameraShotBlockUpdatedEvent;
import gui.events.ShotblockUpdatedEvent;
import javafx.event.EventHandler;
import lombok.Getter;

/**
 * Class that represents a camera shot block.
 */
public class CameraShotBlock extends ShotBlock {

    // The number of the timetable this shot belongs to
    @Getter
    private int timetableNumber;

    // The id of the shot
    @Getter
    private int shotId;

    private double previousBeginCount;
    private double previousEndCount;
    private int previousTimetableNumber;

    // The grid this camershotblock belongs to
    @Getter
    private TimelinesGridPane grid;
    

    /**
     * Constructor.
     * @param timetableNumber - the timeTableNumber this shot belongs to
     * @param rootCenterArea - the rootCenterArea this shot belongs to
     * @param handler - The handler for this camerashotblock
     * @param shot - The shot in the model belonging to this camerashotblock
     */
    public CameraShotBlock(int timetableNumber, RootCenterArea rootCenterArea,
                           EventHandler<CameraShotBlockUpdatedEvent> handler, CameraShot shot) {

        super(rootCenterArea, shot, CameraTimetableBlock.class);

        this.shotId = shot.getInstance();
        this.timetableNumber = timetableNumber;
        this.grid = rootCenterArea.getMainTimeLineGridPane();

        this.previousBeginCount = -1;
        this.previousEndCount = -1;
        this.previousTimetableNumber = -1;

        this.getShot().getBeginCountProperty().addListener((observable, oldValue, newValue) -> {
                this.forceSetBeginCount(newValue.doubleValue());
            });

        this.getShot().getEndCountProperty().addListener(((observable, oldValue, newValue) -> {
                this.forceSetEndCount(newValue.doubleValue());
            }));

        this.getTimetableBlock().addEventHandler(ShotblockUpdatedEvent.SHOTBLOCK_UPDATED, e -> {
                this.previousTimetableNumber = this.timetableNumber;
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

    @Override
    public void setBeginCount(double count, boolean recompute) {
        this.previousBeginCount = this.getBeginCount();
        super.setBeginCount(count, recompute);
    }

    @Override
    public void setEndCount(double count, boolean recompute) {
        this.previousEndCount = this.getEndCount();
        super.setEndCount(count, recompute);
    }

    /**
     * Remove this CameraShotBlock's view from the view that it's in.
     */
    public void removeFromView() {
        this.grid.removeCameraShotBlock(this);
    }

    /**
     * Restore this CameraShotBlock to its previous position.
     */
    public void restorePreviousPosition() {
        this.setBeginCount(this.previousBeginCount);
        this.setEndCount(this.previousEndCount);
        this.setTimetableNumber(this.previousTimetableNumber);
        this.recompute();
    }
}
