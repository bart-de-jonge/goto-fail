package xml;

import data.DirectorTimeline;
import data.ScriptingProject;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
        try {
            // open document and top-level element
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element projectElement = doc.createElement("scripting-project");
            doc.appendChild(projectElement);
            projectElement.setAttribute("description", project.getDescription());

            // Add childs
            projectElement.appendChild(writeDirectorTimeline(project.getDirectorTimeline(), doc));


            // Write document to file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
            return true;

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Element writeDirectorTimeline(DirectorTimeline directorTimeline, Document doc) {
        Element directorTimelineElement = doc.createElement("director-timeline");

        directorTimelineElement.setAttribute("description", directorTimeline.getDescription());

        return directorTimelineElement;
    }
}
