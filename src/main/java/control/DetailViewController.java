package control;

import gui.centerarea.CameraShotBlock;
import gui.centerarea.ShotBlock;
import gui.headerarea.DetailView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;

/**
 * Created by Bart.
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

        detailView.getBeginCountField().setOnKeyPressed(event -> {
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
        if (manager.getActiveShotBlock().isPresent()) {
            ShotBlock activeBlock = manager.getActiveShotBlock().get();
            String newValue = CountUtilities.parseCountNumber(
                    detailView.getBeginCountField().getText());
            detailView.getBeginCountField().setText(newValue);
            double newVal = Double.parseDouble(newValue);

            activeBlock.setBeginCount(newVal);
            if (activeBlock instanceof CameraShotBlock) {
                ((CameraShotBlock) activeBlock).getShot()
                        .setBeginCount(newVal);
            }
        }
    }

    /**
     * Init the endcuont handlers.
     */
    private void initEndCount() {
        detailView.setEndCount(0);

        detailView.getEndCountField().focusedProperty()
                .addListener(this::endCountFocusListener);

        detailView.getEndCountField().setOnKeyPressed(event -> {
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
        if (manager.getActiveShotBlock().isPresent()) {
            ShotBlock activeBlock = manager.getActiveShotBlock().get();
            String newValue = CountUtilities.parseCountNumber(
                    detailView.getEndCountField().getText());
            double newVal = Double.parseDouble(newValue);
            detailView.getEndCountField().setText(newValue);

            activeBlock.setEndCount(newVal);
            if (activeBlock instanceof CameraShotBlock) {
                ((CameraShotBlock) activeBlock).getShot()
                        .setEndCount(newVal);
            }
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
        if (manager.getActiveShotBlock().isPresent()) {
            ShotBlock activeBlock = manager.getActiveShotBlock().get();
            activeBlock.setDescription(newValue);
            if (activeBlock instanceof CameraShotBlock) {
                ((CameraShotBlock) activeBlock).getShot()
                        .setDescription(newValue);
            }
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
     * Changelistener for when tge text in namefield changes.
     * @param observable - the observable
     * @param oldValue - the old value of the field
     * @param newValue - the new value of the field
     */
    void nameTextChangedListener(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {
        if (manager.getActiveShotBlock().isPresent()) {
            ShotBlock activeBlock = manager.getActiveShotBlock().get();
            activeBlock.setName(newValue);
            if (activeBlock instanceof CameraShotBlock) {
                ((CameraShotBlock) activeBlock)
                        .getShot().setName(newValue);
            }
        }
    }

    /**
     * Method to signal that the active block is changed so we can update it.
     */
    public void activeBlockChanged() {
        if (manager.getActiveShotBlock().isPresent()) {
            ShotBlock activeBlock = manager.getActiveShotBlock().get();
            detailView.setDescription(activeBlock.getDescription());
            detailView.setName(activeBlock.getName());

            detailView.setBeginCount(activeBlock.getBeginCount());
            detailView.setEndCount(activeBlock.getEndCount());
        } else {
            detailView.resetDetails();
        }
    }
}
