import express from "express";
import CameraShot from "../objects/CameraShot";
import CameraTimeline from "../objects/CameraTimeline";
import xml2js from "xml2js";
import fs from "fs";
const router = new express.Router();
const parser = new xml2js.Parser();


/* GET home page. */
router.get("/", (req, res) => {

    // Dummyfile Todo: replace with dynamic
    fs.readFile(`${__dirname}/../test_project.scp`, (err, data) => {
        parser.parseString(data, (err, result) => {
            const cameraTimelinesXML = result.scriptingProject["camera-centerarea"][0].cameraTimeline;

            const cameraTimelines = new Array();
            const flattenedCameraTimelines = new Array();

            cameraTimelinesXML.forEach(timeline => {
                console.log(timeline);
                const cameraTimeline = new CameraTimeline("dummy", "dymmy");
                if (typeof timeline.shotList[0].shot !== "undefined") {
                    timeline.shotList[0].shot.forEach(shot => {
                        const cameraShot = new CameraShot(shot.beginCount[0], shot.endCount[0], shot.name[0], shot.description[0]);
                        cameraTimeline.addCameraShot(cameraShot);
                        flattenedCameraTimelines.push(cameraShot);
                    });
                } else {
                    console.log("undefined bitch")
                }

                cameraTimelines.push(cameraTimeline);
            });

            // Calculate minimum and maximum counts
            let minCount = 0;
            let maxCount = 0;

            if (flattenedCameraTimelines.length > 0) {
                minCount = Number(flattenedCameraTimelines[0].beginCount);
                maxCount = Number(flattenedCameraTimelines[0].endCount);
                flattenedCameraTimelines.forEach(shot => {
                    if (shot.beginCount < minCount) {
                        minCount = Number(shot.beginCount);
                    }
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
