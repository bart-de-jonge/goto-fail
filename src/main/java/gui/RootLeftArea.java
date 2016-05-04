package gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lombok.Getter;

/**
 * Created by markv on 4/26/2016.
 * Mwahahaha I created this class, losers.
 */
public class RootLeftArea extends ScrollPane {

    @Getter
    private RootPane rootPane;

    @Getter
    private AnchorPane parentPane;

    @Getter
    private GridPane gridPane;

    /**
     * Constructor class.
     * @param rootPane parent pane passed through.
     */
    RootLeftArea(RootPane rootPane) {
        this.rootPane = rootPane;

        parentPane = new AnchorPane();
        gridPane = new GridPane();
        parentPane.setLeftAnchor(gridPane, 0.0);
        parentPane.setRightAnchor(gridPane, 0.0);
        parentPane.setTopAnchor(gridPane, 0.0);
        parentPane.minHeightProperty().bind(rootPane.getRootCenterArea().getParentPane().heightProperty());
        parentPane.maxHeightProperty().bind(rootPane.getRootCenterArea().getParentPane().heightProperty());
        parentPane.getChildren().add(gridPane);
        vvalueProperty().bind(rootPane.getRootCenterArea().vvalueProperty());
        setContent(parentPane);
        //setVbarPolicy(new ScrollBarPolicy());
        gridPane.setGridLinesVisible(true);

    }

}
