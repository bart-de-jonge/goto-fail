package xml;

import data.ScriptingProject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Bart.
 */
public class XmlReader {

    // The filename pointing to the file belonging to this reader
    @Getter @Setter
    private String fileName;

    /**
     * Constructor.
     * @param fileName - the filename pointing to the file belonging to this reader
     */
    public XmlReader(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Write the project to the given fileName in XML format.
     * @return          - the read project, or null of the read was not successful
     */
    public ScriptingProject readProject() {
        return null;
    }
}
