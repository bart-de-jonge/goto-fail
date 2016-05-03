package control;

import gui.RootPane;
import gui.ToolButton;

/**
 * Controller that manages the tool menu (e.g. shot creation menu).
 * @author alex
 */
public class ToolViewController {

    private RootPane rootPane;

    /**
     * Constructor.
     * @param rootPane Root Pane of Application.
     */
    public ToolViewController(RootPane rootPane) {
        this.rootPane = rootPane;
        initializeTools();
    }

    /**
     * Initialize all tools in the toolbox. Adds the corresponding buttons to the views and sets up
     * the event handlers.
     */
    private void initializeTools() {
        ToolButton blockCreationTool = new ToolButton("Add a block",
                                                      this.rootPane.getRootHeaderArea());
    }
}
