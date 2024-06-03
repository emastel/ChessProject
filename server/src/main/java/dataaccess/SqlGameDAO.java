package dataaccess;

import model.GameData;

import java.sql.SQLException;

public class SqlGameDAO {

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

//    public GameData getGame(int id) throws DataAccessException {
//
//    }
//
//    public Collection<GameData> listGames() throws DataAccessException {
//
//    }
//
//    public void updateGame(GameData input) throws DataAccessException {
//
//    }





//    public GameData getGame(int id) throws DataAccessException {
//        try(var con = DatabaseManager.getConnection()) {
//            var statement = "SELECT id, json FROM game WHERE id=?";
//            try(var ps = con.prepareStatement(statement)) {
//                ps.setInt(1, id);
//                try(var rs = ps.executeQuery()) {
//                    if(rs.next()) {
//                        return readGame(rs);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new DataAccessException(e.getMessage());
//        }
//        return null;
//    }


//    private GameData readGame(ResultSet rs) throws SQLException {
//        var id = rs.getInt("id");
//        var json = rs.getString("json");
//        var game = new Gson().fromJson(json, GameData.class);
//        return game.setGameID(id);
//    }

//    private int executeUpdate(String statement, Object... params) throws DataAccessException {
//        try(var con = DatabaseManager.getConnection()) {
//            try(var ps = con.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
//                for (var i = 0; i < params.length; i++) {
//                    var param = params[i];
//                    if (param instanceof String p) ps.setString(i+1,p);
//                    else if (param instanceof Integer p) ps.setInt(i+1,p);
//                    else if (param instanceof GameData p) ps.setString(i+1,p.toString());
//                    else if (param == null) ps.setNull(i+1,NULL);
//                }
//                ps.executeUpdate();
//
//                var rs = ps.getGeneratedKeys();
//                if(rs.next()) {
//                    return rs.getInt(1);
//                }
//            }
//            return 0;
//        }
//        catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//    }




}

