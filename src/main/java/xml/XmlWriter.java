package xml;

import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.DirectorShot;
import data.DirectorTimeline;
import data.ScriptingProject;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
            
            ArrayList<CameraTimeline> cameraTimelines = project.getCameraTimelines();
            for (CameraTimeline c : cameraTimelines) {
                projectElement.appendChild(writeCameraTimeline(c, doc));
            }


            // Write document to file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            // Commented because of problems between windows and linux
            // maybe we will find a solution in the future
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
            return true;

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Write a camera timeline to an element.
     * @param cameraTimeline the camera timeline to write
     * @param doc the document to write with
     * @return an element for this camera timeline
     */
    private Element writeCameraTimeline(CameraTimeline cameraTimeline, Document doc) {
        Element cameraTimelineElement = doc.createElement("camera-timeline");
        cameraTimelineElement.setAttribute("description", cameraTimeline.getDescription());
        cameraTimelineElement.appendChild(writeCamera(cameraTimeline.getCamera(), doc));
        cameraTimelineElement.appendChild(writeCameraShots(cameraTimeline.getShots(), doc));
        return cameraTimelineElement;
    }
    
    /**
     * Write a camera to an element.
     * @param camera the camera to write
     * @param doc the document to write with
     * @return an element for this camera
     */
    private Element writeCamera(Camera camera, Document doc) {
        Element cameraElement = doc.createElement("camera");
        cameraElement.setAttribute("name", camera.getName());
        cameraElement.setAttribute("description", camera.getDescription());
        cameraElement.setAttribute("movement-margin", Double.toString(camera.getMovementMargin()));
        cameraElement.appendChild(writeCameraType(camera.getCameraType(), doc));
        return cameraElement;
    }
    
    /**
     * Write a camera type to an element.
     * @param type the camera type to write
     * @param doc the document to write with
     * @return an element for this camera type
     */
    private Element writeCameraType(CameraType type, Document doc) {
        Element cameraTypeElement = doc.createElement("camera-type");
        cameraTypeElement.setAttribute("name", type.getName());
        cameraTypeElement.setAttribute("description", type.getDescription());
        cameraTypeElement.setAttribute("movement-margin",
                Double.toString(type.getMovementMargin()));
        return cameraTypeElement;
    }
    
    /**
     * Write a list of camera shots to an element.
     * @param shots the list of camera shots to write
     * @param doc the document to write with
     * @return an element for this lists of camera shots
     */
    private Element writeCameraShots(ArrayList<CameraShot> shots, Document doc) {
        Element shotsElement = doc.createElement("camera-shots");
        shotsElement.setAttribute("number-of-shots", String.format("%d", shots.size()));

        for (CameraShot shot : shots) {
            shotsElement.appendChild(writeCameraShot(shot, doc));
        }

        return shotsElement;
    }
    
    /**
     * Write a camera shot to an element.
     * @param shot the camera shot to write
     * @param doc the document to write with
     * @return an element for this camera shot
     */
    private Element writeCameraShot(CameraShot shot, Document doc) {
        Element shotElement = doc.createElement("camera-shot");
        
        shotElement.setAttribute("name", shot.getName());
        shotElement.setAttribute("description", shot.getDescription());
        shotElement.setAttribute("instance", String.format("%d", shot.getInstance()));
        
        return shotElement;

        
    }

    /**
     * Write a directorTimeline to an element using the given document.
     * @param directorTimeline - the directortimeline to write
     * @param doc - the document to write to
     * @return - the resulting element
     */
    private Element writeDirectorTimeline(DirectorTimeline directorTimeline, Document doc) {
        Element directorTimelineElement = doc.createElement("director-timeline");

        directorTimelineElement.setAttribute("description", directorTimeline.getDescription());
        directorTimelineElement.appendChild(writeDirectorShots(directorTimeline.getShots(), doc));

        return directorTimelineElement;
    }

    /**
     * Write a list of directorshots to an element using the given document.
     * @param shots - the list of shots to write
     * @param doc - the document to write to
     * @return - the written element
     */
    private Element writeDirectorShots(ArrayList<DirectorShot> shots, Document doc) {
        Element shotsElement = doc.createElement("director-shots");
        shotsElement.setAttribute("number-of-shots", String.format("%d", shots.size()));

        for (DirectorShot shot : shots) {
            shotsElement.appendChild(writeDirectorShot(shot, doc));
        }

        return shotsElement;
    }

    /**
     * Write a DirectorShot to an element using the given document.
     * @param shot - the directorshot to write
     * @param doc - the document to write to
     * @return - the wirtten element
     */
    private Element writeDirectorShot(DirectorShot shot, Document doc) {
        Element shotElement = doc.createElement("director-shot");

        shotElement.setAttribute("name", shot.getName());
        shotElement.setAttribute("description", shot.getDescription());
        shotElement.setAttribute("instance", String.format("%d", shot.getInstance()));

        return shotElement;
    }
}
