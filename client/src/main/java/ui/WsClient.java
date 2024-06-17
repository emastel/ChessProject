package ui;

import chess.ChessMove;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class WsClient extends Endpoint {

    public static void main(String[] args) throws Exception {
        var ws = new WsClient();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Test");
        while(true) {
            ws.send(scanner.nextLine());
        }
    }

    public Session session;

    public WsClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    public void send(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }

    public void connect(Session session, int id) {

    }

    public void makeMove(Session session, int id, ChessMove move) {

    }

    public void leave(Session session, int id) {

    }

    public void resign(Session session, int id) {

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
