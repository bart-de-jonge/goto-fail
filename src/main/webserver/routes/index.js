import express from "express";
const router = express.Router(); // eslint-disable-line new-cap

/* GET home page. */
router.get("/", (req, res) => {
    res.render("index", { title: "Express" });
});

module.exports = router;
