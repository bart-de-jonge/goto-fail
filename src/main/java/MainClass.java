import gui.RootPane;
import javafx.application.Application;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainClass {

    /**
     *  @param args - the main arguments.
     */
    public static void main(String[] args) {

        log.info("--------------------------------------------------------------------------");
        log.error("Starting application.");
        log.info("--------------------------------------------------------------------------");

        // Keep this statement at the bottom of this method, it is blocking
        Application.launch(RootPane.class, args);
    }
    
}
