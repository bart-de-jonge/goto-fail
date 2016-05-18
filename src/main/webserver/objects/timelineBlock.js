class TimelineBlock {
    constructor(beginCount, endCount) {
        this.beginCount = beginCount;
        this.endCount = endCount;
    }

    getLength() {
        return this.endCount - this.beginCount;
    }
}

export default TimelineBlock;
