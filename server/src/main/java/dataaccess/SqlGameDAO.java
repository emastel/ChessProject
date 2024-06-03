package dataaccess;

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
            whiteUsername varchar(256) not null,
            blackUsername varchar(256) not null,
            gameName varchar(256) not null,
            game varchar(256) not null,
            PRIMARY KEY (gameID),
            )
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }

    public void createGame(GameData game) throws DataAccessException, SQLException {
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?,?,?,?,?), RETURN_GENERATED_KEYS")) {
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
            try(var preparedStatement = con.prepareStatement("SELECT game FROM games WHERE id=?")) {
                preparedStatement.setInt(1, id);
                try(var rs = preparedStatement.executeQuery()) {
                    while(rs.next()) {
                        var game = rs.getString("game");
                        return gson.fromJson(game, GameData.class);
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

    public Collection<GameData> listGames() throws DataAccessException, SQLException {
        Collection<GameData> games = new ArrayList<>();
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("SELECT game FROM users")) {
                try(var rs = preparedStatement.executeQuery()) {
                    while(rs.next()) {
                        var game = rs.getString("game");
                        games.add(gson.fromJson(game, GameData.class));
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
        Collection<GameData> games = new ArrayList<>();
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("UPDATE game SET game=? WHERE id=?")) {
                preparedStatement.setString(1, input.getGameName());
                preparedStatement.setInt(2, id);
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

