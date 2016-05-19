import express from "express";
import CameraShot from "../objects/CameraShot";
import CameraTimeline from "../objects/CameraTimeline";
import xml2js from "xml2js";
import fs from "fs";
const router = new express.Router();
const parser = new xml2js.Parser();


/* GET home page. */
router.get("/", (req, res) => {
    // Dummy array here
    // Todo: replace with real things,
    // note: the array must be ordered or be sorted here
    // timelineBlocks.push(new CameraShot(3, 5));
    // timelineBlocks.push(new CameraShot(7, 12));
    // timelineBlocks.push(new CameraShot(15, 22));

    fs.readFile(`${__dirname}/../test_project.scp`, (err, data) => {
        parser.parseString(data, (err, result) => {
            const cameraTimelines = result.scriptingProject["camera-centerarea"][0].cameraTimeline;
            const firstTimeline = cameraTimelines[0].shotList[0];

            const cameraTimeline = new CameraTimeline("dummyName", "dummyDescription");
            firstTimeline.shot.forEach(shot => {
                cameraTimeline.addCameraShot(new CameraShot(shot.beginCount[0],
                    shot.endCount[0], shot.name[0], shot.description[0]));
            });

            // Calculate minimum and maximum counts
            let minCount = 0;
            let maxCount = 0;

            if (cameraTimeline.getCameraShots().length > 0) {
                minCount = Number(cameraTimeline.getCameraShots()[0].beginCount);
                cameraTimeline.getCameraShots().forEach(shot => {
                    if (shot.beginCount < minCount) {
                        minCount = Number(shot.beginCount);
                    }
                });
                maxCount = Number(cameraTimeline.getCameraShots()[0].endCount);
                cameraTimeline.getCameraShots().forEach(shot => {
                    if (shot.endCount > maxCount) {
                        maxCount = Number(shot.endCount);
                    }
                });
            }

            res.render("timeline", {
                cameraTimeline,
                minCount,
                maxCount });
        });
    });
});

module.exports = router;
