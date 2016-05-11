package control;

import gui.headerarea.DetailView;
import gui.root.RootHeaderArea;
import gui.root.RootPane;
import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Bart on 12/05/2016.
 */
public class DetailViewControllerTest extends ApplicationTest {

    private ControllerManager manager;
    private DetailView detailView;
    private DetailViewController detailViewController;

    @Before
    public void initialize() {
        manager = Mockito.mock(ControllerManager.class);
        RootPane rootPane = Mockito.mock(RootPane.class);
        RootHeaderArea rootHeaderArea = Mockito.mock(RootHeaderArea.class);
        detailView = Mockito.mock(DetailView.class);
//        TextField descriptionField = Mockito.mock(TextField.class);
        TextField descriptionField = new TextField("aksldjf");

        when(detailView.getDescriptionField()).thenReturn(descriptionField);
        when(manager.getRootPane()).thenReturn(rootPane);
        when(rootPane.getRootHeaderArea()).thenReturn(rootHeaderArea);
        when(rootHeaderArea.getDetailView()).thenReturn(detailView);

        detailViewController = new DetailViewController(manager);
    }

    @Test
    public void constructor() {
        assertNotNull(detailViewController);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new Pane(), 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void initDescription() {
        verify(detailView, times(1)).setDescription(anyString());
    }

    @Test
    public void initName() {

    }


//
//    @Test
//    public void activeBlockChanged() throws Exception {
//
//    }

}