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
public class CameraShot extends Shot {

    // Counter that ensures no shots with duplicate numbers will be created. 
    @Setter @Getter
    private static int instanceCounter = 0;

    @Getter
    @Setter
    @XmlTransient
    private DirectorShot directorShot;
    
 // The instancenumber of the Shot.
    @Getter
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

    /**
     * Static method to increment the instance counter.
     */
    public static void incrementCounter() {
        instanceCounter++;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((directorShot == null) ? 0 : directorShot.hashCode());
        result = prime * result + instance;
        return result;
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
