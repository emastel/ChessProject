package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SqlAuthDAO {

    public SqlAuthDAO() throws DataAccessException {
        String[] createStatements = {"""
            CREATE TABLE IF NOT EXISTS auths (
            id int not null AUTO_INCREMENT,
            token varchar(256) not null,
            username varchar(256) not null,
            PRIMARY KEY (id)
            )
            """};
        DatabaseManager.configureDatabase(createStatements);
    }

    public void createAuth(AuthData input) throws SQLException{
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("INSERT INTO auths (token, username) VALUES (?,?)")) {
                preparedStatement.setString(1, input.authToken());
                preparedStatement.setString(2, input.username());
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    }

    public String getUser(String token) throws SQLException{
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("SELECT id, token, username FROM auths WHERE token=?")) {
                preparedStatement.setString(1, token);
                try(var rs = preparedStatement.executeQuery()) {
                    while(rs.next()) {
                        return rs.getString("username");
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

    public void clear() throws DataAccessException{
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("DELETE FROM auths")) {
                preparedStatement.executeUpdate();
            }
        }
        catch(Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteAuth(String username, String token) throws SQLException, DataAccessException{
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("DELETE FROM auths WHERE username=? AND token=?")) {
                preparedStatement.setString(1,username);
                preparedStatement.setString(2,token);
                preparedStatement.executeUpdate();
            }
        }
        catch(Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public String getToken(String user) throws SQLException{
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("SELECT id, token, username FROM auths WHERE username=?")) {
                preparedStatement.setString(1, user);
                try(var rs = preparedStatement.executeQuery()) {
                    while(rs.next()) {
                        return rs.getString("token");
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

}
