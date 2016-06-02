package data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Getter;
import lombok.Setter;

/**
 * Class that represents a user.
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    
    /**
     * Enum for all the roles a user can have.
     */
    public static enum Role {
        CAMERA_OPERATOR(0), SHOT_CALLER(1), DIRECTOR(2), NONE(-1);
        
        private int value;
        
        /**
         * Role constructor.
         * @param value the value of the role
         */
        private Role(int value) {
            this.value = value;
        }
        
        /**
         * Get the value.
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }
    
    @Getter @Setter
    private String name;
    
    @Getter
    @XmlTransient
    // DO NOT ADD AN @SETTER FOR THIS VARIABLE
    private Role role;
    
    @Getter @Setter
    private int roleValue;
    
    @Getter @Setter
    @XmlElementWrapper(name = "chosenTimelines")
    @XmlElement(name = "chosenTimeline")
    private ArrayList<Integer> chosenTimelines;
    
    /**
     * Construct a new User.
     * @param name the name of the user
     * @param role the role of the user
     * @param chosenTimelines the timelines chosen by this user
     */
    public User(String name, Role role, ArrayList<Integer> chosenTimelines) {
        this.name = name;
        this.role = role;
        this.chosenTimelines = chosenTimelines;
        this.roleValue = role.getValue();
    }
    
    /**
     * Default constructor.
     */
    public User() {
        this("", Role.NONE, null);
        
    }
    
    /**
     * Setter for role. Seperate because role value needs update as well.
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
        this.roleValue = role.getValue();
    }
}
