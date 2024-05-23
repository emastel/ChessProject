package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.Map;
import java.util.Objects;

public class GameService {

    private AuthDAO authDAO;

    private GameDAO gameDAO;

    private int gameIdBase = 0;

    public Map<Integer, GameData> listGames(String authToken) throws DataAccessException {
        AuthData retrievedAuth = authDAO.getAuth(authToken);
        if(retrievedAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        AuthData retrievedAuth = authDAO.getAuth(authToken);
        if(retrievedAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        int gameID = ++gameIdBase;
        gameIdBase += 1;
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDAO.createGame(gameData);
        return gameData;
    }

    public void joinGame(String playerColor, int gameID, String authToken) throws DataAccessException {
        GameData retrievedGame = gameDAO.getGame(gameID);
        AuthData retrievedAuth = authDAO.getAuth(authToken);
        if(retrievedGame == null) {
            throw new DataAccessException("Error: bad request");
        }
        else {
            if(Objects.equals(playerColor, "white")) {
                if(retrievedGame.whiteUsername() != null) {
                    throw new DataAccessException("Error: already taken");
                }
            }
            else if(Objects.equals(playerColor, "black")) {
                if(retrievedGame.blackUsername() != null) {
                    throw new DataAccessException("Error: already taken");
                }
            }
            retrievedGame = new GameData(gameID,retrievedAuth.username(),retrievedGame.blackUsername(),retrievedGame.gameName(),retrievedGame.game());
        }

    }

    public void clearGames() {
        gameDAO.clear();
    }
}
