package control;

import lombok.Getter;

/**
 * Controller for the DirectorTimeline.
 */
public class DirectorTimelineController {

    @Getter
    private final ControllerManager controllerManager;

    public DirectorTimelineController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
    }
}
