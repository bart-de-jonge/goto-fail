import index from "./index";

module.exports.addRoutes = (app) => {
    app.use("/", index);
};
