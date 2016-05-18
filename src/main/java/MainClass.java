import gui.root.RootPane;
import javafx.application.Application;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainClass {

    /**
     *  @param args - the main arguments.
     */
    public static void main(String[] args) {

        System.out.println("              _           __       _ _ ");
        System.out.println("   __ _  ___ | |_ ___    / _| __ _(_) |  _ ");
        System.out.println("  / _` |/ _ \\| __/ _ \\  | |_ / _` | | | (_) ");
        System.out.println(" | (_| | (_) | || (_) | |  _| (_| | | |  _ ");
        System.out.println("  \\__, |\\___/ \\__\\___/  |_|  \\__,_|_|_| ( ) ");
        System.out.println("  |___/                                 |/ ");

        // Keep this statement at the bottom of this method, it is blocking
        Application.launch(RootPane.class, args);
    }
    
}
