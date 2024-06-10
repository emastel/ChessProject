package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SqlGameDAO {

    public static Gson gson = new Gson();

    public SqlGameDAO() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
            gameID int not null,
            whiteUsername varchar(256),
            blackUsername varchar(256),
            gameName varchar(256) not null,
            game varchar(4096) not null,
            PRIMARY KEY (gameID)
            )
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }

    public void createGame(GameData game) throws DataAccessException, SQLException {
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?,?,?,?,?)")) {
                preparedStatement.setInt(1,game.getGameID());
                preparedStatement.setString(2, game.getWhiteUsername());
                preparedStatement.setString(3, game.getBlackUsername());
                preparedStatement.setString(4, game.getGameName());
                preparedStatement.setString(5,game.gameToString());
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    public GameData getGame(int id) throws DataAccessException, SQLException {
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("SELECT * FROM games WHERE gameID=?")) {
                preparedStatement.setInt(1, id);
                try(var rs = preparedStatement.executeQuery()) {
                    while(rs.next()) {
                        var gameId = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var game = rs.getString("game");
                        var gameClass = gson.fromJson(game, ChessGame.class);
                        return new GameData(gameId,whiteUsername,blackUsername,gameName,gameClass);
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
        return null;
    }

    public int getID(String name) throws DataAccessException, SQLException {
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("SELECT gameID FROM games WHERE gameName=?")) {
                preparedStatement.setString(1, name);
                try(var rs = preparedStatement.executeQuery()) {
                    while(rs.next()) {
                        return rs.getInt("gameID");
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
        return 0;
    }

    public Collection<GameData> listGames() throws DataAccessException, SQLException {
        Collection<GameData> games = new ArrayList<>();
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("SELECT * FROM games")) {
                try(var rs = preparedStatement.executeQuery()) {
                    while(rs.next()) {
                        var gameId = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var game = rs.getString("game");
                        var gameClass = gson.fromJson(game, ChessGame.class);
                        games.add(new GameData(gameId,whiteUsername,blackUsername,gameName,gameClass));
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
        return games;
    }

    public void updateGame(GameData input, int id) throws DataAccessException, SQLException {
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("UPDATE games SET whiteUsername=?, blackUsername=?, game=?  WHERE gameID=?")) {
                preparedStatement.setString(1, input.getWhiteUsername());
                preparedStatement.setString(2, input.getBlackUsername());
                preparedStatement.setString(3, input.gameToString());
                preparedStatement.setInt(4, id);
                preparedStatement.executeUpdate();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    }

    public void clear() throws DataAccessException{
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("DELETE FROM games")) {
                preparedStatement.executeUpdate();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }










}

