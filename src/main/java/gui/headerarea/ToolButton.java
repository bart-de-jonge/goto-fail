package gui.headerarea;

import gui.root.RootHeaderArea;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import lombok.Getter;

/**
 * Class that is responsible for managing the actions associated with a tool.
 * @author alex
 */
public class ToolButton {

    private String name;
    @Getter
    private Button button;

    /**
     * Constructor.
     * @param toolName the tool's name
     */
    public ToolButton(String toolName) {
        this.name = toolName;
        initializeButton();
    }

    /**
     * Instantiates the button and adds it to the tool view.
     */
    private void initializeButton() {
        this.button = new Button(this.name);
    }

    /**
     * Enable the button.
     */
    public void enableButton() {
        this.button.setDisable(false);
    }

    /**
     * Disable the button.
     */
    public void disableButton() {
        this.button.setDisable(true);
    }
}
