package data;

import javax.xml.bind.annotation.XmlRootElement;

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
    
    /**
     * Default Constructor.
     */
    public DirectorShot() {
        this("", "", 0, 0);
    }

    /**
     * The constructor for the Shot.
     * @param name the name of the Shot
     * @param description the description of the Shot
     * @param startCount the start count of the Shot
     * @param endCount the end count of the Shot
     */
    public DirectorShot(String name, String description, double startCount, double endCount) {
        super(instanceCounter, name, description, startCount, endCount);
        log.debug("Created new DirectorShot");
        instanceCounter++;
    }
}
