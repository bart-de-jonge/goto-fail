package gui.centerarea;

import java.util.Set;

import data.DirectorShot;
import gui.events.DirectorShotBlockUpdatedEvent;
import gui.events.ShotblockUpdatedEvent;
import gui.root.RootCenterArea;
import javafx.event.EventHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * Class that represents a shotblock for the director.
 */
public class DirectorShotBlock extends ShotBlock {

    // The id of the shot
    @Getter
    private int shotId;
    
    @Getter
    private double paddingBefore;
    
    @Getter
    private double paddingAfter;
    
    @Getter @Setter
    private Set<Integer> timelineIndices;

    // The directorGridpane the shot is in
    @Getter
    private DirectorGridPane grid;
 
    
    /**
     * Constructor.
     * @param rootCenterArea - the rootCenterArea this shot belongs to
     * @param handler - The handler for this DirectorShotBlock
     * @param shot - The shot in the model belonging to this DirectorShotBlock
     */
    public DirectorShotBlock(RootCenterArea rootCenterArea,
                             EventHandler<DirectorShotBlockUpdatedEvent> handler,
                             DirectorShot shot) {

        super(rootCenterArea, shot, DirectorTimetableBlock.class);
        
        this.paddingBefore = shot.getFrontShotPadding();
        this.paddingAfter = shot.getEndShotPadding();
        this.timelineIndices = shot.getTimelineIndices();
        

        this.shotId = shot.getInstance();
        this.grid = rootCenterArea.getDirectorGridPane();

        this.getTimetableBlock().addEventHandler(ShotblockUpdatedEvent.SHOTBLOCK_UPDATED, e -> {

                if (e instanceof DirectorShotBlockUpdatedEvent) {
                    handler.handle((DirectorShotBlockUpdatedEvent) e);
                }
            });

        this.getTimetableBlock().setPrefWidth(grid.getHorizontalElementMinimumSize());
        this.getTimetableBlock().setPrefHeight(grid.getNumberOfVerticalGrids()
                * grid.getVerticalElementSize());
        grid.addDirectorShotBlock(this);
    }

    @Override
    public ShotblockUpdatedEvent getShotBlockUpdatedEvent() {
        return new DirectorShotBlockUpdatedEvent(this);
    }
    
    @Override
    public void setBeginCount(double count, boolean recompute) {
        super.setBeginCount(count, recompute);
    }
    
    /**
     * Set the padding before this director shot block.
     * @param padding the padding to set
     */
    public void setPaddingBefore(double padding) {
        this.paddingBefore = padding;
        ((DirectorTimetableBlock) this.getTimetableBlock())
            .getPaddingBeforeLabel().setText(
                "Front Padding: " + Double.toString(padding));
        ((DirectorTimetableBlock) this.getTimetableBlock())
                .getPaddingBeforeDraggedLabel().setText(
                "Front Padding: " + Double.toString(padding));
    }
    
    /**
     * Set the padding after this director shot block.
     * @param padding the padding to set.
     */
    public void setPaddingAfter(double padding) {
        this.paddingAfter = padding;
        ((DirectorTimetableBlock) this.getTimetableBlock())
            .getPaddingAfterLabel().setText(
                    "Back Padding: " + Double.toString(padding));
        ((DirectorTimetableBlock) this.getTimetableBlock())
                .getPaddingAfterDraggedLabel().setText(
                "Back Padding: " + Double.toString(padding));
    }
    
    @Override
    public void setEndCount(double count, boolean recompute) {
        super.setEndCount(count, recompute);
    }

    @Override
    public DirectorShot getShot() {
        return (DirectorShot) super.getShot();
    }

    /**
     * Remove this DirectorShotBlock's view from the view that it's in.
     */
    public void removeFromView() {
        this.grid.removeDirectorShotBlock(this);
    }
    
    /**
     * Method used for snapping as close to top as possible with current margin.
     */
    public void moveAsCloseToTopAsPossible() {
        double oldBegin = getBeginCount();
        double oldEnd = getEndCount();
        this.setBeginCount(0 + this.getPaddingBefore());
        this.setEndCount(getBeginCount() + (oldEnd - oldBegin));
        this.recompute();
    }
}
