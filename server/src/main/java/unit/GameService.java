package unit;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGameDAO;
import model.GameData;

import java.sql.SQLException;
import java.util.Objects;

public class GameService {

//    private AuthDAO authDAO = new AuthDAO();
//
//    private GameDAO gameDAO = new GameDAO();

    private SqlGameDAO gameDAO;
    private SqlAuthDAO authDAO;

    public GameService() {
        try {
            authDAO = new SqlAuthDAO();
            gameDAO = new SqlGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private int gameIdBase = 0;

    public GameData[] listGames(String authToken) throws UnauthorizedException, SQLException, DataAccessException {
        String retrievedAuth = authDAO.getUser(authToken);
        if(retrievedAuth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameDAO.listGames().toArray(new GameData[0]);
    }

    public GameData createGame(String authToken, String gameName) throws UnauthorizedException, SQLException, DataAccessException {
        String retrievedAuth = authDAO.getUser(authToken);
        if(retrievedAuth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        int gameID = ++gameIdBase;
        //gameIdBase += 1;
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDAO.createGame(gameData);
        return gameData;
    }

    public void joinGame(String playerColor, int gameID, String authToken) throws AlreadyTakenException, BadRequestException, SQLException, DataAccessException {
        GameData retrievedGame = gameDAO.getGame(gameID);
        if(authToken == null) {
            throw new BadRequestException("Error: unauthorized");
        }
        String retrievedUser = authDAO.getUser(authToken);
        if(retrievedUser == null) {
            throw new BadRequestException("Error: unauthorized");
        }
        if(retrievedGame == null || playerColor == null) {
            throw new BadRequestException("Error: bad request");
        }
        else {
            if(Objects.equals(playerColor, "WHITE")) {
                if(retrievedGame.getWhiteUsername() != null) {
                    throw new AlreadyTakenException("Error: already taken");
                }
                retrievedGame.setWhiteUsername(retrievedUser);
                gameDAO.updateGame(retrievedGame, gameID);
            }
            else if(Objects.equals(playerColor, "BLACK")) {
                if(retrievedGame.getBlackUsername() != null) {
                    throw new AlreadyTakenException("Error: already taken");
                }
                retrievedGame.setBlackUsername(retrievedUser);
                gameDAO.updateGame(retrievedGame, gameID);
            }
        }

    }

    public void clearGames() throws DataAccessException {
        gameDAO.clear();
    }
}
