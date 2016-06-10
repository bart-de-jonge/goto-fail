package data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Instrument {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    
    /**
     * Construct a new Instrument.
     * @param name the name of this instrument
     * @param description the description of this instrument
     */
    public Instrument(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
