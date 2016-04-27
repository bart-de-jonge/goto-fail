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
     */
    public DirectorShot(String name, String description) {
        super(instanceCounter, name);
        instanceCounter++;
        this.description = description;
    }
}
