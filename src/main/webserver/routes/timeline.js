/**
 * Created by Bart on 17/05/2016.
 */
var express = require('express');
var router = express.Router();
var TimelineBlock = require('./../objects/timelineBlock');

/* GET home page. */
router.get('/', function(req, res, next) {

    // Dummy array here
    // Todo: replace with real things,
    // note: the array must be ordered or be sorted here
    var timelineBlocks = [];
    timelineBlocks.push(new TimelineBlock(2, 4));
    timelineBlocks.push(new TimelineBlock(7, 12));


    // Calculate minimum and maximum counts
    var minCount = 0;
    var maxCount = 0;

    if (timelineBlocks.length > 0) {
        minCount = timelineBlocks[0].beginCount;
        maxCount = timelineBlocks[timelineBlocks.length - 1].endCount;
    }

    console.log(minCount);
    console.log(maxCount);

    res.render('timeline', {
        timelineBlocks: timelineBlocks,
        minCount: minCount,
        maxCount: maxCount});
});

module.exports = router;
