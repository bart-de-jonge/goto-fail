/*
 * Class for storing a CameraTimeline
 */
class CameraTimeline {
    constructor(name, description) {
        this.name = name;
        this.description = description;
        this.cameraShots = [];
    }

    // Add a camerashot to this timeline
    addCameraShot(cameraShot) {
        this.cameraShots.push(cameraShot);
    }

    // Get the list of cameraShots
    getCameraShots() {
        return this.cameraShots;
    }
}

export default CameraTimeline;
