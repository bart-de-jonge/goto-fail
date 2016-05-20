
// Module used to attach all routes to the correct urls

import index from "./index";
import timeline from "./timeline";
import uploadScp from "./upload-scp";

module.exports.addRoutes = (app) => {
    app.use("/", index);
    app.use("/timeline", timeline);
    app.use("/upload-scp", uploadScp);
};
