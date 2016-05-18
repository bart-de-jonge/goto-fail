/**
 * Created by Bart on 17/05/2016.
 */

import express from "express";
import TimelineBlock from "./../objects/timelineBlock";
const router = new express.Router();

/* GET home page. */
router.get("/", (req, res) => {
    // Dummy array here
    // Todo: replace with real things,
    // note: the array must be ordered or be sorted here
    const timelineBlocks = [];
    timelineBlocks.push(new TimelineBlock(3, 5));
    timelineBlocks.push(new TimelineBlock(7, 12));
    timelineBlocks.push(new TimelineBlock(15, 22));


    // Calculate minimum and maximum counts
    let minCount = 0;
    let maxCount = 0;

    if (timelineBlocks.length > 0) {
        minCount = timelineBlocks[0].beginCount;
        maxCount = timelineBlocks[timelineBlocks.length - 1].endCount;
    }

    res.render("timeline", {
        timelineBlocks,
        minCount,
        maxCount });
});

module.exports = router;
