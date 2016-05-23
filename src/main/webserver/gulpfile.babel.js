import gulp from "gulp";
import nodemon from "gulp-nodemon";
import babel from "gulp-babel";

const paths = {
    client_scripts: ["client_app/**/*.js"],
    client_js_dest: "public/",
};

gulp.task("develop", () => {
    nodemon({
        script: "server.js",
        execMap: {
            js: "./node_modules/babel-cli/bin/babel-node.js",
        },
        ignore: [
            "node_modules/",
            "public/",
        ],
        tasks: ["compile"],
    });
});

gulp.task("compile", () => {
    gulp.src(paths.client_scripts)
        .pipe(babel({
            presets: ["es2015"],
        }))
        .pipe(gulp.dest(paths.client_js_dest));
});
