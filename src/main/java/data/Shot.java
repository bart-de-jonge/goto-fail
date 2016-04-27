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

    // The instancenumber of the Shot.
    @Getter
    private int instance;

    /**
     * The constructor for the Shot.
     * @param instance the instance number of the Shot
     * @param name the name of the Shot
     */
    public Shot(int instance, String name) {
        this.name = name;
        this.instance = instance;
    }
}
