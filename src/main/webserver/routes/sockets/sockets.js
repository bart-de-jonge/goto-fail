/**
 * socket.io Start Point.
 * Loads all necessary event and connection listeners.
 */
import socketio from "socket.io";
import log4js from "log4js";

const logger = log4js.getLogger();

const listen = (server) => {
    const io = socketio.listen(server);

    logger.debug("Initialized socket.io connection.");
    io.on("connection", (socket) => {
        logger.info("a user connected.");
        socket.on("disconnect", () => {
            logger.info("user disconnected.");
        });
    });
};

export default listen;
