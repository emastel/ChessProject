package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SqlUserDAO {

    public SqlUserDAO() throws DataAccessException {
        configureDatabase();
    }

    public void createUser(UserData user) throws SQLException{
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("INSERT INTO users (username, password, email) VALUES (?,?,?), RETURN_GENERATED_KEYS")) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.password());
                preparedStatement.setString(3, user.email());

//                var resultSet = preparedStatement.getGeneratedKeys();
//                var ID = 0;
//                if(resultSet.next()) {
//                    ID = resultSet.getInt(1);
//                }
            }
        }
        catch (Exception e) {
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
            throw new SQLException(e.getMessage());
        }
        return null;
    }

    public void clear() throws DataAccessException{
        try(var con = DatabaseManager.getConnection()) {
            try(var preparedStatement = con.prepareStatement("DROP TABLE users")) {
                preparedStatement.executeUpdate();
            }
        }
        catch(Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void configureDatabase() throws DataAccessException {
        String[] createStatements = new String[]{"""
            CREATE TABLE IF NOT EXISTS users (
            'id' int not null AUTO_INCREMENT,
            'username' varchar(256) not null,
            'password' varchar(256) not null,
            'email' varchar(256) not null,
            PRIMARY KEY ("id")
            INDEX ("username")
            INDEX ("password")
            INDEX ("email")
            )
            """};
        try {
            DatabaseManager.createDatabase();
            try (var con = DatabaseManager.getConnection()) {
                for(var statement : createStatements) {
                    try(var preparedStatement = con.prepareStatement(statement)) {
                        preparedStatement.executeUpdate();
                    }
                }

            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }






}
