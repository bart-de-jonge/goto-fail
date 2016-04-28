package gui;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

/**
 * Class representing the center (main section) of the gui.
 * In other words, the time line view goes here.
 */
public class RootCenterArea extends ScrollPane {

    //private double width, height;

    @Getter @Setter
    private int countHeight = 50;
    @Getter @Setter
    private int timelineWidth = 200;
    @Getter @Setter
    private int numberOfTimelines = 5;
    @Getter @Setter
    private int numberOfCounts = 10;

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
        grid = new TimelinesGridPane(5, 20, 1000, 1000);
        parentPane.getChildren().add(grid);

        parentPane.setMaxWidth(1000);
        parentPane.setMinWidth(1000);
        parentPane.setMinHeight(1000);
        parentPane.setMinWidth(1000);
        setContent(parentPane);
    }


}

