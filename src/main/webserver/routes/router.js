/**
 * Created by Bart on 13/05/2016.
 */
var index = require('./index');
var timeline = require('./timeline');


module.exports.addRoutes = function(app) {
    app.use('/', index);
    app.use('/timeline', timeline);
}