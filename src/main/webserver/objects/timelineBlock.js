module.exports = function TimelineBlock(beginCount, endCount) {
    this.beginCount = beginCount;
    this.endCount = endCount;
    this.getLength = function getLength() {
        return this.endCount - this.beginCount;
    };
};
