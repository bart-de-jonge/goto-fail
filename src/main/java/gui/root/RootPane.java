package gui.root;

import control.ControllerManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Class that represents the whole top of the gui application.
 */
@Log4j2
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
        log.info("Starting RootPane.");
        this.primaryStage = primaryStage;

        // Create a BorderPane,
        // a layout with 5 areas: top, bottom, left, right and center,
        // and add our views to it.
        topLevelPane = new BorderPane();
        // Create scene and set the stage. This is where the window is basically
        // created. Also has some useful settings.

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
        
        controllerManager = new ControllerManager(this);


        // represents simple bar at bottom of gui.
        rootFooterArea = new RootFooterArea();
        topLevelPane.setBottom(rootFooterArea);

        rootCenterArea = new RootCenterArea(this, 0, true);
        topLevelPane.setCenter(rootCenterArea);


        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public void reInitRootCenterArea(RootCenterArea area) {
        topLevelPane.setCenter(area);
        rootCenterArea = area;
    }

}
