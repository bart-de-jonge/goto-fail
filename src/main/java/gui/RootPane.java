package gui;

import control.ControllerManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * Class that represents the whole top of the gui application.
 */
public class RootPane extends Application {

    private int minimumResolutionX = 640;
    private int minimumResolutionY = 480;
    private int startingResolutionX = 800;
    private int startingResolutionY = 600;

    @Getter
    Stage primaryStage;
    @Getter
    private BorderPane topLevelPane;
    @Getter
    private RootHeaderArea rootHeaderArea;
    @Getter
    private RootFooterArea rootFooterArea;
    @Getter
    private RootCenterArea rootCenterArea;
    @Getter
    private ControllerManager controllerManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Create a BorderPane,
        // a layout with 5 areas: top, bottom, left, right and center,
        // and add our views to it.
        topLevelPane = new BorderPane();
        // Create scene and set the stage. This is where the window is basically
        // created. Also has some useful settings.
        //TODO: change this line of code.
        Scene scene = new Scene(topLevelPane);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hoi ben een titel lol.");
        primaryStage.setMinHeight(minimumResolutionY);
        primaryStage.setMinWidth(minimumResolutionX);
        primaryStage.setHeight(startingResolutionY);
        primaryStage.setWidth(startingResolutionX);

        // represents file-view-help bar and button bars at top of gui.
        rootHeaderArea = new RootHeaderArea(this);
        topLevelPane.setTop(rootHeaderArea);

        // represents simple bar at bottom of gui.
        rootFooterArea = new RootFooterArea();
        topLevelPane.setBottom(rootFooterArea);

        rootCenterArea = new RootCenterArea(this);
        topLevelPane.setCenter(rootCenterArea);

        controllerManager = new ControllerManager(this);

        primaryStage.centerOnScreen();
        primaryStage.show();
    }

}
