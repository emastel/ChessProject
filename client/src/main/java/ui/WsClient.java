package ui;

import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WsClient extends Endpoint {

    public Client client;
    public Session session;

    public WsClient(Client client) throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.client = client;
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ServerMessage type = new Gson().fromJson(message, ServerMessage.class);
                if(type.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME)
                {
                    LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                    System.out.println("Game loaded");
                    client.setGame(loadGameMessage.getMessage());
                }
                else if (type.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                    ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
                    System.out.println(error.getErrorMessage());
                }
                else if (type.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                    NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                    System.out.println(notification.getMessage());
                }
            }
        });
    }

    public void connect(String authToken, int id) throws IOException {
        UserGameCommand connect = new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken,id);
        String message =  new Gson().toJson(connect);
        this.session.getBasicRemote().sendText(message);
    }

    public void makeMove(String authToken, int id, ChessMove move) throws IOException {
        MakeMoveCommand makeMove = new MakeMoveCommand(authToken,id,move);
        String message =  new Gson().toJson(makeMove);
        this.session.getBasicRemote().sendText(message);
    }

    public void leave(String authToken, int id) throws IOException {
        UserGameCommand leave = new UserGameCommand(UserGameCommand.CommandType.LEAVE,authToken,id);
        String message =  new Gson().toJson(leave);
        this.session.getBasicRemote().sendText(message);
    }

    public void resign(String authToken, int id) throws IOException {
        UserGameCommand resign = new UserGameCommand(UserGameCommand.CommandType.RESIGN,authToken,id);
        String message =  new Gson().toJson(resign);
        this.session.getBasicRemote().sendText(message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
