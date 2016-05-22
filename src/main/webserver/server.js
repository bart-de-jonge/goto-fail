#!/usr/bin/env node

/**
 * Module dependencies.
 */

import "babel-polyfill";
import app from "./app";
import http from "http";
import log4js from "log4js";
import io from "./routes/sockets/sockets.js";
const debug = require("debug")("webserver:server");

// Logger configuration
log4js.configure({
    appenders: [
        { type: "console" },
    ],
    replaceConsole: true,
});

const logger = log4js.getLogger();
logger.setLevel("DEBUG");

/**
 * Get port from environment and store in Express.
 */

const port = normalizePort(process.env.PORT || "3000");
app.set("port", port);

/**
 * Create HTTP server.
 */

logger.info("Starting HTTP Server");
const server = http.createServer(app);

// Add socket.io sockets
io(server);

/**
 * Listen on provided port, on all network interfaces.
 */

server.listen(port);
server.on("error", onError);
server.on("listening", onListening);

/**
 * Normalize a port into a number, string, or false.
 */

function normalizePort(val) {
    const port = parseInt(val, 10);

    if (isNaN(port)) {
        // named pipe
        return val;
    }

    if (port >= 0) {
        // port number
        return port;
    }

    return false;
}

/**
 * Event listener for HTTP server "error" event.
 */

function onError(error) {
    if (error.syscall !== "listen") {
        throw error;
    }

    const bind = typeof port === "string"
        ? `Pipe ${port}`
        : `Port ${port}`;

    // handle specific listen errors with friendly messages
    switch (error.code) {
    case "EACCES":
        logger.error(`${bind} requires elevated privileges`);
        process.exit(1);
        break;
    case "EADDRINUSE":
        logger.error(`${bind} is already in use`);
        process.exit(1);
        break;
    default:
        throw error;
    }
}

/**
 * Event listener for HTTP server "listening" event.
 */

function onListening() {
    const addr = server.address();
    const bind = typeof addr === "string"
        ? `pipe ${addr}`
        : `port ${addr.port}`;
    debug(`Listening on ${bind}`);
}
