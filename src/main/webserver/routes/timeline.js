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
            const cameraTimelinesXML = result.scriptingProject["camera-centerarea"][0].cameraTimeline;
            const firstTimelineXML = cameraTimelinesXML[0].shotList[0];
            const secondTimelineXML = cameraTimelinesXML[1].shotList[0];

            const cameraTimeline = new CameraTimeline("dummyName", "dummyDescription");
            firstTimelineXML.shot.forEach(shot => {
                cameraTimeline.addCameraShot(new CameraShot(shot.beginCount[0],
                    shot.endCount[0], shot.name[0], shot.description[0]));
            });
            const cameraTimeline2 = new CameraTimeline("dummyName2", "dummyDescription2");
            secondTimelineXML.shot.forEach(shot => {
                cameraTimeline2.addCameraShot(new CameraShot(shot.beginCount[0],
                    shot.endCount[0], shot.name[0], shot.description[0]));
            });

            const cameraTimelines = new Array();
            cameraTimelines.push(cameraTimeline);
            cameraTimelines.push(cameraTimeline2);

            // Calculate minimum and maximum counts
            let minCount = 0;
            let maxCount = 0;

            const flattenedTimeline = new Array();
            cameraTimeline.getCameraShots().forEach(shot => {
                flattenedTimeline.push(shot);
            });
            cameraTimeline2.getCameraShots().forEach(shot => {
                flattenedTimeline.push(shot);
            })

            if (cameraTimeline.getCameraShots().length > 0) {
                minCount = Number(cameraTimeline.getCameraShots()[0].beginCount);
                flattenedTimeline.forEach(shot => {
                    console.log(shot.beginCount);
                    console.log(minCount);
                    if (shot.beginCount < minCount) {
                        minCount = Number(shot.beginCount);
                        console.log(minCount);
                    }
                });
                maxCount = Number(cameraTimeline.getCameraShots()[0].endCount);
                flattenedTimeline.forEach(shot => {
                    if (shot.endCount > maxCount) {
                        maxCount = Number(shot.endCount);
                    }
                });
            }

            res.render("timeline", {
                cameraTimelines,
                minCount,
                maxCount });
        });
    });
});

module.exports = router;
