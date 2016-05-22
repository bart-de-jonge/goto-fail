/**
 * socket.io Start Point.
 * Loads all necessary event and connection listeners.
 */
import socketio from "socket.io";

const listen = (server) => {
    const io = socketio.listen(server);

    console.log("Initialized socket.io connection.");
    io.on("connection", (socket) => {
        console.log("a user connected.");
        socket.on("disconnect", () => {
            console.log("user disconnected.");
        });
    });
};

export default listen;
