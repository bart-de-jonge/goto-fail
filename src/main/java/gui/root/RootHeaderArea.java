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
       
        Menu editMenu = new Menu("Edit");
        
        MenuItem editProjectItem = new MenuItem("Project");
        editProjectItem.setOnAction(e -> {
                rootPane.getControllerManager().getEditMenuController().editProject();
            });
        editMenu.getItems().add(editProjectItem);
        
        Menu helpMenu = new Menu("Help");
        
        MenuItem helpWebsiteItem = new MenuItem("Website");
        helpMenu.setOnAction(e -> {
                try {
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://gotofail.net"));
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            });
        helpMenu.getItems().add(helpWebsiteItem);
        Menu fileMenu = initFileMenu();
        Menu viewMenu = new Menu("View");

        MenuBar topMenuBar = new MenuBar();
        topMenuBar.setUseSystemMenuBar(true);
        topMenuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, helpMenu);
        return topMenuBar;
    }
    
    /**
     * Initialize the file menu.
     * @return the initialized file Menu
     */
    private Menu initFileMenu() {
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
        return fileMenu;
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
