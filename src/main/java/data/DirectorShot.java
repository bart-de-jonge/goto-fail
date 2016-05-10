package data;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

/**
 * The DirectorShot class has more elaborate information for the Director, like the description.
 * Created by martijn.
 */
@XmlRootElement(name = "directorShot")
@ToString
@Log4j2
public class DirectorShot extends Shot {

    // Counter that ensures no shots with duplicate numbers will be created.
    private static int instanceCounter = 0;

    // Additional time to film before the real shot starts
    @Getter
    @Setter
    private double frontShotPadding = 0.0;

    // Additional time to film before the real shot starts
    @Getter
    @Setter
    private double endShotPadding = 0.0;

    /**
     * Default Constructor.
     */
    public DirectorShot() {
        this("", "", 0, 0, 0, 0);
    }

    /**
     * The constructor for the Shot.
     * @param name the name of the Shot
     * @param description the description of the Shot
     * @param startCount the start count of the Shot
     * @param endCount the end count of the Shot
     * @param frontShotPadding the additional time to film before the shot starts
     * @param endShotPadding the additional time to film after the shot starts
     */
    public DirectorShot(String name, String description, double startCount, double endCount,
                        double frontShotPadding, double endShotPadding) {
        super(instanceCounter, name, description, startCount, endCount);
        this.frontShotPadding = frontShotPadding;
        this.endShotPadding = endShotPadding;
        log.debug("Created new DirectorShot");
        instanceCounter++;
    }
}
