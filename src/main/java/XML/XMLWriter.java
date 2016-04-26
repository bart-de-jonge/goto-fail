package xml;

import data.ScriptingProject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Bart.
 */
public class XmlWriter {

    // The filename pointing to the file belonging to this writer
    @Getter @Setter
    private String fileName;

    /**
     * Constructor.
     * @param fileName - the filename pointing to the file belonging to this writer
     */
    public XmlWriter(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Write the project to the given fileName in XML format.
     * @param project   - the project to write
     * @return          - boolean describing if the write was successful
     */
    public boolean writeProject(ScriptingProject project) {
        return false;
    }
}
