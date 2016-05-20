/*
 * Class for storing camerashots
 */
class CameraShot {
    constructor(beginCount, endCount, name, description) {
        this.beginCount = beginCount;
        this.endCount = endCount;
        this.name = name;
        this.description = description;
    }

    // Returns the length of this cameraShot in counts
    getLength() {
        return this.endCount - this.beginCount;
    }
}

export default CameraShot;
