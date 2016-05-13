package gui.headerarea;
import gui.misc.TweakingHelper;
import gui.styling.StyledButton;
import lombok.Getter;

/**
 * Class that is responsible for managing the actions associated with a tool.
 * @author alex
 */
public class ToolButton {

    // tweaking variables
    private static final int buttonWidth = 220;
    private static final int buttonHeight = 0;

    private String name;
    @Getter
    private StyledButton button;

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
        this.button = new StyledButton(this.name);
        this.button.setPrefWidth(buttonWidth);
        this.button.setPrefHeight(buttonHeight);
        this.button.setBorderColor(TweakingHelper.COLOR_BACKGROUND);
        this.button.setFillColor(TweakingHelper.COLOR_PRIMARY);
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
