package server;

import dataaccess.DatabaseManager;
import server.handlers.Handler;
import spark.Spark;

public class Server {

    public Server(){
        try{
            DatabaseManager.createDatabase();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private Handler handler;

    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
    }

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
