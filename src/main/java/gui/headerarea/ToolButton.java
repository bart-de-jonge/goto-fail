package gui.headerarea;

import gui.misc.TweakingHelper;
import gui.styling.StyledButton;
import javafx.geometry.Insets;
import lombok.Getter;

/**
 * Class that is responsible for managing the actions associated with a tool.
 * @author alex
 */
public class ToolButton {

    // tweaking variables
    private static final int buttonWidth = 180;
    private static final int buttonHeight = 26;

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
        this.button.setPadding(new Insets(0,2,0,2));
        this.button.setPrefWidth(buttonWidth);
        this.button.setMinHeight(buttonHeight);
        this.button.setBorderColor(TweakingHelper.COLOR_PRIMARY);
        this.button.setFillColor(TweakingHelper.COLOR_BACKGROUND);
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
