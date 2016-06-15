package data;

import lombok.Getter;
import lombok.Setter;

public class Instrument implements Cloneable {
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
    
    /**
     * Default constructor for XML.
     */
    public Instrument() {
        this("", "");
    }
    
    @Override
    public Instrument clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Instrument instrument = new Instrument(this.name, this.description);
        return instrument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Instrument that = (Instrument) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getDescription() != null ? getDescription().equals(that.getDescription()) : that.getDescription() == null;

    }
}
