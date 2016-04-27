package data;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by martijn.
 * This class contains information about a Shot.
 */
public class Shot {

    // Counter that ensures no shots with duplicate numbers will be created.
    private static int instanceCounter = 0;

    // The name of the Shot.
    @Getter @Setter
    private String name;

    // The description of the Shot.
    @Getter @Setter
    private String description;

    // The number of the Shot.
    @Getter
    private int number;

    /**
     * The constructor for the Shot
     * @param number the number of the Shot
     * @param name the name of the Shot.
     * @param description the description of the Shot.
     */
    public Shot(int number, String name, String description) {
        this.number = instanceCounter;
        instanceCounter++;
        this.name = name;
        this.description = description;
    }
}
