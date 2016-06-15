package data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * This class extends the Shot class, this one is specific for the CameraTimeline.
 */
@XmlRootElement(name = "cameraShot")
@XmlAccessorType(XmlAccessType.FIELD)
@Log4j2
public class CameraShot extends Shot implements Cloneable {

    // Counter that ensures no shots with duplicate numbers will be created. 
    @Setter @Getter
    private static int instanceCounter = 0;

    @Getter
    @Setter
    @XmlTransient
    private DirectorShot directorShot;
    
 // The instancenumber of the Shot.
    @Getter @Setter
    private int instance;
    
    @Getter @Setter
    private int presetId;
   
    /**
     * Default constructor.
     */
    public CameraShot() {
        super();
        this.instance = instanceCounter;
        this.presetId = -1;
        CameraShot.incrementCounter();
    }

    /**
     * The constructor for the Shot.
     * @param shotData the shot data
     * @param directorShot the parent DirectorShot
     */
    public CameraShot(GeneralShotData shotData, DirectorShot directorShot) {
        super(shotData.getName(), shotData.getDescription(), 
                shotData.getStartCount(), shotData.getEndCount());
        log.debug("Created new CameraShot");
        this.directorShot = directorShot;
        this.instance = instanceCounter;
        this.presetId = -1;
        CameraShot.incrementCounter();
    }

    /**
     * Constructor.
     * @see #CameraShot(String, String, double, double, DirectorShot)
     */
    public CameraShot(String name, String description, double startCount, double endCount) {
        this(new GeneralShotData(name, description, startCount, endCount), null);
    }
    
    @Override
    public CameraShot clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        CameraShot result = new CameraShot(getName(), getDescription(),
                getBeginCount(), getEndCount());
        result.setDirectorShot(getDirectorShot());
        result.setColliding(isColliding());
        result.setInstruments(getInstruments());
        result.setPresetId(getPresetId());
        return result;
    }

    /**
     * Static method to increment the instance counter.
     */
    public static void incrementCounter() {
        instanceCounter++;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CameraShot) {
            CameraShot that = (CameraShot) other;
            return instance == that.instance;
        }
        return false;
    }
}
