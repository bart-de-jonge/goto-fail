package gui.root;

import gui.headerarea.DetailView;
import gui.headerarea.ToolView;
import gui.misc.TweakingHelper;
import javafx.scene.control.MenuItem;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;

/**
 * Class that represents the whole of top-level elements in the gui.
 * In other words, the file-edit-view-help menus and any buttons below that.
 */
public class RootHeaderArea extends VBox {

    private RootPane rootPane;
    private VBox headerBar;

    @Getter
    private DetailView detailView;

    @Getter
    private ToolView toolView;

    private DropShadow dropShadow;

    /**
     * RootHeaderArea Constructor.
     * @param rootPane the root pane this pane itself is located in.
     */
    public RootHeaderArea(RootPane rootPane) {
        this.rootPane = rootPane;

        dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0,0,0,0.2), 20, 0.1, 0, 5);
        this.setEffect(dropShadow);

        getChildren().add(initMenus());
        getChildren().add(initHeaderBar());
        
        this.setPrefHeight(50);
    }

    /**
     * Init the header bar of the RootHeaderArea.
     * @return - the hbox displaying the headerbar
     */
    private VBox initHeaderBar() {
        headerBar = new VBox();

        headerBar.getChildren().add(initButtons());

        detailView = new DetailView();
        headerBar.getChildren().add(detailView);

        return headerBar;
    }

    /**
     * Initializes top menu (file, edit, etc).
     * @return MenuBar containing menus.
     */
    private MenuBar initMenus() {
        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(e -> {
                rootPane.getControllerManager().getFileMenuController().newProject();
            });

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(e -> {
                rootPane.getControllerManager().getFileMenuController().save();
            });

        MenuItem saveAsItem = new MenuItem("Save as");
        saveAsItem.setOnAction(e -> {
                rootPane.getControllerManager().getFileMenuController().saveAs();
            });
        
        MenuItem loadItem = new MenuItem("Load");
        loadItem.setOnAction(e -> {
                rootPane.getControllerManager().getFileMenuController().load();
            });

        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(e -> {
                if (rootPane.getControllerManager().getScriptingProject() != null) {
                    if (rootPane.getControllerManager().getScriptingProject().isChanged()) {
                        rootPane.getControllerManager().initSaveModal();
                    }
                }
            });
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(newItem, saveItem, saveAsItem, loadItem, quit);
        Menu editMenu = new Menu("Edit");
        Menu viewMenu = new Menu("View");
        Menu helpMenu = new Menu("Help");

        MenuBar topMenuBar = new MenuBar();
        topMenuBar.setUseSystemMenuBar(true);
        topMenuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, helpMenu);
        return topMenuBar;
    }

    /**
     * Initializes top button area.
     * @return HBox box containing buttons.
     */
    private HBox initButtons() {
        this.toolView = new ToolView();
        return this.toolView;
    }
}
