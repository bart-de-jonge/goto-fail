package gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing the center (main section) of the gui.
 * In other words, the time line view goes here.
 */
public class RootCenterArea extends ScrollPane {

    @Getter @Setter
    private int numberOfTimelines = 8;
    @Getter @Setter
    private int numberOfCounts = 20;

    @Getter
    private RootPane rootPane;

    @Getter
    private TimelinesGridPane grid;

    @Getter
    private AnchorPane parentPane;

    @Getter
    private CameraShotBlock testBlock;


    /**
     * Constructor class
     * @param rootPane parent pane passed through.
     */
    RootCenterArea(RootPane rootPane) {
        this.rootPane = rootPane;

        parentPane = new AnchorPane();
        grid = new TimelinesGridPane(numberOfTimelines, numberOfCounts, 1000, 1000);

        parentPane.setLeftAnchor(grid, 0.0);
        parentPane.setRightAnchor(grid, 0.0);
       // parentPane.setBottomAnchor(grid, 0.0);
        parentPane.setTopAnchor(grid, 0.0);

        parentPane.getChildren().add(grid);
        parentPane.setMinHeight(1000);
        parentPane.setMinWidth(1000);

        setFitToWidth(true);
        //setFitToHeight(true);
        setContent(parentPane);
    }


}

