package control;

import java.util.Set;

import gui.centerarea.CameraShotBlock;
import gui.centerarea.DirectorShotBlock;
import gui.headerarea.DetailView;
import gui.headerarea.DirectorDetailView;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;

/**
 * Controller for the DetailView.
 */
public class DetailViewController {

    private DetailView detailView;
    private ControllerManager manager;

    /**
     * Constructor.
     * @param manager - the controller manager this controller belongs to
     */
    public DetailViewController(ControllerManager manager) {
        this.detailView = manager.getRootPane().getRootHeaderArea().getDetailView();
        this.manager = manager;
        initDescription();
        initName();
        initBeginCount();
        initEndCount();
    }

    /**
     * Init the begincount handlers.
     */
    private void initBeginCount() {
        detailView.setBeginCount(0);

        detailView.getBeginCountField().focusedProperty()
                .addListener(this::beginCountFocusListener);

        detailView.getBeginCountField().setOnKeyPressed(
            event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    this.beginCountUpdateHelper();
                }
            });
    }

    /**
     * Changelistener for when focus on begincountfield changes.
     * @param observable - the observable
     * @param oldValue - the old value of focus
     * @param newValue - the new value of focus
     */
    void beginCountFocusListener(ObservableValue<? extends Boolean> observable,
                                 Boolean oldValue, Boolean newValue) {
        // exiting focus
        if (!newValue) {
            this.beginCountUpdateHelper();
        }
    }

    /**
     * Helper for when the begincount field is edited.
     * Parses entry to quarters and updates all the values
     */
    void beginCountUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {
            String newValue = CountUtilities.parseCountNumber(
                    detailView.getBeginCountField().getText());
            detailView.getBeginCountField().setText(newValue);
            double newVal = Double.parseDouble(newValue);

            manager.getActiveShotBlock().setBeginCount(newVal);
            manager.getActiveShotBlock().getShot().setBeginCount(newVal);
        }
    }

    /**
     * Init the endcuont handlers.
     */
    private void initEndCount() {
        detailView.setEndCount(0);

        detailView.getEndCountField().focusedProperty()
                .addListener(this::endCountFocusListener);

        detailView.getEndCountField().setOnKeyPressed(
            event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    endCountUpdateHelper();
                }
            });
    }

    /**
     * Changelistener for when focus on endcountfield changes.
     * @param observable - the observable
     * @param oldValue - the old value of focus
     * @param newValue - the new value of focus
     */
    void endCountFocusListener(ObservableValue<? extends Boolean> observable,
                                 Boolean oldValue, Boolean newValue) {
        // exiting focus
        if (!newValue) {
            this.endCountUpdateHelper();
        }
    }

    /**
     * Helper for when the endcount field is edited.
     * Parses entry to quarters and updates all the values
     */
    private void endCountUpdateHelper() {
        if (manager.getActiveShotBlock() != null) {

            String newValue = CountUtilities.parseCountNumber(
                    detailView.getEndCountField().getText());
            double newVal = Double.parseDouble(newValue);
            detailView.getEndCountField().setText(newValue);

            manager.getActiveShotBlock().setEndCount(newVal);
            manager.getActiveShotBlock().getShot().setEndCount(newVal);
        }
    }

    /**
     * Init the description handlers.
     */
    private void initDescription() {
        detailView.setDescription("");

        detailView.getDescriptionField().textProperty()
                .addListener(this::descriptionTextChangedListener);
    }

    /**
     * Changelistener for when the text in descriptionfield changes.
     * @param observable - the observable
     * @param oldValue - the old value of the field
     * @param newValue - the new value of the field
     */
    void descriptionTextChangedListener(ObservableValue<? extends String> observable,
                               String oldValue, String newValue) {
        if (manager.getActiveShotBlock() != null) {
            manager.getActiveShotBlock().setDescription(newValue);
            manager.getActiveShotBlock().getShot().setDescription(newValue);
        }
    }

    /**
     * Init the name handler.
     */
    private void initName() {
        detailView.setName("");

        detailView.getNameField().textProperty()
                .addListener(this::nameTextChangedListener);
    }

    /**
     * Changelistener for when the text in namefield changes.
     * @param observable - the observable
     * @param oldValue - the old value of the field
     * @param newValue - the new value of the field
     */
    void nameTextChangedListener(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {
        if (manager.getActiveShotBlock() != null) {
            manager.getActiveShotBlock().setName(newValue);
            manager.getActiveShotBlock().getShot().setName(newValue);
        }
    }

    /**
     * Method to signal that the active block is changed so we can update it.
     */
    public void activeBlockChanged() {
        if (manager.getActiveShotBlock() != null) {
            System.out.println("PASSED NULL CHECK");
            if (manager.getActiveShotBlock() instanceof CameraShotBlock) {
                System.out.println("IS CAMERA SHOT BLOCK");
                detailView = new DetailView();
                detailView.setDescription(manager.getActiveShotBlock().getDescription());
                detailView.setName(manager.getActiveShotBlock().getName());
                detailView.setBeginCount(manager.getActiveShotBlock().getBeginCount());
                detailView.setEndCount(manager.getActiveShotBlock().getEndCount());
                detailView.setVisible();
                detailView.setVisible(true);
                manager.getRootPane().getRootHeaderArea().setDetailView(detailView);  
                manager.getRootPane().getRootHeaderArea().reInitHeaderBar(detailView);
            } else {
                System.out.println("IS DIRECTOR SHOT BLOCK");
                DirectorShotBlock shotBlock = (DirectorShotBlock) manager.getActiveShotBlock();
                detailView = new DirectorDetailView();
                detailView.setDescription(shotBlock.getDescription());
                detailView.setName(shotBlock.getName());
                detailView.setBeginCount(shotBlock.getBeginCount());
                detailView.setEndCount(shotBlock.getEndCount());
                ((DirectorDetailView) detailView).getPaddingBeforeField().setText(detailView.formatDouble(shotBlock.getPaddingBefore()));
                ((DirectorDetailView) detailView).getPaddingAfterField().setText(detailView.formatDouble(shotBlock.getPaddingAfter()));
                initDropDown(shotBlock);
                detailView.setVisible();
                detailView.setVisible(true);
                manager.getRootPane().getRootHeaderArea().reInitHeaderBar(detailView);

            }
        } else {
            detailView.resetDetails();
            detailView.setInvisible();
        }
    }
    
    private void initDropDown(DirectorShotBlock shotBlock) {
        Set<Integer> indices = shotBlock.getTimelineIndices();
        ((DirectorDetailView) detailView).getSelectCamerasDropDown().getItems().clear();
        indices.forEach(e -> {
            String name = manager.getScriptingProject().getCameras().get(e).getName();
            ((DirectorDetailView) detailView).getSelectCamerasDropDown().getItems().add(name);
        });
    }
}
