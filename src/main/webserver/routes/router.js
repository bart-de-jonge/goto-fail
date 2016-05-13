/**
 * Created by Bart on 13/05/2016.
 */
var index = require('./index');


module.exports.addRoutes = function(app) {
    app.use('/', index);
}