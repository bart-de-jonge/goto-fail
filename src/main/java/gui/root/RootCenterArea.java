package gui.root;

import gui.centerarea.CounterGridPane;
import gui.centerarea.DirectorGridPane;
import gui.centerarea.TimelinesGridPane;
import gui.misc.BlurHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing the center (main section) of the gui.
 * In other words, the time line view goes here.
 */
public class RootCenterArea extends StackPane {
    
    private static final int DEFAULT_TIMELINES = 0;

    @Getter @Setter
    private int numberOfTimelines = 8;
    @Getter @Setter
    private int numberOfCounts = 100;
    @Getter
    private int countHeight = 10; // 10 works well, if you've changed this.
    @Getter
    private int counterWidth = 10;
    @Getter
    private int directorTimelineWidth = 200;
    @Getter
    private int timelineWidth = 100; // 100 works well, if you've changed this.
    @Getter
    private RootPane rootPane;

    /**
     * Trio of panes necessary for the main timeline.
     */
    @Getter
    private ScrollPane mainTimelineScrollpane;
    @Getter
    private AnchorPane mainTimeLineAnchorPane;
    @Getter
    private TimelinesGridPane mainTimeLineGridPane;

    /**
     * Combination between an HBox and double trio of panes, necessary
     * for the more detailed counter display, and the director timeline.
     */

    @Getter
    private HBox counterAndDirectorPane;

    @Getter
    private ScrollPane counterScrollpane;
    @Getter
    private AnchorPane counterAnchorPane;
    @Getter
    private CounterGridPane counterGridPane;

    @Getter
    private ScrollPane directorScrollpane;
    @Getter
    private AnchorPane directorAnchorPane;
    @Getter
    private DirectorGridPane directorGridPane;
    
    @Getter
    private Button newButton;
    
    @Getter
    private Button loadButton;
    
    /**
     * Construct a new RootCenterArea.
     * @param rootPane the rootPane that this RootCenterArea is a part of.
     * @param numberOfTimelines the number of timelines in this RootCenterArea.
     * @param empty if the RootCenterArea should be initialized empty or not. Empty in this case
     means that there are buttons shown to create/load a project instead of timelines.
     */
    public RootCenterArea(RootPane rootPane, int numberOfTimelines, boolean empty) {
        if (empty) {
            this.rootPane = rootPane;
            this.numberOfTimelines = numberOfTimelines;
            HBox buttonBox = new HBox();
            buttonBox.setSpacing(10);
            newButton = new Button("Create new project");
            loadButton = new Button("Load project");
            buttonBox.getChildren().addAll(newButton, loadButton);
            this.getChildren().addAll(buttonBox);
            
            
        } else {
            this.rootPane = rootPane;
            this.numberOfTimelines = numberOfTimelines;
    
            initMainTimeLinePane();
    
            counterAndDirectorPane = new HBox();
            setAlignment(counterAndDirectorPane, Pos.CENTER_LEFT);
            counterAndDirectorPane.setMaxWidth(counterWidth + timelineWidth);
            counterAndDirectorPane.maxHeightProperty().bind(mainTimelineScrollpane.heightProperty());
            getChildren().add(counterAndDirectorPane);
    
            initCounterPane();
            initDirectorPane();
            initScrollbar();
        }
    }

    /**
     * Constructor class
     * @param rootPane parent pane passed through.
     */
    public RootCenterArea(RootPane rootPane) {
        this(rootPane, DEFAULT_TIMELINES, false);
    }

    /**
     * Initializes the central timeline in this stackpane.
     */
    private void initMainTimeLinePane() {
        // main timeline panes
        mainTimelineScrollpane = new ScrollPane();
        mainTimeLineAnchorPane = new AnchorPane();
        mainTimeLineGridPane = new TimelinesGridPane(numberOfTimelines, numberOfCounts,
                timelineWidth,  countHeight, counterWidth + directorTimelineWidth);
        mainTimeLineAnchorPane.setLeftAnchor(mainTimeLineGridPane, 0.0);
        mainTimeLineAnchorPane.setRightAnchor(mainTimeLineGridPane, 0.0);
        mainTimeLineAnchorPane.setTopAnchor(mainTimeLineGridPane, 0.0);
        mainTimeLineAnchorPane.getChildren().add(mainTimeLineGridPane);
        mainTimelineScrollpane.setFitToWidth(true);
        mainTimelineScrollpane.setContent(mainTimeLineAnchorPane);
        mainTimelineScrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        getChildren().add(mainTimelineScrollpane);
    }

    /**
     * Initializes the counter timeline to the left.
     */
    private void initCounterPane() {
        counterScrollpane = new ScrollPane();
        counterAnchorPane = new AnchorPane();
        counterGridPane = new CounterGridPane(numberOfCounts, counterWidth, countHeight);
        counterAnchorPane.setLeftAnchor(counterGridPane, 0.0);
        counterAnchorPane.setRightAnchor(counterGridPane, 0.0);
        counterAnchorPane.setTopAnchor(counterGridPane, 0.0);
        counterAnchorPane.getChildren().add(counterGridPane);
        counterScrollpane.setContent(counterAnchorPane);
        counterScrollpane.setFitToWidth(true);
        counterScrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        counterScrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        counterScrollpane.vvalueProperty().bindBidirectional(
                mainTimelineScrollpane.vvalueProperty());
        counterAndDirectorPane.getChildren().add(counterScrollpane);
    }

    /**
     * Initializes the director timeline to the left.
     */
    private void initDirectorPane() {
        directorScrollpane = new ScrollPane();
        directorAnchorPane = new AnchorPane();
        directorGridPane = new DirectorGridPane(numberOfCounts, directorTimelineWidth, countHeight);
        directorAnchorPane.setLeftAnchor(directorGridPane, 0.0);
        directorAnchorPane.setRightAnchor(directorGridPane, 0.0);
        directorAnchorPane.setTopAnchor(directorGridPane, 0.0);
        directorAnchorPane.getChildren().add(directorGridPane);
        directorScrollpane.setContent(directorAnchorPane);
        directorScrollpane.setFitToWidth(true);
        directorScrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        directorScrollpane.setStyle("-fx-background-color: lightblue"); // debugcolor for now
        directorScrollpane.vvalueProperty().bindBidirectional(
                mainTimelineScrollpane.vvalueProperty());
        counterAndDirectorPane.getChildren().add(directorScrollpane);
    }

    /**
     * Initializes the scrollbar for the main timeline.
     */
    private void initScrollbar() {
        ScrollBar scrollbar = new ScrollBar();
        scrollbar.setMin(0);
        scrollbar.setMax(1);
        scrollbar.maxWidthProperty().bind(widthProperty()
                .subtract(counterWidth + directorTimelineWidth));
        mainTimelineScrollpane.hvalueProperty().bind(scrollbar.valueProperty());
        getChildren().add(scrollbar);
        setAlignment(scrollbar, Pos.BOTTOM_RIGHT);
    }


}

