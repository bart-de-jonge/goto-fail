package gui.centerarea;

import control.CountUtilities;
import data.Instrument;
import data.Shot;
import gui.misc.TweakingHelper;
import gui.root.RootCenterArea;
import gui.events.ShotblockUpdatedEvent;
import javafx.event.EventHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 *      Abstract superclass for different kind of shots in the gui.
 *      This class is the edge between the View and the Controller.
 *      The controller only talks with this class and all the gui stuff
 *      is done for him.
 */
@Log4j2
public abstract class ShotBlock {

    // The timetableBlock used for displaying this block
    @Getter @Setter
    private TimetableBlock timetableBlock;

    // The begin count of this shot
    @Getter
    private double beginCount;

    // The end shot of this count
    @Getter
    private double endCount;

    // Drag and drop helper fields
    private double tempBeginCount;
    private double tempEndCount;

    // The description of this shotblock
    @Getter
    private String description;

    // The name of this shotblock
    @Getter
    private String name;

    // The actual shot in the model that belongs to this ShotBlock
    // For updating the model using the controller of events
    @Getter
    private Shot shot;

    @Getter
    private boolean colliding;
    
    @Getter
    private ArrayList<Instrument> instruments;
    
    private static String BACKGROUND_COLOR_STRING = "-fx-background-color: ";

    /**
     * Constructor.
     * @param rootCenterArea - the rootCenterArea this shot belongs to
     * @param shot - the shot of this ShotBlock
     * @param timetableBlockClass - the class of the timetableblock implementation
     *        that belongs to this shotblock. This must be a valid subclass of
     *        TimetableBlock with its default constructor implemented. Otherwise
     *        timetableBlock is initialized to null.
     */
    public ShotBlock(RootCenterArea rootCenterArea, Shot shot, Class<?> timetableBlockClass) {
        this.description = shot.getDescription();
        this.name = shot.getName();
        this.beginCount = shot.getBeginCount();
        this.endCount = shot.getEndCount();
        this.shot = shot;
        this.instruments = shot.getInstruments();
        this.colliding = false;
        this.tempBeginCount = -1;
        this.tempEndCount = -1;

        // TimetableBlock set in subclass constructors!!
        try {
            Constructor<?> constructor = timetableBlockClass
                    .getConstructor(RootCenterArea.class, ShotBlock.class);

            this.setTimetableBlock((TimetableBlock) constructor.newInstance(rootCenterArea, this));
        } catch (Exception e) {
            log.error("No valid timetableblock class, could not initialize timetableblock!");
            this.timetableBlock = null;
        }
    }

    /**
     * Set colliding field of this camerashotblock.
     * Apply styling changes here
     * @param colliding - the new colliding value
     */
    public void setColliding(boolean colliding) {
        // TODO: Mark do styling stuff here
        this.colliding = colliding;

        if (colliding) {
            this.timetableBlock.setStyle(BACKGROUND_COLOR_STRING
                    + TweakingHelper.getColorString(0) + ";"
                    + "-fx-border-color: red;"
                    + "-fx-border-width: 3;");
            this.timetableBlock.getContentPane().setStyle(BACKGROUND_COLOR_STRING
                    + TweakingHelper.getColorString(3) + ";"
                    + "-fx-border-color: "
                    + TweakingHelper.getColorString(2) + ";");
        } else {
            this.timetableBlock.setStyle(BACKGROUND_COLOR_STRING
                    + TweakingHelper.getColorString(0) + ";"
                    + "-fx-border-color: "
                    + TweakingHelper.getColorString(1) + ";");
            this.timetableBlock.getContentPane().setStyle(BACKGROUND_COLOR_STRING
                    + TweakingHelper.getColorString(3) + ";"
                    + "-fx-border-color: "
                    + TweakingHelper.getColorString(2) + ";");
        }
    }

    /**
     * Set the begin count of this ShotBlock.
     * @param count - the new begincount
     * @param recompute - should we recompute after setting
     */
    public void setBeginCount(double count, boolean recompute) {
        if (count < this.endCount || !recompute) {
            this.beginCount = count;
        }
        if (recompute) {
            this.recompute();
        }
        redrawCounts();
    }

