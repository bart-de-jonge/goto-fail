package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by markv on 4/26/2016.
 */
public class RootPane extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        Scene scene = new Scene(root, 640, 480);

        /*
            This is a button. You give it a name, and an action and stuff.
            Looks can be easily customized.
         */
        Button btn = new Button();
        btn.setText("A button");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Button press.");
            }
        });

        /*
            This is just text. It doesn't do anything special, and is different from a label.
         */
        Text txt = new Text("A bunch of text");
        txt.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));

        Label label = new Label("A textlabel");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); // sets alignment of the grid INSIDE its parent, which is (in this case) scene.
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25., 25., 25., 25.));

        BorderPane toplevelPane = new BorderPane();

        /*
            Top bar. Contains file/edit/view menus, buttons, and room for expansion.
         */

        VBox toplevelTop = new VBox();
        toplevelTop.setStyle("-fx-border-style: solid inside;" +
                "-fx-border-width: 1;");
        toplevelPane.setTop(toplevelTop);

        MenuBar topMenuBar = new MenuBar();
        Menu menu1 = new Menu("File");
        Menu menu2 = new Menu("Edit");
        Menu menu3 = new Menu("View");
        Menu menu4 = new Menu("Help");
        topMenuBar.getMenus().addAll(menu1, menu2, menu3, menu4);
        toplevelTop.getChildren().add(topMenuBar);

        HBox topButtonBar = new HBox();
        topButtonBar.setSpacing(15);
        topButtonBar.setPadding(new Insets(5, 10, 5, 10));
        Button btn1 = new Button("A button");
        Button btn2 = new Button("Another button");
        topButtonBar.getChildren().addAll(btn1, btn2);
        toplevelTop.getChildren().add(topButtonBar);

        /*
            Bottom bar. For now, just contains some dummy text.
         */

        HBox bottomlevelTop = new HBox();
        bottomlevelTop.setPadding(new Insets(5, 10, 5, 10));
        bottomlevelTop.setStyle("-fx-border-style: solid inside;" +
                "-fx-border-width: 1;");

        toplevelPane.setBottom(bottomlevelTop);

        Label lbl = new Label("Text output goes here.");
        bottomlevelTop.getChildren().add(lbl);

        /*
            Scene. Where it all comes together.
         */

        Scene scene = new Scene(toplevelPane, 640, 480);

        primaryStage.setTitle("Hoi ben een titel lol."); // title bar title
        primaryStage.setScene(scene); // start resolution of application (keep low?)
        primaryStage.show();
    }

}
