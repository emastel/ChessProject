package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class CustomTests {

    private SqlGameDAO gameDAO;
    private SqlAuthDAO authDAO;
    private SqlUserDAO userDAO;

    public CustomTests() {
        try {
            authDAO = new SqlAuthDAO();
            gameDAO = new SqlGameDAO();
            userDAO = new SqlUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Create AuthToken")
    public void createAuthToken() throws SQLException {
        AuthData testInput = new AuthData("123", "user");
        try {
            authDAO.createAuth(testInput);
            assertUsername("user", authDAO.getUser("123"));
        } catch (SQLException e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @Order(2)
    @DisplayName("AuthToken Fail")
    public void badAuthToken() throws SQLException {
        AuthData testInput = new AuthData(null, "user");
        try {
            authDAO.createAuth(testInput);
        } catch (SQLException e) {
            assertNullError(e.getMessage());
        }

    }

    @Test
    @Order(3)
    @DisplayName("Get AuthToken")
    public void getAuthToken() throws SQLException {
        AuthData testInput = new AuthData("123", "user");
        try {
            authDAO.createAuth(testInput);
            String receivedUser = authDAO.getUser("123");
            assertUsername("user", receivedUser);
        } catch (SQLException e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @Order(4)
    @DisplayName("Bad Get AuthToken")
    public void badGetAuthToken() throws SQLException {
        AuthData testInput = new AuthData("123", "user");
        try {
            authDAO.createAuth(testInput);
            String receivedUser = authDAO.getUser("456");
            Assertions.assertNull(receivedUser);
        } catch (SQLException e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @Order(5)
    @DisplayName("Clear AuthToken")
    public void clearAuthToken() throws SQLException {
        AuthData testInput = new AuthData("123", "user");
        try {
            authDAO.createAuth(testInput);
            authDAO.clear();
            String receivedUser = authDAO.getUser("123");
            Assertions.assertNull(receivedUser);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @Order(6)
    @DisplayName("Delete AuthToken")
    public void deleteAuthToken() throws SQLException {
        AuthData testInput = new AuthData("123", "user");
        try {
            authDAO.createAuth(testInput);
            authDAO.deleteAuth("user", "123");
            String receivedUser = authDAO.getUser("123");
            Assertions.assertNull(receivedUser);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @Order(7)
    @DisplayName("Bad Delete AuthToken")
    public void badDeleteAuthToken() throws SQLException {
        AuthData testInput = new AuthData("123", "user");
        try {
            authDAO.createAuth(testInput);
            authDAO.deleteAuth("user", "456");
            assertUsername("user", authDAO.getUser("123"));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @Order(8)
    @DisplayName("Create User")
    public void createUser() throws SQLException {
        UserData testInput = new UserData("user", "password", "email");
        try {
            userDAO.createUser(testInput);
            assertUsername("user", userDAO.getElement("user","username"));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @Order(9)
    @DisplayName("Bad Create User")
    public void badCreateUser() throws SQLException {
        UserData testInput = new UserData(null, "password", "email");
        try {
            userDAO.createUser(testInput);
        } catch (Exception e) {
            assertNullName(e.getMessage());
        }

    }

    @Test
    @Order(10)
    @DisplayName("get user")
    public void getUser() throws SQLException {
        UserData testInput = new UserData("user", "password", "email");
        try {
            userDAO.createUser(testInput);
            String receivedUser = userDAO.getElement("user", "username");
            assertUsername("user", receivedUser);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @Order(11)
    @DisplayName("bad get user")
    public void badGetUser() throws SQLException {
        UserData testInput = new UserData("user", "password", "email");
        try {
            userDAO.createUser(testInput);
            String receivedUser = userDAO.getElement("WRONG", "username");
            Assertions.assertNull(receivedUser);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @Order(12)
    @DisplayName("user clear")
    public void clearUser() throws SQLException {
        UserData testInput = new UserData("user", "password", "email");
        try {
            userDAO.createUser(testInput);
            userDAO.clear();
            String receivedUser = userDAO.getElement("user", "username");
            Assertions.assertNull(receivedUser);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @Order(13)
    @DisplayName("create game")
    public void createGame() throws SQLException, DataAccessException {
        gameDAO.clear();
        GameData testInput = new GameData(1, "white", "black", "the game", new ChessGame());
        try {
            gameDAO.createGame(testInput);
            assertUsername("white",gameDAO.getGame(1).getWhiteUsername());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(14)
    @DisplayName("bad create game")
    public void badCreateGame() throws SQLException, DataAccessException {
        gameDAO.clear();
        GameData testInput = new GameData(1, "white", "black", null, new ChessGame());
        try {
            gameDAO.createGame(testInput);
        } catch (Exception e) {
            assertNullGamName(e.getMessage());
        }
    }

    @Test
    @Order(15)
    @DisplayName("join game")
    public void joinGame() throws SQLException, DataAccessException {
        gameDAO.clear();
        GameData testInput = new GameData(1, null, "black", "the game", new ChessGame());
        try {
            gameDAO.createGame(testInput);
            testInput.setWhiteUsername("white");
            gameDAO.updateGame(testInput,1);
            assertUsername("white",gameDAO.getGame(1).getWhiteUsername());
        } catch (Exception e) {
            assertNullGamName(e.getMessage());
        }
    }

    @Test
    @Order(16)
    @DisplayName("bad join game")
    public void badJoinGame() throws SQLException, DataAccessException {
        gameDAO.clear();
        GameData testInput = new GameData(1, null, "black", "the game", new ChessGame());
        try {
            gameDAO.createGame(testInput);
            testInput.setWhiteUsername("white");
            gameDAO.updateGame(testInput,2);
            Assertions.assertNull(gameDAO.getGame(1).getWhiteUsername());
        } catch (Exception e) {
            assertNullGamName(e.getMessage());
        }
    }

    @Test
    @Order(17)
    @DisplayName("list games")
    public void listGames() throws SQLException, DataAccessException {
        gameDAO.clear();
        GameData testInput = new GameData(1, null, "black", "the game", new ChessGame());
        try {
            gameDAO.createGame(testInput);
            testInput = new GameData(2, "white", null, "new game", new ChessGame());
            gameDAO.createGame(testInput);
            assertSize(gameDAO.listGames().size(), 2);
        } catch (Exception e) {
            assertNullGamName(e.getMessage());
        }
    }

    @Test
    @Order(18)
    @DisplayName("bad list games")
    public void badListGames() throws SQLException, DataAccessException {
        gameDAO.clear();
        GameData testInput = new GameData(1, null, "black", "the game", new ChessGame());
        try {
            gameDAO.listGames();
            assertSize(gameDAO.listGames().size(), 0);
        } catch (Exception e) {
            assertNullGamName(e.getMessage());
        }
    }

    @Test
    @Order(19)
    @DisplayName("clear games")
    public void clearGames() throws SQLException, DataAccessException {
        try {
            gameDAO.clear();
            assertSize(gameDAO.listGames().size(), 0);
        } catch (Exception e) {
            assertNullGamName(e.getMessage());
        }
    }


    private void assertUsername(String expected, String actual) {
        Assertions.assertEquals(expected, actual, "username is incorrect");
    }

    private void assertNullError(String actual) {
        Assertions.assertEquals("Column 'token' cannot be null", actual, "valid token");
    }

    private void assertNullName(String actual) {
        Assertions.assertEquals("Column 'username' cannot be null", actual, "valid name");
    }

    private void assertNullGamName(String actual) {
        Assertions.assertEquals("Column 'gameName' cannot be null", actual, "valid name");
    }

    private void assertSize(int actual, int expected) {
        Assertions.assertEquals(expected,actual,"wrong size");
    }
}
