class CameraTimeline {
    constructor(name, description) {
        this.name = name;
        this.description = description;
        this.cameraShots = new Array();
    }

    addCameraShot(cameraShot) {
        this.cameraShots.push(cameraShot);
    }

    getCameraShots() {
        return this.cameraShots;
    }
}

export default CameraTimeline;
