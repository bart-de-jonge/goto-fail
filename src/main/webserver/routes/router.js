
// Module used to attach all routes to the correct urls

import index from "./index";
import timeline from "./timeline";
module.exports.addRoutes = (app) => {
    app.use("/", index);
    app.use("/timeline", timeline);
};
