package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SqlUserDAO {

    public SqlUserDAO() throws DataAccessException {
        String[] createStatements = {"""
            CREATE TABLE IF NOT EXISTS users(
            id int not null AUTO_INCREMENT,
            username varchar(256) not null,
            password varchar(256) not null,
            email varchar(256) not null,
            PRIMARY KEY (id)
            )
            """};
        DatabaseManager.configureDatabase(createStatements);
    }

    public void createUser(UserData user) throws SQLException{
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("INSERT INTO users (username, password, email) VALUES (?,?,?)")) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.password());
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    }

    public String getElement(String username, String type) throws SQLException{
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("SELECT id, username, password, email FROM users WHERE username=?")) {
                preparedStatement.setString(1, username);
                try(var rs = preparedStatement.executeQuery()) {
                    while(rs.next()) {
                        var id = rs.getInt("id");
                        var name = rs.getString("username");
                        var password = rs.getString("password");
                        var email = rs.getString("email");

                        if(type.equals("password")) {
                            return password;
                        }
                        else if (type.equals("username")) {
                            return name;
                        }
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
            try(var preparedStatement = con.prepareStatement("DELETE FROM users")) {
                preparedStatement.executeUpdate();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

}
