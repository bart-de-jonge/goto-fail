package gui;

import javafx.scene.control.Button;
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
     * @param headerArea Header area to which to add this tool button
     */
    public ToolButton(String toolName, RootHeaderArea headerArea) {
        this.name = toolName;
        initializeButton(headerArea);
    }

    /**
     * Instantiates the button and adds it to the tool view.
     * TODO: add event handler to button
     * @param headerArea Area in which the tool view is found.
     */
    private void initializeButton(RootHeaderArea headerArea) {
        this.button = new Button(this.name);

        this.button.setOnMouseClicked(e -> System.out.println("ZOMG"));

        headerArea.getToolView().addToolButton(this);
    }
}
