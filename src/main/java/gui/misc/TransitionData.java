package gui.misc;

import javafx.animation.Interpolator;
import javafx.beans.property.Property;
import lombok.Getter;

public class TransitionData<T> {
    @Getter
    private Property<T> property;
    @Getter
    private int ms;
    @Getter
    private Interpolator interpolator;
    
    /**
     * Constructor.
     * @param property the property for which the transition is used
     * @param ms the time in ms the transition takes
     * @param interpolator the interpolator to use
     */
    public TransitionData(Property<T> property, int ms, Interpolator interpolator) {
        this.property = property;
        this.ms = ms;
        this.interpolator = interpolator;
    }
}
