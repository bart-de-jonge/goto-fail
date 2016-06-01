package gui.root;

import gui.headerarea.DetailView;
import gui.headerarea.DirectorDetailView;
import gui.headerarea.ToolView;
import gui.misc.TweakingHelper;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    /**
     * RootHeaderArea Constructor.
     * @param rootPane the root pane this pane itself is located in.
     */
    public RootHeaderArea(RootPane rootPane) {
        this.rootPane = rootPane;

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

        detailView = new DirectorDetailView();
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
                rootPane.getControllerManager().getProjectController().editProject();
            });
        editMenu.getItems().add(editProjectItem);

        MenuItem preferencesItem = new MenuItem("Preferences");
        preferencesItem.setOnAction(e -> {
                rootPane.getControllerManager().getPreferencesViewController()
                        .showPreferencesWindow();
            });
        preferencesItem.setAccelerator(new KeyCodeCombination(KeyCode.COMMA,
                                                              KeyCombination.SHORTCUT_DOWN));
        editMenu.getItems().add(preferencesItem);
        
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
        topMenuBar.setStyle("-c-color-primary: "
                + TweakingHelper.getColorString(0) + ";");
        topMenuBar.setUseSystemMenuBar(true);
        topMenuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, helpMenu);
        return topMenuBar;
    }
    
    /**
     * Initialize the file menu.
     * @return the initialized file Menu
     */
    private Menu initFileMenu() {
        Menu fileMenu = new Menu("File");

        initializeLocalFileItems(fileMenu);

        MenuItem uploadItem = new MenuItem("Upload to webserver");
        uploadItem.setOnAction(e -> {
                rootPane.getControllerManager().getProjectController().uploadToWebserver();
            });
        uploadItem.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.SHORTCUT_DOWN));

        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(e -> {
                if (rootPane.getControllerManager().getScriptingProject() != null
                        && rootPane.getControllerManager()
                                   .getScriptingProject().isChanged()) {
                    rootPane.getControllerManager().initSaveModal();
                } else {
                    rootPane.getPrimaryStage().close();
                }
            });
        fileMenu.getItems().addAll(quit, uploadItem);
        return fileMenu;
    }

    /**
     * Initializes MenuItems for local file handling.
     * @param fileMenu Menu to add items to.
     */
    private void initializeLocalFileItems(Menu fileMenu) {
        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(e -> {
                rootPane.getControllerManager().getProjectController().newProject();
            });
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN,
                                                      KeyCombination.ALT_DOWN));

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(e -> {
                rootPane.getControllerManager().getProjectController().save();
            });
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));

        MenuItem saveAsItem = new MenuItem("Save as");
        saveAsItem.setOnAction(e -> {
                rootPane.getControllerManager().getProjectController().saveAs();
            });
        saveAsItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN,
                                                         KeyCombination.SHIFT_DOWN));

        MenuItem loadItem = new MenuItem("Load");
        loadItem.setOnAction(e -> {
                rootPane.getControllerManager().getProjectController().load();
            });
        loadItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));

        fileMenu.getItems().addAll(newItem, saveItem, saveAsItem, loadItem);
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
