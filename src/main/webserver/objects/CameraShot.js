class CameraShot {
    constructor(beginCount, endCount, name, description) {
        this.beginCount = beginCount;
        this.endCount = endCount;
        this.name = name;
        this.description = description;
    }

    getLength() {
        return this.endCount - this.beginCount;
    }
}

export default CameraShot;
