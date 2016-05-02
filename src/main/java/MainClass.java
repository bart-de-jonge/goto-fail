import gui.RootPane;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainClass {
    /**
     * Add a logger to the application to keep track of all the things happening in the
     * code. This logger has different levels and is able to distingguish between error
     * levels.
     */
    static final Logger logger = LogManager.getLogger();

    /**
     *  @param args - the main arguments.
     */
    public static void main(String[] args) {
        logger.error("-------------------------------------------------------------------");
        logger.error("| Starting application.");
        logger.error("-------------------------------------------------------------------");

        // Keep this statement at the bottom of this method, it is blocking
        Application.launch(RootPane.class, args);
    }
}
