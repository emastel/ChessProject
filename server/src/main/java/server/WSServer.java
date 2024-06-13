package server;

import spark.Spark;

import javax.websocket.Session;
import java.io.IOException;

public class WSServer {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/ws", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }
    public void onMessage(Session session, String msg) throws IOException {
        System.out.printf("Received Message: %s", msg);
        session.getBasicRemote().sendText("WebSocket response: " + msg);
    }

}
