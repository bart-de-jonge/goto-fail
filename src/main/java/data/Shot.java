package data;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by martijn.
 * This class contains information about a Shot.
 */
public abstract class Shot {

    // The name of the Shot.
    @Getter @Setter
    private String name;

    /**
     * The constructor for the Shot.
     * @param name the name of the Shot
     */
    public Shot(String name) {
        this.name = name;
    }
}
