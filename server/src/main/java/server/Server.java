package server;

import server.handlers.Handler;
import spark.*;

public class Server {

    private Handler handler;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", Handler::clearRequest);
        Spark.post("/user", Handler::registerRequest);
        Spark.post("/session", Handler::loginRequest);
        Spark.delete("/session", Handler::logoutRequest);
        Spark.get("/game", Handler::listGamesRequest);
        Spark.post("/game", Handler::createGameRequest);
        Spark.put("/game", Handler::joinGameRequest);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}
