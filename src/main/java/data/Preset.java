package data;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@XmlRootElement(name = "preset")
public class Preset {
    
    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    
    public Preset(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    public Preset() {
        this(-1, "", "");
    }

}
