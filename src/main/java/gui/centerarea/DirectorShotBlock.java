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
    
    @Getter @Setter
    private double paddingBefore;
    
    @Getter @Setter
    private double paddingAfter;
    
    @Getter @Setter
    private Set<Integer> timelineIndices;

    // The directorGridpane the shot is in
    @Getter
    private DirectorGridPane grid;
    
    
    

    public DirectorShotBlock(DirectorShot shot, RootCenterArea rootCenterArea,
                             EventHandler<DirectorShotBlockUpdatedEvent> handler) {
        this(shot.getInstance(), rootCenterArea, shot.getBeginCount(),
                shot.getEndCount(), shot.getDescription(), shot.getName(), handler, shot);
    }
    
    /**
     * Construct a new DirectorShotBlock.
     * @param shotId the id of this shot block
     * @param rootCenterArea the RootCenterArea that contains this shot block
     * @param beginCount the begin count of the shot
     * @param endCount the end count of the shot
     * @param description the description of the shot
     * @param paddingBefore the padding before the shot
     * @param paddingAfter the padding after the shot
     * @param timelineIndices the timeline indices for this shot
     * @param handler the changed handler for this shot
     * @param shot the shot
     */
    public DirectorShotBlock(int shotId, RootCenterArea rootCenterArea,
                             double beginCount, double endCount, String description, 
                             double paddingBefore, double paddingAfter, 
                             Set<Integer> timelineIndices,
                             EventHandler<DirectorShotBlockUpdatedEvent> handler,
                             DirectorShot shot) {
        this(shotId, rootCenterArea, beginCount, endCount, description,
                shot.getName(), handler, shot);
        
        this.paddingBefore = paddingBefore;
        this.paddingAfter = paddingAfter;
        this.timelineIndices = timelineIndices;
        
        
    }

    /**
     * Constructor.
     * @param shotId the shot's unique id
     * @param rootCenterArea - the rootCenterArea this shot belongs to
     * @param beginCount - the begin count of this shot
     * @param endCount - the end count of this shot
     * @param handler - The handler for this DirectorShotBlock
     * @param description - the description of this DirectorShotBlock
     * @param name - the name of this DirectorShotBlock
     * @param shot - The shot in the model belonging to this DirectorShotBlock
     */
    public DirectorShotBlock(int shotId, RootCenterArea rootCenterArea,
                             double beginCount, double endCount, String description, String name,
                             EventHandler<DirectorShotBlockUpdatedEvent> handler,
                             DirectorShot shot) {

        super(rootCenterArea, beginCount, endCount, description,
                name, shot, DirectorTimetableBlock.class);

        this.shotId = shotId;
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
    public DirectorShot getShot() {
        return (DirectorShot) super.getShot();
    }

    /**
     * Remove this DirectorShotBlock's view from the view that it's in.
     */
    public void removeFromView() {
        this.grid.removeDirectorShotBlock(this);
    }
}
