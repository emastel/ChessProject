package server.handlers;

import org.eclipse.jetty.websocket.api.Session;
import dataaccess.SqlAuthDAO;
import server.ConnectionManager;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;

public class WebSocketHandler {

    private SqlAuthDAO auths;
    private ConnectionManager connection;

    public void eval(Session session, UserGameCommand command) throws SQLException, IOException {
        switch(command.getCommandType()) {
            case CONNECT -> loadGame(command.getAuthString(),session);
        }
    }

    public void loadGame(String auth, Session session) throws SQLException, IOException {
        String user = auths.getUser(auth);
        connection.add(user,session);
        var message = String.format("%s joined the game", user);
        ServerMessage mes = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connection.broadcast(user,mes,message);
    }
}
