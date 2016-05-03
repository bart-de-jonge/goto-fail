package control;

import gui.RootPane;

/**
 * Controller that manages the tool menu (e.g. shot creation menu)
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
    }
}
