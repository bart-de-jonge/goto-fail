import lombok.Getter;
import lombok.Setter;

public class MainClass {
    @Getter @Setter
    public static int counter;

    /**
     * @param args - the main arguments.
     */
    public static void main(String[] args) {
        setCounter(5);
        System.out.println(getCounter());
        System.out.println("Welcome to the main class of goto-fail;");
    }
}
