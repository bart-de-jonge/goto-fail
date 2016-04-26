package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Class that represents the whole top of the gui application.
 */
public class RootPane extends Application {

    private int minimumResolutionX = 640;
    private int minimumResolutionY = 480;
    private int startingResolutionX = 800;
    private int startingResolutionY = 600;
    private BorderPane topLevelPane;
    private RootHeaderArea rootHeaderArea;
    private RootFooterArea rootFooterArea;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Create a BorderPane,
        // a layout with 5 areas: top, bottom, left, right and center,
        // and add our views to it.
         topLevelPane = new BorderPane();

        // represents file-view-help bar and button bars at top of gui.
        rootHeaderArea = new RootHeaderArea();
        topLevelPane.setTop(rootHeaderArea);

        // represents simple bar at bottom of gui.
        rootFooterArea = new RootFooterArea();
        topLevelPane.setBottom(rootFooterArea);

        // Create scene and set the stage. This is where the window is basically
        // created. Also has some useful settings.
        primaryStage.setScene(new Scene(topLevelPane));
        primaryStage.setTitle("Hoi ben een titel lol.");
        primaryStage.setMinHeight(minimumResolutionY);
        primaryStage.setMinWidth(minimumResolutionX);
        primaryStage.setHeight(startingResolutionY);
        primaryStage.setWidth(startingResolutionX);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

}
