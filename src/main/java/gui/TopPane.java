package gui;

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
class TopPane extends VBox {

    TopPane() {
        // border style to mark it, for debugging for now.
        setStyle("-fx-border-style: solid inside;" +
                "-fx-border-width: 1;");

        MenuBar topMenuBar = new MenuBar();
        Menu menu1 = new Menu("File");
        Menu menu2 = new Menu("Edit");
        Menu menu3 = new Menu("View");
        Menu menu4 = new Menu("Help");
        topMenuBar.getMenus().addAll(menu1, menu2, menu3, menu4);

        getChildren().add(topMenuBar);

        HBox topButtonBar = new HBox();
        topButtonBar.setSpacing(15);
        topButtonBar.setPadding(new Insets(5, 10, 5, 10));
        Button btn1 = new Button("A button");
        Button btn2 = new Button("Another button");
        topButtonBar.getChildren().addAll(btn1, btn2);

        getChildren().add(topButtonBar);
    }

}