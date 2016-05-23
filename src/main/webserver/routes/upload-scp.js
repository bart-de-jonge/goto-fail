import log4js from "log4js";
import express from "express";
const router = new express.Router();
import fs from "fs";
import multipart from "connect-multiparty";
const multipartMiddleware = multipart();

const logger = log4js.getLogger();

router.post("/", multipartMiddleware, (req, res) => {
    const newPath = `${__dirname}/../project-scp-files/project.scp`;

    if (req.files.project.name.endsWith(".scp")) {
        fs.rename(req.files.project.path, newPath, (err) => {
            if (err) {
	        logger.info(err);
                res.json({
                    succes: false, message: "Some error occurred, please try again later!" });
            } else {
                res.json({ succes: true, message: "Project successfully uploaded!" });
            }
        });
    } else {
        res.json({ succes: false, message: "Please upload a .scp file." });
    }
});

module.exports = router;
