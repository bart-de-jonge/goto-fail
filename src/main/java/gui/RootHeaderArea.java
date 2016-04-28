package gui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Class that represents the whole of top-level elements in the gui.
 * In other words, the file-edit-view-help menus and any buttons below that.
 */
class RootHeaderArea extends VBox {

    private RootPane rootPane;

    /**
     * RootHeaderArea Constructor.
     * @param rootPane the root pane this pane itself is located in.
     */
    RootHeaderArea(RootPane rootPane) {
        this.rootPane = rootPane;
        // border style to mark it, for debugging for now.
        setStyle("-fx-border-style: solid inside;"
                + "-fx-border-width: 1;");

        getChildren().add(initMenus());

        getChildren().add(initButtons());
        this.setPrefHeight(50);
    }

    /**
     * Initializes top menu (file, edit, etc).
     * @return MenuBar containing menus.
     */
    private MenuBar initMenus() {
        MenuBar topMenuBar = new MenuBar();
        Menu menu1 = new Menu("File");
        Menu menu2 = new Menu("Edit");
        Menu menu3 = new Menu("View");
        Menu menu4 = new Menu("Help");
        topMenuBar.getMenus().addAll(menu1, menu2, menu3, menu4);
        return topMenuBar;
    }

    /**
     * Initializes top button area.
     * @return HBox box containing buttons.
     */
    private HBox initButtons() {
        HBox topButtonBar = new HBox();
        topButtonBar.setSpacing(15);
        topButtonBar.setPadding(new Insets(5, 10, 5, 10));

        // Crashes (no bueno)
        Button btn1 = new Button("DO NOT CLICK");

        EventHandler eventHandler = evt -> {
            String text = "";
            String eventType = evt.getEventType().toString();
            switch (eventType) {
                case "MOUSE_CLICKED":
                    text = "Mouse Clicked";
                    rootPane.getRootCenterArea().getTestBlock().setEndCount(12);
                    break;
                case "MOUSE_ENTERED":
                    text = "Mouse entered";
                    break;
                case "MOUSE_EXITED":
                    text = "Mouse exited";
                    break;
                default: break;
            }
            System.out.println(text);
            rootPane.getRootFooterArea().getTextOutputLabel().setText(text);
        };


        btn1.setOnMouseClicked(eventHandler);
        btn1.setOnMouseEntered(eventHandler);
        btn1.setOnMouseExited(eventHandler);

        Button shotCreation = createShotButton();
        topButtonBar.getChildren().addAll(btn1, shotCreation);
        return topButtonBar;
    }

    /**
     * Create camera shot creation button.
     * TODO: Replace mocked object
     * @return Creation Button.
     */
    private Button createShotButton() {
        Button shotCreation = new Button("Add Camera Shot");

        shotCreation.setOnMouseClicked(e ->
            rootPane.getControllerManager().getTimelineControl().addCameraShot(1, "BOOM", "", 1, 2)
        );

        return shotCreation;
    }

}