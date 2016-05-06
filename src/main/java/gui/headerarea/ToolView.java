package gui.headerarea;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that is responsible for displaying the toolbox.
 * @author alex
 */
public class ToolView extends HBox {

    private List<ToolButton> buttonList;

    /**
     * Constructor.
     */
    public ToolView() {
        this.setSpacing(15);
        this.setPadding(new Insets(5, 10, 5, 10));

        buttonList = new ArrayList<>();
    }

    /**
     * Add a tool to the toolbox.
     * @param tool Tool to be displayed.
     */
    public void addToolButton(ToolButton tool) {
        this.buttonList.add(tool);
        this.getChildren().add(tool.getButton());
    }
}
