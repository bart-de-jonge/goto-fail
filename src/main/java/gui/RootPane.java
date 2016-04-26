package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Class that represents the whole top of the gui application.
 */
public class RootPane extends Application {

    private int minResX = 640;
    private int minResY = 480;
    private int startResX = 800;
    private int startResY = 600;

    //CHECKSTYLE:OFF: MagicNumber
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Create a BorderPane,
        // a layout with 5 areas: top, bottom, left, right and center,
        // and add our views to it.

        BorderPane topLevelPane = new BorderPane();

        // represents file-view-help bar and button bars at top of gui.
        TopPane topPane = new TopPane();
        topLevelPane.setTop(topPane);

        // represents simple bar at bottom of gui.
        BottomPane bottomPane = new BottomPane();
        topLevelPane.setBottom(bottomPane);

        // Create scene and set the stage. This is where the window is basically
        // created. Also has some useful settings.

        primaryStage.setScene(new Scene(topLevelPane));
        primaryStage.setTitle("Hoi ben een titel lol.");
        primaryStage.setMinHeight(minResY);
        primaryStage.setMinWidth(minResX);
        primaryStage.setHeight(startResY);
        primaryStage.setWidth(startResX);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    //CHECKSTYLE:ON: MagicNumber
}