    /**
     * Set the begin count of this ShotBlock.
     * @param count - the new begincount
     */
    public void setBeginCount(double count) {
        this.setBeginCount(count, true);
    }

    /**
     * Set the end count of this ShotBlock.
     * @param count - the new endcount
     * @param recompute - should we recompute after setting
     */
    public void setEndCount(double count, boolean recompute) {
        if (count > this.beginCount || !recompute) {
            this.endCount = count;
        }
        if (recompute) {
            this.recompute();
        }
        redrawCounts();
    }

    /**
     * Set the end count of this ShotBlock.
     * @param count - the new endcount
     */
    public void setEndCount(double count) {
        this.setEndCount(count, true);
    }

    /**
     * Force the begin count to be set during a split drag and drop.
     * If it violates a ShotBlock's assertion that the begin count must be less
     * than the end count then it is cached unless the end count is already cached.
     * If this is the case then both are discarded.
     * @param beginCount Begin count to set
     */
    public void forceSetBeginCount(double beginCount) {
        // Take care of temporary movement
        double newEndCount = this.endCount;
        boolean cacheUntouched = true;
        if (this.tempEndCount >= 0) {
            cacheUntouched = false;
            newEndCount = this.tempEndCount;
            this.tempEndCount = -1;
        }

        if (beginCount < newEndCount) {
            this.beginCount = beginCount;
            this.endCount = newEndCount;
            this.recompute();
            this.redrawCounts();
        } else if (cacheUntouched) {
            // Cache if the end count wasn't already cached
            this.tempBeginCount = beginCount;
        }
    }

    /**
     * Force the end count to be set during a split drag and drop.
     * If it violates a ShotBlock's assertion that the end count must be greater
     * than the begin count then it is cached unless the begin count is already cached.
     * If this is the case then both are discarded.
     * @param endCount End count to set
     */
    public void forceSetEndCount(double endCount) {
        // Take care of temporary movement
        double newBeginCount = this.beginCount;
        boolean cacheUntouched = true;
        if (this.tempBeginCount >= 0) {
            cacheUntouched = false;
            newBeginCount = this.tempBeginCount;
            this.tempBeginCount = -1;
        }

        if (newBeginCount < endCount) {
            this.beginCount = newBeginCount;
            this.endCount = endCount;
            this.recompute();
            this.redrawCounts();
        } else if (cacheUntouched) {
            // Cache if the begin count wasn't already cached
            this.tempEndCount = endCount;
        }
    }

    /**
     * Set name of this ShotBlock. No Lombok because it does some extra work for the GUI.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
        timetableBlock.getTitleNormalLabel().setText(name);
        timetableBlock.getTitleDraggedLabel().setText(name);
    }

    /**
     * Set description of this ShotBlock. No Lombok because it does some extra work for the GUI.
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;

        timetableBlock.getDescriptionNormalLabel().setText("Description: " + description);
        timetableBlock.getDescriptionDraggedLabel().setText("Description: " + description);
    }

    /**
     * Helper function to redraw block counts.
     */
    private void redrawCounts() {
        timetableBlock.getCountNormalLabel().setText("Count: " + beginCount + " - " + endCount);
        timetableBlock.getCountDraggedLabel().setText("Counnt: " + beginCount + " - " + endCount);
    }

    /**
     * Recompute position in grid and repaint with these settings.
     */
    public void recompute() {
        TimelinesGridPane.setRowIndex(timetableBlock,
                (int) Math.round(beginCount * CountUtilities.NUMBER_OF_CELLS_PER_COUNT));
        TimelinesGridPane.setRowSpan(timetableBlock,
                (int) Math.round((endCount - beginCount)
                        * CountUtilities.NUMBER_OF_CELLS_PER_COUNT));
    }

    /**
     * Attach an eventhandler to this block.
     * @param handler - the handler to attach
     */
    public void attachEventHandler(EventHandler<ShotblockUpdatedEvent> handler) {
        this.getTimetableBlock().addEventHandler(ShotblockUpdatedEvent.SHOTBLOCK_UPDATED, handler);
    }

    /**
     * Return correct event for the specific ShotBlock.
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
