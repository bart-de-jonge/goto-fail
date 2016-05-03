package gui;

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

    private EventHandler<MouseEvent> clickHandler;

    /**
     * Constructor.
     * @param toolName the tool's name
     * @param headerArea Header area to which to add this tool button
     */
    public ToolButton(String toolName, RootHeaderArea headerArea, EventHandler<MouseEvent> clickHandler) {
        this.name = toolName;
        this.clickHandler = clickHandler;
        initializeButton(headerArea);
    }

    /**
     * Instantiates the button and adds it to the tool view.
     * TODO: add event handler to button
     * @param headerArea Area in which the tool view is found.
     */
    private void initializeButton(RootHeaderArea headerArea) {
        this.button = new Button(this.name);

        this.button.setOnMouseClicked(clickHandler);

        headerArea.getToolView().addToolButton(this);
    }
}
