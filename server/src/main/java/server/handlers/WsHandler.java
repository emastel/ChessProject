package server.handlers;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.*;

@WebSocket
public class WsHandler {

    private static Map<Integer,List<Session>> games = new HashMap<>();
    private static SqlGameDAO gameDAO;
    private static SqlAuthDAO authDAO;

    static {
        try {
            gameDAO = new SqlGameDAO();
            authDAO = new SqlAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command.getGameID(), command.getAuthString());
            case MAKE_MOVE -> makeMove(session, message);
            case LEAVE -> leave(session, command.getGameID(), command.getAuthString());
            case RESIGN -> resign(session, command.getGameID(), command.getAuthString());
        }
    }

    private void sendNotifOthers(Session session, int id, String notification) throws Exception {
        NotificationMessage notifMessage = new NotificationMessage(notification);
        String message = new Gson().toJson(notifMessage);
        sendOthers(session,message,id);
    }

    private void sendNotifAll(Session session, int id, String notification) throws Exception {
        NotificationMessage notifMessage = new NotificationMessage(notification);
        String message = new Gson().toJson(notifMessage);
        sendAll(message,id);
    }

    private void sendError(Session session, String error) throws Exception {
        ErrorMessage errorMessage = new ErrorMessage(error);
        String message = new Gson().toJson(errorMessage);
        session.getRemote().sendString(message);
    }

    private void sendOthers(Session thisSession, String message, int id) throws Exception {
        List<Session> sessions = games.get(id);
        if (sessions != null) {
            for (Session session : sessions) {
                if(session != thisSession) {
                    session.getRemote().sendString(message);
                }
            }
        }
    }

    private void sendAll(String message, int id) throws Exception {
        List<Session> sessions = games.get(id);
        if (sessions == null) {
            for (Session session : sessions) {
                session.getRemote().sendString(message);
            }
        }
    }

    private void sendLoadGame(Session thisSession, int id) {
        try {
            GameData game = gameDAO.getGame(id);
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            String message = new Gson().toJson(loadGameMessage);
            thisSession.getRemote().sendString(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void connect(Session session, int id, String auth) throws Exception {
        List<Session> sessions = new ArrayList<>();
        if(authDAO.getUser(auth) == null) {
            sendError(session, "Invalid authToken");
        }
        else if (!games.containsKey(id)) {
            if(gameDAO.getGame(id) == null) {
                sendError(session, "Invalid id");
            }
            else
            {
                sessions.add(session);
                games.put(id, sessions);
            }
        }
        else {
            sessions = games.get(id);
            sessions.add(session);
            games.put(id, sessions);
        }
        if(!sessions.isEmpty()) {
            try {
                String user = authDAO.getUser(auth);
                sendLoadGame(session,id);
                sendNotifOthers(session, id, user + " connected to game " + id);
            } catch (Exception e) {
                sendError(session, "Failed to connect");
            }
        }
    }

    private void makeMove(Session session, String message) throws Exception {
        try {
            MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
            int id = command.getGameID();
            ChessMove move = command.getMove();
            GameData data = gameDAO.getGame(id);
            ChessGame game = data.getGame();
            game.makeMove(move);
            data.setGame(game);
            gameDAO.updateGame(data, id);
            sendNotifOthers(session, id, move.toString());
            if(game.isInCheck(ChessGame.TeamColor.WHITE)) {
                sendNotifAll(session, id, "White is in check");
            }
            else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                sendNotifAll(session, id, "Black is in check");
            }
            else if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                sendNotifAll(session, id, "White is in checkmate");
            }
            else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                sendNotifAll(session, id, "Black is in checkmate");
            }
            else if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
                sendNotifAll(session, id, "Game is in stalemate");
            }
            LoadGameMessage loadGameMessage = new LoadGameMessage(data);
            String msg = new Gson().toJson(loadGameMessage);
            sendAll(msg,id);
        } catch (Exception e) {
            sendError(session, "Failed to make move");
        }
    }

    private void leave(Session session, int id, String auth) throws Exception {
        try {
            GameData data = gameDAO.getGame(id);
            String user = authDAO.getUser(auth);
            if(Objects.equals(user, data.getWhiteUsername())) {
                data.setWhiteUsername(null);
                gameDAO.updateGame(data, id);
            } else if (Objects.equals(user, data.getBlackUsername())) {
                data.setBlackUsername(null);
                gameDAO.updateGame(data, id);
            }
            sendOthers(session,user + " has left the game",id);
        } catch (Exception e) {
            sendError(session, "Failed to leave");
        }
    }

    private void resign(Session session, int id, String auth) throws Exception {
        try {
            GameData data = gameDAO.getGame(id);
            data.setGameOver(true);
            gameDAO.updateGame(data, id);
            String user = authDAO.getUser(auth);
            sendNotifAll(session, id, user + " has resigned");
        } catch (Exception e) {
            sendError(session, "Failed to resign");
        }
    }


}
