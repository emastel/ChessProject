package net;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler messageHandler;

    public WebSocketFacade(String url, NotificationHandler messageHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.messageHandler = messageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,socketURI);
            this.session.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    NotificationHandler.notify(notification);
                }

            });
        }
        catch(DeploymentException | IOException | URISyntaxException e) {
            throw new ResponseException(500,e.getMessage());
        }

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void loadGame(String auth) throws ResponseException {
        try {
            var command = new UserGameCommand(auth);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
