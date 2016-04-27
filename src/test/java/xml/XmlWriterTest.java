package xml;

import data.DirectorTimeline;
import data.ScriptingProject;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by Bart.
 */
public class XmlWriterTest {
    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @Test
    public void writeProject() throws Exception {
        ScriptingProject project = new ScriptingProject("A test project");
        XmlWriter writer = new XmlWriter("src/test/java/xml/test_files/test-write.xml");
        project.setDirectorTimeline(new DirectorTimeline("A test director timeline"));

        writer.writeProject(project);

        String file = readFile("src/test/java/xml/test_files/test-write.xml", StandardCharsets.UTF_8);

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n" +
                "<scripting-project description=\"A test project\">\r\n" +
                "    <director-timeline description=\"A test director timeline\"/>\r\n" +
                "</scripting-project>\r\n", file);
    }

    @Test
    public void getFileName() throws Exception {
        XmlWriter writer = new XmlWriter("src/test/java/xml/test_files/test-write.xml");
        assertEquals("src/test/java/xml/test_files/test-write.xml", writer.getFileName());
    }

    @Test
    public void setFileName() throws Exception {
        XmlWriter writer = new XmlWriter("src/test/java/xml/test_files/test-write.xml");
        writer.setFileName("new=file-name");
        assertEquals("new=file-name", writer.getFileName());
    }

}