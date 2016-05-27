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
    @Setter
    private static int instanceCounter = 0;

    @Getter
    @Setter
    @XmlTransient
    private DirectorShot directorShot;
    
 // The instancenumber of the Shot.
    @Getter
    private int instance;
   
    /**
     * Default constructor.
     */
    public CameraShot() {
        super();
        this.instance = instanceCounter;
        CameraShot.incrementCounter();
    }

    /**
     * The constructor for the Shot.
     * @param name the name of the Shot
     * @param description the description of the Shot
     * @param startCount the count the Shot starts
     * @param endCount the count the Shot ends
     * @param directorShot the parent DirectorShot
     */
    public CameraShot(String name, String description,
                      double startCount, double endCount, DirectorShot directorShot) {
        super(name, description, startCount, endCount);
        log.debug("Created new CameraShot");
        this.directorShot = directorShot;
        this.instance = instanceCounter;
        CameraShot.incrementCounter();
    }

    /**
     * Constructor.
     * @see #CameraShot(String, String, double, double, DirectorShot)
     */
    public CameraShot(String name, String description, double startCount, double endCount) {
        this(name, description, startCount, endCount, null);
    }

    /**
     * Static method to increment the instance counter.
     */
    public static void incrementCounter() {
        log.error("GAVE INSTANCE {}",instanceCounter);
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
