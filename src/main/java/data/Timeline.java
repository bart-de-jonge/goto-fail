package data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Bart.
 * Abstract class for centerarea.
 */
@XmlRootElement(name = "timeline")
@ToString
public abstract class Timeline {

    // Description of this Timeline.
    @Getter
    private String description;

    // The project this timeline is currently in
    @Setter
    @XmlTransient
    private ScriptingProject project;
    
    /**
     * Default constructor.
     */
    public Timeline() {
        description = "";
        project = null;
    }

    /**
     * Constructor.
     * @param description the description of this timeline
     * @param project the project that contains this timeline
     */
    public Timeline(String description, ScriptingProject project) {
        this.description = description;
        this.project = project;
    }

    /**
     * Checks overlap between two shots. If the two shots are colliding, the two shots will have
     * their colliding variables set to true.
     * @param s1 the first Shot to check overlap
     * @param s2 the other Shot to check overlap
     * @param seconds the seconds to use for the margin
     * @return true when the two shots are colliding, false if not
     */
    public boolean checkOverlap(Shot s1, Shot s2, double seconds) {
        if (s1.areOverlapping(s2, project.secondsToCounts(seconds))) {
            s1.setColliding(true);
            s2.setColliding(true);
            return true;
        }
        return false;
    }
    
    @XmlTransient
    public ScriptingProject getProject() {
        return project;
    }
}
