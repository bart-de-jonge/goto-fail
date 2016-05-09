package gui.modal;

import gui.misc.BlurHelper;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledCheckbox;
import gui.styling.StyledTextfield;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Modal view for gui testing and mockups. Temporary until style basics are pretty much worked out.
 * @author Mark
 */
public class mockupModalView extends ModalView {

    private StackPane viewPane;
    private ScrollPane scrollPane;
    private VBox vBoxScrollable;
    private VBox vBox;

    /**
     * Constructor.
     * @param rootPane the root pane at which application starts.
     */
    public mockupModalView(RootPane rootPane) {
        super(rootPane, 400, 600);

        this.viewPane = new StackPane();
        this.viewPane.setStyle("-fx-background-color: rgb(255,255,255);");
        //this.viewPane.getChildren().add(new Label("Lol ben een label!"));
        //this.viewPane.setPadding(new Insets(10,10,10,10));

        this.scrollPane = new ScrollPane();
        this.vBoxScrollable = new VBox();
        this.vBoxScrollable.setSpacing(40.0);
        this.vBoxScrollable.setPadding(new Insets(0, 0, 0, 30));
        this.scrollPane.setContent(vBoxScrollable);
        this.viewPane.setAlignment(vBoxScrollable, Pos.TOP_CENTER);

        this.vBox = new VBox();
        this.vBox.setMaxHeight(400.0);
        this.vBox.setPadding(new Insets(20, 20, 20, 20));
        this.vBox.setSpacing(20.0);
        this.vBox.setStyle("-fx-border-width: 0 0 1 0;" +
                "-fx-border-color: rgba(0,0,0, 0.5);" +
                "-fx-border-style: solid inside;");
        this.viewPane.setAlignment(vBox, Pos.TOP_CENTER);
        this.viewPane.getChildren().add(scrollPane);
        this.viewPane.getChildren().add(vBox);

        initExampleButtons();
        initExampleTextfields();
        initExampleButtons2();
        initExampleTextfield();

        super.setModalView(this.viewPane);
        super.displayModal();

        BlurHelper blurHelper = new BlurHelper(this.vBox);
        this.viewPane.setAlignment(blurHelper.getImageView(), Pos.TOP_CENTER);
        this.viewPane.getChildren().add(1, blurHelper.getImageView());
        blurHelper.processBlurUsingBounds();

        blurHelper.watchScrolling(scrollPane);

//        scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                blurHelper.processBlurUsingBounds();
//            }
//        });

//        this.vBox.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                blurHelper.processBlurUsingBounds();
//            }
//        });
//
//        this.vBox.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                blurHelper.processBlurUsingBounds();
//            }
//        });


    }

    private void initExampleTextfields() {
        for (int i = 0; i < 30; i++) {
            StyledTextfield textTextField = new StyledTextfield("I'm a textfield!");

            this.vBoxScrollable.getChildren().add(textTextField);
        }

    }

    private void initExampleButtons() {
        StyledButton testButton = new StyledButton("I'm a button!");
        testButton.setButtonColor(100, 195, 50);
        this.vBox.getChildren().add(testButton);

        StyledButton testButton2 = new StyledButton("I'm another button!");
        testButton2.setButtonColor(200, 75, 175);
        this.vBox.getChildren().add(testButton2);

    }

    private void initExampleButtons2() {
        StyledButton testRoundButton = new StyledButton("+");
        testRoundButton.setStyle("-fx-font-size: 32;"
                + "-fx-background-radius: 5em;"
                + "-fx-min-width: 60; -fx-max-width: 60;"
                + "-fx-min-height: 60; -fx-max-height: 60;");
       // testRoundButton.setButtonColor(54, 200, 178);
        testRoundButton.setButtonColor(240, 240, 240);
        testRoundButton.setTextColor(100, 100, 100);
        this.vBox.getChildren().add(testRoundButton);

        StyledButton testRoundButton2 = new StyledButton("-");
        testRoundButton2.setStyle("-fx-font-size: 32;"
                + "-fx-background-radius: 5em;"
                + "-fx-min-width: 60; -fx-max-width: 60;"
                + "-fx-min-height: 60; -fx-max-height: 60;");
        testRoundButton2.setButtonColor(120, 180, 215);
        this.vBox.getChildren().add(testRoundButton2);
    }

    private void initExampleTextfield() {
        StyledCheckbox testCheckbox = new StyledCheckbox("tetter");
        testCheckbox.setMarkColor(80, 220, 255);
        testCheckbox.setBoxColor(225, 225, 225);
        testCheckbox.setSelected(true);
        this.vBox.getChildren().add(testCheckbox);
    }

    @Override
    public void setModalView(Pane modalView) {
        setDisplayScene(new Scene(modalView));
        getDisplayScene().getStylesheets().add("stylesheets/mockupstylesheet.css");
        getModalStage().setScene(getDisplayScene());
    }

}
