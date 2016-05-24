package gui.headerarea;

import java.util.ArrayList;
import java.util.List;

import gui.misc.TweakingHelper;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import lombok.Getter;

/**
 * Class that is responsible for displaying the toolbox.
 */
public class ToolView extends HBox {

    private List<ToolButton> buttonList;

    private String style = "-fx-background-color: "
            + TweakingHelper.getColorString(0) + ";"
            + "-fx-border-width: 0 0 8 0;"
            + "-fx-border-color:"
            + TweakingHelper.getColorString(1) + ";";

    @Getter
    private ToolButton directorBlockCreationTool;
    @Getter
    private ToolButton cameraBlockCreationTool;
    @Getter
    private ToolButton blockDeletionTool;
    @Getter
    private ToolButton shotGenerationTool;
    @Getter
    private ToolButton allShotGenerationTool;

    /**
     * Constructor.
     */
    public ToolView() {
        this.setSpacing(15);
        this.setPadding(new Insets(8, 10, 8, 10));
        this.setStyle(style);

        buttonList = new ArrayList<>();

        // init buttons
        this.directorBlockCreationTool = this.addToolButton("Add directorshot");
        this.cameraBlockCreationTool = this.addToolButton("Add camerashot");
        this.blockDeletionTool = this.addToolButton("Delete shot");
        this.shotGenerationTool = this.addToolButton("Generate CameraShots");
        this.allShotGenerationTool = this.addToolButton("Generate All Shots");
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
