package data;

import lombok.Getter;
import lombok.Setter;

/**
 * The DirectorShot class has more elaborate information for the Director, like the description.
 * Created by martijn.
 */
public class DirectorShot extends Shot {

    // Counter that ensures no shots with duplicate numbers will be created.
    private static int instanceCounter = 0;

    // The description provides additional information for the director.
    @Getter @Setter
    private String description;

    /**
     * The constructor for the Shot.
     * @param name the name of the Shot
     * @param description the description of the Shot
     * @param startCount the start count of the Shot
     * @param endCount the end count of the Shot
     */
    public DirectorShot(String name, String description, int startCount, int endCount) {
        super(instanceCounter, name, startCount, endCount);
        instanceCounter++;
        this.description = description;
    }
}
