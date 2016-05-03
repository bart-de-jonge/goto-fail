package control;

import gui.DetailView;

/**
 * Created by Bart.
 */
public class DetailViewController {

    private DetailView detailView;
    private ControllerManager manager;

    public DetailViewController(ControllerManager manager) {
        this.detailView = manager.getRootPane().getRootHeaderArea().getDetailView();
        this.manager = manager;
        initDescription();
    }

    private void initDescription() {
        System.out.println("hoi");
        detailView.setDescription("hoi");
    }
}
