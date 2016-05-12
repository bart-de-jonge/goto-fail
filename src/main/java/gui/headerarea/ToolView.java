package gui.headerarea;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that is responsible for displaying the toolbox.
 * @author alex
 */
public class ToolView extends HBox {

    private List<ToolButton> buttonList;

    @Getter
    private ToolButton directorBlockCreationTool;
    @Getter
    private ToolButton cameraBlockCreationTool;
    @Getter
    private ToolButton blockDeletionTool;
    @Getter
    private ToolButton shotGenerationTool;

    /**
     * Constructor.
     */
    public ToolView() {
        this.setSpacing(15);
        this.setPadding(new Insets(5, 10, 5, 10));

        buttonList = new ArrayList<>();

        // init buttons
        this.directorBlockCreationTool = this.addToolButton("Add directorshot");
        this.cameraBlockCreationTool = this.addToolButton("Add camerashot");
        this.blockDeletionTool = this.addToolButton("Delete shot");
        this.shotGenerationTool = this.addToolButton("Generate CameraShots");
    }

    /**
     * Add a new tool to the toolbox.
     * @param toolName - the name of the new tool.
     * @return - the created and added toolbutton
     */
    private ToolButton addToolButton(String toolName) {
        ToolButton tool = new ToolButton(toolName);
        this.buttonList.add(tool);
        this.getChildren().add(tool.getButton());
        return tool;
    }
}
