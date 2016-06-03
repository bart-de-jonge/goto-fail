package gui.modal;

import gui.misc.TweakingHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Class that creates and displays a modal (popup) view.
 */
@Log4j2
public abstract class ModalView {

    @Getter
    private Stage modalStage;
    @Getter @Setter
    private Scene displayScene;
    @Getter
    private final int width;
    @Getter
    private final int height;
    private final int buttonWidth = 140;
    private final int buttonHeight = 25;

    /**
     * Constructor.
     * @param rootPane root pane on top of which to display the modal.
     * @param width width of the modal window.
     * @param height height of the modal window.
     */
    public ModalView(RootPane rootPane, int width, int height) {
        this.width = width;
        this.height = height;
        this.modalStage = new Stage();
        this.modalStage.initModality(Modality.APPLICATION_MODAL);
        this.modalStage.initOwner(rootPane.getPrimaryStage());
    }

    /**
     * If a child view has been added then the modal view is shown.
     */
    public void displayModal() {
        if (this.displayScene != null) {
            log.info("Displaying modal view.");
            this.modalStage.show();
            this.modalStage.getScene().getAccelerators()
                    .put(new KeyCodeCombination(KeyCode.ESCAPE),
                         this::hideModal);
        }
    }

    /**
     * Hides the modal from view.
     */
    public void hideModal() {
        log.info("Closing modal view.");
        modalStage.close();
    }

    /**
     * Set the view that will be displayed in the modal.
     * @param modalView Modal view pane
     */
    public void setModalView(Pane modalView) {
        this.displayScene = new Scene(modalView, width, height);
        this.displayScene.getStylesheets().add("Stylesheets/Misc.css");
        this.displayScene.getStylesheets().add("Stylesheets/StyledButton.css");
        this.displayScene.getStylesheets().add("Stylesheets/StyledCheckbox.css");
        this.displayScene.getStylesheets().add("Stylesheets/StyledTextfield.css");
        this.displayScene.getStylesheets().add("Stylesheets/StyledListview.css");
        this.modalStage.setScene(displayScene);
    }

    /**
     * Creates a button, with proper Modal View styling applied.
     * @param title title of the button.
     * @param reversed whether colors should be reversed.
     * @return the button.
     */
    protected StyledButton createButton(String title, boolean reversed) {
        StyledButton button = new StyledButton(title);
        button.setFillColor(reversed ? Color.WHITE : TweakingHelper.getColor(0));
        button.setBorderColor(reversed ? TweakingHelper.getColor(0) : Color.WHITE);
        button.setPrefWidth(buttonWidth);
        button.setPrefHeight(buttonHeight);
        return button;
    }
}
