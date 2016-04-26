import gui.RootPane;

public class MainClass {

    /**
     *  @param args - the main arguments.
     */
    public static void main(String[] args) {
        RootPane root = new RootPane();
        root.launch(RootPane.class, args);
        System.out.println("Welcome to the main class of goto-fail;");
    }
    
}
