package gui;

import javafx.scene.control.MenuItem;

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
        
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().add(saveItem);
        saveItem.setOnAction(e -> {
            rootPane.getControllerManager().getTimelineControl().save();
        });
        
        MenuItem loadItem = new MenuItem("Load");
        fileMenu.getItems().add(loadItem);
        loadItem.setOnAction(e -> {
            rootPane.getControllerManager().getTimelineControl().load();
        });
        
        Menu editMenu = new Menu("Edit");
        Menu viewMenu = new Menu("View");
        Menu helpMenu = new Menu("Help");
        topMenuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, helpMenu);
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

        Button shotCreation = createShotButton();
        topButtonBar.getChildren().add(shotCreation);
        return topButtonBar;
    }

    /**
     * Create camera shot creation button.
     * TODO: Replace mocked object
     * @return Creation Button.
     */
    private Button createShotButton() {
        Button shotCreation = new Button("Add Camera Shot");

        shotCreation.setOnMouseClicked(e -> {
                rootPane.getControllerManager().getTimelineControl()
                    .addCameraShot(1, "BOOM", "", 1, 2);
            });

        return shotCreation;
    }

}