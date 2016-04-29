import data.ScriptingProject;
import gui.RootPane;
import javafx.application.Application;
import xml.XmlWriter;

public class MainClass {

    /**
     *  @param args - the main arguments.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the main class of goto-fail;");

        // Keep this statement at the bottom of this method, it is blocking
        Application.launch(RootPane.class, args);
    }
}
