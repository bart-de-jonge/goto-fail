package control;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import data.Camera;
import data.CameraShot;
import data.CameraTimeline;
import data.CameraType;
import data.DirectorShot;
import data.DirectorTimeline;
import data.Instrument;
import data.ScriptingProject;
import data.User;
import gui.modal.AddCameraModalView;
import gui.modal.AddCameraTypeModalView;
import gui.modal.AddInstrumentModalView;
import gui.modal.DeleteCameraTypeWarningModalView;
import gui.modal.EditProjectModalView;
import gui.modal.ErrorWhileUploadingModalView;
import gui.modal.UploadSuccessModalView;
import gui.root.RootCenterArea;
import gui.root.RootPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProjectController {
    
    @Getter
    private ControllerManager controllerManager;
    
    
    private EditProjectModalView editProjectModal;
    private AddCameraModalView cameraModal;
    private AddCameraTypeModalView cameraTypeModal;
    private AddInstrumentModalView instrumentModal;
    @Setter
    private DeleteCameraTypeWarningModalView typeWarningModal;
    private ErrorWhileUploadingModalView errorModal;
    private UploadSuccessModalView successModal;

    public static final int UNUSED_BLOCK_OFFSET = 4000000;
    private static final String NAME_DESC_SEPERATOR = " - ";
    
    // Upload variables
    // Todo: replace with popup or something like that for user
    private String url = "http://localhost:3000/upload-scp";

    public void setEditProjectModal(EditProjectModalView modal) {
        editProjectModal = modal;
    }
    
    /**
     * Construct a new FileMenuController.
     * @param manager the controllermanager that manages this controller
     */
    public ProjectController(ControllerManager manager) {
        this.controllerManager = manager;
        controllerManager.getRootPane().getStartupModalView().getNewButton()
                         .setOnMouseClicked(this::newProject);
        controllerManager.getRootPane().getStartupModalView().getLoadButton()
                         .setOnMouseClicked(this::loadProject);
        controllerManager.getRootPane().getStartupModalView().getExitButton()
                         .setOnMouseClicked(this::exit);
    }

    /**
     * Upload the current project to the webserver.
     */
    public void uploadToWebserver() {
        // Save the project
        this.save();

        // Make request
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("project", new File(
                controllerManager.getScriptingProject().getFilePath()),
                ContentType.APPLICATION_XML, "project.scp");
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        try {
            // get response
            CloseableHttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();
            JSONObject resultJson = new JSONObject(EntityUtils.toString(responseEntity));
            boolean result = resultJson.getBoolean("succes");

            // Do something with response
            if (result) {
                showSuccessModal();
                System.out.println("Upload successful");
            } else {
                showErrorModal();
                System.out.println("Upload failed");
            }

        } catch (IOException e) {
            showErrorModal();
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Show the upload success modal.
     */
    private void showSuccessModal() {
        successModal = new UploadSuccessModalView(controllerManager.getRootPane());
        successModal.getCloseButton().setOnMouseClicked(this::successModalClose);
        successModal.getGoToWebsiteButton().setOnMouseClicked(this::goToWebsite);
    }
    
    /**
     * Handler for the close button of the success modal.
     * @param event the MouseEvent for this event
     */
    private void successModalClose(MouseEvent event) {
        successModal.hideModal();
    }
    
    /**
     * Handler for the go to website button of the sucess modal.
     * @param event the MouseEvent for this event
     */
    private void goToWebsite(MouseEvent event) {
        try {
            Desktop.getDesktop().browse(URI.create("http://localhost:3000/timeline"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Show the upload error modal.
     */
    private void showErrorModal() {
        errorModal = new ErrorWhileUploadingModalView(controllerManager.getRootPane());
        errorModal.getOkButton().setOnMouseClicked(this::errorModalOk);
    }
    
    /**
     * Handler for the OK button for the error modal.
     * @param event the MouseEvent for this event
     */
    private void errorModalOk(MouseEvent event) {
        errorModal.hideModal();
    }
    
    
    
    /**
     * Save the current project state to file.
     * A file chooser window will be opened to select a file
     */
    public void saveAs() {
        log.info("Saving Project to file");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        ExtensionFilter scpFilter = new ExtensionFilter("Scripting Project", "*.scp");
        ExtensionFilter allFilter = new ExtensionFilter("All files", "*.*");
        fileChooser.getExtensionFilters().addAll(scpFilter, allFilter);
        File file = fileChooser.showSaveDialog(controllerManager.getRootPane().getPrimaryStage());
        if (file != null) {
            controllerManager.getScriptingProject().setFilePath(file.getAbsolutePath());
            controllerManager.getScriptingProject().write(file);
            this.changeConfigFile(controllerManager.getScriptingProject());
        } else {
            log.info("User did not select a file");
        }
    }

    /**
     * This method reinitializes all the cameratimelines and the directortimeline. This method takes
     * care of the removal and addition of cameratimelines.
     * @param newProject the project that will be newly added to the scripting project with
     *                   optionally changed number of timelines
     */
    private void reInitTimelines(ScriptingProject newProject) {
        ScriptingProject oldProject = editProjectModal.getProject();
        if (oldProject != null) {
            for (int i = 0; i < newProject.getCameras().size(); i++) {
                Camera newCamera = newProject.getCameras().get(i);
                Camera oldCamera = null;
                CameraTimeline newTimeline = newProject.getCameraTimelines().get(i);
                CameraTimeline oldTimeline = null;
                /*
                    Yes, O-n-squared. No, I couldn't care less.
                    Finds old shot with same instance as new shot, so we know
                    that it is the same even if the name, description
                    or position in the list has changed.
                */
                for (int j = 0; j < oldProject.getCameras().size(); j++) {
                    if (oldProject.getCameras().get(j).getInstance()
                            == newCamera.getInstance()) {
                        oldCamera = oldProject.getCameras().get(j);
                        oldTimeline = oldProject.getCameraTimelines().get(j);
                    }
                }
                if (oldCamera != null) {
                    LinkedList<CameraShot> shots = new LinkedList<>();
                    oldTimeline.getShots().forEach(shots::add);
                    int j = i;
                    shots.forEach(shot -> {
                            newTimeline.addShot(shot);
                            if (!(shot.getBeginCount() == UNUSED_BLOCK_OFFSET)) {
                                controllerManager.getTimelineControl().addCameraShot(j, shot);
                            }
                        });
                    controllerManager.getScriptingProject().getCameraTimelines()
                            .get(i).setShots(shots);
                }
            }
            oldProject.getDirectorTimeline().getShots()
                .forEach(shot -> controllerManager
                    .getDirectorTimelineControl().addDirectorShot(shot));
        }  
    }

    /**
     * Init project with modal data.
     * @return the initialized project
     */
    private ScriptingProject constructProjectFromModalData() {
        String name = editProjectModal.getNameField().getText();
        String description = editProjectModal.getDescriptionField().getText();
        String directorTimelineDescription = editProjectModal
                .getDirectorTimelineDescriptionField().getText();
        double secondsPerCount = Double.parseDouble(
                editProjectModal.getSecondsPerCountField().getText());
        ScriptingProject project = new ScriptingProject(name, description, secondsPerCount);
        if (controllerManager.getScriptingProject() != null) {
            project.setFilePath(controllerManager.getScriptingProject().getFilePath());
        }
        project.setDirectorTimeline(new DirectorTimeline(directorTimelineDescription, null));
        project.setCameraTypes(editProjectModal.getCameraTypes());
        project.setCameras(editProjectModal.getCameras());
        project.setCameraTimelines(editProjectModal.getTimelines());
        project.setInstruments(editProjectModal.getInstruments());
        return project;
    }
    
    /**
     * Handler for when the apply button is clicked for existing project.
     * Differs from when the apply button is clicked for new project, obviously.
     * @param event the MouseEvent for this handler
     */
    private void applyEdit(MouseEvent event) {
        if (validateProjectData()) {
            editProjectModal.hideModal();
            ScriptingProject project = constructProjectFromModalData();
            project.getDirectorTimeline().setProject(project);
            project.getCameraTimelines().forEach(c -> c.setProject(project));
            ArrayList<User> oldUsers = controllerManager.getScriptingProject().getUsers();
            project.setUsers(oldUsers);
            controllerManager.setScriptingProject(project);
            controllerManager.updateWindowTitle();
            RootCenterArea area = new RootCenterArea(controllerManager.getRootPane(),
                    editProjectModal.getTimelines().size(), false);
            controllerManager.getRootPane().reInitRootCenterArea(area);
            reInitTimelines(project);
        }
    }

    /**
     * Handler for when the apply button is clicked for new project.
     * Differs from when the apply button is clicked for edit project, obviously.
     * @param event the MouseEvent for this handler
     */
    private void applyNew(MouseEvent event) {
        if (validateProjectData()) {
            editProjectModal.hideModal();
            ScriptingProject project = constructProjectFromModalData();
            project.getDirectorTimeline().setProject(project);
            project.getCameraTimelines().forEach(c -> c.setProject(project));
            controllerManager.setScriptingProject(project);
            controllerManager.updateWindowTitle();
            RootCenterArea area = new RootCenterArea(controllerManager.getRootPane(),
                    editProjectModal.getTimelines().size(), false);
            controllerManager.getRootPane().reInitRootCenterArea(area);
            reInitTimelines(project);
        }
    }


    /**
     * Save the current project.
     If the file path of this project is already known, write to that file
     Otherwise, treat as if save as was clicked
     */
    public void save() {
        if (controllerManager.getScriptingProject().getFilePath() == null) {
            saveAs();
        } else {
            controllerManager.getScriptingProject().write();
        }
    }
    
    /**
     * Load a project from the given path.
     * @param file the file to load from
     */
    public void load(File file) {
        ScriptingProject temp = null;
        try {
            temp = ScriptingProject.read(file);
        } catch (Exception e) {
            log.error("previously opened file could not be found.");
        }
        if (temp == null) {
            controllerManager.getRootPane().getPrimaryStage().close();
            controllerManager.getRootPane().showStartupScreen(true);
        } else {
            controllerManager.getRootPane().closeStartupScreen();
            controllerManager.setScriptingProject(temp);
            controllerManager.getRootPane()
                    .reInitRootCenterArea(new RootCenterArea(
                            controllerManager.getRootPane(),
                            controllerManager.getScriptingProject()
                                    .getCameraTimelines()
                                    .size(), false));
            removeRedundantCameraBlocks(controllerManager.getScriptingProject());
            List<Integer> addedBlocks = addLoadedDirectorShotBlocks(
                    controllerManager.getScriptingProject());
            addLoadedCameraShotBlocks(controllerManager.getScriptingProject(), addedBlocks);
            changeConfigFile(temp);
            int maxInstance = temp.getMaxInstance();
            CameraShot.setInstanceCounter(maxInstance + 1);
            DirectorShot.setInstanceCounter(maxInstance + 1);
            controllerManager.getScriptingProject().removeOffsettedCameraBlocks();
        }
    }
    
    /**
     * Load a project from file.
     * A file chooser window will be opened to select the file.
     */
    public void load() {
        log.info("Loading Project from file");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        ExtensionFilter scpFilter = new ExtensionFilter("Scripting Project", "*.scp");
        ExtensionFilter allFilter = new ExtensionFilter("All files", "*.*");
        fileChooser.getExtensionFilters().addAll(scpFilter, allFilter);
        File file = fileChooser.showOpenDialog(controllerManager.getRootPane().getPrimaryStage());
        if (file != null) {
            load(file);
        } else {
            log.info("User did not select a file");
        }
    }
    
    /**
     * Removes redundant camera blocks from the view.
     * @param project the project to do this for
     */
    private void removeRedundantCameraBlocks(ScriptingProject project) {
        for (int i = 0;i < project.getDirectorTimeline().getShots().size();i++) {
            DirectorShot dShot = project.getDirectorTimeline().getShots().get(i);
            dShot.getCameraShots().forEach(cameraShot -> {
                    for (int j = 0;j < project.getCameraTimelines().size();j++) {
                        CameraTimeline timeline = project.getCameraTimelines().get(j);
                        timeline.getShots().forEach(shot -> {
                                if (cameraShot.getInstance() == shot.getInstance()) {
                                    shot.setBeginCount(UNUSED_BLOCK_OFFSET);
                                    shot.setEndCount(UNUSED_BLOCK_OFFSET);
                                }
                            });
                    }
                });
        }
    }

    

    /**
     * Overwrite most recent project path in config file.
     * @param project the project to write the path from
     */
    private void changeConfigFile(ScriptingProject project) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File(RootPane.getCONFIG_FILEPATH()), "UTF-8");
            writer.write(project.getFilePath());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
                controllerManager.updateWindowTitle();
            }
        }
    }

    /**
     * Remove most recent project path in config file.
     * Used, for example, if the file cannot be found.
     */
    public void emptyConfigFile() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File(RootPane.getCONFIG_FILEPATH()), "UTF-8");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Add the loaded camera shotblocks that were loaded from file to the UI.
     * @param project the project that was loaded
     * @param addedBlocks the blocks that were already added
     */
    public void addLoadedCameraShotBlocks(ScriptingProject project, List<Integer> addedBlocks) {
        log.info("Adding loaded CameraShotBlocks");
        for (int i = 0; i < project.getCameraTimelines().size();i++) {
            CameraTimeline timeline = project.getCameraTimelines().get(i);
            int amountShots = timeline.getShots().size();
            for (int j = 0; j < amountShots;j++) {
                CameraShot shot = timeline.getShots().get(j);
                if (!addedBlocks.contains(shot.getInstance())) {
                    addCameraShotForLoad(i, shot);
                } else {
                    log.info("Shot with instance {} was already loaded", shot.getInstance());
                }
            }
        }
    }

    /**
     * Add the loaded director shotblocks that were loaded from the file to the UI.
     * @param project the project that was loaded
     * @return A list of camera shot blocks that were already added
     */
    private List<Integer> addLoadedDirectorShotBlocks(ScriptingProject project) {
        log.info("Adding loaded DirectorShotBlocks");
        DirectorTimeline timeline = project.getDirectorTimeline();
        List<Integer> addedCameraBlocks = new ArrayList<Integer>();
        
        for (int i = 0; i < timeline.getShots().size(); i++) {
            DirectorShot shot = timeline.getShots().get(i);
            // add instance numbers to the list
            shot.getCameraShots().forEach(e -> {
                    int timelineNumber = -1;
                    addedCameraBlocks.add(e.getInstance());
                    for (int j = 0;j < project.getCameraTimelines().size();j++) {
                        if (project.getCameraTimelines().get(j).getShots().contains(e)) {
                            timelineNumber = j;
                            break;
                        }
                    }
                    if (timelineNumber != -1) {
                        e.setDirectorShot(shot);
                        controllerManager.getTimelineControl().addCameraShot(timelineNumber, e);
                    } else {
                        log.error("Mistake while finding timeline number");
                    }
                });
            
            addDirectorShotForLoad(timeline.getShots().get(i));
        }
        return addedCameraBlocks;
    }

    /**
     * Add a camera shot that is loaded from file.
     * @param cameraIndex the index of the camera timeline to use
     * @param shot the shot to add
     */
    private void addCameraShotForLoad(int cameraIndex, CameraShot shot) {
        if (!(shot.getBeginCount() == UNUSED_BLOCK_OFFSET)) {
            controllerManager.getTimelineControl().initShotBlock(cameraIndex, shot);
        }
    }

    /**
     * Add a director shot that is loaded from file.
     * @param shot the shot to add
     */
    private void addDirectorShotForLoad(DirectorShot shot) {
        controllerManager.getDirectorTimelineControl().initShotBlock(shot);
    }
    
    private void deleteInstrument(MouseEvent event) {
        int selectedIndex = editProjectModal.getInstrumentList().getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            editProjectModal.getInstrumentList().getItems().remove(selectedIndex);
            editProjectModal.getInstruments().remove(selectedIndex);
        } else {
            editProjectModal.getTitleLabel().setText("Please select an instrument first");
            editProjectModal.getTitleLabel().setTextFill(Color.RED);
        }
    }

   
    /**
     * Handler for deleting a camera type.
     * @param event the MouseEvent for this handler
     */
    private void deleteCameraType(MouseEvent event) {
        int selectedIndex = editProjectModal.getCameraTypeList()
                                            .getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            List<Camera> camerasUsingType = getCamerasThatUse(
                    editProjectModal.getCameraTypes().get(selectedIndex));
            if (camerasUsingType.size() == 0) {
                editProjectModal.getCameraTypes().remove(selectedIndex);
                editProjectModal.getCameraTypeList().getItems().remove(selectedIndex);
            } else {
                typeWarningModal = new DeleteCameraTypeWarningModalView(
                        controllerManager.getRootPane(), camerasUsingType);
                typeWarningModal.getConfirmButton().setOnMouseClicked(
                        e -> confirmTypeDelete(e, camerasUsingType, selectedIndex));
                typeWarningModal.getCancelButton().setOnMouseClicked(this::cancelTypeDelete);
            }
        } else {
            editProjectModal.getTitleLabel().setText("Please select a camera type first");
            editProjectModal.getTitleLabel().setTextFill(Color.RED);
        }
    }
    
    /**
     * Event handler for when the warning modal for type delete is shown and the confirm
     button is clicked.
     * @param event the MouseEvent for this event
     * @param toBeDeleted the list of camera's to be deleted
     * @param selectedIndex the index of the type selected
     */
    private void confirmTypeDelete(MouseEvent event, List<Camera> toBeDeleted, int selectedIndex) {
        typeWarningModal.hideModal();
        if (selectedIndex < editProjectModal.getCameraTypes().size()) {
            editProjectModal.getCameraTypes().remove(selectedIndex);
            editProjectModal.getCameraTypeList().getItems().remove(selectedIndex);
            toBeDeleted.forEach(e -> {
                    int index = editProjectModal.getCameras().indexOf(e);
                    editProjectModal.getCameras().remove(index);
                    editProjectModal.getTimelines().remove(index);
                    editProjectModal.getCameraList().getItems().remove(index);
                });
        }
    }
    
    /**
     * Event handler for when the cancel button on the type delete warning modal is clicked.
     * @param event the MouseEvent for this event
     */
    private void cancelTypeDelete(MouseEvent event) {
        typeWarningModal.hideModal();
    }
    
    /**
     * Get a list of cameras that use a certain camera type.
     * @param type the type to check for
     * @return a list of cameras that use type as their camera type
     */
    private List<Camera> getCamerasThatUse(CameraType type) {
        return editProjectModal.getCameras().stream()
                                   .filter(e -> e.getCameraType().equals(type))
                                   .collect(Collectors.toList());
    }
    
    /**
     * Handler for when the cancel button is pressed.
     * @param event the MouseEvent for this handler
     */
    private void cancel(MouseEvent event) {
        editProjectModal.hideModal();
    }
    
    /**
     * Handler for deleting a camera.
     * @param event the MouseEvent for this handler
     */
    private void deleteCamera(MouseEvent event) {
        // TODO: Show prompt if there are shots on the timeline.
        int selectedIndex = editProjectModal.getCameraList().getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            editProjectModal.getCameras().remove(selectedIndex);
            editProjectModal.getTimelines().remove(selectedIndex);
            editProjectModal.getCameraList().getItems().remove(selectedIndex);
        } else {
            editProjectModal.getTitleLabel().setText("Please select a camera first");
            editProjectModal.getTitleLabel().setTextFill(Color.RED);
        }
    }
    
    /**
     * Method to show the modal to create a new project.
     */
    public void newProject() {
        editProjectModal = new EditProjectModalView(controllerManager.getRootPane(), false);
        initHandlersForNewProjectModal();
    }
    
    /**
     * Method to show the modal to create a new project.
     Has MouseEvent so it can be used in an onclick for a button.
     * @param event the MouseEvent, e.g. when clicking the new project button
     */
    public void newProject(MouseEvent event) {
        controllerManager.getRootPane().getStartupModalView().hideModal();
        newProject();
    }
    
    /**
     * Start the edit project modal with information of the current project filled in.
     */
    public void editProject() {
        editProjectModal = new EditProjectModalView(controllerManager.getRootPane(), true);
        initHandlersForEditProjectModal();
    }
    
    /**
     * Init the button handlers for the edit project modal.
     */
    private void initHandlersForEditProjectModal() {
        initGeneralHandlers();
        editProjectModal.getApplyButton().setOnMouseClicked(this::applyEdit);
    }
    
    private void initGeneralHandlers() {
        editProjectModal.getAddCameraButton().setOnMouseClicked(this::addCamera);
        editProjectModal.getEditCameraButton().setOnMouseClicked(this::editCamera);
        editProjectModal.getDeleteCameraButton().setOnMouseClicked(this::deleteCamera);
        editProjectModal.getAddCameraTypeButton().setOnMouseClicked(this::addCameraType);
        editProjectModal.getEditCameraTypeButton().setOnMouseClicked(this::editCameraType);
        editProjectModal.getDeleteCameraTypeButton().setOnMouseClicked(this::deleteCameraType);
        editProjectModal.getAddInstrumentButton().setOnMouseClicked(this::addInstrument);
        editProjectModal.getEditInstrumentButton().setOnMouseClicked(this::editInstrument);
        editProjectModal.getDeleteInstrumentButton().setOnMouseClicked(this::deleteInstrument);
        editProjectModal.getCancelButton().setOnMouseClicked(this::cancel);
    }

    /**
     * Init the button handlers for the new project modal.
     */
    private void initHandlersForNewProjectModal() {
        initGeneralHandlers();
        editProjectModal.getApplyButton().setOnMouseClicked(this::applyNew);
    }
    
    private void addInstrument(MouseEvent event) {
        instrumentModal = new AddInstrumentModalView(controllerManager.getRootPane());
        instrumentModal.getAddInstrumentButton().setOnMouseClicked(this::instrumentAdded);
        instrumentModal.getCancelButton().setOnMouseClicked(this::cancelAddInstrument);
    }
    
    
    private void cancelAddInstrument(MouseEvent event) {
        instrumentModal.hideModal();
    }
    
    private void editInstrument(MouseEvent event) {
        int selectedIndex = editProjectModal.getInstrumentList().getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            instrumentModal = new AddInstrumentModalView(controllerManager.getRootPane(),
                    editProjectModal.getInstruments().get(selectedIndex));
            instrumentModal.getAddInstrumentButton().setOnMouseClicked(e -> instrumentEdited(e, selectedIndex));
            instrumentModal.getCancelButton().setOnMouseClicked(this::instrumentEditCancelled);
        } else {
            editProjectModal.getTitleLabel().setText("Please select an instrument to edit");
            editProjectModal.getTitleLabel().setTextFill(Color.RED);
        }
    }
    
    private void instrumentEdited(MouseEvent event, int selectedIndex) {
        if (this.validateInstrumentData()) {
            instrumentModal.hideModal();
            String name = instrumentModal.getNameField().getText();
            String description = instrumentModal.getDescriptionField().getText();
            editProjectModal.getInstruments().get(selectedIndex).setName(name);
            editProjectModal.getInstruments().get(selectedIndex).setDescription(description);
            HBox box = new HBox();
            if (description.isEmpty()) {
                box.getChildren().add(new Label(name));
            } else {
                box.getChildren().addAll(new Label(name), new Label(NAME_DESC_SEPERATOR), new Label(description));
            }
            editProjectModal.getInstrumentList().getItems().set(selectedIndex, box);
        }
    }
    
    private void instrumentEditCancelled(MouseEvent event) {
        instrumentModal.hideModal();
    }
    
   
    
    /**
     * Event handler for when the edit camera button is clicked.
     * @param event the MouseEvent for this event
     */
    private void editCamera(MouseEvent event) {
        int selectedIndex = editProjectModal.getCameraList().getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            cameraModal = new AddCameraModalView(controllerManager.getRootPane(),
                    editProjectModal.getCameraTypes(),
                    editProjectModal.getCameras().get(selectedIndex),
                    editProjectModal.getCameraTypes().indexOf(
                            editProjectModal.getCameras().get(selectedIndex).getCameraType()));
            cameraModal.getAddCameraButton().setOnMouseClicked(e -> cameraEdited(e, selectedIndex));
            cameraModal.getCancelButton().setOnMouseClicked(this::cameraEditCancelled);
        } else {
            editProjectModal.getTitleLabel().setText("Please select a camera to edit");
            editProjectModal.getTitleLabel().setTextFill(Color.RED);
        }
    }
    
    /**
     * Event handler for when the applyEdit button in the camera edit modal is clicked.
     * @param event the MouseEvent for this event
     * @param selectedIndex the index of the camera to change
     */
    private void cameraEdited(MouseEvent event, int selectedIndex) {
        if (this.validateCameraData()) {
            cameraModal.hideModal();
            String name = cameraModal.getNameField().getText();
            String description = cameraModal.getDescriptionField().getText();
            int index = cameraModal.getCameraTypes().getSelectionModel().getSelectedIndex();
            CameraType type = editProjectModal.getCameraTypes().get(index);
            editProjectModal.getCameras().get(selectedIndex).setName(name);
            editProjectModal.getCameras().get(selectedIndex).setDescription(description);
            editProjectModal.getCameras().get(selectedIndex).setCameraType(type);
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(NAME_DESC_SEPERATOR),
                    new Label(description));
            editProjectModal.getCameraList().getItems().set(selectedIndex, box);
        }
    }
    
    /**
     * Event handler for when the cancel button in the camera edit modal is clicked.
     * @param event the MouseEvent for this event
     */
    private void cameraEditCancelled(MouseEvent event) {
        cameraModal.hideModal();
    }
    
    /**
     * Event handler for when the edit camera type button is clicked.
     * @param event the MouseEvent for this event.
     */
    private void editCameraType(MouseEvent event) {
        int selectedIndex = editProjectModal.getCameraTypeList()
                                            .getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            cameraTypeModal = new AddCameraTypeModalView(
                    controllerManager.getRootPane(),
                    editProjectModal.getCameraTypes().get(selectedIndex));
            cameraTypeModal.getAddCameraTypeButton().setOnMouseClicked(
                    e -> cameraTypeEdited(e, selectedIndex));
            cameraTypeModal.getCancelButton().setOnMouseClicked(this::cameraTypeEditCancelled);
        } else {
            editProjectModal.getTitleLabel().setText("Please select a camera type to edit");
            editProjectModal.getTitleLabel().setTextFill(Color.RED);
        }
    }
    
    /**
     * Event handler for when the apply button in the edit camera type modal is clicked.
     * @param event the MouseEvent for this event.
     * @param selectedIndex the index of the camera type to edit.
     */
    private void cameraTypeEdited(MouseEvent event, int selectedIndex) {
        if (validateCameraTypeData()) {
            cameraTypeModal.hideModal();
            
            
            String name = cameraTypeModal.getNameField().getText();
            String description = cameraTypeModal.getDescriptionField().getText();
            double movementMargin = Double.parseDouble(
                    cameraTypeModal.getMovementMarginField().getText());
            
            
            CameraType type = new CameraType(name, description, movementMargin);
            
            editProjectModal.getCameras().forEach(e -> {
                    if (e.getCameraType().equals(
                            editProjectModal.getCameraTypes().get(selectedIndex))) {
                        e.setCameraType(type);
                    }
                });
            
            editProjectModal.getCameraTypes().set(selectedIndex, type);
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(NAME_DESC_SEPERATOR),
                    new Label(description));
            editProjectModal.getCameraTypeList().getItems().set(selectedIndex, box);
        }
    }
    
    /**
     * Event handler for when the cancel button in the camera type edit modal is clicked.
     * @param event the MouseEvent for this event
     */
    private void cameraTypeEditCancelled(MouseEvent event) {
        cameraTypeModal.hideModal();
    }

    /**
     * Event handler for when the add camera button in the NewProjectModalView is clicked.
     * @param event the mouse event related to this event
     */
    private void addCamera(MouseEvent event) {
        cameraModal = new AddCameraModalView(controllerManager.getRootPane(),
                                             editProjectModal.getCameraTypes());
        cameraModal.getAddCameraButton().setOnMouseClicked(this::cameraAdded);
        cameraModal.getCancelButton().setOnMouseClicked(this::cancelAddCamera);
    }
    
    /**
     * Event handler for when the cancel button is clicked in the add camera modal.
     * @param event the MouseEvent for this event
     */
    private void cancelAddCamera(MouseEvent event) {
        cameraModal.hideModal();
    }
    
    private void instrumentAdded(MouseEvent event) {
        if (validateInstrumentData()) {
            instrumentModal.hideModal();
            String name = instrumentModal.getNameField().getText();
            String description = instrumentModal.getDescriptionField().getText();
            Instrument instrument = new Instrument(name, description);
            editProjectModal.getInstruments().add(instrument);
            HBox box = new HBox();
            if (description.isEmpty()) {
                box.getChildren().add(new Label(name));
            } else {
                box.getChildren().addAll(new Label(name), new Label(NAME_DESC_SEPERATOR),
                        new Label(description));
            }
            editProjectModal.getInstrumentList().getItems().add(box);
        }
    }
    
    private boolean validateInstrumentData() {
        String errorString = "";
        String name = instrumentModal.getNameField().getText();
        if (name.isEmpty()) {
            errorString = "Please enter an instrument name\n";
        }
        
        instrumentModal.getTitleLabel().setText(errorString);
        instrumentModal.getTitleLabel().setTextFill(Color.RED);
        
        return errorString.isEmpty();
    }
    
    /**
     * Event handler for when the add camera button is clicked in the AddCameraModalView.
     * This adds a camera, and a timeline, all in one.
     * @param event the mouse event related to this event
     */
    private void cameraAdded(MouseEvent event) {
        if (validateCameraData()) {
            // add camera
            cameraModal.hideModal();
            String name = cameraModal.getNameField().getText();
            String description = cameraModal.getDescriptionField().getText();
            int selectedIndex = cameraModal.getCameraTypes().getSelectionModel().getSelectedIndex();
            CameraType type = cameraModal.getCameraTypeList().get(selectedIndex);
            Camera camera = new Camera(name, description, type);
            editProjectModal.getCameras().add(camera);
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(NAME_DESC_SEPERATOR), 
                    new Label(description));
            editProjectModal.getCameraList().getItems().add(box);
            // add timeline
            CameraTimeline timeline = new CameraTimeline(camera, null);
            editProjectModal.getTimelines().add(timeline);
        }
    }
    
    /**
     * Validate the data entered by the user in the modal to add a camera.
     * @return true is the data is legit, false otherwise
     */
    private boolean validateCameraData() {
        String errorString = "";
        String name = cameraModal.getNameField().getText();
        String description = cameraModal.getDescriptionField().getText();
        int selectedIndex = cameraModal.getCameraTypes().getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            errorString = "Please select a camera type\n";
        }
        
        if (description.isEmpty()) {
            errorString = "Please enter a camera description\n";
        }

        if (name.isEmpty()) {
            errorString = "Please enter a camera name\n";
        }

        
        cameraModal.getTitleLabel().setText(errorString);
        cameraModal.getTitleLabel().setTextFill(Color.RED);
        
        return errorString.isEmpty();
    }
    
    /**
     * Event handler for when the add camera type button in the NewProjectModalView is clicked.
     * @param event the mouse event related to this event
     */
    private void addCameraType(MouseEvent event) {
        cameraTypeModal = new AddCameraTypeModalView(controllerManager.getRootPane());
        cameraTypeModal.getAddCameraTypeButton().setOnMouseClicked(this::typeAdded);
        cameraTypeModal.getCancelButton().setOnMouseClicked(this::cancelAddCameraType);
    }
    
    /**
     * Event handler for when the cancel button is clicked in the add camera type modal.
     * @param event the MouseEvent for this event
     */
    private void cancelAddCameraType(MouseEvent event) {
        cameraTypeModal.hideModal();
    }
    
    /**
     * Event handler for when the add camera type button in the AddCameraTypeModalView is clicked.
     * @param event the the mouse event related to this event
     */
    private void typeAdded(MouseEvent event) {
        if (validateCameraTypeData()) {
            cameraTypeModal.hideModal();
            String name = cameraTypeModal.getNameField().getText();
            String description = cameraTypeModal.getDescriptionField().getText();
            double movementMargin = Double.parseDouble(
                    cameraTypeModal.getMovementMarginField().getText());
            CameraType type = new CameraType(name, description, movementMargin);
            editProjectModal.getCameraTypes().add(type);
            HBox box = new HBox();
            box.getChildren().addAll(new Label(name), new Label(NAME_DESC_SEPERATOR),
                    new Label(description));
            editProjectModal.getCameraTypeList().getItems().add(box);
        }
    }
    
    /**
     * Validate the data entered by the user in the modal to add a camera type.
     * @return true is the data is legit, false otherwise
     */
    private boolean validateCameraTypeData() {
        String errorString = "";
        String name = cameraTypeModal.getNameField().getText();
        String description = cameraTypeModal.getDescriptionField().getText();
        String movementMargin = cameraTypeModal.getMovementMarginField().getText();

        if (movementMargin.isEmpty()) {
            errorString = "Please enter a movement margin\n";
        }

        if (description.isEmpty()) {
            errorString = "Please enter a description\n";
        }

        if (name.isEmpty()) {
            errorString = "Please enter a name\n";
        }

        cameraTypeModal.getTitleLabel().setText(errorString);
        cameraTypeModal.getTitleLabel().setTextFill(Color.RED);
        
        return errorString.isEmpty();
    }
    
    
    
    /**
     * Validate the data entered by the user in the modal to create a project.
     * @return true is the data is legit, false otherwise
     */
    private boolean validateProjectData() {
        String errorString = "";

        String directorTimelineDescription = editProjectModal.getDirectorTimelineDescriptionField()
                                                            .getText();
        if (directorTimelineDescription.isEmpty()) {
            errorString = "Please enter a director timeline description\n";
        }

        String secondsPerCount = editProjectModal.getSecondsPerCountField()
                .getText();
        if (secondsPerCount.isEmpty()) {
            errorString = "Please enter the seconds per count\n";
        }

        String description = editProjectModal.getDescriptionField().getText();
        if (description.isEmpty()) {
            errorString = "Please enter a project description\n";
        }

        String name = editProjectModal.getNameField().getText();
        if (name.isEmpty()) {
            errorString = "Please enter a project name\n";
        }

        if (!errorString.equals((""))) {
            editProjectModal.getTitleLabel().setText(errorString);
            editProjectModal.getTitleLabel().setTextFill(Color.RED);
        }

        return errorString.isEmpty();
    }
    
    /**
     * Event handler for when the load button is clicked.
     * @param event the MouseEvent for this event
     */
    public void loadProject(MouseEvent event) {
        load();
    }

    /**
     * Event handler for when the exit button is clicked in the start up screen.
     * @param event the MouseEvent for this event.
     */
    private void exit(MouseEvent event) {
        controllerManager.getRootPane().closeStartupScreen();
    }

}
